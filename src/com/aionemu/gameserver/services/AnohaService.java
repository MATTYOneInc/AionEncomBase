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

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.schedule.AnohaSchedule;
import com.aionemu.gameserver.configs.schedule.AnohaSchedule.Anoha;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.anoha.AnohaLocation;
import com.aionemu.gameserver.model.anoha.AnohaStateType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.anohaspawns.AnohaSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.anohaservice.AnohaStartRunnable;
import com.aionemu.gameserver.services.anohaservice.BerserkAnoha;
import com.aionemu.gameserver.services.anohaservice.DanuarHero;
import com.aionemu.gameserver.services.teleport.TeleportService2;
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

public class AnohaService
{
	private AnohaSchedule anohaSchedule;
	private Map<Integer, AnohaLocation> anoha;
	private static final int duration = CustomConfig.ANOHA_DURATION;
	private static final Logger log = LoggerFactory.getLogger(AnohaService.class);

	//Berserk Anoha 4.7
	private FastMap<Integer, VisibleObject> adventSwordEffect = new FastMap<Integer, VisibleObject>();

	private final Map<Integer, BerserkAnoha<?>> activeAnoha = new FastMap<Integer, BerserkAnoha<?>>().shared();
	
	public void initAnohaLocations() {
		if (CustomConfig.ANOHA_ENABLED) {
			anoha = DataManager.ANOHA_DATA.getAnohaLocations();
			for (AnohaLocation loc: getAnohaLocations().values()) {
				spawn(loc, AnohaStateType.PEACE);
			}
			log.info("[AnohaService] Loaded " + anoha.size() + " locations.");
		} else {
			log.info("[AnohaService] Anoha is disabled in config...");
			anoha = Collections.emptyMap();
		}
	}
	
	public void initAnoha() {
		if (CustomConfig.ANOHA_ENABLED) {
			log.info("[AnohaService] is initialized...");
		    anohaSchedule = AnohaSchedule.load();
		    for (Anoha anoha: anohaSchedule.getAnohasList()) {
			    for (String berserkTime: anoha.getBerserkTimes()) {
				    CronService.getInstance().schedule(new AnohaStartRunnable(anoha.getId()), berserkTime);
			    }
			}
		}
	}
	
	public void startAnoha(final int id) {
		final BerserkAnoha<?> danuarhero;
		synchronized (this) {
			if (activeAnoha.containsKey(id)) {
				return;
			}
			danuarhero = new DanuarHero(anoha.get(id));
			activeAnoha.put(id, danuarhero);
		}
		danuarhero.start();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				stopAnoha(id);
			}
		}, duration * 3600 * 1000);
	}
	
	public void stopAnoha(int id) {
		if (!isAnohaInProgress(id)) {
			return;
		}
		BerserkAnoha<?> danuarhero;
		synchronized (this) {
			danuarhero = activeAnoha.remove(id);
		} if (danuarhero == null || danuarhero.isFinished()) {
			return;
		}
		danuarhero.stop();
	}
	
	public void spawn(AnohaLocation loc, AnohaStateType cstate) {
		if (cstate.equals(AnohaStateType.FIGHT)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getAnohaSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				AnohaSpawnTemplate anohatemplate = (AnohaSpawnTemplate) st;
				if (anohatemplate.getCStateType().equals(cstate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(anohatemplate, 1));
				}
			}
		}
	}
	
	public boolean adventSwordEffectSP(int id) {
        switch (id) {
            case 1:
                adventSwordEffect.put(702644, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(600090000, 702644, 791.27985f, 489.02353f, 142.90796f, (byte) 30), 1));
			    return true;
            default:
                return false;
        }
    }
	
	public boolean berserkAnohaMsg1(int id) {
        switch (id) {
            case 1:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Berserk Anoha will return to Kaldor in 30 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Named_Spawn_System, 0);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean wealhtheowGuardianMsg1(int id) {
        switch (id) {
            case 1:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Enraged Wealhtheow Guardian will appear in 5 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Anoha_01, 0);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean wealhtheowGuardianMsg2(int id) {
        switch (id) {
            case 1:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Enraged Wealhtheow Guardian will appear in 3 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Anoha_02, 0);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean wealhtheowGuardianMsg3(int id) {
        switch (id) {
            case 1:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Enraged Wealhtheow Guardian will appear in 1 minute.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Anoha_03, 0);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	public void sendRequest(final Player player) {
        String message = "Berserk Anoha has appeared. Do you want to fight ?";
        RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
            @Override
            public void acceptRequest(Creature requester, Player responder) {
				TeleportService2.teleportTo(responder, 600090000, 813.6149f, 503.42126f, 143.75f, (byte) 72);
            }
            @Override
            public void denyRequest(Creature requester, Player responder) {
            }
        };
        boolean requested = player.getResponseRequester().putRequest(902247, responseHandler);
        if (requested) {
            PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(902247, 0, 0, message));
        }
    }
	
	public void despawn(AnohaLocation loc) {
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
	
	public boolean isAnohaInProgress(int id) {
		return activeAnoha.containsKey(id);
	}
	
	public Map<Integer, BerserkAnoha<?>> getActiveAnoha() {
		return activeAnoha;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public AnohaLocation getAnohaLocation(int id) {
		return anoha.get(id);
	}
	
	public Map<Integer, AnohaLocation> getAnohaLocations() {
		return anoha;
	}
	
	public static AnohaService getInstance() {
		return AnohaServiceHolder.INSTANCE;
	}
	
	private static class AnohaServiceHolder {
		private static final AnohaService INSTANCE = new AnohaService();
	}
}