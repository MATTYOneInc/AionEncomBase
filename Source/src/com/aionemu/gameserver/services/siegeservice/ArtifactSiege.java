/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.landing.LandingPointsEnum;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.OutpostService;
import com.aionemu.gameserver.services.RvrService;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ArtifactSiege extends Siege<ArtifactLocation>
{
	private static final Logger log = LoggerFactory.getLogger(ArtifactSiege.class.getName());
	
	public ArtifactSiege(ArtifactLocation siegeLocation) {
		super(siegeLocation);
	}
	
	@Override
	protected void onSiegeStart() {
		initSiegeBoss();
	}
	
	@Override
	protected void onSiegeFinish() {
		unregisterSiegeBossListeners();
		deSpawnNpcs(getSiegeLocationId());
		if (isBossKilled()) {
			onCapture();
			broadcastUpdate(getSiegeLocation());
		} else {
			log.error("Artifact siege (artifactId:" + getSiegeLocationId() + ") ended without killing a boss.");
		}
		spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.PEACE);
		DAOManager.getDAO(SiegeDAO.class).updateLocation(getSiegeLocation());
		getSiegeLocation().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				player.unsetInsideZoneType(ZoneType.SIEGE);
				player.getController().updateZone();
			    player.getController().updateNearbyQuests();
				if (isBossKilled() && (SiegeRace.getByRace(player.getRace()) == getSiegeLocation().getRace())) {
					QuestEngine.getInstance().onKill(new QuestEnv(getBoss(), player, 0, 0));
				}
			}
		});
		startSiege(getSiegeLocationId());
	}
	
	protected void onCapture() {
		SiegeRaceCounter wRaceCounter = getSiegeCounter().getWinnerRaceCounter();
		getSiegeLocation().setRace(wRaceCounter.getSiegeRace());
		Integer wLegionId = wRaceCounter.getWinnerLegionId();
		getSiegeLocation().setLegionId(wLegionId != null ? wLegionId : 0);
		if (getSiegeLocation().getRace() == SiegeRace.BALAUR) {
			final AionServerPacket lRacePacket = new SM_SYSTEM_MESSAGE(1320004, getSiegeLocation().getNameAsDescriptionId(), getSiegeLocation().getRace().getDescriptionId());
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player object) {
					PacketSendUtility.sendPacket(object, lRacePacket);
				}
			});
		} else {
			String wPlayerName = "";
			final Race wRace = wRaceCounter.getSiegeRace() == SiegeRace.ELYOS ? Race.ELYOS : Race.ASMODIANS;
			Legion wLegion = wLegionId != null ? LegionService.getInstance().getLegion(wLegionId) : null;
			if (!wRaceCounter.getPlayerDamageCounter().isEmpty()) {
				Integer wPlayerId = wRaceCounter.getPlayerDamageCounter().keySet().iterator().next();
				wPlayerName = PlayerService.getPlayerName(wPlayerId);
			}
			final String winnerName = wLegion != null ? wLegion.getLegionName() : wPlayerName;
			final AionServerPacket wRacePacket = new SM_SYSTEM_MESSAGE(1320002, wRace.getRaceDescriptionId(), winnerName, getSiegeLocation().getNameAsDescriptionId());
			final AionServerPacket lRacePacket = new SM_SYSTEM_MESSAGE(1320004, getSiegeLocation().getNameAsDescriptionId(), wRace.getRaceDescriptionId());
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, player.getRace().equals(wRace) ? wRacePacket : lRacePacket);
				}
			});
		}
		//Abyss Landing 4.9
		if (getSiegeLocation().getLocationId() == 1224 ||
		    getSiegeLocation().getLocationId() == 1401 ||
			getSiegeLocation().getLocationId() == 1402 ||
			getSiegeLocation().getLocationId() == 1403) {
			if (getSiegeLocation().getRace() == SiegeRace.BALAUR) {
				return;
			} if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
				AbyssLandingService.getInstance().updateRedemptionLanding(8000, LandingPointsEnum.ARTIFACT, false);
				AbyssLandingService.getInstance().updateHarbingerLanding(8000, LandingPointsEnum.ARTIFACT, true);
			} if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
				AbyssLandingService.getInstance().updateRedemptionLanding(8000, LandingPointsEnum.ARTIFACT, true);
				AbyssLandingService.getInstance().updateHarbingerLanding(8000, LandingPointsEnum.ARTIFACT, false);
			}
		}
		//Outpost 5.8
		if (getSiegeLocation().getLocationId() >= 8011 && getSiegeLocation().getLocationId() <= 8017 ||
		    getSiegeLocation().getLocationId() >= 9011 && getSiegeLocation().getLocationId() <= 9017) {
			if (getSiegeLocation().getRace() == SiegeRace.BALAUR) {
				OutpostService.getInstance().capture(getSiegeLocation().getOutpostId(), Race.NPC);
			} if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
				OutpostService.getInstance().capture(getSiegeLocation().getOutpostId(), Race.ASMODIANS);
			} if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
				OutpostService.getInstance().capture(getSiegeLocation().getOutpostId(), Race.ELYOS);
			}
		}

		if (getSiegeLocation().getLocationId() >= 4012 && getSiegeLocation().getLocationId() <= 4052) {
			if (getSiegeLocation().getRace() == SiegeRace.BALAUR) {
				OutpostService.getInstance().capture(getSiegeLocation().getOutpostId(), Race.NPC);
			} 
			if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
				OutpostService.getInstance().capture(getSiegeLocation().getOutpostId(), Race.ASMODIANS);
			} 
			if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
				OutpostService.getInstance().capture(getSiegeLocation().getOutpostId(), Race.ELYOS);
			}
		}

		//Iluma/Norsvold Artifact 5.8
		if (getSiegeLocation().getLocationId() == 8021 ||
		    getSiegeLocation().getLocationId() == 9021) {
			if (SiegeRace.BALAUR != getSiegeLocation().getRace()) {
				switch (getSiegeLocation().getLocationId()) {
					case 8021:
					    if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							RvrService.getInstance().startRvr(7);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//마족이 아스테라의 모든 기지를 점령하자 아스테라 수비대 지원 병력이 추가로 파견되었습니다.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_Occupy_All_Start_MSG, 0);
								}
							});
						} else if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							RvrService.getInstance().stopRvr(7);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//마족이 점령하고 있던 아스테라의 기지를 탈환하자 아스테라 수비대 지원 병력이 복귀했습니다.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LF6_Occupy_All_End_MSG, 0);
								}
							});
						}
					break;
					case 9021:
					    if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS) {
							RvrService.getInstance().startRvr(8);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//천족이 노스폴드의 모든 기지를 점령하자 노스폴드 수비대 지원 병력이 추가로 파견되었습니다.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_Occupy_All_Start_MSG, 0);
								}
							});
						} else if (getSiegeLocation().getRace() == SiegeRace.ELYOS) {
							RvrService.getInstance().stopRvr(8);
							World.getInstance().doOnAllPlayers(new Visitor<Player>() {
								@Override
								public void visit(Player player) {
									//천족이 점령하고 있던 노스폴드의 기지를 탈환하자 노스폴드 수비대 지원 병력이 복귀했습니다.
									PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_DF6_Occupy_All_End_MSG, 0);
								}
							});
						}
					break;
				}
			}
		}
	}
	
	@Override
	public boolean isEndless() {
		return true;
	}
	
	@Override
	public void addAbyssPoints(Player player, int abysPoints) {
	}
}