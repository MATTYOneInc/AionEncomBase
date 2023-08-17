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
import com.aionemu.gameserver.configs.schedule.DredgionSchedule;
import com.aionemu.gameserver.configs.schedule.DredgionSchedule.Dredgion;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.zorshivdredgionspawns.ZorshivDredgionSpawnTemplate;
import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionLocation;
import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionStateType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.zorshivdredgionservice.DredgionStartRunnable;
import com.aionemu.gameserver.services.zorshivdredgionservice.Zorshiv;
import com.aionemu.gameserver.services.zorshivdredgionservice.ZorshivDredgion;
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
public class ZorshivDredgionService
{
	private DredgionSchedule dredgionSchedule;
	private Map<Integer, ZorshivDredgionLocation> zorshivDredgion;
	private static final int duration = CustomConfig.ZORSHIV_DREDGION_DURATION;
	private final Map<Integer, ZorshivDredgion<?>> activeZorshivDredgion = new FastMap<Integer, ZorshivDredgion<?>>().shared();
	private static final Logger log = LoggerFactory.getLogger(ZorshivDredgionService.class);

	//Inggison Invasion
	private FastMap<Integer, VisibleObject> adventPortal = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventEffect = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventControl = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventDirecting = new FastMap<Integer, VisibleObject>();
	
	public void initZorshivDredgionLocations() {
		if (CustomConfig.ZORSHIV_DREDGION_ENABLED) {
			zorshivDredgion = DataManager.ZORSHIV_DREDGION_DATA.getZorshivDredgionLocations();
			for (ZorshivDredgionLocation loc: getZorshivDredgionLocations().values()) {
				spawn(loc, ZorshivDredgionStateType.PEACE);
			}
			log.info("[ZorshivDredgionService] Loaded " + zorshivDredgion.size() + " locations.");
		} else {
			log.info("[ZorshivDredgionService] Zorshiv Dredgion is disabled in config...");
			zorshivDredgion = Collections.emptyMap();
		}
	}
	
	public void initZorshivDredgion() {
		if (CustomConfig.ZORSHIV_DREDGION_ENABLED) {
			log.info("[ZorshivDredgionService] is initialized...");
			dredgionSchedule = DredgionSchedule.load();
		    for (Dredgion dredgion: dredgionSchedule.getDredgionsList()) {
			    for (String zorshivTime: dredgion.getZorshivTimes()) {
				    CronService.getInstance().schedule(new DredgionStartRunnable(dredgion.getId()), zorshivTime);
			    }
			}
		}
	}
	
	public void startZorshivDredgion(final int id) {
		final ZorshivDredgion<?> zorshiv;
		synchronized (this) {
			if (activeZorshivDredgion.containsKey(id)) {
				return;
			}
			zorshiv = new Zorshiv(zorshivDredgion.get(id));
			activeZorshivDredgion.put(id, zorshiv);
		}
		zorshiv.start();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				stopZorshivDredgion(id);
			}
		}, duration * 3600 * 1000);
	}
	
	public void stopZorshivDredgion(int id) {
		if (!isZorshivDredgionInProgress(id)) {
			return;
		}
		ZorshivDredgion<?> zorshiv;
		synchronized (this) {
			zorshiv = activeZorshivDredgion.remove(id);
		} if (zorshiv == null || zorshiv.isPeace()) {
			return;
		}
		zorshiv.stop();
	}
	
	public void spawn(ZorshivDredgionLocation loc, ZorshivDredgionStateType zstate) {
		if (zstate.equals(ZorshivDredgionStateType.LANDING)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getZorshivDredgionSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				ZorshivDredgionSpawnTemplate zorshivDredgiontemplate = (ZorshivDredgionSpawnTemplate) st;
				if (zorshivDredgiontemplate.getZStateType().equals(zstate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(zorshivDredgiontemplate, 1));
				}
			}
		}
	}
	
   /**
	* Dredgion Invasion Msg.
	*/
	public boolean levinshorMsg(int id) {
        switch (id) {
            case 1:
			case 2:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys3Message(player, "\uE050", "The <Zorshiv Dredgion> to lands at levinshor !!!");
						//The Balaur Dredgion has appeared.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_FIELDABYSS_CARRIER_SPAWN, 120000);
						//The Dredgion has dropped Balaur Troopers.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_FIELDABYSS_CARRIER_DROP_DRAGON, 300000);
						//The Balaur Dredgion has disappeared.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_FIELDABYSS_CARRIER_DESPAWN, 3600000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean inggisonMsg(int id) {
        switch (id) {
            case 3:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys3Message(player, "\uE050", "The <Zorshiv Dredgion> to lands at inggison !!!");
						//The Balaur Dredgion has appeared.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_FIELDABYSS_CARRIER_SPAWN, 120000);
						//The Dredgion has dropped Balaur Troopers.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_FIELDABYSS_CARRIER_DROP_DRAGON, 300000);
						//The Balaur Dredgion has disappeared.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_FIELDABYSS_CARRIER_DESPAWN, 3600000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	public boolean adventControlSP(int id) {
        switch (id) {
            case 3:
                adventControl.put(702529, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210130000, 702529, 1439.8473f, 407.9271f, 552.26624f, (byte) 78), 1));
			    return true;
            default:
                return false;
        }
    }
	public boolean adventEffectSP(int id) {
        switch (id) {
            case 3:
                adventEffect.put(702549, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210130000, 702549, 1439.8473f, 407.9271f, 552.26624f, (byte) 78), 1));
			    return true;
            default:
                return false;
        }
	}
	public boolean adventPortalSP(int id) {
        switch (id) {
            case 3:
                adventPortal.put(702550, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210130000, 702550, 1439.8473f, 407.9271f, 552.26624f, (byte) 78), 1));
			    return true;
            default:
                return false;
        }
    }
	public boolean adventDirectingSP(int id) {
        switch (id) {
            case 3:
                adventDirecting.put(855231, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(210130000, 855231, 1439.8473f, 407.9271f, 552.26624f, (byte) 78), 1));
			    return true;
            default:
                return false;
        }
	}
	
	public void despawn(ZorshivDredgionLocation loc) {
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
	
	public boolean isZorshivDredgionInProgress(int id) {
		return activeZorshivDredgion.containsKey(id);
	}
	
	public Map<Integer, ZorshivDredgion<?>> getActiveZorshivDredgion() {
		return activeZorshivDredgion;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public ZorshivDredgionLocation getZorshivDredgionLocation(int id) {
		return zorshivDredgion.get(id);
	}
	
	public Map<Integer, ZorshivDredgionLocation> getZorshivDredgionLocations() {
		return zorshivDredgion;
	}
	
	public static ZorshivDredgionService getInstance() {
		return ZorshivDredgionServiceHolder.INSTANCE;
	}
	
	private static class ZorshivDredgionServiceHolder {
		private static final ZorshivDredgionService INSTANCE = new ZorshivDredgionService();
	}
}