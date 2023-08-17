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
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.LandMarkReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.LandMarkPlayerReward;
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
/****/

@InstanceID(301680000)
public class IdgelDomeLandmarkInstance extends GeneralInstanceHandler
{
    private long instanceTime;
	private Map<Integer, StaticDoor> doors;
    protected LandMarkReward landMarkReward;
    private float loosingGroupMultiplier = 1;
    private boolean isInstanceDestroyed = false;
	private List<Integer> movies = new ArrayList<Integer>();
    protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
    private final FastList<Future<?>> landMarkTask = FastList.newInstance();
    
    protected LandMarkPlayerReward getPlayerReward(Player player) {
        landMarkReward.regPlayerReward(player);
        return (LandMarkPlayerReward) landMarkReward.getPlayerReward(player.getObjectId());
    }
	
    private boolean containPlayer(Integer object) {
        return landMarkReward.containPlayer(object);
    }
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
            case 834168: //Bomb Support Box.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000413, 1)); //Support Bomb.
			break;
			case 834169: //Bomb Restraint Support Box.
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000414, 1)); //Support Restraining Bomb.
			break;
        }
    }
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(164000413, storage.getItemCountByItemId(164000413)); //Support Bomb.
		storage.decreaseByItemId(164000414, storage.getItemCountByItemId(164000414)); //Support Restraining Bomb.
	}
	
    protected void startInstanceTask() {
    	instanceTime = System.currentTimeMillis();
        landMarkReward.setInstanceStartTime();
		landMarkTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!landMarkReward.isRewarded()) {
				    openFirstDoors();
					spawn(833898, 264.65891f, 259.27396f, 88.502739f, (byte) 0, 60); //Sealed Reian Relic.
					spawn(806303, 249.47313f, 172.33987f, 79.688995f, (byte) 0, 198); //Central Square Teleport.
					spawn(806304, 279.98080f, 346.39691f, 79.695137f, (byte) 0, 197); //Central Square Teleport.
				    //The member recruitment window has passed. You cannot recruit any more members.
				    sendMsgByRace(1401181, Race.PC_ALL, 5000);
					//You need to activate the Aether Supply Device.
					sendMsgByRace(1403564, Race.PC_ALL, 10000);
                    landMarkReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
                    startInstancePacket();
                    landMarkReward.sendPacket(4, null);
				}
            }
        }, 90000));
		landMarkTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				sendPacket(false);
                landMarkReward.sendPacket(4, null);
                //A bomb support chest has appeared at the Blood War Room.
				sendMsgByRace(1403625, Race.ELYOS, 0);
				//A bomb support chest has appeared at the Blood War Room.
				sendMsgByRace(1403626, Race.ASMODIANS, 0);
				sp(834168, 252.9754f, 246.21234f, 92.94253f, (byte) 15, 0); //Bomb Support Box.
				sp(834169, 276.4865f, 271.9778f, 92.94253f, (byte) 75, 0); //Bomb Restraint Support Box.
            }
        }, 300000));
		landMarkTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
            	if (!landMarkReward.isRewarded()) {
					Race winnerRace = landMarkReward.getWinnerRaceByScore();
					stopInstance(winnerRace);
				}
            }
        }, 1200000));
    }
	
    protected void stopInstance(Race race) {
        stopInstanceTask();
        landMarkReward.setWinnerRace(race);
        landMarkReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
        reward();
        landMarkReward.sendPacket(5, null);
    }
	
    @Override
    public void onEnterInstance(final Player player) {
        if (!containPlayer(player.getObjectId())) {
            landMarkReward.regPlayerReward(player);
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
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), landMarkReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), landMarkReward, player.getObjectId(), 0, 0));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), landMarkReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
            }
        });
    }
	
    private void sendPacket(boolean isObjects) {
    	if (isObjects) {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), landMarkReward, instance.getPlayersInside(), true));
                }
            });
    	} else {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), landMarkReward, instance.getPlayersInside(), true));
                }
            });
    	}
    }
	
    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        landMarkReward = new LandMarkReward(mapId, instanceId, instance);
        landMarkReward.setInstanceScoreType(InstanceScoreType.PREPARING);
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
			LandMarkPlayerReward playerReward = landMarkReward.getPlayerReward(player.getObjectId());
			int abyssPoint = 3163;
			int gloryPoint = 150;
			int expPoint = 10000;
			playerReward.setRewardAp((int) abyssPoint);
            playerReward.setRewardGp((int) gloryPoint);
			playerReward.setRewardExp((int) expPoint);
			if (player.getRace().equals(landMarkReward.getWinnerRace())) {
                abyssPoint += landMarkReward.AbyssReward(true, true);
                gloryPoint += landMarkReward.GloryReward(true, true);
				expPoint += landMarkReward.ExpReward(true, true);
                playerReward.setBonusAp(landMarkReward.AbyssReward(true, true));
                playerReward.setBonusGp(landMarkReward.GloryReward(true, true));
				playerReward.setBonusExp(landMarkReward.ExpReward(true, true));
				playerReward.setBrokenSpinel(188100391);
				playerReward.setBonusReward(186000243);
				playerReward.setLandMarkBox(188053030);
				playerReward.setAdditionalReward(188055396); //ìœ ì ?ì§€ ìµœìƒ?ê¸‰ ë³´ìƒ? ìƒ?ìž?.
			} else {
                abyssPoint += landMarkReward.AbyssReward(false, false);
                gloryPoint += landMarkReward.GloryReward(false, false);
				expPoint += landMarkReward.ExpReward(false, false);
				playerReward.setRewardAp(landMarkReward.AbyssReward(false, false));
                playerReward.setRewardGp(landMarkReward.GloryReward(false, false));
				playerReward.setRewardExp(landMarkReward.ExpReward(false, false));
				playerReward.setBrokenSpinel(188100391);
				playerReward.setBonusReward(186000243);
            }
			ItemService.addItem(player, 188055396, 1); //ìœ ì ?ì§€ ìµœìƒ?ê¸‰ ë³´ìƒ? ìƒ?ìž?.
            ItemService.addItem(player, 188053030, 1);
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
        landMarkReward.portToPosition(player);
        return true;
    }
	
    @Override
    public boolean onDie(Player player, Creature lastAttacker) {
		LandMarkPlayerReward ownerReward = landMarkReward.getPlayerReward(player.getObjectId());
		ownerReward.endBoostMoraleEffect(player);
		ownerReward.applyBoostMoraleEffect(player);
        int points = 60;
        if (lastAttacker instanceof Player) {
            if (lastAttacker.getRace() != player.getRace()) {
                InstancePlayerReward playerReward = landMarkReward.getPlayerReward(player.getObjectId());
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
        return landMarkReward.getPvpKillsByRace(race);
    }
	
    private MutableInt getPointsByRace(Race race) {
        return landMarkReward.getPointsByRace(race);
    }
	
    private void addPointsByRace(Race race, int points) {
        landMarkReward.addPointsByRace(race, points);
    }
	
    private void addPvpKillsByRace(Race race, int points) {
        landMarkReward.addPvpKillsByRace(race, points);
    }
	
    private void addPointToPlayer(Player player, int points) {
        landMarkReward.getPlayerReward(player.getObjectId()).addPoints(points);
    }
	
    private void addPvPKillToPlayer(Player player) {
        landMarkReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
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
        landMarkReward.sendPacket(11, player.getObjectId());
    }
	
    @Override
	public void onDie(Npc npc) {
        int point = 0;
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
        if (mostPlayerDamage == null) {
            return;
        } switch (npc.getNpcId()) {
		    case 243965: //Rotten Clodworm.
			case 243966: //Rotten Mudthorn.
                point = 50;
				despawnNpc(npc);
            break;
        }
        updateScore(mostPlayerDamage, npc, point, false);
    }
	
    @Override
    public void handleUseItemFinish(Player player, Npc npc) {
		int point = 0;
		switch (npc.getNpcId()) {
			case 833898: //Sealed Reian Relic.
				point = 1000;
				despawnNpc(npc);
			break;
			/**
			 * Unsealing Device [Elyos]
			 */
			case 806343: //Unsealing Device.
				point = 200;
				despawnNpc(npc);
				//The Elyos activated stage 1 of the device.
				sendMsgByRace(1403428, Race.PC_ALL, 0);
			break;
			case 806344: //Unsealing Device.
			    point = 1000;
				despawnNpc(npc);
				//The Elyos activated stage 2 of the device.
				sendMsgByRace(1403429, Race.PC_ALL, 0);
			break;
			case 806345: //Unsealing Device.
			    point = 500;
				despawnNpc(npc);
				//The Elyos activated stage 3 of the device.
				sendMsgByRace(1403430, Race.PC_ALL, 0);
			break;
			case 806346: //Unsealing Device.
			    point = 50000;
				despawnNpc(npc);
				//The Elyos are activating the last stage of the device.
				sendMsgByRace(1403431, Race.PC_ALL, 0);
				//The Elyos successfully occupied this area.
				sendMsgByRace(1403434, Race.PC_ALL, 10000);
			break;
			/**
			 * Unsealing Device [Asmodians]
			 */
			case 806375: //Unsealing Device.
			    point = 200;
				despawnNpc(npc);
				//The Asmodians activated stage 1 of the device.
				sendMsgByRace(1403435, Race.PC_ALL, 0);
			break;
			case 806376: //Unsealing Device.
			    point = 1000;
				despawnNpc(npc);
				//The Asmodians activated stage 2 of the device.
				sendMsgByRace(1403436, Race.PC_ALL, 0);
			break;
			case 806377: //Unsealing Device.
			    point = 500;
				despawnNpc(npc);
				//The Asmodians activated stage 3 of the device.
				sendMsgByRace(1403437, Race.PC_ALL, 0);
			break;
			case 806378: //Unsealing Device.
			    point = 50000;
				despawnNpc(npc);
				//The Asmodians are activating the last stage of the device.
				sendMsgByRace(1403438, Race.PC_ALL, 0);
				//The Asmodians successfully occupied this area.
				sendMsgByRace(1403441, Race.PC_ALL, 10000);
			break;
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
		updateScore(player, npc, point, false);
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
        isInstanceDestroyed = true;
        landMarkReward.clear();
        stopInstanceTask();
        doors.clear();
    }
	
    protected void openFirstDoors() {
        openDoor(180);
		openDoor(181);
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
        landMarkTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        landMarkTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        landMarkTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        for (FastList.Node<Future<?>> n = landMarkTask.head(), end = landMarkTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
    @Override
    public InstanceReward<?> getInstanceReward() {
        return landMarkReward;
    }
	
    @Override
    public void onExitInstance(Player player) {
        TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
	
    @Override
    public void onLeaveInstance(Player player) {
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
		LandMarkPlayerReward playerReward = landMarkReward.getPlayerReward(player.getObjectId());
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
        landMarkReward.sendPacket(10, player.getObjectId());
    }
}