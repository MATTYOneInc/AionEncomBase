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
package com.aionemu.gameserver.services;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.instance.InstanceEngine;
import com.aionemu.gameserver.model.autogroup.AGPlayer;
import com.aionemu.gameserver.model.autogroup.AGQuestion;
import com.aionemu.gameserver.model.autogroup.AutoGroupType;
import com.aionemu.gameserver.model.autogroup.AutoInstance;
import com.aionemu.gameserver.model.autogroup.EntryRequestType;
import com.aionemu.gameserver.model.autogroup.LookingForParty;
import com.aionemu.gameserver.model.autogroup.SearchInstance;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.templates.InstanceCooltime;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AUTO_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.instance.AsyunatarService;
import com.aionemu.gameserver.services.instance.DredgionService2;
import com.aionemu.gameserver.services.instance.EngulfedOphidanBridgeService;
import com.aionemu.gameserver.services.instance.GrandArenaTrainingCampService;
import com.aionemu.gameserver.services.instance.HallOfTenacityService;
import com.aionemu.gameserver.services.instance.IDRunService;
import com.aionemu.gameserver.services.instance.IdgelDomeLandmarkService;
import com.aionemu.gameserver.services.instance.IdgelDomeService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.instance.IronWallWarfrontService;
import com.aionemu.gameserver.services.instance.KamarBattlefieldService;
import com.aionemu.gameserver.services.instance.SuspiciousOphidanBridgeService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.WorldMapInstanceFactory;

import javolution.util.FastList;
import javolution.util.FastMap;

public class AutoGroupService {

	private Logger log = LoggerFactory.getLogger(AutoGroupService.class);
	private FastMap<Integer, LookingForParty> searchers = new FastMap<Integer, LookingForParty>().shared();
	private FastMap<Integer, AutoInstance> autoInstances = new FastMap<Integer, AutoInstance>().shared();
	private Collection<Integer> penaltys = new FastList<Integer>().shared();
	private Lock lock = new ReentrantLock();
	
	private AutoGroupService() {
	}
	
