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

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.HallOfTenacityReward;
import com.aionemu.gameserver.model.instance.playerreward.HallOfTenacityPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

/****/
/** Author (Encom)
/****/

@InstanceID(302310000)
public class ArenaOfTenacityInstance extends GeneralInstanceHandler {

	private long instanceTime;
	private Map<Integer, StaticDoor> doors;
	protected HallOfTenacityReward instanceReward;
    protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
    private final FastList<Future<?>> hotTask = FastList.newInstance();
    
    protected HallOfTenacityPlayerReward getPlayerReward(Integer object) {
		instanceReward.regPlayerReward(object);
		return (HallOfTenacityPlayerReward) instanceReward.getPlayerReward(object);
	}

    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        instanceReward = new HallOfTenacityReward(mapId, instanceId, instance);
        instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
        doors = instance.getDoors();
        startInstanceTask();
    }
    
    @Override
    public void onEnterInstance(final Player player) {
        PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, player, 0, instanceReward));
    	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(4, player, getTime(), instanceReward));
    	Iterator<Player> iter = instance.getPlayersInside().iterator();
    	while (iter.hasNext()) {
    		PacketSendUtility.sendPacket(iter.next(), new SM_INSTANCE_SCORE(11, iter.next(), iter.next(), 0, instanceReward));
    	}
        //sendEnterPacket(player);
    }
    
    @Override
    public boolean onReviveEvent(Player player) {
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        PlayerReviveService.revive(player, 100, 100, false, 0);
        player.getGameStats().updateStatsAndSpeedVisually();
        instanceReward.portToArena(player);
        return true;
    }
    
    @Override
    public boolean onDie(Player player, Creature lastAttacker) {
    	HallOfTenacityPlayerReward ownerReward = instanceReward.getPlayerReward(player.getObjectId());
    	sendPacket();
		ownerReward.endBoostMoraleEffect(player);
		ownerReward.applyBoostMoraleEffect(player);
        int points = 60;
        if (lastAttacker instanceof Player) {
            if (lastAttacker.getRace() != player.getRace()) {
                InstancePlayerReward playerReward = instanceReward.getPlayerReward(player.getObjectId());
				points = 0;//TODO points
                updateScore((Player) lastAttacker, player, points, true);
            }
        }
        updateScore(player, player, -points, false);
        return true;
    }
    
    protected void sendPacket() {
		instanceReward.sendPacket();
	}
    
    protected void updateScore(Player player, Creature target, int points, boolean pvpKill) {
    	//TODO
    }
    
    private void sendEnterPacket(final Player player) {
    	instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
            	
            }
        });
    }
    
    protected void startInstanceTask() {
    	instanceTime = System.currentTimeMillis();
    	instanceReward.setInstanceStartTime();
    	hotTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!instanceReward.isRewarded()) {
                	openDoors();
                	instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
				    Iterator<Player> iter = instance.getPlayersInside().iterator();
			    	while (iter.hasNext()) {
			    		PacketSendUtility.sendPacket(iter.next(), new SM_INSTANCE_SCORE(5, iter.next(), iter.next(), getTime(), instanceReward));
			    	}
                    //startInstancePacket();
				}
            }
        }, 60000));
    	hotTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!instanceReward.isRewarded()) {
                	stopInstance();
				}
            }
        }, 300000));
    }
    
    protected void stopInstance() {
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
    
    protected void openDoors() {
        openDoor(157);
		openDoor(7);
    }
    
    protected void openDoor(int doorId) {
        StaticDoor door = doors.get(doorId);
        if (door != null) {
            door.setOpen(true);
        }
    }
    
    private void startInstancePacket() {
    	instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
            	//TODO packets
            	//PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), hallOfTenacityReward, instance.getPlayersInside(), true));
            	//PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), hallOfTenacityReward, player.getObjectId(), 0, 0));
            	//PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), hallOfTenacityReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(5, getTime(), instanceReward, player.getObjectId()));
            }
        });
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