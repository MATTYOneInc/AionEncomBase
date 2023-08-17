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
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.EvergaleCanyonReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.EvergaleCanyonPlayerReward;
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
import javolution.util.*;
import org.apache.commons.lang.mutable.MutableInt;

import java.util.*;
import java.util.concurrent.Future;

/****/
/** Author (Encom)
/** Source: https://aionpowerbook.com/powerbook/Windy_Gorge
/****/

@InstanceID(302350000)
public class EvergaleCanyonInstance extends GeneralInstanceHandler
{
	private long instanceTime;
	private Map<Integer, StaticDoor> doors;
	private Race RaceKilledCommander = null;
	private float loosingGroupMultiplier = 1;
	protected EvergaleCanyonReward evergaleCanyonReward;
	private boolean isInstanceDestroyed = false;
	private List<Integer> movies = new ArrayList<Integer>();
	private final FastList<Future<?>> evergaleCanyonTask = FastList.newInstance();
	
	protected EvergaleCanyonPlayerReward getPlayerReward(Player player) {
        evergaleCanyonReward.regPlayerReward(player);
        return (EvergaleCanyonPlayerReward) evergaleCanyonReward.getPlayerReward(player.getObjectId());
    }
	
    private boolean containPlayer(Integer object) {
        return evergaleCanyonReward.containPlayer(object);
    }
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
        }
    }
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(0, storage.getItemCountByItemId(0));
	}
	
	protected void startInstanceTask() {
		instanceTime = System.currentTimeMillis();
        evergaleCanyonReward.setInstanceStartTime();
		evergaleCanyonTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!evergaleCanyonReward.isRewarded()) {
				    openFirstDoors();
				    //The member recruitment window has passed. You cannot recruit any more members.
				    sendMsgByRace(1401181, Race.PC_ALL, 5000);
                    evergaleCanyonReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
                    startInstancePacket();
                    evergaleCanyonReward.sendPacket(4, null);
					//Teleport Statue.
				    sp(835273, 446.27618f, 752.14069f, 334.35410f, (byte) 0, 198, 0, 0, null);
					//Teleport Statue.
				    sp(835286, 1050.0051f, 752.30511f, 334.31192f, (byte) 0, 236, 0, 0, null);
					//Teleport Statue.
				    sp(835411, 719.26935f, 396.20844f, 305.75839f, (byte) 0, 253, 0, 0, null);
				    //Teleport Statue.
				    sp(835412, 746.86560f, 850.73126f, 347.88959f, (byte) 0, 65, 10000, 0, null);
				    //Teleport Statue.
				    sp(835413, 451.62146f, 1079.1924f, 347.28760f, (byte) 0, 55, 15000, 0, null);
				    //Teleport Statue.
				    sp(835414, 1035.4257f, 1065.4717f, 350.22650f, (byte) 0, 117, 20000, 0, null);
				}
            }
        }, 90000));
		evergaleCanyonTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//An element of the 2nd stage was added.
				sendMsgByRace(1404174, Race.PC_ALL, 0);
				//Mahot has appeared at the Eon Anvil.
				sendMsgByRace(1404254, Race.PC_ALL, 5000);
				//Daglon has appeared at the Eternal Anvil.
				sendMsgByRace(1404255, Race.PC_ALL, 10000);
				//Furtive Kaisan has appeared at the remaining altar.
				sendMsgByRace(1404275, Race.PC_ALL, 15000);
				//Corrupt Bagatur has appeared at the Jotun Garden.
				sendMsgByRace(1404276, Race.PC_ALL, 20000);
				sp(246701, 330.9594f, 957.66223f, 353.8341f, (byte) 82, 5000);
				sp(246702, 1185.338f, 957.84283f, 368.1132f, (byte) 111, 10000);
				sp(246703, 743.7306f, 487.89166f, 305.3329f, (byte) 23, 15000);
            }
        }, 120000));
		evergaleCanyonTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				//Corrupt Bagatur has appeared at the Jotun Garden.
				sendMsgByRace(1404173, Race.PC_ALL, 0);
            	sp(246704, 747.3699f, 1029.9686f, 334.3001f, (byte) 90, 0);
            }
        }, 480000));
		evergaleCanyonTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
            	if (!evergaleCanyonReward.isRewarded()) {
					Race winnerRace = evergaleCanyonReward.getWinnerRaceByScore();
					stopInstance(winnerRace);
				}
            }
        }, 1800000));
    }
	
	protected void stopInstance(Race race) {
        stopInstanceTask();
        evergaleCanyonReward.setWinnerRace(race);
        evergaleCanyonReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
        reward();
        evergaleCanyonReward.sendPacket(5, null);
    }
	
	@Override
    public void onEnterInstance(final Player player) {
        if (!containPlayer(player.getObjectId())) {
            evergaleCanyonReward.regPlayerReward(player);
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
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), evergaleCanyonReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), evergaleCanyonReward, player.getObjectId(), 0, 0));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), evergaleCanyonReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
            }
        });
    }
	
    private void sendPacket(boolean isObjects) {
    	if (isObjects) {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), evergaleCanyonReward, instance.getPlayersInside(), true));
                }
            });
    	} else {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), evergaleCanyonReward, instance.getPlayersInside(), true));
                }
            });
    	}
    }
	
	@Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
		doors = instance.getDoors();
		evergaleCanyonReward = new EvergaleCanyonReward(mapId, instanceId, instance);
        evergaleCanyonReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		startInstanceTask();
		switch (Rnd.get(1, 2)) {
		    case 1:
				spawn(835355, 725.68774f, 575.82648f, 321.44675f, (byte) 0, 105); //Firmly Closed Door.
				spawn(835356, 772.28290f, 614.70581f, 321.89883f, (byte) 0, 389); //Firmly Closed Door.
			break;
			case 2:
				spawn(835356, 725.68774f, 575.82648f, 321.44675f, (byte) 0, 105); //Firmly Closed Door.
				spawn(835355, 772.28290f, 614.70581f, 321.89883f, (byte) 0, 389); //Firmly Closed Door.
			break;
		}
    }
	
	protected void reward() {
        int ElyosPvPKills = getPvpKillsByRace(Race.ELYOS).intValue();
        int ElyosPoints = getPointsByRace(Race.ELYOS).intValue();
        int AsmoPvPKills = getPvpKillsByRace(Race.ASMODIANS).intValue();
        int AsmoPoints = getPointsByRace(Race.ASMODIANS).intValue();
        for (Player player: instance.getPlayersInside()) {
            if (PlayerActions.isAlreadyDead(player)) {
				PlayerReviveService.duelRevive(player);
			}
			EvergaleCanyonPlayerReward playerReward = evergaleCanyonReward.getPlayerReward(player.getObjectId());
			int abyssPoint = 3163;
			int gloryPoint = 150;
			int expPoint = 10000;
			playerReward.setRewardAp((int) abyssPoint);
            playerReward.setRewardGp((int) gloryPoint);
			playerReward.setRewardExp((int) expPoint);
			if (player.getRace().equals(evergaleCanyonReward.getWinnerRace())) {
                abyssPoint += evergaleCanyonReward.AbyssReward(true, isCommanderKilled(player.getRace()));
                gloryPoint += evergaleCanyonReward.GloryReward(true, isCommanderKilled(player.getRace()));
				expPoint += evergaleCanyonReward.ExpReward(true, isCommanderKilled(player.getRace()));
                playerReward.setBonusAp(evergaleCanyonReward.AbyssReward(true, isCommanderKilled(player.getRace())));
                playerReward.setBonusGp(evergaleCanyonReward.GloryReward(true, isCommanderKilled(player.getRace())));
				playerReward.setBonusExp(evergaleCanyonReward.ExpReward(true, isCommanderKilled(player.getRace())));
				playerReward.setCoinIdEternityWar01(186000472);
				playerReward.setBrokenSpinel(188100391);
			} else {
                abyssPoint += evergaleCanyonReward.AbyssReward(false, isCommanderKilled(player.getRace()));
                gloryPoint += evergaleCanyonReward.GloryReward(false, isCommanderKilled(player.getRace()));
				expPoint += evergaleCanyonReward.ExpReward(false, isCommanderKilled(player.getRace()));
				playerReward.setRewardAp(evergaleCanyonReward.AbyssReward(false, isCommanderKilled(player.getRace())));
                playerReward.setRewardGp(evergaleCanyonReward.GloryReward(false, isCommanderKilled(player.getRace())));
				playerReward.setRewardExp(evergaleCanyonReward.ExpReward(false, isCommanderKilled(player.getRace())));
				playerReward.setCoinIdEternityWar01(186000472);
				playerReward.setBrokenSpinel(188100391);
            } if (RaceKilledCommander == player.getRace()) {
				playerReward.setCashMinionContract01(190080008);
				playerReward.setCoinIdEternityWar01(186000472);
				playerReward.setBrokenSpinel(188100391);
				ItemService.addItem(player, 190080008, 1); //cash_minion_contract01.
			    ItemService.addItem(player, 186000472, 20); //coin_ideternity_war_01.
                ItemService.addItem(player, 188100391, 1000); //Fragmented Spinel 5.5
			}
			ItemService.addItem(player, 186000472, 10); //coin_ideternity_war_01.
            ItemService.addItem(player, 188100391, 500); //Fragmented Spinel 5.5
			AbyssPointsService.addAp(player, (int) abyssPoint);
            AbyssPointsService.addGp(player, (int) gloryPoint);
            player.getCommonData().addExp(expPoint, RewardType.HUNTING);
        } for (Npc npc: instance.getNpcs()) {
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
		evergaleCanyonReward.portToPosition(player);
        return true;
    }
	
	@Override
    public boolean onDie(Player player, Creature lastAttacker) {
		EvergaleCanyonPlayerReward ownerReward = evergaleCanyonReward.getPlayerReward(player.getObjectId());
		ownerReward.endBoostMoraleEffect(player);
		ownerReward.applyBoostMoraleEffect(player);
        int points = 10;
        if (lastAttacker instanceof Player) {
            if (lastAttacker.getRace() != player.getRace()) {
                InstancePlayerReward playerReward = evergaleCanyonReward.getPlayerReward(player.getObjectId());
				if (getPointsByRace(lastAttacker.getRace()).compareTo(getPointsByRace(player.getRace())) < 0) {
                    points *= loosingGroupMultiplier;
                } else if (loosingGroupMultiplier == 10 || playerReward.getPoints() == 0) {
                    points = 0;
                }
				ItemService.addItem(player, 186000470, 10); //War Points.
                updateScore((Player) lastAttacker, player, points, true);
            }
        }
        updateScore(player, player, -points, false);
        return true;
    }
	
	private boolean isCommanderKilled(Race PlayerRace) {
    	if (PlayerRace == RaceKilledCommander) {
    		return true;
    	}
    	return false;
    }
	
	private MutableInt getPvpKillsByRace(Race race) {
        return evergaleCanyonReward.getPvpKillsByRace(race);
    }
	
    private MutableInt getPointsByRace(Race race) {
        return evergaleCanyonReward.getPointsByRace(race);
    }
	
    private void addPointsByRace(Race race, int points) {
        evergaleCanyonReward.addPointsByRace(race, points);
    }
	
    private void addPvpKillsByRace(Race race, int points) {
        evergaleCanyonReward.addPvpKillsByRace(race, points);
    }
	
    private void addPointToPlayer(Player player, int points) {
        evergaleCanyonReward.getPlayerReward(player.getObjectId()).addPoints(points);
    }
	
    private void addPvPKillToPlayer(Player player) {
        evergaleCanyonReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
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
        } for (Player playerToGainScore : playersToGainScore) {
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
        evergaleCanyonReward.sendPacket(11, player.getObjectId());
    }
	
	@Override
	public void onDie(Npc npc) {
		int point = 0;
		Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
        if (mostPlayerDamage == null) {
            return;
        }
		Race race = mostPlayerDamage.getRace();
		switch (npc.getNpcId()) {
			case 246701: //Mahot.
				if (race.equals(Race.ELYOS)) {
				   //The Asmodians eliminated Mahot.
				   sendMsgByRace(1404186, Race.ELYOS, 0);
				   ItemService.addItem(mostPlayerDamage, 186000470, 200); //War Points.
				} else if (race.equals(Race.ASMODIANS)) {
				   //The Elyos eliminated Mahot.
				   sendMsgByRace(1404185, Race.ASMODIANS, 0);
				   ItemService.addItem(mostPlayerDamage, 186000470, 200); //War Points.
				}
				//"Mahot" appears every 3min after being killed.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
				        //"Mahot" has appeared at the remaining altar.
						sendMsgByRace(1404413, Race.PC_ALL, 0);
						spawn(246701, 330.9594f, 957.66223f, 353.8341f, (byte) 82); //Mahot.
					}
				}, 180000);
			break;
			case 246702: //Daglon.
				if (race.equals(Race.ELYOS)) {
				   //The Asmodians eliminated Daglon.
				   sendMsgByRace(1404188, Race.ELYOS, 0);
				   ItemService.addItem(mostPlayerDamage, 186000470, 200); //War Points.
				} else if (race.equals(Race.ASMODIANS)) {
				   //The Elyos eliminated Daglon.
				   sendMsgByRace(1404187, Race.ASMODIANS, 0);
				   ItemService.addItem(mostPlayerDamage, 186000470, 200); //War Points.
				}
				//"Daglon" appears every 3min after being killed.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						//"Daglon" has appeared at the remaining altar.
						sendMsgByRace(1404414, Race.PC_ALL, 0);
						spawn(246702, 1185.338f, 957.84283f, 368.1132f, (byte) 111); //Daglon.
					}
				}, 180000);
			break;
			case 246703: //Furtive Kaisan.
				if (race.equals(Race.ELYOS)) {
				   //The Asmodians eliminated Furtive Kaisan.
				   sendMsgByRace(1404190, Race.ELYOS, 0);
				   ItemService.addItem(mostPlayerDamage, 186000470, 200); //War Points.
				} else if (race.equals(Race.ASMODIANS)) {
				   //The Elyos eliminated Furtive Kaisan.
				   sendMsgByRace(1404189, Race.ASMODIANS, 0);
				   ItemService.addItem(mostPlayerDamage, 186000470, 200); //War Points.
				}
				//"Furtive Kaisan" appears every 3min after being killed.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						//"Furtive Kaisan" has appeared at the remaining altar.
						sendMsgByRace(1404172, Race.PC_ALL, 0);
						spawn(246703, 743.7306f, 487.89166f, 305.3329f, (byte) 23); //Furtive Kaisan.
					}
				}, 180000);
			break;
			case 246704: //Corrupt Bagatur.
				if (race.equals(Race.ELYOS)) {
				   //The Asmodians eliminated Corrupt Kaisan.
				   sendMsgByRace(1404192, Race.ELYOS, 0);
				   ItemService.addItem(mostPlayerDamage, 186000470, 200); //War Points.
				} else if (race.equals(Race.ASMODIANS)) {
				   //The Elyos eliminated Corrupt Kaisan.
				   sendMsgByRace(1404191, Race.ASMODIANS, 0);
				   ItemService.addItem(mostPlayerDamage, 186000470, 200); //War Points.
				}
				//"Corrupt Bagatur" appears every 8min after being killed.
				ThreadPoolManager.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						//"Corrupt Bagatur" has appeared at the Jotun Garden.
						sendMsgByRace(1404173, Race.PC_ALL, 0);
						spawn(246704, 747.3699f, 1029.9686f, 334.3001f, (byte) 90); //Corrupt Bagatur.
					}
				}, 480000);
			break;
			case 246709: //Archon Detachment Captain.
				point = 2000;
				//The Elyos have eliminated the Archon Detachment Captain.
				sendMsgByRace(1404209, Race.ASMODIANS, 0);
				//The Elyos have eliminated the Archon Detachment Captain.
				sendMsgByRace(1404365, Race.ASMODIANS, 10000);
				RaceKilledCommander = mostPlayerDamage.getRace();
				ItemService.addItem(mostPlayerDamage, 186000470, 500); //War Points.
			break;
			case 246714: //Guardian Detachment Captain.
				point = 2000;
				//The Asmodians have eliminated the Guardian Detachment Captain.
				sendMsgByRace(1404210, Race.ELYOS, 0);
				//The Asmodians have eliminated the Guardian Detachment Captain.
				sendMsgByRace(1404366, Race.ELYOS, 10000);
				RaceKilledCommander = mostPlayerDamage.getRace();
				ItemService.addItem(mostPlayerDamage, 186000470, 500); //War Points.
			break;
        }
		updateScore(mostPlayerDamage, npc, point, false);
    }
	
	@Override
    public void handleUseItemFinish(Player player, Npc npc) {
		int point = 0;
		switch (npc.getNpcId()) {
		   /**
			* Pure Neutral
			*/
			case 835210: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    //The Elyos took possession of the fragment at the Temple of Origin.
						sendMsgByRace(1404179, Race.PC_ALL, 2000);
					    sp(835304, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 6, 2000, 0, null); //Temple Of Origin.
						sp(835455, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //Temple Of Origin [Flag]
						sp(835238, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v03_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    //The Asmodians took possession of the fragment at the Temple of Origin.
						sendMsgByRace(1404180, Race.PC_ALL, 2000);
					    sp(835309, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 6, 2000, 0, null); //Temple Of Origin.
						sp(835456, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //Temple Of Origin [Flag]
						sp(835239, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v03_Flag_D.
					break;
				}
			break;
			case 835211: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    //The Elyos took possession of the fragment at the Northern Cave.
						sendMsgByRace(1404181, Race.PC_ALL, 2000);
					    //The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246750, 206.65828f, 450.01440f, 295.48935f, (byte) 0, 3000);
						sp(246750, 193.34024f, 436.78748f, 302.56314f, (byte) 0, 5000);
						sp(246750, 219.44083f, 452.26108f, 295.20300f, (byte) 0, 7000);
						sp(246750, 190.00842f, 448.16849f, 302.56314f, (byte) 0, 9000);
						sp(246750, 223.94318f, 439.94461f, 302.56314f, (byte) 0, 11000);
					    sp(835305, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 11, 2000, 0, null); //Northern Cave.
						sp(835455, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //Northern Cave [Flag]
						sp(835240, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v04_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    //The Asmodians took possession of the fragment at the Northern Cave.
						sendMsgByRace(1404182, Race.PC_ALL, 2000);
					    //The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246751, 206.65828f, 450.01440f, 295.48935f, (byte) 0, 3000);
						sp(246751, 193.34024f, 436.78748f, 302.56314f, (byte) 0, 5000);
						sp(246751, 219.44083f, 452.26108f, 295.20300f, (byte) 0, 7000);
						sp(246751, 190.00842f, 448.16849f, 302.56314f, (byte) 0, 9000);
						sp(246751, 223.94318f, 439.94461f, 302.56314f, (byte) 0, 11000);
					    sp(835310, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 11, 2000, 0, null); //Northern Cave.
						sp(835456, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //Northern Cave [Flag]
						sp(835241, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v04_Flag_D.
					break;
				}
			break;
			case 835212: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    //The Elyos took possession of the fragment at the Wall Ruins.
						sendMsgByRace(1404175, Race.PC_ALL, 2000);
					    sp(835306, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 12, 2000, 0, null); //Wall Ruins's.
						sp(835455, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //Wall Ruins's. [Flag]
						sp(835242, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v05_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    //The Asmodians took possession of the fragment at the Wall Ruins.
						sendMsgByRace(1404176, Race.PC_ALL, 2000);
					    sp(835311, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 12, 2000, 0, null); //Wall Ruins's.
						sp(835456, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //Wall Ruins's. [Flag]
						sp(835243, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v05_Flag_D.
					break;
				}
			break;
			case 835213: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    //The Elyos took possession of the fragment at the Collapsed Wall.
						sendMsgByRace(1404177, Race.PC_ALL, 2000);
					    sp(835307, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 41, 2000, 0, null); //Collapsed Wall's.
						sp(835455, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //Collapsed Wall's. [Flag]
						sp(835244, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v06_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    //The Asmodians took possession of the fragment at the Collapsed Wall.
						sendMsgByRace(1404178, Race.PC_ALL, 2000);
					    sp(835312, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 41, 2000, 0, null); //Collapsed Wall's.
						sp(835456, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //Collapsed Wall's. [Flag]
						sp(835245, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v06_Flag_D.
					break;
				}
			break;
			case 835214: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    //The Elyos took possession of the fragment at the Southern Cave.
						sendMsgByRace(1404183, Race.PC_ALL, 2000);
					    //The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246752, 1246.7339f, 405.43893f, 312.49734f, (byte) 0, 3000);
						sp(246752, 1235.1982f, 381.30444f, 312.49734f, (byte) 0, 5000);
						sp(246752, 1240.6429f, 393.54608f, 312.49734f, (byte) 0, 7000);
						sp(246752, 1260.6436f, 399.44736f, 312.49734f, (byte) 0, 9000);
						sp(246752, 1258.8136f, 387.54639f, 312.49734f, (byte) 0, 11000);
						sp(246752, 1246.9137f, 385.63171f, 312.49734f, (byte) 0, 13000);
					    sp(835308, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 42, 2000, 0, null); //Southern Cave.
						sp(835455, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //Southern Cave [Flag]
						sp(835246, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v07_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    //The Asmodians took possession of the fragment at the Southern Cave.
						sendMsgByRace(1404184, Race.PC_ALL, 2000);
					    //The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246753, 1246.7339f, 405.43893f, 312.49734f, (byte) 0, 3000);
						sp(246753, 1235.1982f, 381.30444f, 312.49734f, (byte) 0, 5000);
						sp(246753, 1240.6429f, 393.54608f, 312.49734f, (byte) 0, 7000);
						sp(246753, 1260.6436f, 399.44736f, 312.49734f, (byte) 0, 9000);
						sp(246753, 1258.8136f, 387.54639f, 312.49734f, (byte) 0, 11000);
						sp(246753, 1246.9137f, 385.63171f, 312.49734f, (byte) 0, 13000);
					    sp(835313, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 42, 2000, 0, null); //Southern Cave.
						sp(835456, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //Southern Cave [Flag]
						sp(835247, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v07_Flag_L.
					break;
				}
			break;
		   /**
			* Pure Light
			*/
			case 835304: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Temple of Origin.
						sendMsgByRace(1404179, Race.PC_ALL, 2000);
					    sp(835304, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 6, 2000, 0, null); //Temple Of Origin.
						sp(835455, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //Temple Of Origin [Flag].
						sp(835238, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v03_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Temple of Origin.
						sendMsgByRace(1404180, Race.PC_ALL, 2000);
					    sp(835309, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 6, 2000, 0, null); //Temple Of Origin.
						sp(835456, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //Temple Of Origin [Flag].
						sp(835239, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v03_Flag_D.
					break;
				}
			break;
			case 835305: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Northern Cave.
						sendMsgByRace(1404181, Race.PC_ALL, 2000);
						//The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246750, 206.65828f, 450.01440f, 295.48935f, (byte) 0, 3000);
						sp(246750, 193.34024f, 436.78748f, 302.56314f, (byte) 0, 5000);
						sp(246750, 219.44083f, 452.26108f, 295.20300f, (byte) 0, 7000);
						sp(246750, 190.00842f, 448.16849f, 302.56314f, (byte) 0, 9000);
						sp(246750, 223.94318f, 439.94461f, 302.56314f, (byte) 0, 11000);
					    sp(835305, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 11, 2000, 0, null); //Northern Cave.
						sp(835455, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //Northern Cave [Flag]
						sp(835240, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v04_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Northern Cave.
						sendMsgByRace(1404182, Race.PC_ALL, 2000);
						//The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246751, 206.65828f, 450.01440f, 295.48935f, (byte) 0, 3000);
						sp(246751, 193.34024f, 436.78748f, 302.56314f, (byte) 0, 5000);
						sp(246751, 219.44083f, 452.26108f, 295.20300f, (byte) 0, 7000);
						sp(246751, 190.00842f, 448.16849f, 302.56314f, (byte) 0, 9000);
						sp(246751, 223.94318f, 439.94461f, 302.56314f, (byte) 0, 11000);
					    sp(835310, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 11, 2000, 0, null); //Northern Cave.
						sp(835456, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //Northern Cave [Flag]
						sp(835241, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v04_Flag_D.
					break;
				}
			break;
			case 835306: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Wall Ruins.
						sendMsgByRace(1404175, Race.PC_ALL, 2000);
					    sp(835306, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 12, 2000, 0, null); //Wall Ruins's.
						sp(835455, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //Wall Ruins's. [Flag]
						sp(835242, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v05_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Wall Ruins.
						sendMsgByRace(1404176, Race.PC_ALL, 2000);
					    sp(835311, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 12, 2000, 0, null); //Wall Ruins's.
						sp(835456, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //Wall Ruins's. [Flag]
						sp(835243, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v05_Flag_D.
					break;
				}
			break;
			case 835307: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Collapsed Wall.
						sendMsgByRace(1404177, Race.PC_ALL, 2000);
					    sp(835307, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 41, 2000, 0, null); //Collapsed Wall's.
						sp(835455, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //Collapsed Wall's [Flag]
						sp(835244, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v06_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Collapsed Wall.
						sendMsgByRace(1404178, Race.PC_ALL, 2000);
					    sp(835312, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 41, 2000, 0, null); //Collapsed Wall's.
						sp(835456, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //Collapsed Wall's [Flag]
						sp(835245, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v06_Flag_D.
					break;
				}
			break;
			case 835308: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Southern Cave.
						sendMsgByRace(1404183, Race.PC_ALL, 2000);
						//The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246752, 1246.7339f, 405.43893f, 312.49734f, (byte) 0, 3000);
						sp(246752, 1235.1982f, 381.30444f, 312.49734f, (byte) 0, 5000);
						sp(246752, 1240.6429f, 393.54608f, 312.49734f, (byte) 0, 7000);
						sp(246752, 1260.6436f, 399.44736f, 312.49734f, (byte) 0, 9000);
						sp(246752, 1258.8136f, 387.54639f, 312.49734f, (byte) 0, 11000);
						sp(246752, 1246.9137f, 385.63171f, 312.49734f, (byte) 0, 13000);
					    sp(835308, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 42, 2000, 0, null); //Southern Cave.
						sp(835455, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //Southern Cave [Flag]
						sp(835246, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v07_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Southern Cave.
						sendMsgByRace(1404184, Race.PC_ALL, 2000);
						//The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246753, 1246.7339f, 405.43893f, 312.49734f, (byte) 0, 3000);
						sp(246753, 1235.1982f, 381.30444f, 312.49734f, (byte) 0, 5000);
						sp(246753, 1240.6429f, 393.54608f, 312.49734f, (byte) 0, 7000);
						sp(246753, 1260.6436f, 399.44736f, 312.49734f, (byte) 0, 9000);
						sp(246753, 1258.8136f, 387.54639f, 312.49734f, (byte) 0, 11000);
						sp(246753, 1246.9137f, 385.63171f, 312.49734f, (byte) 0, 13000);
					    sp(835313, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 42, 2000, 0, null); //Southern Cave.
						sp(835456, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //Southern Cave [Flag]
						sp(835247, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v07_Flag_D.
					break;
				}
			break;
		   /**
			* Pure Dark
			*/
			case 835309: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Temple of Origin.
						sendMsgByRace(1404179, Race.PC_ALL, 2000);
					    sp(835304, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 6, 2000, 0, null); //Temple Of Origin.
						sp(835455, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //Temple Of Origin [Flag].
						sp(835238, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v03_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Temple of Origin.
						sendMsgByRace(1404180, Race.PC_ALL, 2000);
					    sp(835309, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 6, 2000, 0, null); //Temple Of Origin.
						sp(835456, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //Temple Of Origin [Flag].
						sp(835239, 744.87201f, 756.45233f, 338.42093f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v03_Flag_D.
					break;
				}
			break;
			case 835310: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Northern Cave.
						sendMsgByRace(1404181, Race.PC_ALL, 2000);
						//The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246750, 206.65828f, 450.01440f, 295.48935f, (byte) 0, 3000);
						sp(246750, 193.34024f, 436.78748f, 302.56314f, (byte) 0, 5000);
						sp(246750, 219.44083f, 452.26108f, 295.20300f, (byte) 0, 7000);
						sp(246750, 190.00842f, 448.16849f, 302.56314f, (byte) 0, 9000);
						sp(246750, 223.94318f, 439.94461f, 302.56314f, (byte) 0, 11000);
					    sp(835305, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 11, 2000, 0, null); //Northern Cave.
						sp(835455, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //Northern Cave [Flag]
						sp(835240, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v04_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Northern Cave.
						sendMsgByRace(1404182, Race.PC_ALL, 2000);
						//The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246751, 206.65828f, 450.01440f, 295.48935f, (byte) 0, 3000);
						sp(246751, 193.34024f, 436.78748f, 302.56314f, (byte) 0, 5000);
						sp(246751, 219.44083f, 452.26108f, 295.20300f, (byte) 0, 7000);
						sp(246751, 190.00842f, 448.16849f, 302.56314f, (byte) 0, 9000);
						sp(246751, 223.94318f, 439.94461f, 302.56314f, (byte) 0, 11000);
					    sp(835310, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 11, 2000, 0, null); //Northern Cave.
						sp(835456, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //Northern Cave [Flag]
						sp(835241, 212.27483f, 443.66403f, 294.31482f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v04_Flag_D.
					break;
				}
			break;
			case 835311: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Wall Ruins.
						sendMsgByRace(1404175, Race.PC_ALL, 2000);
					    sp(835306, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 12, 2000, 0, null); //Wall Ruins's
						sp(835455, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //Wall Ruins's [Flag]
						sp(835242, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v05_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Wall Ruins.
						sendMsgByRace(1404176, Race.PC_ALL, 2000);
					    sp(835311, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 12, 2000, 0, null); //Wall Ruins's
						sp(835456, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //Wall Ruins's [Flag]
						sp(835243, 592.07892f, 640.55408f, 324.73904f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v05_Flag_D.
					break;
				}
			break;
			case 835312: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Collapsed Wall.
						sendMsgByRace(1404177, Race.PC_ALL, 2000);
					    sp(835307, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 41, 2000, 0, null); //Collapsed Wall's.
						sp(835455, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //Collapsed Wall's [Flag]
						sp(835244, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v06_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Collapsed Wall.
						sendMsgByRace(1404178, Race.PC_ALL, 2000);
					    sp(835312, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 41, 2000, 0, null); //Collapsed Wall's.
						sp(835456, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //Collapsed Wall's [Flag]
						sp(835245, 900.56952f, 637.95612f, 325.18738f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v06_Flag_D.
					break;
				}
			break;
			case 835313: //Artifact Core Fragment.
				despawnNpc(npc);
				switch (player.getRace()) {
					case ELYOS:
					    point = 200;
					    deleteNpc(835456);
					    //The Elyos took possession of the fragment at the Southern Cave.
						sendMsgByRace(1404183, Race.PC_ALL, 2000);
						//The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246752, 1246.7339f, 405.43893f, 312.49734f, (byte) 0, 3000);
						sp(246752, 1235.1982f, 381.30444f, 312.49734f, (byte) 0, 5000);
						sp(246752, 1240.6429f, 393.54608f, 312.49734f, (byte) 0, 7000);
						sp(246752, 1260.6436f, 399.44736f, 312.49734f, (byte) 0, 9000);
						sp(246752, 1258.8136f, 387.54639f, 312.49734f, (byte) 0, 11000);
						sp(246752, 1246.9137f, 385.63171f, 312.49734f, (byte) 0, 13000);
					    sp(835308, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 42, 2000, 0, null); //Southern Cave.
						sp(835455, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //Southern Cave [Flag]
						sp(835246, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v07_Flag_L.
					break;
					case ASMODIANS:
					    point = 200;
					    deleteNpc(835455);
					    //The Asmodians took possession of the fragment at the Southern Cave.
						sendMsgByRace(1404184, Race.PC_ALL, 2000);
						//The fairy Elb is requesting help from inside the cave.
						sendMsgByRace(1404246, Race.PC_ALL, 11000);
						//Elb: <Canyon Fairy>
						sp(246753, 1246.7339f, 405.43893f, 312.49734f, (byte) 0, 3000);
						sp(246753, 1235.1982f, 381.30444f, 312.49734f, (byte) 0, 5000);
						sp(246753, 1240.6429f, 393.54608f, 312.49734f, (byte) 0, 7000);
						sp(246753, 1260.6436f, 399.44736f, 312.49734f, (byte) 0, 9000);
						sp(246753, 1258.8136f, 387.54639f, 312.49734f, (byte) 0, 11000);
						sp(246753, 1246.9137f, 385.63171f, 312.49734f, (byte) 0, 13000);
					    sp(835313, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 42, 2000, 0, null); //Southern Cave.
						sp(835456, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //Southern Cave [Flag]
						sp(835247, 1250.4025f, 396.76108f, 309.01529f, (byte) 0, 0, 2000, 0, null); //IDEternityWar_v07_Flag_D.
					break;
				}
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
		evergaleCanyonReward.clear();
        stopInstanceTask();
        doors.clear();
    }
	
	protected void openFirstDoors() {
        openDoor(352);
		openDoor(507);
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
        evergaleCanyonTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        evergaleCanyonTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        evergaleCanyonTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        for (FastList.Node<Future<?>> n = evergaleCanyonTask.head(), end = evergaleCanyonTask.tail(); (n = n.getNext()) != end;) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
	@Override
    public InstanceReward<?> getInstanceReward() {
        return evergaleCanyonReward;
    }
	
	@Override
    public void onExitInstance(Player player) {
        TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
	
	@Override
    public void onLeaveInstance(Player player) {
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
		EvergaleCanyonPlayerReward playerReward = evergaleCanyonReward.getPlayerReward(player.getObjectId());
		playerReward.endBoostMoraleEffect(player);
    }
	
	private void sendMovie(Player player, int movie) {
        if (!movies.contains(movie)) {
             movies.add(movie);
             PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
        }
    }
	
	@Override
    public void onPlayerLogin(Player player) {
        evergaleCanyonReward.sendPacket(10, player.getObjectId());
    }
}