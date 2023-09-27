/*

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
package com.aionemu.gameserver.model.team2.common.service;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.gameobjects.player.XPCape;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.player.AtreianBestiaryService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.GrowthEnergy;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.StatFunctions;
import com.google.common.base.Predicate;

import java.util.ArrayList;
import java.util.List;

public class PlayerTeamDistributionService
{
	public static void doReward(TemporaryPlayerTeam<?> team, float damagePercent, Npc owner, AionObject winner) {
		if (team == null || owner == null) {
			return;
		}
		PlayerTeamRewardStats filteredStats = new PlayerTeamRewardStats(owner);
		team.applyOnMembers(filteredStats);
		if (filteredStats.players.isEmpty() || !filteredStats.hasLivingPlayer) {
			return;
		}
		long expReward;
		int kinahCount = 0;
		if (filteredStats.players.size() + filteredStats.mentorCount == 1) {
			expReward = (long) (StatFunctions.calculateSoloExperienceReward(filteredStats.players.get(0), owner));
		} else {
			expReward = (long) (StatFunctions.calculateGroupExperienceReward(filteredStats.highestLevel, owner));
		}
		//Party Bonus:
		//2 Members 10%
		int size = filteredStats.players.size();
		int bonus = 100;
		if (size > 1) {
			bonus = 150 + (size - 2) * 10;
		} for (Player member: filteredStats.players) {
			if (member.isMentor() || member.getLifeStats().isAlreadyDead()) {
				continue;
			}
			//Reward InGameShop.
			switch (member.getWorldId()) {
			    //Idian Depths.
				case 210090000:
			    case 220100000:
			        InGameShopEn.getInstance().addToll(member, (long) (0 * member.getRates().getTollRewardRate()));
			        PacketSendUtility.sendSys1Message(member, "\uE083", "Kamu Jangan Ngepet ya");
			    break;
			}
			//Berdin's Star.
			if (owner.getLevel() >= 10) {
				member.getCommonData().addBerdinStar(1575000); //0.14%
				PacketSendUtility.sendPacket(member, new SM_STATS_INFO(member));
			}
			//Aura Of Growth.
			if (owner.getLevel() >= 66) {
				if (Rnd.get(1, 100) < RateConfig.AURA_OF_GROWTH) {
					GrowthEnergy.getInstance().addGrowthEnergy(member);
					PacketSendUtility.sendPacket(member, new SM_STATS_INFO(member));
				}
			}
			//Atreian Bestiary.
			if (owner.getLevel() >= 66) {
			    AtreianBestiaryService.getInstance().onKill(member, owner.getNpcId());
			}
			//Auto Drop Kinah.
     		if (CustomConfig.AUTO_KINAH_ENABLED) {
				switch (member.getWorldId()) {
					case 210010000: //Poeta.
					case 220010000: //Ishalgen.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 500) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 210030000: //Verteron.
					case 220030000: //Altgard.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 1500) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 210020000: //Eltnen.
					case 220020000: //Morheim.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 2000) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 210040000: //Heiron.
					case 220040000: //Beluslan.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 2500) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 210060000: //Theobomos.
					case 220050000: //Brushtonin.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 3000) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 210050000: //Inggison.
					case 220070000: //Gelkmaros.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 3500) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 210070000: //Cygnea.
					case 220080000: //Enshar.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 4000) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 400010000: //Reshanta.
					case 400020000: //Belus.
					case 400040000: //Aspida.
					case 400050000: //Atanatos.
					case 400060000: //Disillon.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 4500) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 600090000: //Kaldor.
					case 600100000: //Levinshor.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 5000) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 210100000: //Iluma.
					case 220110000: //Norsvold.
					case 600040000: //Tiamaranta's Eye.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 5500) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					case 210090000: //Idian Depths E.
					case 220100000: //Idian Depths A.
						if (member.getLevel() < owner.getLevel() + 5) {
							kinahCount = Rnd.get(100, 6000) * member.getLevel();
						} else if (member.getLevel() > owner.getLevel() + 5) {
							kinahCount = 1000;
						}
					break;
					default:
						kinahCount = 0;
					break;
				} if (member.isInInstance() && member.getLevel() < owner.getLevel() + 5) {
					kinahCount = Rnd.get(100, 1000) * member.getLevel();
				} else if (member.isInInstance() && member.getLevel() > owner.getLevel() + 5) {
					kinahCount = 1000;
				}
				member.getInventory().increaseKinah(kinahCount);
     		}
			long rewardXp = (long) (expReward * bonus * member.getLevel()) / (filteredStats.partyLvlSum * 100);
			int rewardDp = StatFunctions.calculateGroupDPReward(member, owner);
			float rewardAp = 1;
			//Players 10 levels below highest member get 0 reward.
			if (filteredStats.highestLevel - member.getLevel() >= 10) {
				rewardXp = 0;
				rewardDp = 0;
			} else if (filteredStats.mentorCount > 0) {
				int cape = XPCape.values()[(int) member.getLevel()].value();
				if (cape < rewardXp) {
					rewardXp = cape;
				}
			}
			rewardXp *= damagePercent;
			rewardDp *= damagePercent;
			rewardAp *= damagePercent;
			//Reward XP Group (New system, Exp Retail NA)
			switch (member.getWorldId()) {
				case 301540000: //Archives Of Eternity.
				case 301550000: //Cradle Of Eternity.
				case 301560000: //Trials Of Eternity.
				case 301600000: //Adma's Fall.
				case 301610000: //Theobomos Test Chamber.
				case 301620000: //Drakenseer's Lair.
				case 301650000: //Ashunatal Dredgion.
				case 301660000: //Fallen Poeta.
				case 302200000: //Dredgion Defense: Sanctum.
				case 302300000: //Dredgion Defense: Pandaemonium.
				case 302340000: //Bastion Of Souls.
				case 302350000: //Evergale Canyon.
				case 310160000: //Divine Tower L.
				case 320160000: //Divine Tower D.
					member.getCommonData().addExp(Rnd.get(480000, 550000), RewardType.GROUP_HUNTING, owner.getObjectTemplate().getNameId());
				break;
				case 300040000: //Dark Poeta.
				case 300100000: //Steel Rake.
				case 300110000: //Baranath Dredgion.
				case 300170000: //Beshmundir Temple.
				case 300210000: //Chantra Dredgion.
				case 300300000: //Empyrean Crucible.
				case 300440000: //Terath Dredgion.
				case 300510000: //Tiamat Stronghold.
				case 300520000: //Dragon Lord's Refuge.
				case 300620000: //Occupied Rentus Base.
				case 300630000: //Anguished Dragon Lord's Refuge.
				case 300700000: //The Hexway.
				case 300800000: //Infinity Shard.
				case 301140000: //Seized Danuar Sanctuary.
				case 301380000: //Danuar Sanctuary.
				case 301390000: //Drakenspire Depths.
				case 310110000: //Theobomos Lab.
				case 320100000: //Fire Temple.
				case 320130000: //Adma Stronghold.
				case 320150000: //Padmarashka's Cave.
					member.getCommonData().addExp(Rnd.get(110000, 150000), RewardType.GROUP_HUNTING, owner.getObjectTemplate().getNameId());
				break;
				case 210100000: //Iluma.
				case 220110000: //Norsvold.
					AbyssPointsService.addAp(member, owner, Rnd.get(60, 100));
					member.getCommonData().addExp(Rnd.get(480000, 550000), RewardType.GROUP_HUNTING, owner.getObjectTemplate().getNameId());
				break;
				case 600040000: //Tiamaranta's Eye.
					member.getCommonData().addExp(Rnd.get(480000, 550000), RewardType.GROUP_HUNTING, owner.getObjectTemplate().getNameId());
				break;
				case 600090000: //Kaldor.
				case 600100000: //Levinshor.
					member.getCommonData().addExp(Rnd.get(50000, 100000), RewardType.GROUP_HUNTING, owner.getObjectTemplate().getNameId());
				break;
				default:
					member.getCommonData().addExp(rewardXp, RewardType.GROUP_HUNTING, owner.getObjectTemplate().getNameId());
				break;
			}
			member.getCommonData().addDp(rewardDp);
			if (owner.isRewardAP() && !(filteredStats.mentorCount > 0 && CustomConfig.MENTOR_GROUP_AP)) {
				rewardAp *= StatFunctions.calculatePvEApGained(member, owner);
				int ap = (int) rewardAp / filteredStats.players.size();
				if (ap >= 1) {
					member.getCommonData().addAbyssFavor(1500); //0.15% Abyss Favor Energy.
					AbyssPointsService.addAp(member, owner, ap);
					PacketSendUtility.sendPacket(member, new SM_STATS_INFO(member));
				}
			}
			if (CustomConfig.ENABLE_PVE_TOLL_REWARD) {
				if (Rnd.get(0, 100) > CustomConfig.TOLL_PVE_CHANCE) {
					for (String worldIds : CustomConfig.TOLL_PVE_WORLDID.split(",")) {
						if (member.getWorldId() == Integer.parseInt(worldIds)) {
							InGameShopEn.getInstance().addToll(member, CustomConfig.TOLL_PVE_QUANTITY);
							PacketSendUtility.sendMessage(member, "You have received " + CustomConfig.TOLL_PVE_QUANTITY+ " tolls from PvE!" );
						}
					}

				}
			}
		}
		Player mostDamagePlayer = owner.getAggroList().getMostPlayerDamageOfMembers(team.getMembers(), filteredStats.highestLevel);
		if (mostDamagePlayer == null) {
			return;
		} if (winner.equals(team) && (!owner.getAi2().getName().equals("chest") || filteredStats.mentorCount == 0)) {
			DropRegistrationService.getInstance().registerDrop(owner, mostDamagePlayer, filteredStats.highestLevel, filteredStats.players);
		}
	}
	
	private static class PlayerTeamRewardStats implements Predicate<Player> {
		final List<Player> players = new ArrayList<Player>();
		int partyLvlSum = 0;
		int highestLevel = 0;
		int mentorCount = 0;
		boolean hasLivingPlayer = false;
		Npc owner;
		public PlayerTeamRewardStats(Npc owner) {
			this.owner = owner;
		}
		@Override
		public boolean apply(Player member) {
			if (member.isOnline()) {
				if (MathUtil.isIn3dRange(member, owner, GroupConfig.GROUP_MAX_DISTANCE)) {
					QuestEngine.getInstance().onKill(new QuestEnv(owner, member, 0, 0));
					if (member.isMentor()) {
						mentorCount++;
						return true;
					}
					if (!hasLivingPlayer && !member.getLifeStats().isAlreadyDead()) {
						hasLivingPlayer = true;
					}
					players.add(member);
					partyLvlSum += member.getLevel();
					if (member.getLevel() > highestLevel) {
						highestLevel = member.getLevel();
					}
				}
			}
			return true;
		}
	}
}