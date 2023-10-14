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
package com.aionemu.gameserver.services.teleport;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.team2.league.League;
import com.aionemu.gameserver.model.templates.InstanceCooltime;
import com.aionemu.gameserver.model.templates.portal.ItemReq;
import com.aionemu.gameserver.model.templates.portal.PortalLoc;
import com.aionemu.gameserver.model.templates.portal.PortalPath;
import com.aionemu.gameserver.model.templates.portal.PortalReq;
import com.aionemu.gameserver.model.templates.portal.QuestReq;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMapInstance;

public class PortalService
{
	private static Logger log = LoggerFactory.getLogger(PortalService.class);
	
	public static void port(final PortalPath portalPath, final Player player, int npcObjectId) {
		if (!CustomConfig.ENABLE_INSTANCES) {
			return;
		}
		PortalLoc loc = DataManager.PORTAL_LOC_DATA.getPortalLoc(portalPath.getLocId());
		if (loc == null) {
			log.warn("No portal loc for locId" + portalPath.getLocId());
			return;
		}
		boolean instanceTitleReq = false;
		boolean instanceLevelReq = false;
		boolean instanceRaceReq = false;
		boolean instanceQuestReq = false;
		boolean instanceGroupReq = false;
		boolean instanceItemReq = false;
		int instanceCooldownRate = 0;
		int mapId = loc.getWorldId();
		int playerSize = portalPath.getPlayerCount();
		boolean isInstance = portalPath.isInstance();
		InstanceCooltime clt = DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(mapId);
		if (player.getAccessLevel() < AdminConfig.INSTANCE_REQ) {
			instanceTitleReq = !player.havePermission(MembershipConfig.INSTANCES_TITLE_REQ);
			instanceLevelReq = !player.havePermission(MembershipConfig.INSTANCES_LEVEL_REQ);
			instanceRaceReq = !player.havePermission(MembershipConfig.INSTANCES_RACE_REQ);
			instanceQuestReq = !player.havePermission(MembershipConfig.INSTANCES_QUEST_REQ);
			instanceGroupReq = !player.havePermission(MembershipConfig.INSTANCES_GROUP_REQ);
			instanceItemReq = !player.havePermission(MembershipConfig.INSTANCES_ITEM_REQ);
			instanceCooldownRate = InstanceService.getInstanceRate(player, loc.getWorldId());
		} if (instanceRaceReq && !checkRace(player, portalPath.getRace())) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MOVE_PORTAL_ERROR_INVALID_RACE);
			return;
		} if (instanceGroupReq && !checkPlayerSize(player, portalPath, npcObjectId)) {
			return;
		}
		int siegeId = portalPath.getSiegeId();
		if (instanceRaceReq && siegeId != 0 && !checkSiegeId(player, siegeId)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MOVE_PORTAL_ERROR_INVALID_RACE);
			return;
		}
		PortalReq portalReq = portalPath.getPortalReq();
		if (portalReq != null) {
			if (instanceLevelReq && !checkEnterLevel(player, mapId, portalReq, npcObjectId)) {
				return;
			} if (instanceQuestReq && !checkQuestsReq(player, npcObjectId, portalReq.getQuestReq())) {
				return;
			} if (instanceItemReq && !checkItemReq(player, npcObjectId, portalReq.getItemReq())) {
				return;
			}
			int titleId = portalReq.getTitleId();
			if (instanceTitleReq && titleId != 0) {
				if (!checkTitle(player, titleId)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_DIRECT_PORTAL_NOT_TITLE);
					return;
				}
			} if (!checkKinah(player, portalReq.getKinahReq())) {
				return;
			}
		}
		boolean reenter = false;
		int useDelay = 0;
		int instanceCooldown = 0;
		if (clt != null) {
            instanceCooldown = clt.getEntCoolTime();
        } if (instanceCooldownRate > 0) {
            useDelay = instanceCooldown / instanceCooldownRate;
        }
		WorldMapInstance instance = null;
		if (player.getPortalCooldownList().isPortalUseDisabled(mapId) && useDelay > 0) {
			switch (playerSize) {
				case 0:
					instance = InstanceService.getRegisteredInstance(mapId, player.getObjectId());
				break;
				case 3:
					if (player.getPlayerGroup2() != null) {
						instance = InstanceService.getRegisteredInstance(mapId, player.getPlayerGroup2().getTeamId());
					}
				break;
				case 6:
					if (player.getPlayerGroup2() != null) {
						instance = InstanceService.getRegisteredInstance(mapId, player.getPlayerGroup2().getTeamId());
					}
				break;
				default:
					if (player.isInAlliance2()) {
						if (player.isInLeague()) {
							instance = InstanceService.getRegisteredInstance(mapId, player.getPlayerAlliance2().getLeague().getObjectId());
						} else {
							instance = InstanceService.getRegisteredInstance(mapId, player.getPlayerAlliance2().getObjectId());
						}
					}
				break;
			} if (instance == null) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_MAKE_INSTANCE_COOL_TIME);
				return;
			} else {
				if (!instance.isRegistered(player.getObjectId())) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_MAKE_INSTANCE_COOL_TIME);
					return;
				} else {
					reenter = true;
					log.debug(player.getName() + "has been in intance and also have cd, can reenter.");
				}
			}
		} else {
			log.debug(player.getName() + "doesn't have cd of this instance, can enter and will be registed to this intance");
		}
		PlayerGroup group = player.getPlayerGroup2();
		switch (playerSize) {
			case 0:
				if (group != null && !instanceGroupReq) {
					instance = InstanceService.getRegisteredInstance(mapId, group.getTeamId());
				} else {
					instance = InstanceService.getRegisteredInstance(mapId, player.getObjectId());
				} if (instance == null && group != null && !instanceGroupReq) {
					for (Player member : group.getMembers()) {
						instance = InstanceService.getRegisteredInstance(mapId, member.getObjectId());
						if (instance != null) {
							break;
						}
					} if (instance == null && isInstance) {
						instance = registerGroup(group, mapId);
					}
				} if (instance != null) {
					if (loc.getWorldId() != player.getWorldId()) {
						reenter = true;
						transfer(player, loc, instance, reenter);
						return;
					}
				}
				port(player, loc, reenter, isInstance);
			break;
			case 3:
				if (group != null || !instanceGroupReq) {
					if (group != null) {
						instance = InstanceService.getRegisteredInstance(mapId, group.getTeamId());
					} else {
						instance = InstanceService.getRegisteredInstance(mapId, player.getObjectId());
					} if (instance == null && group != null && !instanceGroupReq) {
						for (Player member : group.getMembers()) {
							instance = InstanceService.getRegisteredInstance(mapId, member.getObjectId());
							if (instance != null) {
								break;
							}
						} if (instance == null) {
							instance = registerGroup(group, mapId);
						}
					} else if (instance == null && instanceGroupReq) {
						instance = registerGroup(group, mapId);
					} else if (instance == null && !instanceGroupReq && group == null) {
						instance = InstanceService.getNextAvailableInstance(mapId);
					}
					transfer(player, loc, instance, reenter);
				}
			break;
			case 6:
				if (group != null || !instanceGroupReq) {
					if (group != null) {
						instance = InstanceService.getRegisteredInstance(mapId, group.getTeamId());
					} else {
						instance = InstanceService.getRegisteredInstance(mapId, player.getObjectId());
					} if (instance == null && group != null && !instanceGroupReq) {
						for (Player member : group.getMembers()) {
							instance = InstanceService.getRegisteredInstance(mapId, member.getObjectId());
							if (instance != null) {
								break;
							}
						} if (instance == null) {
							instance = registerGroup(group, mapId);
						}
					} else if (instance == null && instanceGroupReq) {
						instance = registerGroup(group, mapId);
					} else if (instance == null && !instanceGroupReq && group == null) {
						instance = InstanceService.getNextAvailableInstance(mapId);
					}
					transfer(player, loc, instance, reenter);
				}
			break;
			default:
				PlayerAlliance allianceGroup = player.getPlayerAlliance2();
				if (allianceGroup != null || !instanceGroupReq) {
					Integer allianceId = player.getObjectId();
					League league = null;
					if (allianceGroup != null) {
						league = allianceGroup.getLeague();
						if (player.isInLeague()) {
							allianceId = league.getObjectId();
						} else {
							allianceId = allianceGroup.getObjectId();
							instance = InstanceService.getRegisteredInstance(mapId, allianceId);
						}
					} else {
						instance = InstanceService.getRegisteredInstance(mapId, allianceId);
					} if (instance == null && allianceGroup != null && !instanceGroupReq) {
						if (league != null) {
							for (PlayerAlliance alliance : allianceGroup.getLeague().getMembers()) {
								for (Player member : alliance.getMembers()) {
									instance = InstanceService.getRegisteredInstance(mapId, member.getObjectId());
									if (instance != null) {
										break;
									}
								}
							}
						} else {
							for (Player member : allianceGroup.getMembers()) {
								instance = InstanceService.getRegisteredInstance(mapId, member.getObjectId());
								if (instance != null) {
									break;
								}
							}
						} if (instance == null) {
							if (league != null) {
								instance = registerLeague(league, mapId);
							} else {
								instance = registerAlliance(allianceGroup, mapId);
							}
						}
					} else if (instance == null && instanceGroupReq) {
						if (league != null) {
							instance = registerLeague(league, mapId);
						} else {
							instance = registerAlliance(allianceGroup, mapId);
						}
					} else if (instance == null && !instanceGroupReq && allianceGroup == null) {
						instance = InstanceService.getNextAvailableInstance(mapId);
					} if (instance.getPlayersInside().size() < playerSize) {
						transfer(player, loc, instance, reenter);
					}
				}
			break;
		}
	}
	
	private static boolean checkKinah(Player player, int kinah) {
		Storage inventory = player.getInventory();
		if (!inventory.tryDecreaseKinah(kinah)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_KINA(kinah));
			return false;
		}
		return true;
	}
	
	private static boolean checkEnterLevel(Player player, int mapId, PortalReq portalReq, int npcObjectId) {
		int enterMinLvl = portalReq.getMinLevel();
		InstanceCooltime instancecooltime = DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(mapId);
		if (instancecooltime != null && player.isMentor()) {
			if (!instancecooltime.getCanEnterMentor()) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_MENTOR_CANT_ENTER(World.getInstance().getWorldMap(mapId).getName()));
				return false;
			}
		} if (player.getLevel() < enterMinLvl) {
			int errDialog = portalReq.getErrLevel();
			if (errDialog != 0) {
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcObjectId, errDialog));
			} else {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
			}
			return false;
		}
		return true;
	}
	
	private static boolean checkPlayerSize(Player player, PortalPath portalPath, int npcObjectId) {
		int errDialog = portalPath.getErrGroup();
		int playerSize = portalPath.getPlayerCount();
		if (playerSize > 2 && playerSize <= 6) {
			if (!player.isInGroup2()) {
				if (errDialog != 0) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcObjectId, errDialog));
				} else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_PARTY_DON);
				}
				return false;
			}
		} else if (playerSize > 6 && playerSize <= 24) {
			if (!player.isInAlliance2()) {
				if (errDialog != 0) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcObjectId, errDialog));
				} else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_FORCE_DON);
				}
				return false;
			}
		} else if (playerSize > 24) {
			if (!player.isInLeague()) {
				if (errDialog != 0) {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcObjectId, errDialog));
				} else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENTER_ONLY_UNION_DON);
				}
				return false ;
			}
		}
		return true;
	}
	
	private static boolean checkRace(Player player, Race portalRace) {
		return player.getRace().equals(portalRace) || portalRace.equals(Race.PC_ALL);
	}
	
	private static boolean checkSiegeId(Player player, int siegeId) {
		FortressLocation loc = SiegeService.getInstance().getFortress(siegeId);
		if (loc != null && loc.getRace().getRaceId() != player.getRace().getRaceId()) {
			return false;
		}
		return true;
	}
	
	private static boolean checkTitle(Player player, int titleId) {
		return player.getCommonData().getTitleId() == titleId;
	}
	
	private static boolean checkQuestsReq(Player player, int npcObjectId, List<QuestReq> questReq) {
		if (questReq != null) {
			for (QuestReq quest : questReq) {
				int questId = quest.getQuestId();
				int questStep = quest.getQuestStep();
				final QuestState qs = player.getQuestStateList().getQuestState(questId);
				if (qs == null || (questStep == 0 && qs.getStatus() != QuestStatus.COMPLETE || (qs.getQuestVarById(0) < quest.getQuestStep() && qs.getStatus() != QuestStatus.COMPLETE))) {
					int errDialog = quest.getErrQuest();
					if (errDialog != 0) {
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcObjectId, errDialog));
					} else {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_MOVE_TO_AIRPORT_NEED_FINISH_QUEST);
					}
					return false;
				}
			}
		}
		return true;
	}
	
	private static boolean checkItemReq(Player player, int npcObjectId, List<ItemReq> itemReq) {
		if (itemReq != null) {
			Storage inventory = player.getInventory();
			for (ItemReq item : itemReq) {
				if (inventory.getItemCountByItemId(item.getItemId()) < item.getItemCount()) {
					int errDialog = item.getErrItem();
					if (errDialog != 0) {
				        PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(npcObjectId, errDialog));
			        } else {
				        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_CANT_ENTER_WITHOUT_ITEM_TRY_LATER);
			        }
			        return false;
				}
			}
			for (ItemReq item : itemReq) {
				inventory.decreaseByItemId(item.getItemId(), item.getItemCount());
			}
		}
		return true;
	}
	
	private static void port(Player requester, PortalLoc loc, boolean reenter, boolean isInstance) {
		WorldMapInstance instance = null;
		if (isInstance) {
			instance = InstanceService.getNextAvailableInstance(loc.getWorldId(), requester.getObjectId());
			InstanceService.registerPlayerWithInstance(instance, requester);
			transfer(requester, loc, instance, reenter);
		} else {
			easyTransfer(requester, loc);
		}
	}
	
	private static WorldMapInstance registerGroup(PlayerGroup group, int mapId) {
		WorldMapInstance instance = InstanceService.getNextAvailableInstance(mapId);
		InstanceService.registerGroupWithInstance(instance, group);
		return instance;
	}
	
	private static WorldMapInstance registerAlliance(PlayerAlliance group, int mapId) {
		WorldMapInstance instance = InstanceService.getNextAvailableInstance(mapId);
		InstanceService.registerAllianceWithInstance(instance, group);
		return instance;
	}
	
	private static WorldMapInstance registerLeague(League group, int mapId) {
		WorldMapInstance instance = InstanceService.getNextAvailableInstance(mapId);
		InstanceService.registerLeagueWithInstance(instance, group);
		return instance;
	}
	
	private static void transfer(Player player, PortalLoc loc, WorldMapInstance instance, boolean reenter) {
		player.setInstanceStartPos(loc.getX(), loc.getY(), loc.getZ());
		InstanceService.registerPlayerWithInstance(instance, player);
		TeleportService2.teleportTo(player, loc.getWorldId(), instance.getInstanceId(), loc.getX(), loc.getY(), loc.getZ(), loc.getH(), TeleportAnimation.FIRE_ANIMATION);
		if (!reenter) {
            if (player.getPortalCooldownList().getPortalCooldownItem(loc.getWorldId()) == null) {
                player.getPortalCooldownList().addPortalCooldown(loc.getWorldId(), 1, DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCooltime(player, loc.getWorldId()));
            } else {
                player.getPortalCooldownList().addEntry(loc.getWorldId());
				//You have successfully entered the area, consuming one of your permitted entries.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_DUNGEON_COUNT_USE);
            }
        }
		//Remove player from "Group/Alliance" if "Instance Solo/Quest Zone"
		switch (loc.getWorldId()) {
			case 300190000: //Taloc's Hollow.
			case 300200000: //Haramel.
			case 300230000: //Kromede Trial.
			case 300240000: //Aturam Sky Fortress.
			case 300241000: //[Opportunity] Aturam Sky Fortress.
			case 300320000: //Crucible Challenge.
			case 300460000: //Steel Rake Cabin.
			case 300480000: //Sealed Danuar Mysticarium.
			case 300610000: //Raksang Ruins.
			case 301270000: //Linkgate Foundry.
			case 301340000: //[Quest] Linkgate Foundry.
			case 301510000: //Sealed Argent Manor.
			case 301520000: //[Quest] Drakenspire Depths.
			case 301570000: //[Quest] Archives Of Eternity.
			case 301580000: //[Quest] Sanctuary Dungeon.
			case 301630000: //Contaminated Underpath.
			case 301631000: //[Event] Contaminated Underpath.
			case 301632000: //험난한 오염된 지하 통로.
			case 301640000: //Secret Munitions Factory.
			case 301690000: //[Quest] Aether Mine.
			case 301720000: //Mirash Sanctuary.
			case 302100000: //Fissure Of Oblivion.
			case 302110000: //[Opportunity] Fissure Of Oblivion.
			case 302330000: //Kumuki Cave.
			case 302400000: //Crucible Spire.
			//Quest & Mission
			case 310010000: //Karamatis A.
			case 310030000: //Aerdina.
			case 310040000: //Geranaia.
			case 310070000: //Sliver Of Darkness.
			case 310080000: //Sanctum Underground Arena.
			case 310120000: //Ataxiar A.
			case 320020000: //Ataxiar C.
			case 320030000: //Bregirun.
			case 320040000: //Nidalber.
			case 320070000: //Space Of Destiny.
			case 320090000: //Triniel Underground Arena.
			case 320120000: //Shadow Court Dungeon.
			case 320140000: //Ataxiar D.
				PlayerGroupService.removePlayer(player);
				PlayerAllianceService.removePlayer(player);
				//This Instance Group has no more openings.
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401718));
			break;
		}
	}
	
	private static void easyTransfer(Player player, PortalLoc loc) {
		TeleportService2.teleportTo(player, loc.getWorldId(), loc.getX(), loc.getY(), loc.getZ(), loc.getH(), TeleportAnimation.FIRE_ANIMATION);
	}
}