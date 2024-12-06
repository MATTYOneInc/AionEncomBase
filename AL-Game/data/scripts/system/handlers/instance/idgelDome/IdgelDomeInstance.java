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
package instance.idgelDome;

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
import com.aionemu.gameserver.model.instance.instancereward.IdgelDomeReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.IdgelDomePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
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
/** Source: http://aion.power.plaync.com/wiki/%EB%A3%A8%EB%82%98%ED%86%A0%EB%A6%AC%EC%9B%80
/****/

@InstanceID(301310000)
public class IdgelDomeInstance extends GeneralInstanceHandler
{
    private long instanceTime;
	private Race RaceKilledKunax = null;
	private Map<Integer, StaticDoor> doors;
    protected IdgelDomeReward idgelDomeReward;
    private float loosingGroupMultiplier = 1;
    private boolean isInstanceDestroyed = false;
	private List<Integer> movies = new ArrayList<Integer>();
    protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
    private final FastList<Future<?>> idgelTask = FastList.newInstance();
    
    protected IdgelDomePlayerReward getPlayerReward(Player player) {
        idgelDomeReward.regPlayerReward(player);
        return (IdgelDomePlayerReward) idgelDomeReward.getPlayerReward(player.getObjectId());
    }
	
    private boolean containPlayer(Integer object) {
        return idgelDomeReward.containPlayer(object);
    }
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
            case 702581: //Intelligence Supply Box.
			case 702582: //Intelligence Supply Box.
			case 702583: //Intelligence Supply Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000314, 1)); //Devastation Bomb.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000315, 1)); //Freeze Bomb.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000316, 1)); //PvP Defense Scroll.
			break;
			case 234190: //Destroyer Kunax.
                for (Player player: instance.getPlayersInside()) {
                    if (player.isOnline()) {
                        dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053033, 1)); //Kunax's Equipment Box.
                    }
                }
            break;
        }
    }
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(164000314, storage.getItemCountByItemId(164000314)); //Devastation Bomb.
		storage.decreaseByItemId(164000315, storage.getItemCountByItemId(164000315)); //Freeze Bomb.
		storage.decreaseByItemId(164000316, storage.getItemCountByItemId(164000316)); //PvP Defense Scroll.
	}
	
    protected void startInstanceTask() {
    	instanceTime = System.currentTimeMillis();
        idgelDomeReward.setInstanceStartTime();
		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!idgelDomeReward.isRewarded()) {
				    openFirstDoors();
				    //The member recruitment window has passed. You cannot recruit any more members.
				    sendMsgByRace(1401181, Race.PC_ALL, 5000);
                    idgelDomeReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
                    startInstancePacket();
                    idgelDomeReward.sendPacket(4, null);
				}
            }
        }, 90000));
		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                idgelDomeReward.sendPacket(4, null);
				//Supplies have been dropped in a confidential area.
				sendMsgByRace(1402086, Race.PC_ALL, 0);
				sp(702581, 312.9132f, 311.31152f, 79.86219f, (byte) 104, 0); //Intelligence Supply Box.
				sp(702582, 216.0075f, 209.24077f, 79.86219f, (byte) 44, 0); //Intelligence Supply Box.
				sp(702583, 252.9754f, 246.21234f, 92.94253f, (byte) 15, 0); //Intelligence Supply Box.
				sp(702583, 276.4865f, 271.9778f, 92.94253f, (byte) 75, 0); //Intelligence Supply Box.
            }
        }, 300000));
		idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
            	sendPacket(false);
                idgelDomeReward.sendPacket(4, null);
                //Destroyer Kunax has spawned.
				sendMsgByRace(1402598, Race.PC_ALL, 0);
				//Destroyer Kunax has appeared in the Slaying Arena.
				sendMsgByRace(1402367, Race.PC_ALL, 10000);
                sp(234190, 266.579f, 257.436f, 85.81963f, (byte) 46, 0); //Destroyer Kunax.
				sp(234751, 250.67055f, 257.33798f, 85.81963f, (byte) 62, 0); //Sheban Elite Stalwart.
				sp(234752, 265.60724f, 272.637f, 85.81963f, (byte) 36, 0); //Sheban Elite Sniper.
				sp(234753, 263.66858f, 245.04124f, 85.81963f, (byte) 101, 0); //Sheban Elite Marauder.
				sp(234754, 278.0694f, 262.5485f, 85.81963f, (byte) 2, 0); //Sheban Elite Medic.
            }
        }, 600000));
    }
	
    protected void stopInstance(Race race) {
        stopInstanceTask();
        idgelDomeReward.setWinnerRace(race);
        idgelDomeReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
        reward();
        idgelDomeReward.sendPacket(5, null);
    }
	
    @Override
    public void onEnterInstance(final Player player) {
        if (!containPlayer(player.getObjectId())) {
            idgelDomeReward.regPlayerReward(player);
        } switch (player.getRace()) {
			case ELYOS:
			    sendMovie(player, 901);
			break;
			case ASMODIANS:
			    sendMovie(player, 902);
		    break;
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
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), idgelDomeReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), idgelDomeReward, player.getObjectId(), 0, 0));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), idgelDomeReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
            }
        });
    }
	
    private void sendPacket(boolean isObjects) {
    	if (isObjects) {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), idgelDomeReward, instance.getPlayersInside(), true));
                }
            });
    	} else {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), idgelDomeReward, instance.getPlayersInside(), true));
                }
            });
    	}
    }
	
    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        idgelDomeReward = new IdgelDomeReward(mapId, instanceId, instance);
        idgelDomeReward.setInstanceScoreType(InstanceScoreType.PREPARING);
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
			IdgelDomePlayerReward playerReward = idgelDomeReward.getPlayerReward(player.getObjectId());
			int abyssPoint = 3163;
			int gloryPoint = 150;
			int expPoint = 10000;
			playerReward.setRewardAp((int) abyssPoint);
            playerReward.setRewardGp((int) gloryPoint);
			playerReward.setRewardExp((int) expPoint);
			if (player.getRace().equals(idgelDomeReward.getWinnerRace())) {
                abyssPoint += idgelDomeReward.AbyssReward(true, isKunaxKilled(player.getRace()));
                gloryPoint += idgelDomeReward.GloryReward(true, isKunaxKilled(player.getRace()));
				expPoint += idgelDomeReward.ExpReward(true, isKunaxKilled(player.getRace()));
                playerReward.setBonusAp(idgelDomeReward.AbyssReward(true, isKunaxKilled(player.getRace())));
                playerReward.setBonusGp(idgelDomeReward.GloryReward(true, isKunaxKilled(player.getRace())));
				playerReward.setBonusExp(idgelDomeReward.ExpReward(true, isKunaxKilled(player.getRace())));
				playerReward.setBloodMark(186000236);
				playerReward.setBonusReward(186000243);
				playerReward.setIdgelDomeBox(188053030);
			} else {
                abyssPoint += idgelDomeReward.AbyssReward(false, isKunaxKilled(player.getRace()));
                gloryPoint += idgelDomeReward.GloryReward(false, isKunaxKilled(player.getRace()));
				expPoint += idgelDomeReward.ExpReward(false, isKunaxKilled(player.getRace()));
				playerReward.setRewardAp(idgelDomeReward.AbyssReward(false, isKunaxKilled(player.getRace())));
                playerReward.setRewardGp(idgelDomeReward.GloryReward(false, isKunaxKilled(player.getRace())));
				playerReward.setRewardExp(idgelDomeReward.ExpReward(false, isKunaxKilled(player.getRace())));
				playerReward.setBloodMark(186000236);
				playerReward.setBonusReward(186000243);
            } if (RaceKilledKunax == player.getRace()) {
			    playerReward.setAdditionalReward(188053032);
				ItemService.addItem(player, 188053032, 1);
			}
            ItemService.addItem(player, 188053030, 1);
            ItemService.addItem(player, 186000236, 1);
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
        } else if (result < 1200000) { //20-Mins
            return (int) (1200000 - (result - 90000));
        }
        return 0;
    }
	
    @Override
    public boolean onReviveEvent(Player player) {
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        PlayerReviveService.revive(player, 100, 100, false, 0);
        player.getGameStats().updateStatsAndSpeedVisually();
        idgelDomeReward.portToPosition(player);
        return true;
    }
	
    @Override
    public boolean onDie(Player player, Creature lastAttacker) {
		IdgelDomePlayerReward ownerReward = idgelDomeReward.getPlayerReward(player.getObjectId());
		ownerReward.endBoostMoraleEffect(player);
		ownerReward.applyBoostMoraleEffect(player);
        int points = 60;
        if (lastAttacker instanceof Player) {
            if (lastAttacker.getRace() != player.getRace()) {
                InstancePlayerReward playerReward = idgelDomeReward.getPlayerReward(player.getObjectId());
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
	
	private boolean isKunaxKilled(Race PlayerRace) {
    	if (PlayerRace == RaceKilledKunax) {
    		return true;
    	}
    	return false;
    }
	
	private MutableInt getPvpKillsByRace(Race race) {
        return idgelDomeReward.getPvpKillsByRace(race);
    }
    
    private MutableInt getPointsByRace(Race race) {
        return idgelDomeReward.getPointsByRace(race);
    }
	
    private void addPointsByRace(Race race, int points) {
        idgelDomeReward.addPointsByRace(race, points);
    }
	
    private void addPvpKillsByRace(Race race, int points) {
        idgelDomeReward.addPvpKillsByRace(race, points);
    }
	
    private void addPointToPlayer(Player player, int points) {
        idgelDomeReward.getPlayerReward(player.getObjectId()).addPoints(points);
    }
	
    private void addPvPKillToPlayer(Player player) {
        idgelDomeReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
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
        idgelDomeReward.sendPacket(11, player.getObjectId());
        if (idgelDomeReward.hasCapPoints()) {
            stopInstance(idgelDomeReward.getWinnerRaceByScore());
        }
    }
	
    @Override
	public void onDie(Npc npc) {
        int point = 0;
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
        if (mostPlayerDamage == null) {
            return;
        } switch (npc.getNpcId()) {
		    case 234186: //Sheban Intelligence Unit Ridgeblade.
            case 234187: //Sheban Intelligence Unit Hunter.
            case 234188: //Sheban Intelligence Unit Mongrel.
            case 234189: //Sheban Intelligence Unit Stitch.
                point = 120;
				despawnNpc(npc);
            break;
			case 234751: //Sheban Elite Stalwart.
            case 234752: //Sheban Elite Sniper.
            case 234753: //Sheban Elite Marauder.
            case 234754: //Sheban Elite Medic.
                point = 200;
				despawnNpc(npc);
            break;
			case 234190: //Destroyer Kunax.
                point = 6000;
				RaceKilledKunax = mostPlayerDamage.getRace();
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
					public void run() {
						if (!idgelDomeReward.isRewarded()) {
							Race winnerRace = idgelDomeReward.getWinnerRaceByScore();
							stopInstance(winnerRace);
						}
					}
				}, 30000);
            break;
        }
        updateScore(mostPlayerDamage, npc, point, false);
    }
	
    @Override
    public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
		    case 802192: //Flame Vent [Elyos].
			    //The Asmodian Flame Vent has been activated.\nThe Asmodians are trapped!
				sendMsgByRace(1402368, Race.PC_ALL, 0);
				sp(702404, 234.43842f, 194.1041f, 79.23065f, (byte) 105, 0);
				sp(702405, 234.13383f, 194.39594f, 79.23065f, (byte) 105, 0);
                sp(702405, 234.62419f, 193.95747f, 79.23065f, (byte) 45, 0);
                sp(702405, 234.42247f, 194.1363f, 79.23065f, (byte) 16, 0);
                sp(702405, 234.53394f, 194.27177f, 79.23065f, (byte) 75, 0);
			break;
			case 802193: //Flame Vent [Asmodians]
			    //The Elyos Flame Vent has been activated.\nThe Elyos are trapped!
				sendMsgByRace(1402369, Race.PC_ALL, 0);
				sp(702404, 294.57443f, 324.22205f, 79.23065f, (byte) 45, 0);
				sp(702405, 294.53418f, 324.0909f, 79.23065f, (byte) 105, 0);
                sp(702405, 294.66284f, 324.29172f, 79.23065f, (byte) 75, 0);
                sp(702405, 294.4634f, 323.84235f, 79.23065f, (byte) 15, 0);
                sp(702405, 294.70172f, 324.23065f, 79.23065f, (byte) 45, 0);
			break;
        }
    }
	
	private void despawnNpc(Npc npc) {
		if (npc != null) {
			npc.getController().onDelete();
		}
	}
	
    @Override
    public void onInstanceDestroy() {
        isInstanceDestroyed = true;
        idgelDomeReward.clear();
        stopInstanceTask();
        doors.clear();
    }
	
    protected void openFirstDoors() {
        openDoor(1);
		openDoor(99);
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
        idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        idgelTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        for (FastList.Node<Future<?>> n = idgelTask.head(), end = idgelTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
    @Override
    public InstanceReward<?> getInstanceReward() {
        return idgelDomeReward;
    }
	
    @Override
    public void onExitInstance(Player player) {
        TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
	
    @Override
    public void onLeaveInstance(Player player) {
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
		IdgelDomePlayerReward playerReward = idgelDomeReward.getPlayerReward(player.getObjectId());
		playerReward.endBoostMoraleEffect(player);
		removeItems(player);
    }
	
	@Override
	public void onPlayerLogOut(Player player) {
		removeItems(player);
	}
	
	private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
             movies.add(movie);
             PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
	
    @Override
    public void onPlayerLogin(Player player) {
        idgelDomeReward.sendPacket(10, player.getObjectId());
    }
}