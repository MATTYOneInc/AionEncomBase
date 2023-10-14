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
package com.aionemu.gameserver.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.towerofeternityspawns.TowerOfEternitySpawnTemplate;
import com.aionemu.gameserver.model.towerofeternity.TowerOfEternityLocation;
import com.aionemu.gameserver.model.towerofeternity.TowerOfEternityStateType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FLAG_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FLAG_UPDATE;
import com.aionemu.gameserver.services.towerofeternityservice.Tower;
import com.aionemu.gameserver.services.towerofeternityservice.TowerOfEternity;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * Created by Wnkrz on 22/08/2017.
 */

public class TowerOfEternityService
{
    private Map<Integer, TowerOfEternityLocation> towerOfEternity;
    private static final int duration = CustomConfig.TOWER_OF_ETERNITY_DURATION;
    private final Map<Integer, TowerOfEternity<?>> activeTowerOfEternity = new FastMap<Integer, TowerOfEternity<?>>().shared();
    private static Logger log = LoggerFactory.getLogger(TowerOfEternityService.class);

    public void initTowerOfEternityLocation() {
        if (CustomConfig.TOWER_OF_ETERNITY_ENABLED) {
            towerOfEternity = DataManager.TOWER_OF_ETERNITY_DATA.getTowerOfEternityLocations();
            for (TowerOfEternityLocation loc: getTowerOfEternityLocations().values()) {
                spawn(loc, TowerOfEternityStateType.CLOSED);
            }
            log.info("[TowerOfEternityService] Loaded " + towerOfEternity.size() + " locations.");

            CronService.getInstance().schedule(new Runnable() {
                @Override
                public void run() {
                    startTowerOfEternity(Rnd.get(1, 5));
                    startTowerOfEternity(Rnd.get(6, 10));
                }
            }, CustomConfig.TOWER_OF_ETERNITY_SCHEDULE);
        } else {
            log.info("[TowerOfEternityService] Tower Of Eternity is disabled in config...");
            towerOfEternity = Collections.emptyMap();
        }
    }

    public void initTowerOfEternity() {
        if (CustomConfig.TOWER_OF_ETERNITY_ENABLED) {
            log.info("[TowerOfEternityService] is initialized...");
        }
    }
	
    public void spawn(TowerOfEternityLocation loc, TowerOfEternityStateType tstate) {
        if (tstate.equals(TowerOfEternityStateType.OPEN)) {
        }
        List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getTowerOfEternitySpawnsByLocId(loc.getId());
        for (SpawnGroup2 group : locSpawns) {
            for (SpawnTemplate st : group.getSpawnTemplates()) {
                TowerOfEternitySpawnTemplate towerOfEternitySpawnTemplate = (TowerOfEternitySpawnTemplate) st;
                if (towerOfEternitySpawnTemplate.getTStateType().equals(tstate)) {
                    loc.getSpawned().add(SpawnEngine.spawnObject(towerOfEternitySpawnTemplate, 1));
                    broadcastUpdate(loc);
                }
            }
        }
    }
	
	public void despawn(TowerOfEternityLocation loc) {
		if (loc.getSpawned() == null) {
        	return;
		} for (VisibleObject obj: loc.getSpawned()) {
            Npc spawned = (Npc) obj;
            spawned.setDespawnDelayed(true);
            if (spawned.getAggroList().getList().isEmpty()) {
                spawned.getController().cancelTask(TaskId.RESPAWN);
                obj.getController().onDelete();
				broadcastDespawn(loc);
            }
        }
        loc.getSpawned().clear();
	}
	
