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
import com.aionemu.gameserver.configs.schedule.AgentSchedule;
import com.aionemu.gameserver.configs.schedule.AgentSchedule.Agent;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.agent.AgentLocation;
import com.aionemu.gameserver.model.agent.AgentStateType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.agentspawns.AgentSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.agentservice.AgentFight;
import com.aionemu.gameserver.services.agentservice.AgentStartRunnable;
import com.aionemu.gameserver.services.agentservice.Fight;
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

public class AgentService
{
	private AgentSchedule agentSchedule;
	private Map<Integer, AgentLocation> agent;
	private static final int duration = CustomConfig.AGENT_DURATION;
	private final Map<Integer, AgentFight<?>> activeFights = new FastMap<Integer, AgentFight<?>>().shared();
	private static final Logger log = LoggerFactory.getLogger(AgentService.class);

	public void initAgentLocations() {
		if (CustomConfig.AGENT_ENABLED) {
			agent = DataManager.AGENT_DATA.getAgentLocations();
			for (AgentLocation loc: getAgentLocations().values()) {
				spawn(loc, AgentStateType.PEACE);
			}
		log.info("[AgentService] Loaded " + agent.size() + " locations.");
		} else {
			log.info("[AgentService] Agent Fight is disabled in config...");
			agent = Collections.emptyMap();
		}
	}
	
	public void initAgent() {
		if (CustomConfig.AGENT_ENABLED) {
			log.info("[AgentService] is initialized...");
		    agentSchedule = AgentSchedule.load();
		    for (Agent agent: agentSchedule.getAgentsList()) {
			    for (String fightTime: agent.getFightTimes()) {
				    CronService.getInstance().schedule(new AgentStartRunnable(agent.getId()), fightTime);
			    }
			}
		}
	}
	
	public void startAgentFight(final int id) {
		final AgentFight<?> fight;
		synchronized (this) {
			if (activeFights.containsKey(id)) {
				return;
			}
			fight = new Fight(agent.get(id));
			activeFights.put(id, fight);
		}
		fight.start();
		empyreanLordCountdownMsg(id);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				stopAgentFight(id);
			}
		}, duration * 3600 * 1000);
	}
	
	public void stopAgentFight(int id) {
		if (!isFightInProgress(id)) {
			return;
		}
		AgentFight<?> fight;
		synchronized (this) {
			fight = activeFights.remove(id);
		} if (fight == null || fight.isFinished()) {
			return;
		}
		fight.stop();
	}
	
	public void spawn(AgentLocation loc, AgentStateType astate) {
		if (astate.equals(AgentStateType.FIGHT)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getAgentSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				AgentSpawnTemplate agenttemplate = (AgentSpawnTemplate) st;
				if (agenttemplate.getAStateType().equals(astate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(agenttemplate, 1));
				}
			}
		}
	}
	
   /**
	* The Empyrean Lord's Agent Countdown.
	*/
	public boolean empyreanLordCountdownMsg(int id) {
        switch (id) {
            case 1:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//The Empyrean Lord's Agent will end the battle in 30 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GODELITE_TimeAttack_Start, 5400000);
						//The Empyrean Lord's Agent has disappeared.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GODELITE_TimeAttack_Fail, 7200000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	public boolean agentBattleMsg1(int id) {
        switch (id) {
            case 1:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//The Agent battle will start in 10 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_GodElite_time_01, 0);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean agentBattleMsg2(int id) {
        switch (id) {
            case 1:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//The Agent battle will start in 5 minutes.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_GodElite_time_02, 0);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	public boolean governorSunayakaMsg(int id) {
        switch (id) {
            case 2:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Tiamat's Incarnation has appeared.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_TIAMATAVATAR_WAKEUP, 0);
						//Tiamat is getting stronger and stronger.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_TIAMATDOWN_USERKICK_MESSAGE, 10000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	public boolean berserkerSunayakaMsg(int id) {
        switch (id) {
            case 3:
                World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						//Tiamat's Incarnation has appeared.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_TIAMATAVATAR_WAKEUP, 0);
						//Tiamat is getting stronger and stronger.
						PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_TIAMATDOWN_USERKICK_MESSAGE, 10000);
					}
				});
			    return true;
            default:
                return false;
        }
    }
	
	public void despawn(AgentLocation loc) {
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
	
	public boolean isFightInProgress(int id) {
		return activeFights.containsKey(id);
	}
	
	public Map<Integer, AgentFight<?>> getActiveFights() {
		return activeFights;
	}
	
	public int getDuration() {
		return duration;
	}
	
	public AgentLocation getAgentLocation(int id) {
		return agent.get(id);
	}
	
	public Map<Integer, AgentLocation> getAgentLocations() {
		return agent;
	}
	
	public static AgentService getInstance() {
		return AgentServiceHolder.INSTANCE;
	}
	
	private static class AgentServiceHolder {
		private static final AgentService INSTANCE = new AgentService();
	}
}