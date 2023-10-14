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
package com.aionemu.gameserver.services.siegeservice;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.landing.LandingPointsEnum;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeLegionReward;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeReward;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.AbyssLandingSpecialService;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.MoltenusService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.mail.AbyssSiegeLevel;
import com.aionemu.gameserver.services.mail.MailFormatter;
import com.aionemu.gameserver.services.mail.SiegeResult;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneName;
import com.google.common.collect.Lists;

public class FortressSiege extends Siege<FortressLocation>
{
	private static final Logger log = LoggerFactory.getLogger("SIEGE_LOG");
	private final AbyssPointsListener addAPListener = new AbyssPointsListener(this);
	
	public FortressSiege(FortressLocation fortress) {
		super(fortress);
	}
	
	@Override
	public void onSiegeStart() {
		getSiegeLocation().setVulnerable(true);
		getSiegeLocation().setUnderShield(true);
		broadcastState(getSiegeLocation());
		getSiegeLocation().clearLocation();
		GlobalCallbackHelper.addCallback(addAPListener);
		deSpawnNpcs(getSiegeLocationId());
		clearPlayers();
		//BattlefieldUnionService.getInstance().onSiegeStart(getSiegeLocation().getLocationId());
		spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.SIEGE);
		initSiegeBoss();
		if (getSiegeLocation().getLocationId() == 1131) {
			switch (getSiegeLocation().getLocationId()) {
				case 1131: //Siel's Western Fortress.
					BaseService.getInstance().capture(108, Race.NPC);
					BaseService.getInstance().capture(109, Race.NPC);
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							//The Gold Sand Negotiation Team is under attack by the Balaur.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_War_Soon, 0);
							//The Balaur have taken control of the Bomishung at Siel's Left Wing.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuDr_05, 6000);
							//The Bomishung at Siel's Left Wing is under attack by the Balaur.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_AtkDr_05, 12000);
							//The Balaur have taken control of the Shairing at the Island of Storm.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuDr_04, 18000);
							//The Shairing at the Island of Storm is under attack by the Balaur.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_AtkDr_04, 24000);
						}
					});
				break;
			}
		} else if (getSiegeLocation().getLocationId() == 1132) {
			switch (getSiegeLocation().getLocationId()) {
				case 1132: //Siel's Eastern Fortress.
					BaseService.getInstance().capture(110, Race.NPC);
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							//The Balaur have taken control of the Sasming at Siel's Right Wing.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuDr_06, 30000);
							//The Sasming at Siel's Right Wing is under attack by the Balaur.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_AtkDr_06, 36000);
						}
					});
				break;
			}
		} else if (getSiegeLocation().getLocationId() == 1141) {
			switch (getSiegeLocation().getLocationId()) {
				case 1141: //Sulfur Fortress.
					BaseService.getInstance().capture(105, Race.NPC);
					BaseService.getInstance().capture(106, Race.NPC);
					BaseService.getInstance().capture(107, Race.NPC);
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							//The Balaur have taken control of the Oharung at the Sulfur Tree Archipelago.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuDr_01, 42000);
							//The Oharung at the Sulfur Tree Archipelago is under attack by the Balaur.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_AtkDr_01, 50000);
							//The Balaur have taken control of the Joarin at Zephyr Island.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuDr_02, 56000);
							//The Joarin at Zephyr Island is under attack by the Balaur.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_AtkDr_02, 62000);
							//The Balaur have taken control of the Temirun at Leibo Island.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_OccuDr_03, 68000);
							//The Temirun at Leibo Island is under attack by the Balaur.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoShip_AtkDr_03, 74000);
						}
					});
				break;
			}
		} else if (getSiegeLocation().getLocationId() == 10111) {
			switch (getSiegeLocation().getLocationId()) {
				case 10111:
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
						@Override
						public void visit(Player player) {
							//The Temple Gate will open in 5 minutes.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START01, 0);
							//The Temple Gate will open in 1 minute.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START02, 240000);
							//The Temple Gate will open in 30 seconds.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START03, 270000);
							//The Temple Gate will open in 10 seconds.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START04, 290000);
							//The Temple Gate has opened.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START05, 300000);
						}
					});
				break;
			}
		}
	}
	
	@Override
	public void onSiegeFinish() {
		GlobalCallbackHelper.removeCallback(addAPListener);
		unregisterSiegeBossListeners();
		SiegeService.getInstance().deSpawnNpcs(getSiegeLocationId());
		getSiegeLocation().setVulnerable(false);
		getSiegeLocation().setUnderShield(false);
		if (isBossKilled()) {
			onCapture();
			applyBuff();
			broadcastUpdate(getSiegeLocation());
		} else {
			broadcastState(getSiegeLocation());
		}
		SiegeService.getInstance().spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.PEACE);
		if (SiegeRace.BALAUR != getSiegeLocation().getRace()) {
			if (getSiegeLocation().getLegionId() > 0) {
				giveRewardsToLegion();
			}
			giveRewardsToPlayers(getSiegeCounter().getRaceCounter(getSiegeLocation().getRace()));
		}
		DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(getSiegeLocation());
		getSiegeLocation().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				player.unsetInsideZoneType(ZoneType.SIEGE);
				player.getController().updateZone();
			    player.getController().updateNearbyQuests();
				if (isBossKilled() && (SiegeRace.getByRace(player.getRace()) == getSiegeLocation().getRace())) {
					QuestEngine.getInstance().onKill(new QuestEnv(getBoss(), player, 0, 0));
				}
				//Enraged Guardian 5.3
				switch (getSiegeLocationId()) {
					case 1131: //Siel's Western Fortress.
						if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							MoltenusService.getInstance().startMoltenus(5);
						} if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							MoltenusService.getInstance().startMoltenus(8);
						}
					break;
					case 1132: //Siel's Eastern Fortress.
						if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							MoltenusService.getInstance().startMoltenus(6);
						} if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							MoltenusService.getInstance().startMoltenus(9);
						}
					break;
					case 1141: //Sulfur Fortress.
						if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							MoltenusService.getInstance().startMoltenus(4);
						} if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							MoltenusService.getInstance().startMoltenus(7);
						}
					break;
				}
			}
		});
	}
	
	public void onCapture() {
		SiegeRaceCounter winner = getSiegeCounter().getWinnerRaceCounter();
		SiegeRace looser = getSiegeLocation().getRace();
		getSiegeLocation().setRace(winner.getSiegeRace());
		getArtifact().setRace(winner.getSiegeRace());
		if (SiegeRace.BALAUR == winner.getSiegeRace()) {
			getSiegeLocation().setLegionId(0);
			getArtifact().setLegionId(0);
		} else {
			Integer topLegionId = winner.getWinnerLegionId();
			getSiegeLocation().setLegionId(topLegionId != null ? topLegionId : 0);
			getArtifact().setLegionId(topLegionId != null ? topLegionId : 0);
		}
		//Abyss Landing 4.9.1
		if (getSiegeLocation().getLocationId() == 1131 ||
			getSiegeLocation().getLocationId() == 1132 ||
			getSiegeLocation().getLocationId() == 1141 ||
		    getSiegeLocation().getLocationId() == 1221 ||
		    getSiegeLocation().getLocationId() == 1231 ||
			getSiegeLocation().getLocationId() == 1241) {
			Player player = null;
			if (SiegeRace.BALAUR != getSiegeLocation().getRace()) {
				switch (getSiegeLocation().getLocationId()) {
					//Siel's Western Fortress.
					case 1131:
					    if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							//Shairing At Carpus Isle.
							BaseService.getInstance().capture(108, Race.ASMODIANS);
							//Bomishung At Siel's Left Wing.
							BaseService.getInstance().capture(109, Race.ASMODIANS);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//The Steel Rose Mercenaries hired by the Asmodians have arrived at the Siel's Western Fortress.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_D_02, 0);
								}
							});
						} if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							//Shairing At Carpus Isle.
							BaseService.getInstance().capture(108, Race.ELYOS);
							//Bomishung At Siel's Left Wing.
							BaseService.getInstance().capture(109, Race.ELYOS);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//The Steel Rose Mercenaries hired by the Elyos have arrived at the Siel's Western Fortress.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_L_02, 0);
								}
							});
						}
					break;
					//Siel's Eastern Fortress.
					case 1132:
					    if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							//Sasming At Siel's Right Wing.
							BaseService.getInstance().capture(110, Race.ASMODIANS);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//The Steel Rose Mercenaries hired by the Asmodians have arrived at the Siel's Eastern Fortress.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_D_03, 0);
								}
							});
						} if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							//Sasming At Siel's Right Wing.
							BaseService.getInstance().capture(110, Race.ELYOS);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//The Steel Rose Mercenaries hired by the Elyos have arrived at the Siel's Eastern Fortress.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_L_03, 0);
								}
							});
						}
					break;
					//Sulfur Fortress.
					case 1141:
					    if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							//Oharung At The Sulfur Archipelago.
							BaseService.getInstance().capture(105, Race.ASMODIANS);
							//Joarin At Zephyr Island.
					        BaseService.getInstance().capture(106, Race.ASMODIANS);
							//Temirun At Leibo Island.
					        BaseService.getInstance().capture(107, Race.ASMODIANS);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//The Steel Rose Mercenaries hired by the Asmodians have arrived at the Sulfur Fortress.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_D_01, 0);
								}
							});
						} if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							//Oharung At The Sulfur Archipelago.
							BaseService.getInstance().capture(105, Race.ELYOS);
							//Joarin At Zephyr Island.
					        BaseService.getInstance().capture(106, Race.ELYOS);
							//Temirun At Leibo Island.
					        BaseService.getInstance().capture(107, Race.ELYOS);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//The Steel Rose Mercenaries hired by the Elyos have arrived at the Sulfur Fortress.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_ShugoSoldier_L_01, 0);
								}
							});
						}
					break;
					//Krotan Refuge.
					case 1221:
						if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							AbyssLandingSpecialService.getInstance().startLanding(16);
							AbyssLandingService.getInstance().updateHarbingerLanding(35000, LandingPointsEnum.SIEGE, true);
						} if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							AbyssLandingSpecialService.getInstance().startLanding(4);
							AbyssLandingService.getInstance().updateRedemptionLanding(35000, LandingPointsEnum.SIEGE, true);
						}
					break;
					//Kysis Fortress.
					case 1231:
						if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							AbyssLandingSpecialService.getInstance().startLanding(18);
							AbyssLandingService.getInstance().updateHarbingerLanding(40000, LandingPointsEnum.SIEGE, true);
						} if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							AbyssLandingSpecialService.getInstance().startLanding(6);
							AbyssLandingService.getInstance().updateRedemptionLanding(40000, LandingPointsEnum.SIEGE, true);
						}
					break;
					//Miren Fortress.
					case 1241:
						if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							AbyssLandingSpecialService.getInstance().startLanding(17);
							AbyssLandingService.getInstance().updateHarbingerLanding(35000, LandingPointsEnum.SIEGE, true);
						} if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							AbyssLandingSpecialService.getInstance().startLanding(5);
							AbyssLandingService.getInstance().updateRedemptionLanding(35000, LandingPointsEnum.SIEGE, true);
						}
					break;
				}
				AbyssLandingService.getInstance().AnnounceToPoints(player, getSiegeLocation().getRace().getDescriptionId(), getSiegeLocation().getNameAsDescriptionId(), 0, LandingPointsEnum.SIEGE);
			} if (SiegeRace.BALAUR == getSiegeLocation().getRace() || winner.getSiegeRace() != looser) {
				switch (getSiegeLocation().getLocationId()) {
					//Siel's Western Fortress.
					case 1131:
					    if (looser == SiegeRace.ASMODIANS) {
							//Shairing At Carpus Isle.
							BaseService.getInstance().capture(108, Race.NPC);
							//Bomishung At Siel's Left Wing.
							BaseService.getInstance().capture(109, Race.NPC);
						} if (looser == SiegeRace.ELYOS) {
							//Shairing At Carpus Isle.
							BaseService.getInstance().capture(108, Race.NPC);
							//Bomishung At Siel's Left Wing.
							BaseService.getInstance().capture(109, Race.NPC);
						}
					break;
					//Siel's Eastern Fortress.
					case 1132:
					    if (looser == SiegeRace.ASMODIANS) {
							//Sasming At Siel's Right Wing.
							BaseService.getInstance().capture(110, Race.NPC);
						} if (looser == SiegeRace.ELYOS) {
							//Sasming At Siel's Right Wing.
							BaseService.getInstance().capture(110, Race.NPC);
						}
					break;
					//Sulfur Fortress.
					case 1141:
					    if (looser == SiegeRace.ASMODIANS) {
							//Oharung At The Sulfur Archipelago.
							BaseService.getInstance().capture(105, Race.NPC);
							//Joarin At Zephyr Island.
					        BaseService.getInstance().capture(106, Race.NPC);
							//Temirun At Leibo Island.
					        BaseService.getInstance().capture(107, Race.NPC);
						} if (looser == SiegeRace.ELYOS) {
							//Oharung At The Sulfur Archipelago.
							BaseService.getInstance().capture(105, Race.NPC);
							//Joarin At Zephyr Island.
					        BaseService.getInstance().capture(106, Race.NPC);
							//Temirun At Leibo Island.
					        BaseService.getInstance().capture(107, Race.NPC);
						}
					break;
					//Krotan Refuge.
					case 1221:
					    if (looser == SiegeRace.ASMODIANS) {
							AbyssLandingSpecialService.getInstance().stopLanding(16);
							AbyssLandingService.getInstance().updateHarbingerLanding(35000, LandingPointsEnum.SIEGE, false);
						} if (looser == SiegeRace.ELYOS) {
							AbyssLandingSpecialService.getInstance().stopLanding(4);
							AbyssLandingService.getInstance().updateRedemptionLanding(35000, LandingPointsEnum.SIEGE, false);
						}
					break;
					//Kysis Fortress.
					case 1231:
						if (looser == SiegeRace.ASMODIANS) {
							AbyssLandingSpecialService.getInstance().stopLanding(18);
							AbyssLandingService.getInstance().updateHarbingerLanding(40000, LandingPointsEnum.SIEGE, false);
						} if (looser == SiegeRace.ELYOS) {
							AbyssLandingSpecialService.getInstance().stopLanding(6);
							AbyssLandingService.getInstance().updateRedemptionLanding(40000, LandingPointsEnum.SIEGE, false);
						}
					break;
					//Miren Fortress.
					case 1241:
						if (looser == SiegeRace.ASMODIANS) {
							AbyssLandingSpecialService.getInstance().stopLanding(17);
							AbyssLandingService.getInstance().updateHarbingerLanding(35000, LandingPointsEnum.SIEGE, false);
						} if (looser == SiegeRace.ELYOS) {
							AbyssLandingSpecialService.getInstance().stopLanding(5);
							AbyssLandingService.getInstance().updateRedemptionLanding(35000, LandingPointsEnum.SIEGE, false);
						}
					break;
				}
			}
		}
	}
	
	public void applyBuff() {
		SiegeRaceCounter winner = getSiegeCounter().getWinnerRaceCounter();
		getSiegeLocation().setRace(winner.getSiegeRace());
		getArtifact().setRace(winner.getSiegeRace());
		if (SiegeRace.BALAUR == winner.getSiegeRace()) {
			getSiegeLocation().setLegionId(0);
			getArtifact().setLegionId(0);
		} else {
			Integer topLegionId = winner.getWinnerLegionId();
			getSiegeLocation().setLegionId(topLegionId != null ? topLegionId : 0);
			getArtifact().setLegionId(topLegionId != null ? topLegionId : 0);
		}
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//Buff for Both Race.
				if (player.getEffectController().hasAbnormalEffect(getSiegeLocation().getBuffId())) {
		    		player.getEffectController().removeEffect(getSiegeLocation().getBuffId());
				} else {
					SkillEngine.getInstance().applyEffectDirectly(getSiegeLocation().getBuffId(), player, player, 0);
				}
				//Buff for Asmodians or Elyos.
				if (player.getEffectController().hasAbnormalEffect(getSiegeLocation().getBuffIdA())) {
		    		player.getEffectController().removeEffect(getSiegeLocation().getBuffIdA());
				} if (player.getEffectController().hasAbnormalEffect(getSiegeLocation().getBuffIdE())) {
		    		player.getEffectController().removeEffect(getSiegeLocation().getBuffIdE());
				} if (player.getCommonData().getRace() == Race.ASMODIANS) {
				    SkillEngine.getInstance().applyEffectDirectly(getSiegeLocation().getBuffIdA(), player, player, 0);
				} if (player.getCommonData().getRace() == Race.ELYOS) {
				    SkillEngine.getInstance().applyEffectDirectly(getSiegeLocation().getBuffIdE(), player, player, 0);
				}
			}
		});
	}
	
	@Override
	public boolean isEndless() {
		return false;
	}
	
	@Override
	public void addAbyssPoints(Player player, int abysPoints) {
		getSiegeCounter().addAbyssPoints(player, abysPoints);
	}
	
	protected void giveRewardsToLegion() {
		if (isBossKilled()) {
			return;
		} if (getSiegeLocation().getLegionId() == 0) {
			return;
		}
		List<SiegeLegionReward> legionRewards = getSiegeLocation().getLegionReward();
		SiegeResult resultLegion = isBossKilled() ? SiegeResult.OCCUPY : SiegeResult.DEFENDER;
		int legionBGeneral = LegionService.getInstance().getLegionBGeneral(getSiegeLocation().getLegionId());
		if (legionBGeneral != 0) {
			PlayerCommonData BGeneral = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(legionBGeneral);
			if (legionRewards != null) {
				for (SiegeLegionReward medalsType : legionRewards) {
					MailFormatter.sendAbyssRewardMail(getSiegeLocation(), BGeneral, AbyssSiegeLevel.VETERAN_SOLDIER, resultLegion, System.currentTimeMillis(), medalsType.getItemId(), medalsType.getCount() * SiegeConfig.SIEGE_MEDAL_RATE, 0);
				}
			}
		}
	}
	
	public boolean isInSiegeZone(Player player) {
		if (player.isInsideZone(ZoneName.get("EYE_OF_RESHANTA_400010000")) ||
		    player.isInsideZone(ZoneName.get("DIVINE_FORTRESS_400010000")) ||
		    player.isInsideZone(ZoneName.get("KROTAN_REFUGE_400010000")) ||
			player.isInsideZone(ZoneName.get("KROTAN_ROCK_400010000")) ||
			player.isInsideZone(ZoneName.get("RATTLEFROST_OUTPOST_400010000")) ||
			player.isInsideZone(ZoneName.get("BLOODBURN_REACH_400010000")) ||
			player.isInsideZone(ZoneName.get("SLIVERSLEET_OUTPOST_400010000")) ||
			player.isInsideZone(ZoneName.get("MIREN_FORTRESS_400010000")) ||
			player.isInsideZone(ZoneName.get("MIREN_ISLAND_400010000")) ||
			player.isInsideZone(ZoneName.get("COLDFORGE_OUTPOST_400010000")) ||
			player.isInsideZone(ZoneName.get("SHIMMERFROST_OUTPOST_400010000")) ||
			player.isInsideZone(ZoneName.get("ICEHOWL_OUTPOST_400010000")) ||
			player.isInsideZone(ZoneName.get("KYSIS_FORTRESS_400010000")) ||
			player.isInsideZone(ZoneName.get("KYSIS_ISLE_400010000")) ||
			player.isInsideZone(ZoneName.get("CHILLHAUNT_OUTPOST_400010000")) ||
			player.isInsideZone(ZoneName.get("SIEL_EASTERN_FORTRESS_400010000")) ||
			player.isInsideZone(ZoneName.get("SIEL_RIGHT_WING_A_400010000")) ||
			player.isInsideZone(ZoneName.get("SIEL_RIGHT_WING_B_400010000")) ||
			player.isInsideZone(ZoneName.get("SIEL_LEFT_WING_A_400010000")) ||
			player.isInsideZone(ZoneName.get("SIEL_LEFT_WING_B_400010000")) ||
			player.isInsideZone(ZoneName.get("WING_OF_SIEL_ARCHIPELAGO_A_400010000")) ||
			player.isInsideZone(ZoneName.get("WING_OF_SIEL_ARCHIPELAGO_B_400010000")) ||
			player.isInsideZone(ZoneName.get("HEART_OF_SIEL_400010000")) ||
			player.isInsideZone(ZoneName.get("SIEL_WESTERN_FORTRESS_400010000")) ||
			player.isInsideZone(ZoneName.get("ISLE_OF_DISGRACE_400010000")) ||
			player.isInsideZone(ZoneName.get("ISLE_OF_ROOT_400010000")) ||
			player.isInsideZone(ZoneName.get("ISLE_OF_REPROACH_400010000")) ||
			player.isInsideZone(ZoneName.get("SULFUR_FLOW_400010000")) ||
			player.isInsideZone(ZoneName.get("SULFUR_SWAMP_400010000")) ||
			player.isInsideZone(ZoneName.get("SULFUR_FORTRESS_400010000")) ||
			player.isInsideZone(ZoneName.get("KRAKON_DISPUTE_400010000")) ||
			player.isInsideZone(ZoneName.get("SULFUR_ARCHIPELAGO_400010000")) ||
			player.isInsideZone(ZoneName.get("WESTERN_RIDGE_400010000")) ||
			player.isInsideZone(ZoneName.get("NORTHERN_RIDGE_400010000")) ||
			player.isInsideZone(ZoneName.get("EASTERN_RIDGE_400010000")) ||
			player.isInsideZone(ZoneName.get("SOUTHERN_RIDGE_400010000")) ||
			player.isInsideZone(ZoneName.get("HEROS_FALL_600090000")) ||
			player.isInsideZone(ZoneName.get("ASHEN_GLADE_600090000")) ||
			player.isInsideZone(ZoneName.get("WEALHTHEOWS_KEEP_600090000")) ||
			player.isInsideZone(ZoneName.get("WEALHTHEOWS_KEEP_RUINS_600090000")) ||
			player.isInsideZone(ZoneName.get("MOLTEN_CLIFFS_600090000")) ||
			player.isInsideZone(ZoneName.get("SOUTH_ROAD_600090000")) ||
			player.isInsideZone(ZoneName.get("SMOLDERING_CRAG_600090000")) ||
			player.isInsideZone(ZoneName.get("ANOHA_PASS_600090000")) ||
			player.isInsideZone(ZoneName.get("ANOHA_BINDING_600090000"))) {
			return true;
		}
		return false;
	}
	
	public void clearPlayers() {
		for (Player player: getSiegeLocation().getPlayers().values()) {
			int worldId = getSiegeLocation().getWorldId();
			if (getSiegeLocation().isEnemy(player) && isInSiegeZone(player)) {
				switch (worldId) {
					case 400010000: //Reshanta.
						if (player.getRace() == Race.ASMODIANS) {
							TeleportService2.teleportTo(player, 400010000, 576.90533f, 2542.4539f, 1636.0665f, (byte) 30); //Primum Landing.
						} else if (player.getRace() == Race.ELYOS) {
							TeleportService2.teleportTo(player, 400010000, 2259.2463f, 663.3353f, 1527.9968f, (byte) 94); //Teminon Landing.
						}
					break;
					case 600090000: //Kaldor.
						if (player.getRace() == Race.ASMODIANS) {
							TeleportService2.teleportTo(player, 600090000, 408.1886f, 1359.2572f, 163.51178f, (byte) 96); //Rubirinerk's Settlement.
						} else if (player.getRace() == Race.ELYOS) {
							TeleportService2.teleportTo(player, 600090000, 1302.5714f, 1315.4507f, 199.75026f, (byte) 97); //Saparinerk's Settlement.
						}
					break;
				}
			}
		}
	}
	
	protected void giveRewardsToPlayers(SiegeRaceCounter winnerDamage) {
		Map<Integer, Long> playerAbyssPoints = winnerDamage.getPlayerAbyssPoints();
		List<Integer> topPlayersIds = Lists.newArrayList(playerAbyssPoints.keySet());
		Map<Integer, String> playerNames = PlayerService.getPlayerNames(playerAbyssPoints.keySet());
		SiegeResult resultPlayers = isBossKilled() ? SiegeResult.OCCUPY : SiegeResult.DEFENDER;
		int i = 0;
		List<SiegeReward> playerRewards = getSiegeLocation().getReward();
		for (SiegeReward topGrade : playerRewards) {
			for (int rewardedPC = 0; i < topPlayersIds.size() && rewardedPC < topGrade.getTop(); ++i) {
				Integer playerId = topPlayersIds.get(i);
				PlayerCommonData pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(playerId);
				++rewardedPC;
				MailFormatter.sendAbyssRewardMail(getSiegeLocation(), pcd, AbyssSiegeLevel.VETERAN_SOLDIER, resultPlayers, System.currentTimeMillis(), topGrade.getItemId(), topGrade.getCount() * SiegeConfig.SIEGE_MEDAL_RATE, 0);
			}
		}
	}
	
	protected ArtifactLocation getArtifact() {
		return SiegeService.getInstance().getFortressArtifacts().get(getSiegeLocationId());
	}
	
	protected boolean hasArtifact() {
		return getArtifact() != null;
	}
}