    public void startTowerOfEternity(final int id) {
        final TowerOfEternity<?> tower;
        synchronized (this) {
            if (activeTowerOfEternity.containsKey(id)) {
                return;
            }
            tower = new Tower(towerOfEternity.get(id));
            activeTowerOfEternity.put(id, tower);
        }
        tower.start();
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                stopTowerOfEternity(id);
            }
        }, duration * 3600 * 1000);
    }
	
    public void stopTowerOfEternity(int id) {
        if (!isTowerOfEternityInProgress(id)) {
            return;
        }
        TowerOfEternity<?> tower;
        synchronized (this) {
            tower = activeTowerOfEternity.remove(id);
        } if (tower == null || tower.isClosed()) {
            return;
        }
        tower.stop();
    }
	
    public boolean isActive(int id) {
        return activeTowerOfEternity.containsKey(id);
    }
	
    public TowerOfEternity<?> getActiveTower(int id) {
        return activeTowerOfEternity.get(id);
    }
	
    public void onEnterTowerWorld(Player player) {
        if (((player.getWorldId() == 210100000) && (player.getRace() == Race.ELYOS)) ||
		    ((player.getWorldId() == 220110000) && (player.getRace() == Race.ELYOS)) ||
			((player.getWorldId() == 210100000) && (player.getRace() == Race.ASMODIANS)) ||
			((player.getWorldId() == 220110000) && (player.getRace() == Race.ASMODIANS))) {
            World.getInstance().getWorldMap(player.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
                public void visit(Player player) {
                    for (VisibleObject npc : World.getInstance().getNpcs()) {
                        if ((npc.getObjectTemplate().getTemplateId() == 833765) && (npc.isSpawned())) {
                            if ((player.getWorldId() == 210100000) && (player.getRace() == Race.ELYOS)) {
                                PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                            }
                        } if ((npc.getObjectTemplate().getTemplateId() == 703146) && (npc.isSpawned())) {
                            if ((player.getWorldId() == 220110000) && (player.getRace() == Race.ELYOS)) {
                                PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                            }
                        } if ((npc.getObjectTemplate().getTemplateId() == 833765) && (npc.isSpawned())) {
                            if ((player.getWorldId() == 210100000) && (player.getRace() == Race.ASMODIANS)) {
                                PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                            }
                        } if ((npc.getObjectTemplate().getTemplateId() == 703146) && (npc.isSpawned())) {
                            if ((player.getWorldId() == 220110000) && (player.getRace() == Race.ASMODIANS)) {
                                PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                            }
                        }
                    }
                }
            });
        }
    }
	
    private void broadcastUpdate(final TowerOfEternityLocation tower) {
        World.getInstance().getWorldMap(tower.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
            public void visit(Player player) {
                for (VisibleObject npc : World.getInstance().getNpcs()) {
                    if ((npc.getObjectTemplate().getTemplateId() == 833765) && (npc.isSpawned())) {
                        if ((player.getWorldId() == 210100000) && (player.getRace() == Race.ELYOS)) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                    } if ((npc.getObjectTemplate().getTemplateId() == 703146) && (npc.isSpawned())) {
                        if ((player.getWorldId() == 220110000) && (player.getRace() == Race.ELYOS)) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                    } if ((npc.getObjectTemplate().getTemplateId() == 833765) && (npc.isSpawned())) {
                        if ((player.getWorldId() == 210100000) && (player.getRace() == Race.ASMODIANS)) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                    } if ((npc.getObjectTemplate().getTemplateId() == 703146) && (npc.isSpawned())) {
                        if ((player.getWorldId() == 220110000) && (player.getRace() == Race.ASMODIANS)) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_INFO(1, (Npc)npc));
                        }
                    }
                }
            }
        });
    }
	
    private void broadcastDespawn(final TowerOfEternityLocation tower) {
        World.getInstance().getWorldMap(tower.getWorldId()).getMainWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
            public void visit(Player player) {
                for (VisibleObject npc : World.getInstance().getNpcs()) {
                    if ((npc.getObjectTemplate().getTemplateId() == 833765) && (npc.isSpawned())) {
                        if ((player.getWorldId() == 210100000) && (player.getRace() == Race.ELYOS)) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_UPDATE((Npc)npc));
                        }
                    } if ((npc.getObjectTemplate().getTemplateId() == 703146) && (npc.isSpawned())) {
                        if ((player.getWorldId() == 220110000) && (player.getRace() == Race.ELYOS)) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_UPDATE((Npc)npc));
                        }
                    } if ((npc.getObjectTemplate().getTemplateId() == 833765) && (npc.isSpawned())) {
                        if ((player.getWorldId() == 210100000) && (player.getRace() == Race.ASMODIANS)) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_UPDATE((Npc)npc));
                        }
                    } if ((npc.getObjectTemplate().getTemplateId() == 703146) && (npc.isSpawned())) {
                        if ((player.getWorldId() == 220110000) && (player.getRace() == Race.ASMODIANS)) {
                            PacketSendUtility.sendPacket(player, new SM_FLAG_UPDATE((Npc)npc));
                        }
                    }
                }
            }
        });
    }
	
    public boolean isTowerOfEternityInProgress(int id) {
        return activeTowerOfEternity.containsKey(id);
    }
	
    public Map<Integer, TowerOfEternity<?>> getActiveTowerOfEternity() {
        return activeTowerOfEternity;
    }
	
    public TowerOfEternity<?> getActiveTowerOfEternity(int id) {
        return activeTowerOfEternity.get(id);
    }
	
    public int getDuration() {
        return duration;
    }
	
    public TowerOfEternityLocation getTowerOfEternityLocation(int id) {
        return towerOfEternity.get(id);
    }
	
    public Map<Integer, TowerOfEternityLocation> getTowerOfEternityLocations() {
        return towerOfEternity;
    }
	
    public static TowerOfEternityService getInstance() {
        return TowerOfEternityServiceHolder.INSTANCE;
    }
	
    private static class TowerOfEternityServiceHolder {
        private static final TowerOfEternityService INSTANCE = new TowerOfEternityService();
    }
}