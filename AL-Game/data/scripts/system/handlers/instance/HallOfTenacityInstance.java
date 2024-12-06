/*
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
package instance;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.HallOfTenacityReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.HallOfTenacityPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

/****/
/** Author (Encom)
/****/

@InstanceID(302320000)
public class HallOfTenacityInstance extends GeneralInstanceHandler {

	private Logger log = LoggerFactory.getLogger(HallOfTenacityInstance.class);
	private long instanceTime;
	private boolean isInstanceDestroyed = false;
	protected HallOfTenacityReward instanceReward;
    protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
    private final FastList<Future<?>> hotTask = FastList.newInstance();

    protected HallOfTenacityPlayerReward getPlayerReward(Integer object) {
		instanceReward.regPlayerReward(object);
		return (HallOfTenacityPlayerReward) instanceReward.getPlayerReward(object);
	}
    
    private boolean containPlayer(Integer object) {
		return instanceReward.containPlayer(object);
	}
    
    @Override
	public InstanceReward<?> getInstanceReward() {
		return instanceReward;
	}

    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        instanceReward = new HallOfTenacityReward(mapId, instanceId, instance);
        instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
        startInstanceTask();
    }
    
    @Override
    public void onEnterInstance(final Player player) {
    	Integer object = player.getObjectId();
    	if (!containPlayer(object)) {
			instanceReward.regPlayerReward(object);
			getPlayerReward(object).applyBoostMoraleEffect(player);
			instanceReward.setStartPositions();
		}
        //sendEnterPacket(player);
    }
    
    @Override
	public void onExitInstance(Player player) {
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
    
    @Override
	public void onLeaveInstance(Player player) {
		//clearDebuffs(player);
    	HallOfTenacityPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward != null) {
			playerReward.endBoostMoraleEffect(player);
			instanceReward.removePlayerReward(playerReward);
		}
	}
    
    @Override
	public void onInstanceDestroy() {
		isInstanceDestroyed = true;
		instanceReward.clear();
	}
    
    private void sendEnterPacket(final Player player) {
    	instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(0, getTime(), instanceReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(9, getTime(), instanceReward, instance.getPlayersInside(), true));
            }
        });
    }

    protected void startInstanceTask() {
    	instanceTime = System.currentTimeMillis();
    	hotTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!instanceReward.isRewarded()) {
                	instanceReward.setInstanceStartTime();
                	instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
                	instanceReward.setCoupleSlotForBattle32();
                	instanceReward.sendLog("Hall Of Tenacity got "+instance.getPlayersInside().size()+" player(s)");
                	//instanceReward.sendPacket(0, null);
                	//instanceReward.sendPacket(9, null);
                	instance.doOnAllPlayers(new Visitor<Player>() {
                        @Override
                        public void visit(Player player) {
                        	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(0, getTime(), instanceReward, instance.getPlayersInside(), true));
                        	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(9, getTime(), instanceReward, instance.getPlayersInside(), true));
                        }
                    });
				}
            }
        }, 60000));//after enter 1 min will show versus board
    	hotTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!instanceReward.isRewarded()) {
				    instance.doOnAllPlayers(new Visitor<Player>() {
				    	
			            @Override
			            public void visit(Player player) {
			            	sendRequest(player);
			            }
			        });
				}
            }
        }, 150000));//after enter 1 min 30s will show enter battle window
    }
    
    public void sendRequest(final Player player) {
        RequestResponseHandler responseHandler = new RequestResponseHandler(player) {
            @Override
            public void acceptRequest(Creature requester, Player responder) {
            	instanceReward.portToArena(player);
            }
            @Override
            public void denyRequest(Creature requester, Player responder) {
            	//TODO skip battle
            }
        };
        boolean requested = player.getResponseRequester().putRequest(907265, responseHandler);
        if (requested) {
            PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(907265, 60, 0));
        }
    }
    
    protected void stopInstance(Race race) {
        stopInstanceTask();
        //hallOfTenacityReward.setWinner(race);
        instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
        reward();
        instanceReward.sendPacket(5, null);//TODO id
    }
    
    protected void reward() {
    	
    }
    
    private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = hotTask.head(), end = hotTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
    
    private int getTime() {
        long result = System.currentTimeMillis() - instanceTime;
        if (result < 60000) {
            return (int) (60000 - result);
        } else if (result < 300000) { //5-Mins
            return (int) (300000 - (result - 60000));
        }
        return 0;
    }
    
    protected void sendMsg(final int msg, final Race race, int time) {
    	hotTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                instance.doOnAllPlayers(new Visitor<Player>() {
                    @Override
                    public void visit(Player player) {
                        if (player.getRace().equals(race) || race.equals(Race.PC_ALL)) {
                            PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
                        }
                    }
                });
            }
        }, time));
    }
}