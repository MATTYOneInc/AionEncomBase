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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.PlayerActions;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.EngulfedOphidanBridgeReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.EngulfedOphidanBridgePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;
import javolution.util.FastList;
import org.apache.commons.lang.mutable.MutableInt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/** Source: //http://aion.power.plaync.com/wiki/%EC%9A%94%EB%A5%B4%EB%AC%B8%EA%B0%84%EB%93%9C+%EC%A7%84%EA%B2%A9%EB%A1%9C+-+%EC%A7%84%ED%96%89+%EC%A0%95%EB%B3%B4
/****/

@InstanceID(301210000)
public class EngulfedOphidanBridgeInstance extends GeneralInstanceHandler
{
	private long instanceTime;
	private int powerGenerator;
	private Map<Integer, StaticDoor> doors;
    protected EngulfedOphidanBridgeReward engulfedOphidanBridgeReward;
    private float loosingGroupMultiplier = 1;
    private boolean isInstanceDestroyed = false;
    protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
    private final FastList<Future<?>> ophidanTask = FastList.newInstance();
	
    protected EngulfedOphidanBridgePlayerReward getPlayerReward(Player player) {
        engulfedOphidanBridgeReward.regPlayerReward(player);
        return (EngulfedOphidanBridgePlayerReward) engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
    }
	