	public void startLooking(Player player, int instanceMaskId, EntryRequestType ert) {
		AutoGroupType agt = AutoGroupType.getAGTByMaskId(instanceMaskId);
		if (agt == null) {
			return;
		}
		if (!canEnter(player, ert, agt)) {
			return;
		}
		Integer obj = player.getObjectId();
		LookingForParty lfp = searchers.get(obj);
		if (penaltys.contains(obj)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400181, agt.getInstanceMapId()));
			return;
		}
		if (lfp == null) {
			searchers.put(obj, new LookingForParty(player, instanceMaskId, ert));
		} else if (lfp.hasPenalty() || lfp.isRegistredInstance(instanceMaskId)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400181, agt.getInstanceMapId()));
			return;
		} else {
			lfp.addInstanceMaskId(instanceMaskId, ert);
		}
		if (ert.isGroupEntry()) {
			for (Player member : player.getPlayerGroup2().getOnlineMembers()) {
				if (agt.isDredgion()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				} if (agt.isAsyunatar()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				} if (agt.isKamar()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				} if (agt.isOphidan()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				} if (agt.isSuspiciousOphidan()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				} if (agt.isBastion()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				} if (agt.isIdgelDome()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				} if (agt.isIdgelDomeLandmark()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				} if (agt.isGrandArenaTrainingCamp()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				} if (agt.isIDRun()) {
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6, true));
				}
				PacketSendUtility.sendPacket(member, new SM_SYSTEM_MESSAGE(1400194, agt.getInstanceMapId()));
				PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 1, ert.getId(), player.getName()));
			}
		} else {
			if (agt.isDredgion()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isAsyunatar()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isKamar()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isOphidan()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isSuspiciousOphidan()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isBastion()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isIdgelDome()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isIdgelDomeLandmark()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isHallOfTenacity()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isGrandArenaTrainingCamp()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			} if (agt.isIDRun()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6, true));
			}
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400194, agt.getInstanceMapId()));
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 1, ert.getId(), player.getName()));
		}
		startSort(ert, instanceMaskId, true);
	}
	
	public synchronized void pressEnter(Player player, int instanceMaskId) {
		AutoInstance instance = getAutoInstance(player, instanceMaskId);
		if (instance == null || instance.players.get(player.getObjectId()).isPressedEnter()) {
			return;
		} if (player.isInGroup2()) {
			PlayerGroupService.removePlayer(player);
		} if (player.isInAlliance2()) {
			PlayerAllianceService.removePlayer(player);
		}
		instance.onPressEnter(player);
		PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 5));
	}
	
	public void onEnterInstance(Player player) {
		if (player.isInInstance()) {
			Integer obj = player.getObjectId();
			AutoInstance autoInstance = autoInstances.get(player.getInstanceId());
			if (autoInstance != null && autoInstance.players.containsKey(obj)) {
				autoInstance.onEnterInstance(player);
			}
		}
	}
	
	public void unregisterLooking(Player player, byte instanceMaskId) {
		Integer obj = player.getObjectId();
		LookingForParty lfp = searchers.get(obj);
		SearchInstance si;
		if (lfp != null) {
			lfp.setPenaltyTime();
			si = lfp.getSearchInstance(instanceMaskId);
			if (si != null) {
				if (lfp.unregisterInstance(instanceMaskId) == 0) {
					searchers.remove(obj);
					startPenalty(obj);
				}
				getInstance().unRegisterSearchInstance(player, si);
			}
		}
	}
	
	public void cancelEnter(Player player, int instanceMaskId) {
		AutoInstance autoInstance = getAutoInstance(player, instanceMaskId);
		if (autoInstance != null) {
			Integer obj = player.getObjectId();
			if (!autoInstance.players.get(obj).isInInstance()) {
				autoInstance.unregister(player);
				if (!searchers.containsKey(obj)) {
					startPenalty(obj);
				} if (autoInstance.agt.hasRegisterFast()) {
					startSort(EntryRequestType.FAST_GROUP_ENTRY, instanceMaskId, false);
				} if (autoInstance.players.isEmpty()) {
					WorldMapInstance instance = autoInstance.instance;
					autoInstance = autoInstances.remove(instance.getInstanceId());
					InstanceService.destroyInstance(instance);
					autoInstance.clear();
				}
			} if (autoInstance.agt.isDredgion() && DredgionService2.getInstance().isDredgionAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isAsyunatar() && AsyunatarService.getInstance().isAsyunatarAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isKamar() && KamarBattlefieldService.getInstance().isKamarAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isOphidan() && EngulfedOphidanBridgeService.getInstance().isOphidanAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isSuspiciousOphidan() && SuspiciousOphidanBridgeService.getInstance().isSuspiciousAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isBastion() && IronWallWarfrontService.getInstance().isBastionAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isIdgelDome() && IdgelDomeService.getInstance().isIdgelAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isIdgelDomeLandmark() && IdgelDomeLandmarkService.getInstance().isLandmarkAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isHallOfTenacity() && HallOfTenacityService.getInstance().isHallAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isGrandArenaTrainingCamp() && GrandArenaTrainingCampService.getInstance().isGrandArenaTrainingCampAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            } if (autoInstance.agt.isIDRun() && IDRunService.getInstance().isIDRunAvailable()) {
                PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
            }
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 2));
		}
	}
	
	public void onPlayerLogin(Player player) {
		if (DredgionService2.getInstance().isDredgionAvailable() && player.getLevel() > DredgionService2.minLevel && player.getLevel() < DredgionService2.capLevel && !DredgionService2.getInstance().hasCoolDown(player)) {
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(DredgionService2.getInstance().getInstanceMaskId(player), SM_AUTO_GROUP.wnd_EntryIcon));
        } if (AsyunatarService.getInstance().isAsyunatarAvailable() && player.getLevel() > AsyunatarService.minLevel && player.getLevel() < AsyunatarService.capLevel && !AsyunatarService.getInstance().hasCoolDown(player)) {
            PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(AsyunatarService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
        } if (KamarBattlefieldService.getInstance().isKamarAvailable() && player.getLevel() > KamarBattlefieldService.minLevel && player.getLevel() < KamarBattlefieldService.capLevel && !KamarBattlefieldService.getInstance().hasCoolDown(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(KamarBattlefieldService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
        } if (EngulfedOphidanBridgeService.getInstance().isOphidanAvailable() && player.getLevel() > EngulfedOphidanBridgeService.minLevel && player.getLevel() < EngulfedOphidanBridgeService.capLevel && !EngulfedOphidanBridgeService.getInstance().hasCoolDown(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(EngulfedOphidanBridgeService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
		} if (SuspiciousOphidanBridgeService.getInstance().isSuspiciousAvailable() && player.getLevel() > SuspiciousOphidanBridgeService.minLevel && player.getLevel() < SuspiciousOphidanBridgeService.capLevel && !SuspiciousOphidanBridgeService.getInstance().hasCoolDown(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(SuspiciousOphidanBridgeService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
		} if (IronWallWarfrontService.getInstance().isBastionAvailable() && player.getLevel() > IronWallWarfrontService.minLevel && player.getLevel() < IronWallWarfrontService.capLevel && !IronWallWarfrontService.getInstance().hasCoolDown(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(IronWallWarfrontService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
		} if (IdgelDomeService.getInstance().isIdgelAvailable() && player.getLevel() > IdgelDomeService.minLevel && player.getLevel() < IdgelDomeService.capLevel && !IdgelDomeService.getInstance().hasCoolDown(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(IdgelDomeService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
		} if (IdgelDomeLandmarkService.getInstance().isLandmarkAvailable() && player.getLevel() > IdgelDomeLandmarkService.minLevel && player.getLevel() < IdgelDomeLandmarkService.capLevel && !IdgelDomeLandmarkService.getInstance().hasCoolDown(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(IdgelDomeLandmarkService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
		} if (HallOfTenacityService.getInstance().isHallAvailable() && player.getLevel() > HallOfTenacityService.minLevel && player.getLevel() < HallOfTenacityService.capLevel && !HallOfTenacityService.getInstance().hasCoolDown(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(HallOfTenacityService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
		} if (GrandArenaTrainingCampService.getInstance().isGrandArenaTrainingCampAvailable() && player.getLevel() > GrandArenaTrainingCampService.minLevel && player.getLevel() < GrandArenaTrainingCampService.capLevel && !GrandArenaTrainingCampService.getInstance().hasCoolDown(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(GrandArenaTrainingCampService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
		} if (IDRunService.getInstance().isIDRunAvailable() && player.getLevel() > IDRunService.minLevel && player.getLevel() < IDRunService.capLevel && !IDRunService.getInstance().hasCoolDown(player)) {
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(IDRunService.maskId, SM_AUTO_GROUP.wnd_EntryIcon));
		}
		Integer obj = player.getObjectId();
		LookingForParty lfp = searchers.get(obj);
		if (lfp != null) {
			for (SearchInstance searchInstance : lfp.getSearchInstances()) {
				if (searchInstance.getEntryRequestType().isGroupEntry() && !player.isInGroup2()) {
					int instanceMaskId = searchInstance.getInstanceMaskId();
					lfp.unregisterInstance(instanceMaskId);
					if (searchInstance.isDredgion() && DredgionService2.getInstance().isDredgionAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isAsyunatar() && AsyunatarService.getInstance().isAsyunatarAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isKamar() && KamarBattlefieldService.getInstance().isKamarAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isOphidan() && EngulfedOphidanBridgeService.getInstance().isOphidanAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isSuspiciousOphidan() && SuspiciousOphidanBridgeService.getInstance().isSuspiciousAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isBastion() && IronWallWarfrontService.getInstance().isBastionAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isIdgelDome() && IdgelDomeService.getInstance().isIdgelAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isIdgelDomeLandmark() && IdgelDomeLandmarkService.getInstance().isLandmarkAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isHallOfTenacity() && HallOfTenacityService.getInstance().isHallAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isGrandArenaTrainingCamp() && GrandArenaTrainingCampService.getInstance().isGrandArenaTrainingCampAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					} if (searchInstance.isIDRun() && IDRunService.getInstance().isIDRunAvailable()) {
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					}
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 2));
					continue;
				}
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), 8, searchInstance.getRemainingTime() + searchInstance.getEntryRequestType().getId(), player.getName()));
				if (searchInstance.isDredgion()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isAsyunatar()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isKamar()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isOphidan()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isSuspiciousOphidan()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isBastion()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isIdgelDome()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isIdgelDomeLandmark()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isHallOfTenacity()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isGrandArenaTrainingCamp()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				} if (searchInstance.isIDRun()) {
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(searchInstance.getInstanceMaskId(), SM_AUTO_GROUP.wnd_EntryIcon, true));
				}
			}
			if (lfp.getSearchInstances().isEmpty()) {
				searchers.remove(obj);
				return;
			}
			lfp.setPlayer(player);
			for (SearchInstance si : lfp.getSearchInstances()) {
				startSort(si.getEntryRequestType(), si.getInstanceMaskId(), true);
			}
		}
	}
	
	public void onPlayerLogOut(Player player) {
		Integer obj = player.getObjectId();
		int instanceId = player.getInstanceId();
		LookingForParty lfp = searchers.get(obj);
		if (lfp != null) {
			lfp.setPlayer(null);
			if (lfp.isOnStartEnterTask()) {
				for (AutoInstance autoInstance : autoInstances.values()) {
					if (autoInstance.players.containsKey(obj) && !autoInstance.players.get(obj).isInInstance()) {
						cancelEnter(player, autoInstance.agt.getInstanceMaskId());
					}
				}
			}
		}
		if (player.isInInstance()) {
			AutoInstance autoInstance = autoInstances.get(instanceId);
			if (autoInstance != null && autoInstance.players.containsKey(obj)) {
				WorldMapInstance instance = autoInstance.instance;
				if (instance != null ) {
					autoInstance.players.get(obj).setOnline(false);
					if (select(autoInstance.players, having(on(AGPlayer.class).isOnline(), equalTo(true))).isEmpty()) {
						autoInstance = autoInstances.remove(instanceId);
						InstanceService.destroyInstance(instance);
						autoInstance.clear();
					}
				}
			}
		}
	}
	
	public void onLeaveInstance(Player player) {
		if (player.isInInstance()) {
			Integer obj = player.getObjectId();
			int instanceId = player.getInstanceId();
			AutoInstance autoInstance = autoInstances.get(instanceId);
			if (autoInstance != null && autoInstance.players.containsKey(obj)) {
				autoInstance.onLeaveInstance(player);
				if (select(autoInstance.players, having(on(AGPlayer.class).isOnline(), equalTo(true))).isEmpty()) {
					WorldMapInstance instance = autoInstance.instance;
					autoInstances.remove(instanceId);
					if (instance != null) {
						InstanceService.destroyInstance(instance);
					}
				} else if (autoInstance.agt.hasRegisterFast()) {
					startSort(EntryRequestType.FAST_GROUP_ENTRY, autoInstance.agt.getInstanceMaskId(), false);
				}
			}
		}
	}
	
	private void startSort(EntryRequestType ert, Integer instanceMaskId, boolean checkNewGroup) {
		lock.lock();
		try {
			Collection<Player> players = new HashSet<Player>();
			if (ert.isFastGroupEntry()) {
				for (LookingForParty lfp : searchers.values()) {
					if (lfp.getPlayer() == null || lfp.isOnStartEnterTask()) {
						continue;
					} for (AutoInstance autoInstance : autoInstances.values()) {
						int searchMaskId = autoInstance.agt.getInstanceMaskId();
						SearchInstance searchInstance = lfp.getSearchInstance(searchMaskId);
						if (searchInstance != null && searchInstance.getEntryRequestType().isFastGroupEntry()) {
							Player owner = lfp.getPlayer();
							if (autoInstance.addPlayer(owner, searchInstance).isAdded()) {
								lfp.setStartEnterTime();
								if (lfp.unregisterInstance(searchMaskId) == 0) {
									players.add(owner);
								}
								PacketSendUtility.sendPacket(lfp.getPlayer(), new SM_AUTO_GROUP(searchMaskId, 4));
							}
						}
					}
				}
				for (Player p : players) {
					searchers.remove(p.getObjectId());
				}
				players.clear();
			}
			if (checkNewGroup) {
				AutoGroupType agt = AutoGroupType.getAGTByMaskId(instanceMaskId);
				AutoInstance autoInstance = agt.getAutoInstance();
				autoInstance.initsialize(instanceMaskId);
				boolean canCreate = false;
				Iterator<LookingForParty> iter = searchers.values().iterator();
				LookingForParty lfp;
				while (iter.hasNext()) {
					lfp = iter.next();
					if (lfp.getPlayer() == null || lfp.isOnStartEnterTask()) {
						continue;
					}
					SearchInstance searchInstance = lfp.getSearchInstance(instanceMaskId);
					if (searchInstance != null) {
						if (searchInstance.getEntryRequestType().isGroupEntry()) {
							if (!lfp.getPlayer().isInGroup2()) {
								if (lfp.unregisterInstance(instanceMaskId) == 0) {
									iter.remove();
								}
								continue;
							}
						}
						AGQuestion question = autoInstance.addPlayer(lfp.getPlayer(), searchInstance);
						if (!question.isFailed()) {
							if (searchInstance.getEntryRequestType().isGroupEntry()) {
								for (Player member : lfp.getPlayer().getPlayerGroup2().getOnlineMembers()) {
									if (searchInstance.getMembers().contains(member.getObjectId())) {
										players.add(member);
									}
								}
							} else {
								players.add(lfp.getPlayer());
							}
						} if (question.isReady()) {
							canCreate = true;
							break;
						}
					}
				}
				if (canCreate) {
					WorldMapInstance instance = createInstance(agt.getInstanceMapId(), agt.getDifficultId());
					autoInstance.onInstanceCreate(instance);
					autoInstances.put(instance.getInstanceId(), autoInstance);
					for (Player player : players) {
						Integer obj = player.getObjectId();
						lfp = searchers.get(obj);
						if (lfp != null) {
							lfp.setStartEnterTime();
							if (lfp.unregisterInstance(autoInstance.agt.getInstanceMaskId()) == 0) {
								searchers.remove(obj);
							}
						}
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 4));
					}
				} else {
					autoInstance.clear();
				}
				players.clear();
			}
		}
		finally {
			lock.unlock();
		}
	}
	
	private boolean canEnter(Player player, EntryRequestType ert, AutoGroupType agt) {
		int mapId = agt.getInstanceMapId();
		int instanceMaskId = agt.getInstanceMaskId();
		if (!agt.hasLevelPermit(player.getLevel())) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
			return false;
		}
		if (agt.isDredgion() && !DredgionService2.getInstance().isDredgionAvailable()) {
			return false;
		} else if (agt.isAsyunatar() && !AsyunatarService.getInstance().isAsyunatarAvailable()) {
			return false;
		} else if (agt.isKamar() && !KamarBattlefieldService.getInstance().isKamarAvailable()) {
			return false;
		} else if (agt.isOphidan() && !EngulfedOphidanBridgeService.getInstance().isOphidanAvailable()) {
			return false;
		} else if (agt.isSuspiciousOphidan() && !SuspiciousOphidanBridgeService.getInstance().isSuspiciousAvailable()) {
			return false;
		} else if (agt.isBastion() && !IronWallWarfrontService.getInstance().isBastionAvailable()) {
			return false;
		} else if (agt.isIdgelDome() && !IdgelDomeService.getInstance().isIdgelAvailable()) {
			return false;
		} else if (agt.isIdgelDomeLandmark() && !IdgelDomeLandmarkService.getInstance().isLandmarkAvailable()) {
			return false;
		} else if (agt.isHallOfTenacity() && !HallOfTenacityService.getInstance().isHallAvailable()) {
			return false;
		} else if (agt.isGrandArenaTrainingCamp() && !GrandArenaTrainingCampService.getInstance().isGrandArenaTrainingCampAvailable()) {
			return false;
		} else if (agt.isIDRun() && !IDRunService.getInstance().isIDRunAvailable()) {
			return false;
		} else if (hasCoolDown(player, mapId)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANNOT_MAKE_INSTANCE_COOL_TIME);
			return false;
		} switch (ert) {
			case NEW_GROUP_ENTRY:
				if (!agt.hasRegisterNew()) {
					return false;
				}
				if (agt.isHallOfTenacity() && HallOfTenacityService.getInstance().hasCoolDown(player)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(player.getName()));
					return false;
				} else if (hasCoolDown(player, mapId)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(player.getName()));
					return false;
				}
				if (!agt.hasLevelPermit(player.getLevel())) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(player.getName()));
					return false;
				}
			break;
			case FAST_GROUP_ENTRY:
				if (!agt.hasRegisterFast()) {
					return false;
				}
			break;
			case GROUP_ENTRY:
				if (!agt.hasRegisterGroup()) {
					return false;
				}
				PlayerGroup group = player.getPlayerGroup2();
				if (group == null || !group.isLeader(player)) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_NOT_LEADER);
					return false;
				}
				if (agt.isHarmonyArena() || agt.isTrainingHarmonyArena()) {
					if (group.getOnlineMembers().size() > 2) {
						PacketSendUtility.sendPacket(player,  SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_TOO_MANY_MEMBERS(3, Integer.toString(mapId)));
						return false;
					}
				} for (Player member: group.getMembers()) {
					if (group.getLeaderObject().equals(member)) {
						continue;
					}
					LookingForParty lfp = searchers.get(member.getObjectId());
					if (lfp != null && lfp.isRegistredInstance(instanceMaskId) ) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isPvPSoloArena() || agt.isTrainingPvPSoloArena() || agt.isPvPFFAArena() ||
					    agt.isTrainingPvPFFAArena() || agt.isTrainingHarmonyArena() || agt.isGloryArena() ||
					    agt.isHarmonyArena()) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isDredgion() && DredgionService2.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isAsyunatar() && AsyunatarService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isKamar() && KamarBattlefieldService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isOphidan() && EngulfedOphidanBridgeService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isSuspiciousOphidan() && SuspiciousOphidanBridgeService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isBastion() && IronWallWarfrontService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isIdgelDome() && IdgelDomeService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isIdgelDomeLandmark() && IdgelDomeLandmarkService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isGrandArenaTrainingCamp() && GrandArenaTrainingCampService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (agt.isIDRun() && IDRunService.getInstance().hasCoolDown(member)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} else if (hasCoolDown(member, mapId)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					} if (!agt.hasLevelPermit(member.getLevel())) {
						PacketSendUtility.sendPacket(member, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_MEMBER(member.getName()));
						return false;
					}
				}
			break;
		}
		return true;
	}
	
	private AutoInstance getAutoInstance(Player player, int instanceMaskId) {
		for (AutoInstance autoInstance : autoInstances.values()) {
			if (autoInstance.agt.getInstanceMaskId() == instanceMaskId && autoInstance.players.containsKey(player.getObjectId())) {
				return autoInstance;
			}
		}
		return null;
	}
	
	private boolean hasCoolDown(Player player, int worldId) {
		int instanceCooldownRate = InstanceService.getInstanceRate(player, worldId);
		int useDelay = 0;
		int instanceCooldown = 0;
		InstanceCooltime clt = DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(worldId);
		if (clt != null) {
			instanceCooldown = clt.getEntCoolTime();
		}
		if (instanceCooldownRate > 0) {
			useDelay = instanceCooldown / instanceCooldownRate;
		}
		return player.getPortalCooldownList().isPortalUseDisabled(worldId) && useDelay > 0;
	}
	
	private WorldMapInstance createInstance(int worldId, byte difficultId) {
		WorldMap map = World.getInstance().getWorldMap(worldId);
		int nextInstanceId = map.getNextInstanceId();
		WorldMapInstance worldMapInstance = WorldMapInstanceFactory.createWorldMapInstance(map, nextInstanceId);
		map.addInstance(nextInstanceId, worldMapInstance);
		SpawnEngine.spawnInstance(worldId, worldMapInstance.getInstanceId(), difficultId);
		InstanceEngine.getInstance().onInstanceCreate(worldMapInstance);
		return worldMapInstance;
	}
	
	private void startPenalty(final Integer obj) {
		if (penaltys.contains(obj)) {
			penaltys.remove(obj);
		}
		penaltys.add(obj);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (penaltys.contains(obj)) {
					penaltys.remove(obj);
				}
			}
		}, 10000);
	}
	
	public void unRegisterInstance(byte instanceMaskId) {
		for (LookingForParty lfp : searchers.values()) {
			if (lfp.isRegistredInstance(instanceMaskId)) {
				if (lfp.getPlayer() != null) {
					getInstance().unregisterLooking(lfp.getPlayer(), instanceMaskId);
				} else {
					getInstance().unRegisterSearchInstance(null, lfp.getSearchInstance(instanceMaskId));
					if (lfp.unregisterInstance(instanceMaskId) == 0) {
						searchers.values().remove(lfp);
					}
				}
			}
		}
	}
	
	private void unRegisterSearchInstance(Player player, SearchInstance si) {
		int instanceMaskId = si.getInstanceMaskId();
		if (si.getEntryRequestType().isGroupEntry() && si.getMembers() != null) {
			for (Integer obj : si.getMembers()) {
				Player member = World.getInstance().findPlayer(obj);
				if (member != null) {
					if (si.isDredgion() && DredgionService2.getInstance().isDredgionAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isAsyunatar() && AsyunatarService.getInstance().isAsyunatarAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isKamar() && KamarBattlefieldService.getInstance().isKamarAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isOphidan() && EngulfedOphidanBridgeService.getInstance().isOphidanAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isSuspiciousOphidan() && SuspiciousOphidanBridgeService.getInstance().isSuspiciousAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isBastion() && IronWallWarfrontService.getInstance().isBastionAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isIdgelDome() && IdgelDomeService.getInstance().isIdgelAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isIdgelDomeLandmark() && IdgelDomeLandmarkService.getInstance().isLandmarkAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isHallOfTenacity() && HallOfTenacityService.getInstance().isHallAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isGrandArenaTrainingCamp() && GrandArenaTrainingCampService.getInstance().isGrandArenaTrainingCampAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					} else if (si.isIDRun() && IDRunService.getInstance().isIDRunAvailable()) {
						PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 6));
					}
					PacketSendUtility.sendPacket(member, new SM_AUTO_GROUP(instanceMaskId, 2));
				}
			}
		}
		if (player != null) {
			if (si.isDredgion() && DredgionService2.getInstance().isDredgionAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isAsyunatar() && AsyunatarService.getInstance().isAsyunatarAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isKamar() && KamarBattlefieldService.getInstance().isKamarAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isOphidan() && EngulfedOphidanBridgeService.getInstance().isOphidanAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isSuspiciousOphidan() && SuspiciousOphidanBridgeService.getInstance().isSuspiciousAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isBastion() && IronWallWarfrontService.getInstance().isBastionAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isIdgelDome() && IdgelDomeService.getInstance().isIdgelAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isIdgelDomeLandmark() && IdgelDomeLandmarkService.getInstance().isLandmarkAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isHallOfTenacity() && HallOfTenacityService.getInstance().isHallAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isGrandArenaTrainingCamp() && GrandArenaTrainingCampService.getInstance().isGrandArenaTrainingCampAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			} else if (si.isIDRun() && IDRunService.getInstance().isIDRunAvailable()) {
				PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 6));
			}
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, 2));
		}
	}
	
	public void unRegisterInstance(Integer instanceId) {
		AutoInstance autoInstance = autoInstances.remove(instanceId);
		if (autoInstance != null) {
			WorldMapInstance instance = autoInstance.instance;
			if (instance != null) {
				InstanceService.destroyInstance(instance);
			}
			autoInstance.clear();
		}
	}
	
	public boolean isAutoInstance(int instanceId) {
		return autoInstances.containsKey(instanceId);
	}
	
	public static AutoGroupService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}
	
	private static class NewSingletonHolder {
		private static final AutoGroupService INSTANCE = new AutoGroupService();
	}
}