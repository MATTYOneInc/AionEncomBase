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
package com.aionemu.gameserver.services;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.iu.IuLocation;
import com.aionemu.gameserver.model.iu.IuStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.iuspawns.IuSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.iuservice.CircusBound;
import com.aionemu.gameserver.services.iuservice.Iu;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import javolution.util.FastMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Rinzler (Encom)
 */

public class IuService
{
	private Map<Integer, IuLocation> iu;
	private static final int duration = CustomConfig.IU_DURATION;
	private final Map<Integer, Iu<?>> activeConcert = new FastMap<Integer, Iu<?>>().shared();
	private static Logger log = LoggerFactory.getLogger(IuService.class);

	public void initConcertLocations() {
		if (CustomConfig.IU_ENABLED) {
			iu = DataManager.IU_DATA.getIuLocations();
			for (IuLocation loc: getIuLocations().values()) {
				spawn(loc, IuStateType.CLOSED);
			}
			log.info("[IuService] Loaded " + iu.size() + " locations.");
			CronService.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					for (IuLocation loc: getIuLocations().values()) {
				        startConcert(loc.getId());
					}
					World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					    @Override
					    public void visit(Player player) {
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_OPEN);
					    }
					});
				}
			}, CustomConfig.IU_SCHEDULE);
		} else {
			log.info("[IuService] Concert Grounds is disabled in config...");
			iu = Collections.emptyMap();
		}
	}

	public void initConcert() {
		if (CustomConfig.IU_ENABLED) {
			log.info("[IuService] is initialized...");
		} else {
			log.info("[IuService] Concert Grounds is disabled in config...");
		}
	}

	public void startConcert(final int id) {
		final Iu<?> circusBound;
		synchronized (this) {
			if (activeConcert.containsKey(id)) {
				return;
			}
			circusBound = new CircusBound(iu.get(id));
			activeConcert.put(id, circusBound);
		}
		circusBound.start();
		lPCHCountdownMsg(id);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				stopConcert(id);
			}
		}, duration * 3600 * 1000);
	}
	
	public void stopConcert(int id) {
		if (!isConcertInProgress(id)) {
			return;
		}
		Iu<?> circusBound;
		synchronized (this) {
			circusBound = activeConcert.remove(id);
		} if (circusBound == null || circusBound.isFinished()) {
			return;
		}
		circusBound.stop();
	}
	
	public void spawn(IuLocation loc, IuStateType iustate) {
		if (iustate.equals(IuStateType.OPEN)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getIuSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				IuSpawnTemplate iutemplate = (IuSpawnTemplate) st;
				if (iutemplate.getIUStateType().equals(iustate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(iutemplate, 1));
				}
			}
		}
	}
	
   /**
	* Live Party Concert Hall Countdown.
	*/
	public boolean lPCHCountdownMsg(int id) {
        switch (id) {
            case 1:
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//The entrance to the Live Party Concert Hall appeared.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_OPEN, 0);
						//The entrance to the Live Party Concert Hall closes in 90 minutes. Escape will engage.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_90M, 1800000);
						//The entrance to the Live Party Concert Hall closes in 60 minutes. Escape will engage.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_60M, 3600000);
						//The entrance to the Live Party Concert Hall closes in 30 minutes. Escape will engage.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_30M, 5400000);
						//The entrance to the Live Party Concert Hall closes in 15 minutes. Escape will engage.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_15M, 6300000);
						//The entrance to the Live Party Concert Hall closes in 10 minutes. Escape will engage.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_10M, 6600000);
						//The entrance to the Live Party Concert Hall closes in 5 minutes. Escape will engage.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_5M, 6900000);
						//The entrance to the Live Party Concert Hall closes in 3 minutes. Escape will engage.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_3M, 7020000);
						//The entrance to the Live Party Concert Hall closes in 2 minutes. Escape will engage.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_2M, 7080000);
						//The entrance to the Live Party Concert Hall closes in 1 minutes. Escape will engage.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_1M, 7140000);
					}
				});
			    return true;
            default:
                return false;
		}
	}
	
	public void despawn(IuLocation loc) {
		if (loc.getSpawned() == null) {
        	return;
		} for (VisibleObject obj: loc.getSpawned()) {
            Npc spawned = (Npc) obj;
            spawned.setDespawnDelayed(true);
            if (spawned.getAggroList().getList().isEmpty()) {
                spawned.getController().cancelTask(TaskId.RESPAWN);
                obj.getController().onDelete();
            }
        }
        loc.getSpawned().clear();
	}
	
	public boolean isConcertInProgress(int id) {
		return activeConcert.containsKey(id);
	}
	
	public Map<Integer, Iu<?>> getActiveIu() {
		return activeConcert;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public IuLocation getIuLocation(int id) {
		return iu.get(id);
	}
	
	public Map<Integer, IuLocation> getIuLocations() {
		return iu;
	}
	
	public static IuService getInstance() {
		return IuServiceHolder.INSTANCE;
	}
	
	private static class IuServiceHolder {
		private static final IuService INSTANCE = new IuService();
	}
}