    private boolean containPlayer(Integer object) {
        return engulfedOphidanBridgeReward.containPlayer(object);
    }
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
        //http://aion.power.plaync.com/wiki/%EC%9A%94%EB%A5%B4%EB%AC%B8%EA%B0%84%EB%93%9C+%EC%A7%84%EA%B2%A9%EB%A1%9C+-+%EC%A7%84%ED%96%89+%EC%A0%95%EB%B3%B4
		switch (npcId) {
			case 701974: //Supply Box.
			case 701975: //Emergency Supply Box.
			case 701976: //Hidden Supply Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000279, 1)); //Advance Route Teleport Scroll.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000148, 1)); //Special Baily Juice.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000278, 1)); //Bombing Device Activation Key.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000150, 1)); //Emergency Stasis Potion.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000149, 1)); //Ambush Scroll.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000147, 1)); //Emergency Support Recovery Potion.
			break;
        }
    }
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(164000279, storage.getItemCountByItemId(164000279)); //Advance Route Teleport Scroll.
		storage.decreaseByItemId(162000148, storage.getItemCountByItemId(162000148)); //Special Baily Juice.
		storage.decreaseByItemId(164000278, storage.getItemCountByItemId(164000278)); //Bombing Device Activation Key.
		storage.decreaseByItemId(162000150, storage.getItemCountByItemId(162000150)); //Emergency Stasis Potion.
		storage.decreaseByItemId(162000149, storage.getItemCountByItemId(162000149)); //Ambush Scroll.
		storage.decreaseByItemId(162000147, storage.getItemCountByItemId(162000147)); //Emergency Support Recovery Potion.
	}
	
    protected void startInstanceTask() {
    	instanceTime = System.currentTimeMillis();
        engulfedOphidanBridgeReward.setInstanceStartTime();
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!engulfedOphidanBridgeReward.isRewarded()) {
				    openFirstDoors();
				    //The member recruitment window has passed. You cannot recruit any more members.
				    sendMsgByRace(1401181, Race.PC_ALL, 5000);
                    engulfedOphidanBridgeReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
                    startInstancePacket();
                    engulfedOphidanBridgeReward.sendPacket(4, null);
				}
            }
        }, 90000));
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//Reinforcements for the weaker camp have arrived at the sentry post.
				sendMsgByRace(1401949, Race.ELYOS, 0);
				//Reinforcements for the weaker camp have arrived at the sentry post.
				sendMsgByRace(1401950, Race.ASMODIANS, 0);
				sp(802023, 755.64215f, 545.90179f, 577.8269f, (byte) 0, 155);
				sp(802023, 337.73990f, 491.16772f, 597.2395f, (byte) 0, 156);
            }
        }, 220000));
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//A hero has been spotted at the starting point.
				sendMsgByRace(1401967, Race.PC_ALL, 0);
				//A hero and their reinforcements have been spotted at the starting point.
				sendMsgByRace(1401968, Race.PC_ALL, 10000);
				sp(701988, 313.6124f, 489.13992f, 597.13184f, (byte) 2, 0); //Rearguard Telekesis.
				sp(801957, 313.6124f, 489.13992f, 597.13184f, (byte) 2, 0); //Elyos Reinforcements Flag.
				sp(701989, 759.2739f, 569.3167f, 577.37885f, (byte) 87, 0); //Rearguard Freidr.
				sp(801958, 759.2739f, 569.3167f, 577.37885f, (byte) 87, 0); //Asmodians Reinforcements Flag.
            }
        }, 400000));
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				//Supplies have been delivered to some of the sentry posts.
				sendMsgByRace(1401965, Race.PC_ALL, 0);
				//Supplies have been dropped in a confidential area.
				sendMsgByRace(1402086, Race.PC_ALL, 10000);
				sp(701974, 322.18567f, 490.11285f, 596.1117f, (byte) 1, 0); //Supply Box.
				sp(701974, 758.0247f, 560.9797f, 576.9838f, (byte) 87, 0); //Supply Box.
				sp(701975, 574.02966f, 477.84848f, 620.6126f, (byte) 93, 10000); //Emergency Supply Box.
                sp(701975, 619.36755f, 515.6929f, 592.13336f, (byte) 55, 10000); //Emergency Supply Box.
                sp(701976, 582.56866f, 396.15695f, 603.4048f, (byte) 2, 10000); //Hidden Supply Box.
            }
        }, 600000));
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                engulfedOphidanBridgeReward.sendPacket(4, null);
				switch (Rnd.get(1, 4)) {
                    case 1:
                        //The Balaur raiders have appeared at some of the sentry posts.
						sendMsgByRace(1401966, Race.PC_ALL, 0);
						//The Balaur have arrived at the Northern Approach Post.
						sendMsgByRace(1402071, Race.PC_ALL, 5000);
						//The Balaur are attacking the Northern Approach Post.
						sendMsgByRace(1402073, Race.PC_ALL, 10000);
						sp(233491, 532.19055f, 445.263f, 620.25f, (byte) 105, 0); //Captain Avran.
						sp(801956, 532.19055f, 445.263f, 620.25f, (byte) 105, 0); //Assault Team Commander Flag.
                    break;
					case 2:
                        //The Balaur raiders have appeared at some of the sentry posts.
						sendMsgByRace(1401966, Race.PC_ALL, 0);
						//The Balaur have arrived at the Southern Approach Post.
						sendMsgByRace(1402066, Race.PC_ALL, 5000);
						//The Southern Approach Post is under attack by the Balaur.
						sendMsgByRace(1402068, Race.PC_ALL, 10000);
						sp(233491, 620.5344f, 562.1826f, 590.91034f, (byte) 81, 0); //Captain Avran.
						sp(801956, 620.5344f, 562.1826f, 590.91034f, (byte) 81, 0); //Assault Team Commander Flag.
                    break;
					case 3:
                        //The Balaur raiders have appeared at some of the sentry posts.
						sendMsgByRace(1401966, Race.PC_ALL, 0);
						//The Balaur have arrived at the Defense Post.
						sendMsgByRace(1402056, Race.PC_ALL, 5000);
						//The Defense Post is under attack by the Balaur.
						sendMsgByRace(1402058, Race.PC_ALL, 10000);
						sp(233491, 688.96906f, 484.00226f, 599.91016f, (byte) 94, 0); //Captain Avran.
						sp(801956, 688.96906f, 484.00226f, 599.91016f, (byte) 94, 0); //Assault Team Commander Flag.
                    break;
					case 4:
                        //The Balaur raiders have appeared at some of the sentry posts.
						sendMsgByRace(1401966, Race.PC_ALL, 0);
						//The Balaur have arrived at the Guard Post.
						sendMsgByRace(1402061, Race.PC_ALL, 5000);
						//The Guard Post is under attack by the Balaur.
						sendMsgByRace(1402063, Race.PC_ALL, 10000);
						sp(233491, 499.92856f, 520.9595f, 597.6485f, (byte) 20, 0); //Captain Avran.
						sp(801956, 499.92856f, 520.9595f, 597.6485f, (byte) 20, 0); //Assault Team Commander Flag.
                    break;
                }
            }
        }, 900000));
		ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!engulfedOphidanBridgeReward.isRewarded()) {
					Race winnerRace = engulfedOphidanBridgeReward.getWinnerRaceByScore();
					stopInstance(winnerRace);
				}
            }
        }, 1800000));
    }
	
    protected void stopInstance(Race race) {
        stopInstanceTask();
        engulfedOphidanBridgeReward.setWinnerRace(race);
        engulfedOphidanBridgeReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
        reward();
        engulfedOphidanBridgeReward.sendPacket(5, null);
    }
	
    @Override
    public void onEnterInstance(final Player player) {
        if (!containPlayer(player.getObjectId())) {
            engulfedOphidanBridgeReward.regPlayerReward(player);
        }
        sendEnterPacket(player);
    }
	
    private void sendEnterPacket(final Player player) {
    	instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player opponent) {
                if (player.getRace() != opponent.getRace()) {
                    PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
                    PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), opponent.getObjectId()));
                    PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime(), getInstanceReward(),  player.getObjectId()));
                } else {
                    PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), opponent.getObjectId()));
                    if (player.getObjectId() != opponent.getObjectId()) {
                        PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime(), getInstanceReward(), player.getObjectId(), 20, 0));
                    }
                }
            }
        });
    	sendPacket(true);
    	sendPacket(false);
        PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(4, getTime(), getInstanceReward(), player.getObjectId(), 20, 0));
    }
	
    private void startInstancePacket() {
    	instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), engulfedOphidanBridgeReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), engulfedOphidanBridgeReward, player.getObjectId(), 0, 0));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), engulfedOphidanBridgeReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
            }
        });
    }
	
    private void sendPacket(boolean isObjects) {
    	if (isObjects) {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), engulfedOphidanBridgeReward, instance.getPlayersInside(), true));
                }
            });
    	} else {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), engulfedOphidanBridgeReward, instance.getPlayersInside(), true));
                }
            });
    	}
    }
	
    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        engulfedOphidanBridgeReward = new EngulfedOphidanBridgeReward(mapId, instanceId, instance);
        engulfedOphidanBridgeReward.setInstanceScoreType(InstanceScoreType.PREPARING);
        doors = instance.getDoors();
        startInstanceTask();
    }
	
	protected void reward() {
        int ElyosPvPKills = getPvpKillsByRace(Race.ELYOS).intValue();
        int ElyosPoints = getPointsByRace(Race.ELYOS).intValue();
        int AsmoPvPKills = getPvpKillsByRace(Race.ASMODIANS).intValue();
        int AsmoPoints = getPointsByRace(Race.ASMODIANS).intValue();
        for (Player player : instance.getPlayersInside()) {
            if (PlayerActions.isAlreadyDead(player)) {
				PlayerReviveService.duelRevive(player);
			}
			EngulfedOphidanBridgePlayerReward playerReward = engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
			int abyssPoint = 3163;
			int gloryPoint = 150;
			int expPoint = 10000;
			playerReward.setRewardAp((int) abyssPoint);
            playerReward.setRewardGp((int) gloryPoint);
			playerReward.setRewardExp((int) expPoint);
			if (player.getRace().equals(engulfedOphidanBridgeReward.getWinnerRace())) {
                abyssPoint += engulfedOphidanBridgeReward.AbyssReward(true, true);
                gloryPoint += engulfedOphidanBridgeReward.GloryReward(true, true);
				expPoint += engulfedOphidanBridgeReward.ExpReward(true, true);
                playerReward.setBonusAp(engulfedOphidanBridgeReward.AbyssReward(true, true));
                playerReward.setBonusGp(engulfedOphidanBridgeReward.GloryReward(true, true));
				playerReward.setBonusExp(engulfedOphidanBridgeReward.ExpReward(true, true));
				playerReward.setBrokenSpinel(188100391);
				playerReward.setBonusReward(186000243);
				playerReward.setAdditionalReward(188052681);
			} else {
                abyssPoint += engulfedOphidanBridgeReward.AbyssReward(false, false);
                gloryPoint += engulfedOphidanBridgeReward.GloryReward(false, false);
				expPoint += engulfedOphidanBridgeReward.ExpReward(false, false);
				playerReward.setRewardAp(engulfedOphidanBridgeReward.AbyssReward(false, false));
                playerReward.setRewardGp(engulfedOphidanBridgeReward.GloryReward(false, false));
				playerReward.setRewardExp(engulfedOphidanBridgeReward.ExpReward(false, false));
				playerReward.setBrokenSpinel(188100391);
				playerReward.setBonusReward(186000243);
            }
			ItemService.addItem(player, 188052681, 1);
            ItemService.addItem(player, 188100391, 750); //5.5
			ItemService.addItem(player, 186000243, 1);
            AbyssPointsService.addAp(player, (int) abyssPoint);
            AbyssPointsService.addGp(player, (int) gloryPoint);
            player.getCommonData().addExp(expPoint, RewardType.HUNTING);
        }
        for (Npc npc : instance.getNpcs()) {
			npc.getController().onDelete();
		}
        ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isInstanceDestroyed) {
					for (Player player : instance.getPlayersInside()) {
						onExitInstance(player);
					}
					AutoGroupService.getInstance().unRegisterInstance(instanceId);
				}
			}
		}, 60000);
    }
	
    private int getTime() {
        long result = System.currentTimeMillis() - instanceTime;
        if (result < 90000) {
            return (int) (90000 - result);
        } else if (result < 1800000) { //30-Mins
            return (int) (1800000 - (result - 90000));
        }
        return 0;
    }
	
    @Override
    public boolean onReviveEvent(Player player) {
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        PlayerReviveService.revive(player, 100, 100, false, 0);
        player.getGameStats().updateStatsAndSpeedVisually();
        engulfedOphidanBridgeReward.portToPosition(player);
        return true;
    }
	
    @Override
    public boolean onDie(Player player, Creature lastAttacker) {
		EngulfedOphidanBridgePlayerReward ownerReward = engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
		ownerReward.endBoostMoraleEffect(player);
		ownerReward.applyBoostMoraleEffect(player);
        int points = 60;
        if (lastAttacker instanceof Player) {
            if (lastAttacker.getRace() != player.getRace()) {
                InstancePlayerReward playerReward = engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
				if (getPointsByRace(lastAttacker.getRace()).compareTo(getPointsByRace(player.getRace())) < 0) {
                    points *= loosingGroupMultiplier;
                } else if (loosingGroupMultiplier == 10 || playerReward.getPoints() == 0) {
                    points = 0;
                }
                updateScore((Player) lastAttacker, player, points, true);
            }
        }
        updateScore(player, player, -points, false);
        return true;
    }
	
	private MutableInt getPvpKillsByRace(Race race) {
        return engulfedOphidanBridgeReward.getPvpKillsByRace(race);
    }
	
    private MutableInt getPointsByRace(Race race) {
        return engulfedOphidanBridgeReward.getPointsByRace(race);
    }
	
    private void addPointsByRace(Race race, int points) {
        engulfedOphidanBridgeReward.addPointsByRace(race, points);
    }
	
    private void addPvpKillsByRace(Race race, int points) {
        engulfedOphidanBridgeReward.addPvpKillsByRace(race, points);
    }
	
    private void addPointToPlayer(Player player, int points) {
        engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId()).addPoints(points);
    }
	
    private void addPvPKillToPlayer(Player player) {
        engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
    }
	
    protected void updateScore(Player player, Creature target, int points, boolean pvpKill) {
        if (points == 0) {
            return;
        }
        addPointsByRace(player.getRace(), points);
        List<Player> playersToGainScore = new ArrayList<Player>();
        if (target != null && player.isInGroup2()) {
            for (Player member : player.getPlayerGroup2().getOnlineMembers()) {
                if (member.getLifeStats().isAlreadyDead()) {
                    continue;
                } if (MathUtil.isIn3dRange(member, target, GroupConfig.GROUP_MAX_DISTANCE)) {
                    playersToGainScore.add(member);
                }
            }
        } else {
            playersToGainScore.add(player);
        }
        for (Player playerToGainScore : playersToGainScore) {
            addPointToPlayer(playerToGainScore, points / playersToGainScore.size());
            if (target instanceof Npc) {
                PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId(((Npc) target).getObjectTemplate().getNameId() * 2 + 1), points));
            } else if (target instanceof Player) {
                PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, target.getName(), points));
            }
        }
        int pointDifference = getPointsByRace(Race.ASMODIANS).intValue() - (getPointsByRace(Race.ELYOS)).intValue();
        if (pointDifference < 0) {
            pointDifference *= -1;
        } if (pointDifference >= 3000) {
            loosingGroupMultiplier = 10;
        } else if (pointDifference >= 1000) {
            loosingGroupMultiplier = 1.5f;
        } else {
            loosingGroupMultiplier = 1;
        } if (pvpKill && points > 0) {
            addPvpKillsByRace(player.getRace(), 1);
            addPvPKillToPlayer(player);
        }
        engulfedOphidanBridgeReward.sendPacket(11, player.getObjectId());
        if (engulfedOphidanBridgeReward.hasCapPoints()) {
            stopInstance(engulfedOphidanBridgeReward.getWinnerRaceByScore());
        }
    }
	
	@Override
    public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("ENGULFED_OPHIDAN_BRIDGE_CHOKEPOINT_DEFENSE_POST_301210000")) {
            powerGenerator = 1;
	    } else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("ENGULFED_OPHIDAN_BRIDGE_NORTHERN_APPROACH_POST_301210000")) {
			powerGenerator = 2;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("ENGULFED_OPHIDAN_BRIDGE_SOUTHERN_APPROACH_POST_301210000")) {
			powerGenerator = 3;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("ENGULFED_OPHIDAN_BRIDGE_BRIDGEWATCH_POST_301210000")) {
			powerGenerator = 4;
		}
    }
	
    @Override
	public void onDie(Npc npc) {
        int point = 0;
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
        if (mostPlayerDamage == null) {
            return;
        }
		Race race = mostPlayerDamage.getRace();
		switch (npc.getObjectTemplate().getTemplateId()) {
			case 233473: //Beritra's Sentinel.
			case 233856: //Beritra Barricade.
				point = 100;
				despawnNpc(npc);
			break;
			case 233474: //Defense Post Magus.
			case 233475: //Defense Post Combatant.
			case 233476: //Defense Post Scout.	
			case 233478: //Northern Approach Post Magus.
			case 233479: //Northern Approach Post Combatant.
			case 233481: //Southern Approach Post Combatant.
			case 233482: //Southern Approach Post Scout.
			case 233484: //Guard Post Magus.
			case 233485: //Guard Post Combatant.
			case 233486: //Guard Post Scout.
			    point = 200;
				despawnNpc(npc);
			break;
			case 233477: //Defense Post Rearguard.
			case 233480: //Northern Approach Post Magician.
			case 233483: //Southern Approach Post Assaulter.
			case 233487: //Guard Post Rearguard.
			    point = 300;
				despawnNpc(npc);
			break;
			case 233846: //Templar Rearguard.
			case 233847: //Cleric Rearguard.
			case 233848: //Sorcerer Rearguard.
			case 233849: //Templar Rearguard.
			case 233850: //Cleric Rearguard.
			case 233851: //Sorcerer Rearguard.
			    point = 1500;
				despawnNpc(npc);
			break;
			case 233491: //Captain Avran.
			    point = 5000;
				despawnNpc(npc);
				deleteNpc(801956);
			break;
			case 701988: //Rearguard Telekesis.
			    despawnNpc(npc);
				//The Elyos Empyrean Lord has died.
				sendMsgByRace(1401959, Race.PC_ALL, 0);
			break;
			case 701989: //Rearguard Freidr.
			    despawnNpc(npc);
				//The Asmodian Empyrean Lord has died.
				sendMsgByRace(1401960, Race.PC_ALL, 0);
			break;
			case 701943: //Elyos Power Generator.
				despawnNpc(npc);
				point = 5000;
				if (powerGenerator == 1) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802033);
						deleteNpc(701969);
						//The Asmodians have captured the Guard Post.
						sendMsgByRace(1401991, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ASMODIANS, 5000);
					    sp(802034, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 0); //Chokepoint Defense Post Flag.
						sp(701944, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 173); //Asmodians Power Generator.
						sp(701969, 762.6721f, 544.30493f, 577.7007f, (byte) 91, 0); //Chokepoint Defense Post Mortar.
						sp(233849, 672.62286f, 467.2902f, 599.53894f, (byte) 107, 0); //Templar Rearguard.
						sp(233849, 663.40594f, 483.60574f, 599.7871f, (byte) 37, 0); //Templar Rearguard.
						sp(233850, 660.30194f, 466.5498f, 599.8218f, (byte) 77, 0); //Cleric Rearguard.
						sp(233851, 674.94977f, 478.36877f, 599.5594f, (byte) 8, 0); //Sorcerer Rearguard.
				    }
				} else if (powerGenerator == 2) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802036);
						deleteNpc(701970);
						//The Asmodians have captured the Northern Approach Post.
						sendMsgByRace(1401992, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ASMODIANS, 5000);
						sp(802037, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 0); //Northern Approach Post Flag.
						sp(701944, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 174); //Asmodians Power Generator.
						sp(701970, 760.40955f, 544.2923f, 577.7035f, (byte) 90, 0); //Northern Approach Post Mortar.
						sp(233849, 519.06854f, 434.295f, 620.125f, (byte) 45, 0); //Templar Rearguard.
						sp(233850, 533.65063f, 428.35898f, 620.25f, (byte) 5, 0); //Cleric Rearguard.
						sp(233851, 525.0882f, 436.3445f, 620.25f, (byte) 27, 0); //Sorcerer Rearguard.
				    }
				} else if (powerGenerator == 3) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802039);
						deleteNpc(701971);
						//The Asmodians have captured the Southern Approach Post.
						sendMsgByRace(1401993, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ASMODIANS, 5000);
						sp(802040, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 0); //Southern Approach Post Flag.
						sp(701944, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 166); //Asmodians Power Generator.
						sp(701971, 750.75836f, 545.71686f, 577.7213f, (byte) 84, 0); //Southern Approach Post Mortar.
						sp(233849, 610.57794f, 559.381f, 590.625f, (byte) 5, 0); //Templar Rearguard.
						sp(233850, 593.646f, 556.11426f, 590.5221f, (byte) 58, 0); //Cleric Rearguard.
						sp(233851, 607.1519f, 548.563f, 590.5f, (byte) 103, 0); //Sorcerer Rearguard.
				    }
				} else if (powerGenerator == 4) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(802042);
						deleteNpc(701972);
						//The Asmodians have captured the Defense Post.
						sendMsgByRace(1401994, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ASMODIANS, 5000);
						sp(802043, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 0); //Bridge Watchpost Flag.
						sp(701944, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 170); //Asmodians Power Generator.
						sp(701972, 748.57916f, 546.3481f, 577.72815f, (byte) 84, 0); //Bridge Watchpost Mortar.
						sp(233849, 483.3327f, 538.1493f, 597.5f, (byte) 58, 0); //Templar Rearguard.
						sp(233849, 498.373f, 543.4837f, 597.5f, (byte) 9, 0); //Templar Rearguard.
						sp(233850, 500.20483f, 532.2458f, 597.5f, (byte) 116, 0); //Cleric Rearguard.
						sp(233851, 484.61765f, 531.6245f, 597.375f, (byte) 70, 0); //Sorcerer Rearguard.
				    }
				}
			break;
			case 701944: //Asmodians Power Generator.
				point = 5000;
				despawnNpc(npc);
				if (powerGenerator == 1) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802034);
						deleteNpc(701969);
						//The Elyos have captured the Guard Post.
						sendMsgByRace(1401961, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ELYOS, 5000);
					    sp(802033, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 0); //Chokepoint Defense Post Flag.
						sp(701943, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 172); //Elyos Power Generator.
						sp(701969, 337.6665f, 498.31458f, 597.0435f, (byte) 3, 0); //Chokepoint Defense Post Mortar.
						sp(233846, 672.62286f, 467.2902f, 599.53894f, (byte) 107, 0); //Templar Rearguard.
						sp(233846, 663.40594f, 483.60574f, 599.7871f, (byte) 37, 0); //Templar Rearguard.
						sp(233847, 660.30194f, 466.5498f, 599.8218f, (byte) 77, 0); //Cleric Rearguard.
						sp(233848, 674.94977f, 478.36877f, 599.5594f, (byte) 8, 0); //Sorcerer Rearguard.
				    }
				} else if (powerGenerator == 2) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802037);
						deleteNpc(701970);
						//The Elyos have captured the Northern Approach Post.
						sendMsgByRace(1401962, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ELYOS, 5000);
						sp(802036, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 0); //Northern Approach Post Flag.
						sp(701943, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 175); //Elyos Power Generator.
						sp(701970, 338.08813f, 496.11847f, 597.04626f, (byte) 3, 0); //Northern Approach Post Mortar.
						sp(233846, 519.06854f, 434.295f, 620.125f, (byte) 45, 0); //Templar Rearguard.
						sp(233847, 533.65063f, 428.35898f, 620.25f, (byte) 5, 0); //Cleric Rearguard.
						sp(233848, 525.0882f, 436.3445f, 620.25f, (byte) 27, 0); //Sorcerer Rearguard.
				    }
				} else if (powerGenerator == 3) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802040);
						deleteNpc(701971);
						//The Elyos have captured the Southern Approach Post.
						sendMsgByRace(1401963, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ELYOS, 5000);
						sp(802039, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 0); //Southern Approach Post Flag.
						sp(701943, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 169); //Elyos Power Generator.
						sp(701971, 338.6412f, 486.42004f, 597.0637f, (byte) 118, 0); //Southern Approach Post Mortar.
						sp(233846, 610.57794f, 559.381f, 590.625f, (byte) 5, 0); //Templar Rearguard.
						sp(233847, 593.646f, 556.11426f, 590.5221f, (byte) 58, 0); //Cleric Rearguard.
						sp(233848, 607.1519f, 548.563f, 590.5f, (byte) 103, 0); //Sorcerer Rearguard.
				    }
				} else if (powerGenerator == 4) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(802043);
						deleteNpc(701972);
						//The Elyos have captured the Defense Post.
						sendMsgByRace(1401964, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ELYOS, 5000);
						sp(802042, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 0); //Bridge Watchpost Flag.
						sp(701943, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 171); //Elyos Power Generator.
						sp(701972, 338.46423f, 484.23608f, 597.07074f, (byte) 118, 0); //Bridge Watchpost Mortar.
						sp(233846, 483.3327f, 538.1493f, 597.5f, (byte) 58, 0); //Templar Rearguard.
						sp(233846, 498.373f, 543.4837f, 597.5f, (byte) 9, 0); //Templar Rearguard.
						sp(233847, 500.20483f, 532.2458f, 597.5f, (byte) 116, 0); //Cleric Rearguard.
						sp(233848, 484.61765f, 531.6245f, 597.375f, (byte) 70, 0); //Sorcerer Rearguard.
				    }
				}
			break;
			case 701945: //Balaur Power Generator.
				point = 5000;
				despawnNpc(npc);
				if (powerGenerator == 1) {
					if (race.equals(Race.ELYOS)) {
					    deleteNpc(802035);
						//*Balaur*//
						deleteNpc(233484); //Guard Post Magus.
						deleteNpc(233485); //Guard Post Combatant.
						deleteNpc(233486); //Guard Post Scout.
						deleteNpc(233487); //Guard Post Rearguard.
						//The Elyos have captured the Guard Post.
						sendMsgByRace(1401961, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ELYOS, 5000);
						sp(802033, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 0); //Chokepoint Defense Post Flag.
						sp(701943, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 172); //Elyos Power Generator.
						sp(701969, 337.6665f, 498.31458f, 597.0435f, (byte) 3, 0); //Chokepoint Defense Post Mortar.
						sp(233846, 672.62286f, 467.2902f, 599.53894f, (byte) 107, 0); //Templar Rearguard.
						sp(233846, 663.40594f, 483.60574f, 599.7871f, (byte) 37, 0); //Templar Rearguard.
						sp(233847, 660.30194f, 466.5498f, 599.8218f, (byte) 77, 0); //Cleric Rearguard.
						sp(233848, 674.94977f, 478.36877f, 599.5594f, (byte) 8, 0); //Sorcerer Rearguard.
					} else if (race.equals(Race.ASMODIANS)) {
					    deleteNpc(802035);
						//*Balaur*//
						deleteNpc(233484); //Guard Post Magus.
						deleteNpc(233485); //Guard Post Combatant.
						deleteNpc(233486); //Guard Post Scout.
						deleteNpc(233487); //Guard Post Rearguard.
						//The Asmodians have captured the Guard Post.
						sendMsgByRace(1401991, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ASMODIANS, 5000);
					    sp(802034, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 0); //Chokepoint Defense Post Flag.
						sp(701944, 667.11389f, 474.22995f, 600.48346f, (byte) 0, 173); //Asmodians Power Generator.
						sp(701969, 762.6721f, 544.30493f, 577.7007f, (byte) 91, 0); //Chokepoint Defense Post Mortar.
						sp(233849, 672.62286f, 467.2902f, 599.53894f, (byte) 107, 0); //Templar Rearguard.
						sp(233849, 663.40594f, 483.60574f, 599.7871f, (byte) 37, 0); //Templar Rearguard.
						sp(233850, 660.30194f, 466.5498f, 599.8218f, (byte) 77, 0); //Cleric Rearguard.
						sp(233851, 674.94977f, 478.36877f, 599.5594f, (byte) 8, 0); //Sorcerer Rearguard.
				    }
				} else if (powerGenerator == 2) {
					if (race.equals(Race.ELYOS)) {
					    deleteNpc(802038);
						//*Balaur*//
						deleteNpc(233478); //Northern Approach Post Magus.
						deleteNpc(233479); //Northern Approach Post Combatant.
						deleteNpc(233480); //Northern Approach Post Magician.
						//The Elyos have captured the Northern Approach Post.
						sendMsgByRace(1401962, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ELYOS, 5000);
						sp(802036, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 0); //Northern Approach Post Flag.
						sp(701943, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 175); //Elyos Power Generator.
						sp(701970, 338.08813f, 496.11847f, 597.04626f, (byte) 3, 0); //Northern Approach Post Mortar.
						sp(233846, 519.06854f, 434.295f, 620.125f, (byte) 45, 0); //Templar Rearguard.
						sp(233847, 533.65063f, 428.35898f, 620.25f, (byte) 5, 0); //Cleric Rearguard.
						sp(233848, 525.0882f, 436.3445f, 620.25f, (byte) 27, 0); //Sorcerer Rearguard.
					} else if (race.equals(Race.ASMODIANS)) {
					    deleteNpc(802038);
						//*Balaur*//
						deleteNpc(233478); //Northern Approach Post Magus.
						deleteNpc(233479); //Northern Approach Post Combatant.
						deleteNpc(233480); //Northern Approach Post Magician.
						//The Asmodians have captured the Northern Approach Post.
						sendMsgByRace(1401992, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ASMODIANS, 5000);
						sp(802037, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 0); //Northern Approach Post Flag.
						sp(701944, 524.84589f, 427.63959f, 621.21320f, (byte) 0, 174); //Asmodians Power Generator.
						sp(701970, 760.40955f, 544.2923f, 577.7035f, (byte) 90, 0); //Northern Approach Post Mortar.
						sp(233849, 519.06854f, 434.295f, 620.125f, (byte) 45, 0); //Templar Rearguard.
						sp(233850, 533.65063f, 428.35898f, 620.25f, (byte) 5, 0); //Cleric Rearguard.
						sp(233851, 525.0882f, 436.3445f, 620.25f, (byte) 27, 0); //Sorcerer Rearguard.
				    }
				} else if (powerGenerator == 3) {
					if (race.equals(Race.ELYOS)) {
					    deleteNpc(802041);
						//*Balaur*//
						deleteNpc(233481); //Southern Approach Post Combatant.
						deleteNpc(233482); //Southern Approach Post Scout.
						deleteNpc(233483); //Southern Approach Post Assaulter.
						//The Elyos have captured the Southern Approach Post.
						sendMsgByRace(1401963, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ELYOS, 5000);
						sp(802039, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 0); //Southern Approach Post Flag.
						sp(701943, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 169); //Elyos Power Generator.
						sp(701971, 338.6412f, 486.42004f, 597.0637f, (byte) 118, 0); //Southern Approach Post Mortar.
						sp(233846, 610.57794f, 559.381f, 590.625f, (byte) 5, 0); //Templar Rearguard.
						sp(233847, 593.646f, 556.11426f, 590.5221f, (byte) 58, 0); //Cleric Rearguard.
						sp(233848, 607.1519f, 548.563f, 590.5f, (byte) 103, 0); //Sorcerer Rearguard.
					} else if (race.equals(Race.ASMODIANS)) {
					    deleteNpc(802041);
						//*Balaur*//
						deleteNpc(233481); //Southern Approach Post Combatant.
						deleteNpc(233482); //Southern Approach Post Scout.
						deleteNpc(233483); //Southern Approach Post Assaulter.
						//The Asmodians have captured the Southern Approach Post.
						sendMsgByRace(1401993, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ASMODIANS, 5000);
						sp(802040, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 0); //Southern Approach Post Flag.
						sp(701944, 602.73395f, 556.29407f, 591.52533f, (byte) 0, 166); //Asmodians Power Generator.
						sp(701971, 750.75836f, 545.71686f, 577.7213f, (byte) 84, 0); //Southern Approach Post Mortar.
						sp(233849, 610.57794f, 559.381f, 590.625f, (byte) 5, 0); //Templar Rearguard.
						sp(233850, 593.646f, 556.11426f, 590.5221f, (byte) 58, 0); //Cleric Rearguard.
						sp(233851, 607.1519f, 548.563f, 590.5f, (byte) 103, 0); //Sorcerer Rearguard.
				    }
				} else if (powerGenerator == 4) {
					if (race.equals(Race.ELYOS)) {
					    deleteNpc(802044);
						//*Balaur*//
						deleteNpc(233474); //Defense Post Magus.
						deleteNpc(233475); //Defense Post Combatant.
						deleteNpc(233476); //Defense Post Scout.
						deleteNpc(233477); //Defense Post Rearguard.
						//The Elyos have captured the Defense Post.
						sendMsgByRace(1401964, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ELYOS, 5000);
						sp(802042, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 0); //Bridge Watchpost Flag.
						sp(701943, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 171); //Elyos Power Generator.
						sp(701972, 338.46423f, 484.23608f, 597.07074f, (byte) 118, 0); //Bridge Watchpost Mortar.
						sp(233846, 483.3327f, 538.1493f, 597.5f, (byte) 58, 0); //Templar Rearguard.
						sp(233846, 498.373f, 543.4837f, 597.5f, (byte) 9, 0); //Templar Rearguard.
						sp(233847, 500.20483f, 532.2458f, 597.5f, (byte) 116, 0); //Cleric Rearguard.
						sp(233848, 484.61765f, 531.6245f, 597.375f, (byte) 70, 0); //Sorcerer Rearguard.
					} else if (race.equals(Race.ASMODIANS)) {
					    deleteNpc(802044);
						//*Balaur*//
						deleteNpc(233474); //Defense Post Magus.
						deleteNpc(233475); //Defense Post Combatant.
						deleteNpc(233476); //Defense Post Scout.
						deleteNpc(233477); //Defense Post Rearguard.
						//The Asmodians have captured the Defense Post.
						sendMsgByRace(1401994, Race.PC_ALL, 0);
						//You have obtained extra points from the power generator.
						sendMsgByRace(1401957, Race.ASMODIANS, 5000);
						sp(802043, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 0); //Bridge Watchpost Flag.
						sp(701944, 492.81982f, 536.56732f, 598.24933f, (byte) 0, 170); //Asmodians Power Generator.
						sp(701972, 748.57916f, 546.3481f, 577.72815f, (byte) 84, 0); //Bridge Watchpost Mortar.
						sp(233849, 483.3327f, 538.1493f, 597.5f, (byte) 58, 0); //Templar Rearguard.
						sp(233849, 498.373f, 543.4837f, 597.5f, (byte) 9, 0); //Templar Rearguard.
						sp(233850, 500.20483f, 532.2458f, 597.5f, (byte) 116, 0); //Cleric Rearguard.
						sp(233851, 484.61765f, 531.6245f, 597.375f, (byte) 70, 0); //Sorcerer Rearguard.
				    }
				}
			break;
        }
        updateScore(mostPlayerDamage, npc, point, false);
    }
	
    @Override
    public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
		    case 701947: //Elyos Field Gun.
			case 701949: //Elyos Field Gun.
                if (player.getInventory().decreaseByItemId(164000277, 1)) { //Power Breaker.
				    //You've used one Power Breaker.
					sendMsgByRace(1402010,  Race.PC_ALL, 1000);
					SkillEngine.getInstance().getSkill(player, 21065, 1, player).useNoAnimationSkill();
			    } else {
					//You need a Power Breaker.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402006));
				}
            break;
			case 701948: //Asmodians Field Gun.
			case 701950: //Asmodians Field Gun.
                if (player.getInventory().decreaseByItemId(164000277, 1)) { //Power Breaker.
				    //You've used one Power Breaker.
					sendMsgByRace(1402010,  Race.PC_ALL, 1000);
					SkillEngine.getInstance().getSkill(player, 21066, 1, player).useNoAnimationSkill();
			    } else {
					//You need a Power Breaker.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402006));
				}
            break;
			case 701969: //Chokepoint Defense Post Mortar.
                if (player.getInventory().decreaseByItemId(164000278, 1)) { //Bombing Device Activation Key.
					//Bombardment has been activated on the siege base.\nBombing will begin soon.
					sendMsgByRace(1402110,  Race.PC_ALL, 0);
					//You've used one Ophidan Bombing Device Activation Key.
					sendMsgByRace(1402009,  Race.PC_ALL, 1000);
                    sp(855240, 659.4056f, 464.89233f, 599.9122f, (byte) 21, 2000);
					sp(855240, 666.66907f, 462.55884f, 599.7151f, (byte) 31, 2500);
					sp(855240, 675.36145f, 465.22815f, 599.625f, (byte) 47, 3000);
					sp(855240, 679.05774f, 473.21796f, 599.6911f, (byte) 60, 3500);
					sp(855240, 677.78613f, 480.65442f, 599.625f, (byte) 72, 4000);
					sp(855240, 669.91797f, 486.01047f, 599.75f, (byte) 88, 4500);
					sp(855240, 662.33215f, 486.08054f, 599.98425f, (byte) 96, 5000);
			    } else {
					//You need an Ophidan Bombing Device Activation Key.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402005));
				}
            break;
			case 701970: //Northern Approach Post Mortar.
                if (player.getInventory().decreaseByItemId(164000278, 1)) { //Bombing Device Activation Key.
					//Bombardment has been activated on the siege base.\nBombing will begin soon.
					sendMsgByRace(1402110,  Race.PC_ALL, 0);
					//You've used one Ophidan Bombing Device Activation Key.
					sendMsgByRace(1402009,  Race.PC_ALL, 1000);
					sp(855240, 529.0096f, 417.22366f, 620.125f, (byte) 43, 2000);
					sp(855240, 533.75183f, 421.21304f, 620.2008f, (byte) 48, 2500);
					sp(855240, 535.80133f, 429.1748f, 620.25f, (byte) 66, 3000);
					sp(855240, 531.301f, 436.3631f, 620.25f, (byte) 76, 3500);
					sp(855240, 525.2899f, 438.66245f, 620.25f, (byte) 88, 4000);
					sp(855240, 516.44604f, 436.7846f, 620.125f, (byte) 102, 4500);
					sp(855240, 512.9798f, 429.75674f, 620.25f, (byte) 116, 5000);
			    } else {
					//You need an Ophidan Bombing Device Activation Key.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402005));
				}
            break;
			case 701971: //Southern Approach Post Mortar.
                if (player.getInventory().decreaseByItemId(164000278, 1)) { //Bombing Device Activation Key.
					//Bombardment has been activated on the siege base.\nBombing will begin soon.
					sendMsgByRace(1402110,  Race.PC_ALL, 0);
					//You've used one Ophidan Bombing Device Activation Key.
					sendMsgByRace(1402009,  Race.PC_ALL, 1000);
					sp(855240, 613.2318f, 552.8324f, 590.625f, (byte) 55, 2000);
					sp(855240, 612.37695f, 559.9156f, 590.625f, (byte) 67, 2500);
					sp(855240, 606.91644f, 565.8719f, 590.5f, (byte) 84, 3000);
					sp(855240, 599.67896f, 566.28455f, 590.8712f, (byte) 96, 3500);
					sp(855240, 594.0308f, 563.2582f, 590.5786f, (byte) 103, 4000);
					sp(855240, 591.802f, 555.8142f, 590.625f, (byte) 2, 4500);
					sp(855240, 594.1872f, 549.05316f, 590.625f, (byte) 14, 5000);
					sp(855240, 600.87866f, 545.5543f, 590.52783f, (byte) 27, 5500);
					sp(855240, 609.32367f, 547.27893f, 590.54504f, (byte) 44, 6000);
			    } else {
					//You need an Ophidan Bombing Device Activation Key.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402005));
				}
            break;
			case 701972: //Bridge Watchpost Mortar.
                if (player.getInventory().decreaseByItemId(164000278, 1)) { //Bombing Device Activation Key.
					//Bombardment has been activated on the siege base.\nBombing will begin soon.
					sendMsgByRace(1402110,  Race.PC_ALL, 0);
					//You've used one Ophidan Bombing Device Activation Key.
					sendMsgByRace(1402009,  Race.PC_ALL, 1000);
					sp(855240, 495.5121f, 527.32605f, 597.5f, (byte) 37, 2000);
					sp(855240, 502.44363f, 531.56396f, 597.5f, (byte) 52, 2500);
					sp(855240, 502.87552f, 538.83093f, 597.5f, (byte) 65, 3000);
					sp(855240, 499.54196f, 544.8168f, 597.5f, (byte) 80, 3500);
					sp(855240, 491.6645f, 546.2845f, 597.5f, (byte) 93, 4000);
					sp(855240, 485.03534f, 543.95984f, 597.5f, (byte) 107, 4500);
					sp(855240, 481.37946f, 538.3027f, 597.4801f, (byte) 118, 5000);
					sp(855240, 483.40137f, 530.04224f, 597.375f, (byte) 13, 5500);
					sp(855240, 489.6f, 525.62933f, 597.475f, (byte) 25, 6000);
			    } else {
					//You need an Ophidan Bombing Device Activation Key.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402005));
				}
            break;
        }
    }
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
	private void deleteNpc(int npcId) {
		if (getNpc(npcId) != null) {
			getNpc(npcId).getController().onDelete();
		}
	}
	
    @Override
    public void onInstanceDestroy() {
        engulfedOphidanBridgeReward.clear();
        isInstanceDestroyed = true;
        stopInstanceTask();
        doors.clear();
    }
	
    protected void openFirstDoors() {
        openDoor(176);
		openDoor(177);
    }
	
    protected void openDoor(int doorId) {
        StaticDoor door = doors.get(doorId);
        if (door != null) {
            door.setOpen(true);
        }
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time) {
        sp(npcId, x, y, z, h, 0, time, 0, null);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race) {
        sp(npcId, x, y, z, h, 0, time, msg, race);
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race) {
        ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    spawn(npcId, x, y, z, h, entityId);
                    if (msg > 0) {
                        sendMsgByRace(msg, race, 0);
                    }
                }
            }
        }, time));
    }
	
    protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId) {
        ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!isInstanceDestroyed) {
                    Npc npc = (Npc) spawn(npcId, x, y, z, h);
                    npc.getSpawn().setWalkerId(walkerId);
                    WalkManager.startWalking((NpcAI2) npc.getAi2());
                }
            }
        }, time));
    }
	
    protected void sendMsgByRace(final int msg, final Race race, int time) {
        ophidanTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
	
	private void sendMsg(final String str) {
		instance.doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendWhiteMessageOnCenter(player, str);
			}
		});
	}
	
    private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = ophidanTask.head(), end = ophidanTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
    @Override
    public InstanceReward<?> getInstanceReward() {
        return engulfedOphidanBridgeReward;
    }
	
    @Override
    public void onExitInstance(Player player) {
        TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
	
    @Override
    public void onLeaveInstance(Player player) {
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
		EngulfedOphidanBridgePlayerReward playerReward = engulfedOphidanBridgeReward.getPlayerReward(player.getObjectId());
		playerReward.endBoostMoraleEffect(player);
		removeItems(player);
    }
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
    @Override
    public void onPlayerLogin(Player player) {
        engulfedOphidanBridgeReward.sendPacket(10, player.getObjectId());
    }
}