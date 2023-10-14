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
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.schedule.MoltenusSchedule;
import com.aionemu.gameserver.configs.schedule.MoltenusSchedule.Moltenus;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.moltenus.MoltenusLocation;
import com.aionemu.gameserver.model.moltenus.MoltenusStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.moltenusspawns.MoltenusSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.moltenusservice.Boss;
import com.aionemu.gameserver.services.moltenusservice.MoltenusFight;
import com.aionemu.gameserver.services.moltenusservice.MoltenusStartRunnable;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 * http://aion.power.plaync.com/wiki/%EB%B6%84%EB%85%B8%EC%9D%98+%ED%8C%8C%ED%8E%B8+%EB%A9%94%EB%85%B8%ED%8B%B0%EC%98%A4%EC%8A%A4
 */

public class MoltenusService
{
	private MoltenusSchedule moltenusSchedule;
	private Map<Integer, MoltenusLocation> moltenus;
	private static final int duration = CustomConfig.MOLTENUS_DURATION;
	private final Map<Integer, MoltenusFight<?>> activeMoltenus = new FastMap<Integer, MoltenusFight<?>>().shared();
	private static final Logger log = LoggerFactory.getLogger(MoltenusService.class);

	public void initMoltenusLocations() {
		if (CustomConfig.MOLTENUS_ENABLED) {
			moltenus = DataManager.MOLTENUS_DATA.getMoltenusLocations();
			for (MoltenusLocation loc: getMoltenusLocations().values()) {
				spawn(loc, MoltenusStateType.PEACE);
			}
			log.info("[MoltenusService] Loaded " + moltenus.size() + " locations.");

		} else {
			log.info("[MoltenusService] Moltenus is disabled in config...");
			moltenus = Collections.emptyMap();
		}
	}
	
	public void initMoltenus() {
		if (CustomConfig.MOLTENUS_ENABLED) {
			log.info("[MoltenusService] is initialized...");
			moltenusSchedule = MoltenusSchedule.load();
		    for (Moltenus moltenus: moltenusSchedule.getMoltenussList()) {
			    for (String fightTime: moltenus.getFightTimes()) {
				    CronService.getInstance().schedule(new MoltenusStartRunnable(moltenus.getId()), fightTime);
			    }
			}
		}
	}
	
	public void startMoltenus(final int id) {
		final MoltenusFight<?> boss;
		synchronized (this) {
			if (activeMoltenus.containsKey(id)) {
				return;
			}
			boss = new Boss(moltenus.get(id));
			activeMoltenus.put(id, boss);
		}
		boss.start();
		moltenusMsg(id);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				stopMoltenus(id);
			}
		}, duration * 3600 * 1000);
	}
	
	public void stopMoltenus(int id) {
		if (!isMoltenusInProgress(id)) {
			return;
		}
		MoltenusFight<?> boss;
		synchronized (this) {
			boss = activeMoltenus.remove(id);
		} if (boss == null || boss.isFinished()) {
			return;
		}
		boss.stop();
	}
	
	public void spawn(MoltenusLocation loc, MoltenusStateType mstate) {
		if (mstate.equals(MoltenusStateType.FIGHT)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getMoltenusSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				MoltenusSpawnTemplate moltenustemplate = (MoltenusSpawnTemplate) st;
				if (moltenustemplate.getMStateType().equals(mstate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(moltenustemplate, 1));
				}
			}
		}
	}
	
	public boolean moltenusMsg(int id) {
        switch (id) {
            case 1:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys3Message(player, "\uE005", "<Resurrected Moltenus> appear in the abyss !!!");
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	public boolean sulfurFortressMsg(int id) {
        switch (id) {
            case 4:
			case 7:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Enraged Sulfur Guardian will appear in 10 minutes.
				        PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Spawnmsg_01, 0);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean westernFortressMsg(int id) {
        switch (id) {
            case 5:
			case 8:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Enraged Western Guardian will appear in 10 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Spawnmsg_02, 10000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean easternFortressMsg(int id) {
        switch (id) {
            case 6:
			case 9:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Enraged Eastern Guardian will appear in 10 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Spawnmsg_03, 20000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	public void despawn(MoltenusLocation loc) {
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
	
	public boolean isMoltenusInProgress(int id) {
		return activeMoltenus.containsKey(id);
	}
	
	public Map<Integer, MoltenusFight<?>> getActiveMoltenus() {
		return activeMoltenus;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public MoltenusLocation getMoltenusLocation(int id) {
		return moltenus.get(id);
	}
	
	public Map<Integer, MoltenusLocation> getMoltenusLocations() {
		return moltenus;
	}
	
	public static MoltenusService getInstance() {
		return MoltenusServiceHolder.INSTANCE;
	}
	
	private static class MoltenusServiceHolder {
		private static final MoltenusService INSTANCE = new MoltenusService();
	}
}