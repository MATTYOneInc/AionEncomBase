/**
 * This file is part of Encom.
 *
 *  Encom is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Encom is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Encom.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.controllers.attack.AggroInfo;
import com.aionemu.gameserver.controllers.attack.AggroList;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.common.service.PlayerTeamDistributionService;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.templates.npc.NpcRank;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.DialogService;
import com.aionemu.gameserver.services.RespawnService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.drop.DropService;
import com.aionemu.gameserver.services.player.AtreianBestiaryService;
import com.aionemu.gameserver.services.player.GrowthEnergy;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.StatFunctions;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneInstance;

public class NpcController extends CreatureController<Npc> {
	private static final Logger log = LoggerFactory.getLogger(NpcController.class);

	@Override
	public void notSee(VisibleObject object, boolean isOutOfRange) {
		super.notSee(object, isOutOfRange);
		if (object instanceof Creature) {
			getOwner().getAi2().onCreatureEvent(AIEventType.CREATURE_NOT_SEE, (Creature) object);
			getOwner().getAggroList().remove((Creature) object);
		}
	}

	@Override
	public void see(VisibleObject object) {
		super.see(object);
		Npc owner = getOwner();
		if (object instanceof Creature) {
			owner.getAi2().onCreatureEvent(AIEventType.CREATURE_SEE, (Creature) object);
		}
		if (object instanceof Player) {
			// TODO see player ai event
			if (owner.getLifeStats().isAlreadyDead()) {
				DropService.getInstance().see((Player) object, owner);
			}
		} else if (object instanceof Summon) {
			// TODO see summon ai event
		}
	}

	@Override
	public void onBeforeSpawn() {
		super.onBeforeSpawn();
		Npc owner = getOwner();

		// set state from npc templates
		if (owner.getObjectTemplate().getState() != 0) {
			owner.setState(owner.getObjectTemplate().getState());
		} else {
			owner.setState(CreatureState.NPC_IDLE);
		}
		owner.getLifeStats().setCurrentHpPercent(100);
		owner.getLifeStats().setCurrentMpPercent(100);
		owner.getAi2().onGeneralEvent(AIEventType.RESPAWNED);

		if (owner.getSpawn().canFly()) {
			owner.setState(CreatureState.FLYING);
		}

		if (owner.getSpawn().getState() != 0) {
			owner.setState(owner.getSpawn().getState());
		}
	}

	@Override
	public void onAfterSpawn() {
		super.onAfterSpawn();
		getOwner().getAi2().onGeneralEvent(AIEventType.SPAWNED);
	}

	@Override
	public void onDespawn() {
		Npc owner = getOwner();
		DropService.getInstance().unregisterDrop(getOwner());
		owner.getAi2().onGeneralEvent(AIEventType.DESPAWNED);
		super.onDespawn();
	}

	public void defeatNamedMsg(final Player player) {
		Npc owner = getOwner();
		final int npcNameId = owner.getObjectTemplate().getNameId();
		NpcRank npcRank = owner.getObjectTemplate().getRank();
		if (npcRank == NpcRank.EXPERT && !player.isInInstance()) {
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player players) {
					// "Player Name" has killed "Named Monster"
					PacketSendUtility.sendPacket(players,
							new SM_SYSTEM_MESSAGE(1400021, player.getName(), new DescriptionId(npcNameId * 2 + 1)));
				}
			});
		}
	}

	@Override
	public void onDie(Creature lastAttacker) {
		Npc owner = getOwner();
		if (owner.getSpawn().hasPool()) {
			owner.getSpawn().setUse(false);
		}

		PacketSendUtility.broadcastPacket(owner,
				new SM_EMOTION(owner, EmotionType.DIE, 0, owner.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()));

		try {
			if (owner.getAi2().poll(AIQuestion.SHOULD_REWARD)) {
				this.doReward();
			}
			owner.getPosition().getWorldMapInstance().getInstanceHandler().onDie(owner);
			owner.getAi2().onGeneralEvent(AIEventType.DIED);
		} finally { // always make sure npc is schedulled to respawn
			if (owner.getAi2().poll(AIQuestion.SHOULD_DECAY)) {
				addTask(TaskId.DECAY, RespawnService.scheduleDecayTask(owner));
			}
			if (owner.getAi2().poll(AIQuestion.SHOULD_RESPAWN) && !owner.isDeleteDelayed()
					&& !SiegeService.getInstance().isSiegeNpcInActiveSiege(owner)) {
				Future<?> respawnTask = scheduleRespawn();
				if (respawnTask != null) {
					addTask(TaskId.RESPAWN, respawnTask);
				}
			} else if (!hasScheduledTask(TaskId.DECAY)) {
				onDelete();
			}
		}
		super.onDie(lastAttacker);
	}

	@Override
	public void doReward() {
		super.doReward();
		int kinahCount = 0;
		AggroList list = getOwner().getAggroList();
		Collection<AggroInfo> finalList = list.getFinalDamageList(true);
		if (getOwner() instanceof SiegeNpc) {
			rewardSiegeNpc();
		}

		AionObject winner = list.getMostDamage();

		if (winner == null) {
			return;
		}

		float totalDmg = 0;

		for (AggroInfo info : finalList) {
			totalDmg += info.getDamage();
		}

		if (totalDmg <= 0) {
			return;
		}

		for (AggroInfo info : finalList) {
			AionObject attacker = info.getAttacker();
			// PvE Toll Reward
			if (attacker instanceof Player) {
				if (CustomConfig.ENABLE_PVE_TOLL_REWARD) {
					if (Rnd.get(0, 100) > CustomConfig.TOLL_PVE_CHANCE) {
						Player player = (Player) attacker;
						for (String worldIds : CustomConfig.TOLL_PVE_WORLDID.split(",")) {
							if (player.getWorldId() == Integer.parseInt(worldIds)) {
								InGameShopEn.getInstance().addToll(player, CustomConfig.TOLL_PVE_QUANTITY);
								PacketSendUtility.sendMessage(player,
										"You have received " + CustomConfig.TOLL_PVE_QUANTITY + " tolls from PvE!");
							}
						}

					}
				}
			}

			// We are not reward Npc's
			if (attacker instanceof Npc) {
				continue;
			}
			float percentage = info.getDamage() / totalDmg;
			if (percentage > 1) {
				continue;
			}
			if (attacker instanceof TemporaryPlayerTeam<?>) {
				PlayerTeamDistributionService.doReward((TemporaryPlayerTeam<?>) attacker, percentage, getOwner(),
						winner);
			} else if (attacker instanceof Player && ((Player) attacker).isInGroup2()) {
				PlayerTeamDistributionService.doReward(((Player) attacker).getPlayerGroup2(), percentage, getOwner(),
						winner);
			} else if (attacker instanceof Player) {
				Player player = (Player) attacker;
				if (!player.getLifeStats().isAlreadyDead()) {
					long rewardXp = StatFunctions.calculateSoloExperienceReward(player, getOwner());
					int rewardDp = StatFunctions.calculateSoloDPReward(player, getOwner());
					float rewardAp = 1;
					rewardXp *= percentage;
					rewardDp *= percentage;
					rewardAp *= percentage;
					QuestEngine.getInstance().onKill(new QuestEnv(getOwner(), player, 0, 0));
					// When a player defeat a "Boss" all ppls on server see!!!
					defeatNamedMsg(player);
					// Reward XP Solo (New system, Exp Retail NA)
					switch (player.getWorldId()) {
					case 301720000: // Mirash Sanctuary.
					case 302100000: // Fissure Of Oblivion.
					case 302110000: // [Opportunity] Fissure Of Oblivion.
					case 302400000: // Crucible Spire.
					case 210100000: // Iluma.
					case 220110000: // Norsvold.
					case 600040000: // Tiamaranta's Eye.
					case 600090000: // Kaldor.
					case 600100000: // Levinshor.
					default:
						player.getCommonData().addExp(rewardXp, RewardType.HUNTING,
								this.getOwner().getObjectTemplate().getNameId());
						break;
					}
					player.getCommonData().addDp(rewardDp);
					if (getOwner().isRewardAP()) {
						int calculatedAp = StatFunctions.calculatePvEApGained(player, getOwner());
						rewardAp *= calculatedAp;
						if (rewardAp >= 1) {
							player.getCommonData().addAbyssFavor(1500); // 0.15% Abyss Favor Energy.
							AbyssPointsService.addAp(player, getOwner(), (int) rewardAp);
							PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
						}
					}
					if (attacker.equals(winner)) {
						DropRegistrationService.getInstance().registerDrop(getOwner(), player, player.getLevel(), null);
					}
					// Auto Drop Kinah.
					if (CustomConfig.AUTO_KINAH_ENABLED) {
						switch (player.getWorldId()) {
						case 210010000: // Poeta.
						case 220010000: // Ishalgen.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 500) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 210030000: // Verteron.
						case 220030000: // Altgard.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 1500) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 210020000: // Eltnen.
						case 220020000: // Morheim.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 2000) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 210040000: // Heiron.
						case 220040000: // Beluslan.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 2500) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 210060000: // Theobomos.
						case 220050000: // Brushtonin.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 3000) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 210050000: // Inggison.
						case 220070000: // Gelkmaros.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 3500) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 210070000: // Cygnea.
						case 220080000: // Enshar.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 4000) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 400010000: // Reshanta.
						case 400020000: // Belus.
						case 400040000: // Aspida.
						case 400050000: // Atanatos.
						case 400060000: // Disillon.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 4500) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 600090000: // Kaldor.
						case 600100000: // Levinshor.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 5000) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 210100000: // Iluma.
						case 220110000: // Norsvold.
						case 600040000: // Tiamaranta's Eye.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 5500) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						case 210090000: // Idian Depths E.
						case 220100000: // Idian Depths A.
							if (player.getLevel() < getOwner().getLevel() + 5) {
								kinahCount = Rnd.get(100, 6000) * player.getLevel();
							} else if (player.getLevel() > getOwner().getLevel() + 5) {
								kinahCount = 1000;
							}
							break;
						default:
							kinahCount = 0;
							break;
						}
						if (player.isInInstance() && player.getLevel() < getOwner().getLevel() + 5) {
							kinahCount = Rnd.get(100, 1000) * player.getLevel();
						} else if (player.isInInstance() && player.getLevel() > getOwner().getLevel() + 5) {
							kinahCount = 1000;
						}
						player.getInventory().increaseKinah(kinahCount);
					}
					// Reward InGameShop.
					switch (player.getWorldId()) {
					// Idian Depths.
					case 210090000:
					case 220100000:
						InGameShopEn.getInstance().addToll(player, (long) (0 * player.getRates().getTollRewardRate()));
						PacketSendUtility.sendSys1Message(player, "\uE083", "Kamu Jangan Ngepet ya");
						break;
					}
					// Berdin's Star.
					if (getOwner().getLevel() >= 10) {
						player.getCommonData().addBerdinStar(1575000); // 0.14%
						PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
					}
					// Aura Of Growth.
					if (getOwner().getLevel() >= 66) {
						if (Rnd.get(1, 100) < RateConfig.AURA_OF_GROWTH) {
							GrowthEnergy.getInstance().addGrowthEnergy(player);
							PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
						}
					}
					// Atreian Bestiary.
					if (getOwner().getLevel() >= 66) {
						AtreianBestiaryService.getInstance().onKill(player, getOwner().getNpcId());
					}
				}
			}
		}
	}

	@Override
	public Npc getOwner() {
		return (Npc) super.getOwner();
	}

	@Override
	public void onDialogRequest(Player player) {
		// notify npc dialog request observer
		if (!getOwner().getObjectTemplate().canInteract()) {
			return;
		}
		player.getObserveController().notifyRequestDialogObservers(getOwner());
		getOwner().getAi2().onCreatureEvent(AIEventType.DIALOG_START, player);
	}

	@Override
	public void onDialogSelect(int dialogId, final Player player, int questId, int extendedRewardIndex, int unk) {// TODO
																													// unk
																													// need
																													// to
																													// be
																													// figure
																													// out
		QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		if (!MathUtil.isInRange(getOwner(), player, getOwner().getObjectTemplate().getTalkDistance() + 2)
				&& !QuestEngine.getInstance().onDialog(env)) {
			return;
		}
		if (!getOwner().getAi2().onDialogSelect(player, dialogId, questId, extendedRewardIndex)) {
			DialogService.onDialogSelect(dialogId, player, getOwner(), questId, extendedRewardIndex);
		}
	}

	@Override
	public void onAttack(Creature creature, int skillId, TYPE type, int damage, boolean notifyAttack, LOG log) {
		if (getOwner().getLifeStats().isAlreadyDead()) {
			return;
		}
		final Creature actingCreature;

		// summon should gain its own aggro
		if (creature instanceof Summon) {
			actingCreature = creature;
		}
		else {
			actingCreature = creature.getActingCreature();
		}

		super.onAttack(actingCreature, skillId, type, damage, notifyAttack, log);

		Npc npc = getOwner();

		if (actingCreature instanceof Player) {
			QuestEngine.getInstance().onAttack(new QuestEnv(npc, (Player) actingCreature, 0, 0));
		}

		PacketSendUtility.broadcastPacket(npc, new SM_ATTACK_STATUS(npc, actingCreature, type, skillId, damage, log));
	}

	@Override
	public void onStopMove() {
		getOwner().getMoveController().setInMove(false);
		super.onStopMove();
	}

	@Override
	public void onStartMove() {
		getOwner().getMoveController().setInMove(true);
		super.onStartMove();
	}

	@Override
	public void onReturnHome() {
		if (getOwner().isDeleteDelayed()) {
			onDelete();
		}
		super.onReturnHome();
	}

	@Override
	public void onEnterZone(ZoneInstance zoneInstance) {
		if (zoneInstance.getAreaTemplate().getZoneName() == null) {
			log.error("No name found for a Zone in the map " + zoneInstance.getAreaTemplate().getWorldId());
		}
	}

	private void rewardSiegeNpc() {
		int totalDamage = getOwner().getAggroList().getTotalDamage();
		for (AggroInfo aggro : getOwner().getAggroList().getFinalDamageList(true)) {
			float percentage = aggro.getDamage() / totalDamage;
			List<Player> players = new ArrayList<Player>();
			if (aggro.getAttacker() instanceof Player) {
				Player player = (Player) aggro.getAttacker();
				if (MathUtil.isIn3dRange(player, getOwner(), GroupConfig.GROUP_MAX_DISTANCE)
						&& !player.getLifeStats().isAlreadyDead()) {
					int apPlayerReward = Math
							.round(StatFunctions.calculatePvEApGained(player, getOwner()) * percentage);
					AbyssPointsService.addAp(player, getOwner(), apPlayerReward);
				}
			} else if (aggro.getAttacker() instanceof PlayerGroup) {
				PlayerGroup group = (PlayerGroup) aggro.getAttacker();
				for (Player member : group.getMembers()) {
					if (MathUtil.isIn3dRange(member, getOwner(), GroupConfig.GROUP_MAX_DISTANCE)
							&& !member.getLifeStats().isAlreadyDead()) {
						players.add(member);
					}
				}
				if (!players.isEmpty()) {
					for (Player member : players) {
						int baseApReward = StatFunctions.calculatePvEApGained(member, getOwner());
						int apRewardPerMember = Math.round(baseApReward * percentage / players.size());
						if (apRewardPerMember > 0) {
							member.getCommonData().addAbyssFavor(1500); // 0.15% Abyss Favor Energy.
							PacketSendUtility.sendPacket(member, new SM_STATS_INFO(member));
							AbyssPointsService.addAp(member, getOwner(), apRewardPerMember);
						}
					}
				}
			} else if ((aggro.getAttacker() instanceof PlayerAlliance)) {
				PlayerAlliance alliance = (PlayerAlliance) aggro.getAttacker();
				players = new ArrayList<Player>();
				for (Player member : alliance.getMembers()) {
					if (MathUtil.isIn3dRange(member, getOwner(), GroupConfig.GROUP_MAX_DISTANCE)
							&& !member.getLifeStats().isAlreadyDead()) {
						players.add(member);
					}
				}
				if (!players.isEmpty()) {
					for (Player member : players) {
						int baseApReward = StatFunctions.calculatePvEApGained(member, getOwner());
						int apRewardPerMember = Math.round(baseApReward * percentage / players.size());
						if (apRewardPerMember > 0) {
							member.getCommonData().addAbyssFavor(1500); // 0.15% Abyss Favor Energy.
							PacketSendUtility.sendPacket(member, new SM_STATS_INFO(member));
							AbyssPointsService.addAp(member, getOwner(), apRewardPerMember);
						}
					}
				}
			}
		}
	}

	/**
	 * Schedule respawn of npc In instances - no npc respawn
	 */
	public Future<?> scheduleRespawn() {
		if (!getOwner().getSpawn().isNoRespawn()) {
			RespawnService.scheduleRespawnTask(getOwner());
		}
		return null;
	}

	public final float getAttackDistanceToTarget() {
		return getOwner().getGameStats().getAttackRange().getCurrent() / 1000f;
	}

	@Override
	public boolean useSkill(int skillId, int skillLevel) {
		SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		if (!getOwner().isSkillDisabled(skillTemplate)) {
			getOwner().getGameStats().renewLastSkillTime();
			return super.useSkill(skillId, skillLevel);
		}
		return false;
	}
}