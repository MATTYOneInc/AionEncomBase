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
import com.aionemu.gameserver.model.instance.instancereward.IronWallWarfrontReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.IronWallWarfrontPlayerReward;
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
/****/

@InstanceID(301220000)
public class IronWallWarfrontInstance extends GeneralInstanceHandler
{
    private int ironWallBase;
	private long instanceTime;
	private Map<Integer, StaticDoor> doors;
	private Race RaceKilledCommander = null;
    protected IronWallWarfrontReward ironWallWarfrontReward;
    private float loosingGroupMultiplier = 1;
    private boolean isInstanceDestroyed = false;
    protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
    private final FastList<Future<?>> ironWallTask = FastList.newInstance();
    
    protected IronWallWarfrontPlayerReward getPlayerReward(Player player) {
        ironWallWarfrontReward.regPlayerReward(player);
        return (IronWallWarfrontPlayerReward) ironWallWarfrontReward.getPlayerReward(player.getObjectId());
    }
	
    private boolean containPlayer(Integer object) {
        return ironWallWarfrontReward.containPlayer(object);
    }
	
	@Override
    public void onDropRegistered(Npc npc) {
        Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
        switch (npcId) {
			//Asmodians Reinforcements.
			case 233510:
			case 233511:
			case 233512:
			//Elyos Reinforcements.
			case 233530:
			case 233531:
			case 233532:
			    dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000287, 5)); //Siege Turret Fuel.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000288, 5)); //Siege Cannonball.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000286, 5)); //Anti-Turret Grenade.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000285, 5)); //Stun Grenade.
			break;
			//Cannon Supplies Box & Ammo Box.
			case 831328:
			case 831329:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182006996, 10)); //Case Shot.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182006997, 10)); //Armor-Piercing Shot.
			break;
			//Cannon Supplies Box.
			case 831330:
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000137, 1)); //Aetheric Power Crystal.
			break;
        }
    }
	
    protected void startInstanceTask() {
    	instanceTime = System.currentTimeMillis();
        ironWallWarfrontReward.setInstanceStartTime();
		ironWallTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!ironWallWarfrontReward.isRewarded()) {
				    openFirstDoors();
				    //The member recruitment window has passed. You cannot recruit any more members.
				    sendMsgByRace(1401181, Race.PC_ALL, 5000);
                    ironWallWarfrontReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
                    startInstancePacket();
                    ironWallWarfrontReward.sendPacket(4, null);
				}
            }
        }, 90000)); //1:30-Mins
		ironWallTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                ironWallWarfrontReward.sendPacket(4, null);
				//A siege weapon has arrived at the siege base.
				sendMsgByRace(1402210,  Race.PC_ALL, 0);
				//A flame vehicle has been spotted in the siege base.
				sendMsgByRace(1402228,  Race.PC_ALL, 10000);
				//A battering ram has been spotted in the siege base.
				sendMsgByRace(1402229,  Race.PC_ALL, 20000);
				sp(701624, 422.98706f, 641.44116f, 214.52452f, (byte) 92, 0);
				sp(702589, 426.4476f, 617.95264f, 214.52452f, (byte) 32, 0);
            }
        }, 600000)); //10-Mins
		ironWallTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                sendPacket(false);
                ironWallWarfrontReward.sendPacket(4, null);
				//Supplies have arrived at their respective bind points.
				sendMsgByRace(1402206,  Race.PC_ALL, 0);
				sp(233510, 298.95648f, 399.21204f, 227.56165f, (byte) 17, 0); //Asmodians Supplies.
				sp(233511, 304.02267f, 396.9381f, 227.68314f, (byte) 26, 0); //Asmodians Supplies.
				sp(233512, 309.49344f, 395.97568f, 227.2273f, (byte) 28, 0); //Asmodians Supplies.
				sp(831979, 298.95648f, 399.21204f, 227.56165f, (byte) 17, 0); //Asmodians Supplies Flag.
				sp(831979, 304.02267f, 396.9381f, 227.68314f, (byte) 26, 0); //Asmodians Supplies Flag.
				sp(831979, 309.49344f, 395.97568f, 227.2273f, (byte) 28, 0); //Asmodians Supplies Flag.
				//**//
				sp(233530, 707.57275f, 648.9977f, 203.91081f, (byte) 10, 0); //Elyos Supplies.
				sp(233531, 706.97595f, 644.1978f, 203.07692f, (byte) 113, 0); //Elyos Supplies.
				sp(233532, 701.6543f, 643.7906f, 202.58696f, (byte) 84, 0); //Elyos Supplies.
				sp(831978, 707.57275f, 648.9977f, 203.91081f, (byte) 10, 0); //Elyos Supplies Flag.
				sp(831978, 706.97595f, 644.1978f, 203.07692f, (byte) 113, 0); //Elyos Supplies Flag.
				sp(831978, 701.6543f, 643.7906f, 202.58696f, (byte) 84, 0); //Elyos Supplies Flag.
            }
        }, 900000)); //15-Mins
		ironWallTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
				sendPacket(false);
                ironWallWarfrontReward.sendPacket(4, null);
				//Grand Commander Pashid has arrived with the Guard to assault the fortress.
				sendMsgByRace(1401819, Race.PC_ALL, 0);
				spawn(233544, 744.06085f, 293.31564f, 233.70102f, (byte) 104); //Commander Pashid.
				spawn(801956, 744.06085f, 293.31564f, 233.70102f, (byte) 104); //Assault Team Commander Flag.
            }
		}, 1800000)); //30-Mins
		ironWallTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (!ironWallWarfrontReward.isRewarded()) {
					Race winnerRace = ironWallWarfrontReward.getWinnerRaceByScore();
					stopInstance(winnerRace);
				}
            }
        }, 2400000)); //40-Mins
    }
	
    protected void stopInstance(Race race) {
        stopInstanceTask();
        ironWallWarfrontReward.setWinnerRace(race);
        ironWallWarfrontReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
        reward();
        ironWallWarfrontReward.sendPacket(5, null);
    }
	
    @Override
    public void onEnterInstance(final Player player) {
        if (!containPlayer(player.getObjectId())) {
            ironWallWarfrontReward.regPlayerReward(player);
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
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), ironWallWarfrontReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), ironWallWarfrontReward, player.getObjectId(), 0, 0));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), ironWallWarfrontReward, instance.getPlayersInside(), true));
            	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
            }
        });
    }
	
    private void sendPacket(boolean isObjects) {
    	if (isObjects) {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), ironWallWarfrontReward, instance.getPlayersInside(), true));
                }
            });
    	} else {
    		instance.doOnAllPlayers(new Visitor<Player>() {
                @Override
                public void visit(Player player) {
                	PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), ironWallWarfrontReward, instance.getPlayersInside(), true));
                }
            });
    	}
    }
	
    @Override
    public void onInstanceCreate(WorldMapInstance instance) {
        super.onInstanceCreate(instance);
        ironWallWarfrontReward = new IronWallWarfrontReward(mapId, instanceId, instance);
        ironWallWarfrontReward.setInstanceScoreType(InstanceScoreType.PREPARING);
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
			IronWallWarfrontPlayerReward playerReward = ironWallWarfrontReward.getPlayerReward(player.getObjectId());
			int abyssPoint = 3163;
			int gloryPoint = 300;
			int expPoint = 10000;
			playerReward.setRewardAp((int) abyssPoint);
            playerReward.setRewardGp((int) gloryPoint);
			playerReward.setRewardExp((int) expPoint);
			if (player.getRace().equals(ironWallWarfrontReward.getWinnerRace())) {
                abyssPoint += ironWallWarfrontReward.AbyssReward(true, isCommanderKilled(player.getRace()));
                gloryPoint += ironWallWarfrontReward.GloryReward(true, isCommanderKilled(player.getRace()));
				expPoint += ironWallWarfrontReward.ExpReward(true, isCommanderKilled(player.getRace()));
                playerReward.setBonusAp(ironWallWarfrontReward.AbyssReward(true, isCommanderKilled(player.getRace())));
                playerReward.setBonusGp(ironWallWarfrontReward.GloryReward(true, isCommanderKilled(player.getRace())));
				playerReward.setBonusExp(ironWallWarfrontReward.ExpReward(true, isCommanderKilled(player.getRace())));
				playerReward.setBrokenSpinel(188100391);
				playerReward.setBonusReward(186000243);
			} else {
                abyssPoint += ironWallWarfrontReward.AbyssReward(false, isCommanderKilled(player.getRace()));
                gloryPoint += ironWallWarfrontReward.GloryReward(false, isCommanderKilled(player.getRace()));
				expPoint += ironWallWarfrontReward.ExpReward(false, isCommanderKilled(player.getRace()));
				playerReward.setRewardAp(ironWallWarfrontReward.AbyssReward(false, isCommanderKilled(player.getRace())));
                playerReward.setRewardGp(ironWallWarfrontReward.GloryReward(false, isCommanderKilled(player.getRace())));
				playerReward.setRewardExp(ironWallWarfrontReward.ExpReward(false, isCommanderKilled(player.getRace())));
				playerReward.setBrokenSpinel(188100391);
				playerReward.setBonusReward(186000243);
            } if (RaceKilledCommander == player.getRace()) {
			    playerReward.setMedalBundle(188052938);
				ItemService.addItem(player, 188052938, 1);
			}
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
        } else if (result < 2400000) { //40-Mins
            return (int) (2400000 - (result - 90000));
        }
        return 0;
    }
	
    @Override
    public boolean onReviveEvent(Player player) {
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
        PlayerReviveService.revive(player, 100, 100, false, 0);
        player.getGameStats().updateStatsAndSpeedVisually();
        ironWallWarfrontReward.portToPosition(player);
        return true;
    }
	
    @Override
    public boolean onDie(Player player, Creature lastAttacker) {
		IronWallWarfrontPlayerReward ownerReward = ironWallWarfrontReward.getPlayerReward(player.getObjectId());
		ownerReward.endBoostMoraleEffect(player);
		ownerReward.applyBoostMoraleEffect(player);
        int points = 60;
        if (lastAttacker instanceof Player) {
            if (lastAttacker.getRace() != player.getRace()) {
                InstancePlayerReward playerReward = ironWallWarfrontReward.getPlayerReward(player.getObjectId());
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
	
	private boolean isCommanderKilled(Race PlayerRace) {
    	if (PlayerRace == RaceKilledCommander) {
    		return true;
    	}
    	return false;
    }
	
	private MutableInt getPvpKillsByRace(Race race) {
        return ironWallWarfrontReward.getPvpKillsByRace(race);
    }
	
    private MutableInt getPointsByRace(Race race) {
        return ironWallWarfrontReward.getPointsByRace(race);
    }
	
    private void addPointsByRace(Race race, int points) {
        ironWallWarfrontReward.addPointsByRace(race, points);
    }
	
    private void addPvpKillsByRace(Race race, int points) {
        ironWallWarfrontReward.addPvpKillsByRace(race, points);
    }
	
    private void addPointToPlayer(Player player, int points) {
        ironWallWarfrontReward.getPlayerReward(player.getObjectId()).addPoints(points);
    }
	
    private void addPvPKillToPlayer(Player player) {
        ironWallWarfrontReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
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
        ironWallWarfrontReward.sendPacket(11, player.getObjectId());
        if (ironWallWarfrontReward.hasCapPoints()) {
            stopInstance(ironWallWarfrontReward.getWinnerRaceByScore());
        }
    }
	
	@Override
    public void onEnterZone(Player player, ZoneInstance zone) {
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("PERIPHERAL_SUPPLY_BASE_301220000")) {
			ironWallBase = 1;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("MILITARY_SUPPLY_BASE_301220000")) {
			ironWallBase = 2;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("CENTRAL_SUPPLY_BASE_301220000")) {
            ironWallBase = 3;
	    } else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("ARTILLERY_COMMAND_CENTER_301220000")) {
			ironWallBase = 4;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("AXIAL_SENTRY_POST_301220000")) {
			ironWallBase = 5;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("ANCILLARY_SENTRY_POST_301220000")) {
			ironWallBase = 6;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("HOLY_GROUND_OF_RESURRECTION_301220000")) {
			ironWallBase = 7;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("ASSAULT_COMMAND_CENTER_301220000")) {
			ironWallBase = 8;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("HEADQUARTERS_301220000")) {
			ironWallBase = 9;
		} else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("HEADQUARTERS_ANNEX_301220000")) {
			ironWallBase = 10;
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
			case 233497: //Bridgewatch Post Scout.
			case 233508: //Asmodian Pathfinder.
			case 233517: //Elyos Garrison Scout.
			case 233528: //Elyos Pathfinder.
			case 233543: //Pashid Scout.
			case 233549: //Pashid Sentinel.
			case 233561: //Pashid Rider.
				despawnNpc(npc);
			break;
			case 233547: //Pashid Summoner Captain.
			case 233548: //Pashid Defender Captain.
			    point = 100;
				despawnNpc(npc);
			break;
			case 233537: //Pashid Beastmaster.
			    point = 250;
				despawnNpc(npc);
			break;
			case 233741: //Pashid Aetheric Cannon.
			    point = 250;
		        despawnNpc(npc);
				if (race.equals(Race.ELYOS)) {
					sp(701596, 726.56537f, 328.0778f, 254.21608f, (byte) 48, 0); //Elyos Cannon.
					sp(801960, 726.56537f, 328.0778f, 254.21608f, (byte) 48, 0); //Elyos Cannon Flag.
				} else if (race.equals(Race.ASMODIANS)) {
					sp(701610, 726.56537f, 328.0778f, 254.21608f, (byte) 48, 0); //Asmodians Cannon.
					sp(801961, 726.56537f, 328.0778f, 254.21608f, (byte) 48, 0); //Asmodians Cannon Flag.
				}
			break;
			case 233742: //Pashid Aetheric Cannon.
			    point = 250;
		        despawnNpc(npc);
				if (race.equals(Race.ELYOS)) {
					sp(701596, 761.6324f, 381.78177f, 240.92268f, (byte) 84, 0); //Elyos Cannon.
					sp(801960, 761.6324f, 381.78177f, 240.92268f, (byte) 84, 0); //Elyos Cannon Flag.
				} else if (race.equals(Race.ASMODIANS)) {
					sp(701610, 761.6324f, 381.78177f, 240.92268f, (byte) 84, 0); //Asmodians Cannon.
					sp(801961, 761.6324f, 381.78177f, 240.92268f, (byte) 84, 0); //Asmodians Cannon Flag.
				}
			break;
			case 233743: //Pashid Aetheric Cannon.
			    point = 250;
		        despawnNpc(npc);
				if (race.equals(Race.ELYOS)) {
					sp(701596, 710.0556f, 410.75735f, 241.01321f, (byte) 32, 0); //Elyos Cannon.
					sp(801960, 710.0556f, 410.75735f, 241.01321f, (byte) 32, 0); //Elyos Cannon Flag.
				} else if (race.equals(Race.ASMODIANS)) {
					sp(701610, 710.0556f, 410.75735f, 241.01321f, (byte) 32, 0); //Asmodians Cannon.
					sp(801961, 710.0556f, 410.75735f, 241.01321f, (byte) 32, 0); //Asmodians Cannon Flag.
				}
			break;
			case 233744: //Pashid Aetheric Cannon.
			    point = 250;
		        despawnNpc(npc);
				if (race.equals(Race.ELYOS)) {
					sp(701596, 625.838f, 339.61523f, 235.74188f, (byte) 54, 0); //Elyos Cannon.
					sp(801960, 625.838f, 339.61523f, 235.74188f, (byte) 54, 0); //Elyos Cannon Flag.
				} else if (race.equals(Race.ASMODIANS)) {
					sp(701610, 625.838f, 339.61523f, 235.74188f, (byte) 54, 0); //Asmodians Cannon.
					sp(801961, 625.838f, 339.61523f, 235.74188f, (byte) 54, 0); //Asmodians Cannon Flag.
				}
			break;
			case 233745: //Pashid Aetheric Cannon.
			    point = 250;
		        despawnNpc(npc);
				if (race.equals(Race.ELYOS)) {
					sp(701596, 644.94305f, 302.79245f, 235.74263f, (byte) 114, 0); //Elyos Cannon.
					sp(801960, 644.94305f, 302.79245f, 235.74263f, (byte) 114, 0); //Elyos Cannon Flag.
				} else if (race.equals(Race.ASMODIANS)) {
					sp(701610, 644.94305f, 302.79245f, 235.74263f, (byte) 114, 0); //Asmodians Cannon.
					sp(801961, 644.94305f, 302.79245f, 235.74263f, (byte) 114, 0); //Asmodians Cannon Flag.
				}
			break;
			case 233746: //Pashid Field Gun.
			    point = 250;
		        despawnNpc(npc);
				if (race.equals(Race.ELYOS)) {
					sp(701596, 618.3957f, 361.9848f, 224.94342f, (byte) 42, 0); //Elyos Cannon.
					sp(801960, 618.3957f, 361.9848f, 224.94342f, (byte) 42, 0); //Elyos Cannon Flag.
				} else if (race.equals(Race.ASMODIANS)) {
					sp(701610, 618.3957f, 361.9848f, 224.94342f, (byte) 42, 0); //Asmodians Cannon.
					sp(801961, 618.3957f, 361.9848f, 224.94342f, (byte) 42, 0); //Asmodians Cannon Flag.
				}
			break;
			case 233747: //Pashid Field Gun.
			    point = 250;
		        despawnNpc(npc);
				if (race.equals(Race.ELYOS)) {
					sp(701596, 685.2113f, 427.90024f, 229.82187f, (byte) 33, 0); //Elyos Cannon.
					sp(801960, 685.2113f, 427.90024f, 229.82187f, (byte) 33, 0); //Elyos Cannon Flag.
				} else if (race.equals(Race.ASMODIANS)) {
					sp(701610, 685.2113f, 427.90024f, 229.82187f, (byte) 33, 0); //Asmodians Cannon.
					sp(801961, 685.2113f, 427.90024f, 229.82187f, (byte) 33, 0); //Asmodians Cannon Flag.
				}
			break;
			case 233748: //Pashid Field Gun.
			    point = 250;
		        despawnNpc(npc);
				if (race.equals(Race.ELYOS)) {
					sp(701596, 670.0691f, 560.67737f, 229.34996f, (byte) 113, 0); //Elyos Cannon.
					sp(801960, 670.0691f, 560.67737f, 229.34996f, (byte) 113, 0); //Elyos Cannon Flag.
				} else if (race.equals(Race.ASMODIANS)) {
					sp(701610, 670.0691f, 560.67737f, 229.34996f, (byte) 113, 0); //Asmodians Cannon.
					sp(801961, 670.0691f, 560.67737f, 229.34996f, (byte) 113, 0); //Asmodians Cannon Flag.
				}
			break;
			case 233749: //Pashid Field Gun.
			    point = 250;
		        despawnNpc(npc);
				if (race.equals(Race.ELYOS)) {
					sp(701596, 518.4247f, 230.84209f, 231.92047f, (byte) 0, 0); //Elyos Cannon.
					sp(801960, 518.4247f, 230.84209f, 231.92047f, (byte) 0, 0); //Elyos Cannon Flag.
				} else if (race.equals(Race.ASMODIANS)) {
					sp(701610, 518.4247f, 230.84209f, 231.92047f, (byte) 0, 0); //Asmodians Cannon.
					sp(801961, 518.4247f, 230.84209f, 231.92047f, (byte) 0, 0); //Asmodians Cannon Flag.
				}
			break;
			case 233510: //Asmodians Reinforcements.
			case 233511: //Asmodians Reinforcements.
			case 233512: //Asmodians Reinforcements.
			case 233530: //Elyos Reinforcements.
			case 233531: //Elyos Reinforcements.
			case 233532: //Elyos Reinforcements.
			    point = 400;
				despawnNpc(npc);
			break;
			case 233564: //Inner Wall.
			case 233565: //Loose Wall.
			case 233566: //Rear Portcullis.
			case 233567: //Main Portcullis.
			case 233568: //Right Gate.
			case 233569: //Left Gate.
		        point = 2000;
		        despawnNpc(npc);
			    //The gate at the The Eternal Bastion has been destroyed.
			    sendMsgByRace(1402207,  Race.PC_ALL, 0);
		    break;
			case 233544: //Commander Pashid.
                point = 200000;
				deleteNpc(801956);
				RaceKilledCommander = mostPlayerDamage.getRace();
				ThreadPoolManager.getInstance().schedule(new Runnable() {
				    @Override
					public void run() {
						if (!ironWallWarfrontReward.isRewarded()) {
							Race winningRace = ironWallWarfrontReward.getWinnerRaceByScore();
							stopInstance(winningRace);
						}
					}
				}, 30000);
			break;
			
		   /**
			* ASMODIANS
			*/
			//Supply Base Beta.
			case 233518: //Elyos Supply Base Beta Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 1) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831875);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831885, 393.92688f, 271.82904f, 253.375f, (byte) 107, 0); //Asmodians Supply Base Beta Flag.
						sp(233498, 393.92688f, 271.82904f, 253.375f, (byte) 107, 0); //Asmodians Supply Base Beta Officer.
						sp(233497, 453.74094f, 264.6635f, 246.40028f, (byte) 36, 0); //Bridgewatch Post Scout.
						sp(233497, 438.01596f, 276.6177f, 246.375f, (byte) 118, 0); //Bridgewatch Post Scout.
						sp(233497, 428.56836f, 270.10815f, 246.45772f, (byte) 117, 0); //Bridgewatch Post Scout.
						sp(233497, 429.64246f, 260.12732f, 246.7947f, (byte) 15, 0); //Bridgewatch Post Scout.
						sp(233508, 452.7445f, 284.59048f, 245.72272f, (byte) 34, 0); //Asmodian Pathfinder.
						sp(233508, 447.10617f, 286.24292f, 245.49513f, (byte) 19, 0); //Asmodian Pathfinder.
						sp(233508, 410.85626f, 258.3087f, 252.87367f, (byte) 7, 0); //Asmodian Pathfinder.
						sp(233508, 410.04175f, 269.14273f, 253.05838f, (byte) 116, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Military Supply Base.
			case 233519: //Elyos Military Supply Base Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 2) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831876);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831886, 259.81546f, 740.3553f, 201.36577f, (byte) 23, 0); //Asmodians Military Supply Base Flag.
						sp(233499, 259.81546f, 740.3553f, 201.36577f, (byte) 23, 0); //Asmodians Military Supply Base Officer.
						sp(233497, 308.43594f, 701.484f, 209.6052f, (byte) 92, 0); //Bridgewatch Post Scout.
						sp(233497, 288.6053f, 735.61084f, 205.65054f, (byte) 97, 0); //Bridgewatch Post Scout.
						sp(233497, 297.2353f, 715.4191f, 206.38576f, (byte) 102, 0); //Bridgewatch Post Scout.
						sp(233497, 262.56683f, 760.37726f, 201.4094f, (byte) 106, 0); //Bridgewatch Post Scout.
						sp(233508, 258.03357f, 751.4065f, 201.31758f, (byte) 23, 0); //Asmodian Pathfinder.
						sp(233508, 267.22363f, 747.40356f, 201.35542f, (byte) 25, 0); //Asmodian Pathfinder.
						sp(233508, 312.15384f, 678.8196f, 212.68584f, (byte) 102, 0); //Asmodian Pathfinder.
						sp(233508, 318.22308f, 678.7845f, 213.0f, (byte) 86, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Supply Base Alpha.
			case 233520: //Elyos Supply Base Alpha Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 3) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831877);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831887, 574.5787f, 505.92386f, 217.75f, (byte) 8, 0); //Asmodians Supply Base Alpha Flag.
						sp(233500, 574.5787f, 505.92386f, 217.75f, (byte) 8, 0); //Asmodians Supply Base Alpha Officer.
						sp(233497, 574.4876f, 487.7919f, 217.75f, (byte) 6, 0); //Bridgewatch Post Scout.
						sp(233497, 605.71234f, 499.11005f, 217.85703f, (byte) 39, 0); //Bridgewatch Post Scout.
						sp(233497, 582.6051f, 496.10278f, 217.75f, (byte) 109, 0); //Bridgewatch Post Scout.
						sp(233497, 591.69977f, 498.41666f, 217.76877f, (byte) 99, 0); //Bridgewatch Post Scout.
						sp(233497, 587.89545f, 513.49084f, 217.75f, (byte) 8, 0); //Bridgewatch Post Scout.
						sp(233508, 606.38684f, 522.7315f, 217.92072f, (byte) 16, 0); //Asmodian Pathfinder.
						sp(233508, 610.4952f, 517.9505f, 218.02477f, (byte) 16, 0); //Asmodian Pathfinder.
						sp(233508, 602.3727f, 479.15283f, 218.125f, (byte) 101, 0); //Asmodian Pathfinder.
						sp(233508, 594.52313f, 476.07288f, 218.0f, (byte) 99, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Artillery Base.
			case 233521: //Elyos Artillery Base Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 4) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831878);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831888, 604.0888f, 878.7746f, 192.95142f, (byte) 88, 0); //Asmodians Artillery Base Flag.
						sp(233501, 604.0888f, 878.7746f, 192.95142f, (byte) 88, 0); //Asmodians Artillery Base Officer.
						sp(233497, 606.8558f, 854.07117f, 192.43098f, (byte) 83, 0); //Bridgewatch Post Scout.
						sp(233497, 598.023f, 854.6677f, 192.43138f, (byte) 95, 0); //Bridgewatch Post Scout.
						sp(233497, 595.8862f, 869.7282f, 193.1967f, (byte) 102, 0); //Bridgewatch Post Scout.
						sp(233508, 608.3503f, 870.7595f, 193.10478f, (byte) 87, 0); //Asmodian Pathfinder.
						sp(233508, 596.8866f, 834.11206f, 188.75f, (byte) 88, 0); //Asmodian Pathfinder.
						sp(233508, 605.67865f, 832.65125f, 188.68343f, (byte) 88, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Sentry Post Alpha.
			case 233522: //Elyos Sentry Post Alpha Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 5) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831879);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831889, 555.3127f, 416.20398f, 222.77223f, (byte) 19, 0); //Asmodians Sentry Post Alpha Flag.
						sp(831907, 567.2498f, 423.46344f, 222.75f, (byte) 54, 0); //Asmodians Sentry Post Alpha Teleport Statue.
						sp(233502, 555.3127f, 416.20398f, 222.77223f, (byte) 19, 0); //Asmodians Sentry Post Alpha Officer.
						sp(233497, 560.5613f, 435.62216f, 222.75f, (byte) 95, 0); //Bridgewatch Post Scout.
						sp(233497, 565.85754f, 416.52362f, 222.75f, (byte) 36, 0); //Bridgewatch Post Scout.
						sp(233508, 553.7597f, 429.06116f, 222.69131f, (byte) 6, 0); //Asmodian Pathfinder.
						sp(233508, 572.70245f, 433.73148f, 222.28357f, (byte) 18, 0); //Asmodian Pathfinder.
						sp(233508, 567.48376f, 438.24005f, 221.95679f, (byte) 18, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Sentry Post Beta.
			case 233523: //Elyos Sentry Post Beta Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 6) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831880);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831890, 819.66156f, 604.6976f, 239.70569f, (byte) 82, 0); //Asmodians Sentry Post Beta Flag.
						sp(831911, 803.916f, 598.6077f, 239.5659f, (byte) 80, 0); //Fortress Wall Bomber.
						sp(831912, 806.1366f, 597.3211f, 239.5659f, (byte) 81, 0); //Fortress Bomber.
						sp(831913, 808.4106f, 596.16705f, 239.5659f, (byte) 81, 0); //Fortress Blanket Bomber.
						sp(233503, 819.66156f, 604.6976f, 239.70569f, (byte) 82, 0); //Asmodians Sentry Post Beta Officer.
						sp(233497, 810.6133f, 577.9746f, 239.5f, (byte) 113, 0); //Bridgewatch Post Scout.
						sp(233497, 831.00385f, 597.151f, 239.5f, (byte) 82, 0); //Bridgewatch Post Scout.
						sp(233497, 803.56793f, 611.3292f, 239.5659f, (byte) 95, 0); //Bridgewatch Post Scout.
						sp(233497, 822.6201f, 586.7958f, 239.55705f, (byte) 90, 0); //Bridgewatch Post Scout.
						sp(233508, 806.08026f, 590.5061f, 239.5659f, (byte) 98, 0); //Asmodian Pathfinder.
						sp(233508, 821.4298f, 560.6091f, 239.2033f, (byte) 81, 0); //Asmodian Pathfinder.
						sp(233508, 827.238f, 557.0582f, 239.19095f, (byte) 80, 0); //Asmodian Pathfinder.
						sp(233508, 809.4041f, 539.61743f, 229.25f, (byte) 74, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Holy Grounds.
			case 233524: //Elyos Holy Grounds Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 7) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831881);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831891, 449.6276f, 448.86804f, 270.74738f, (byte) 78, 0); //Asmodians Holy Grounds Flag.
						sp(831909, 460.60193f, 455.62781f, 270.94815f, (byte) 0, 203); //Siege Base Bomber.
						sp(831910, 457.12491f, 459.35645f, 270.94815f, (byte) 0, 202); //Siege Base Blanket Bomber.
						sp(233504, 449.6276f, 448.86804f, 270.74738f, (byte) 78, 0); //Asmodians Holy Grounds Officer.
						sp(233497, 452.27325f, 385.31363f, 246.42839f, (byte) 0, 0); //Bridgewatch Post Scout.
						sp(233497, 421.9427f, 404.8145f, 258.16705f, (byte) 100, 0); //Bridgewatch Post Scout.
						sp(233497, 439.18732f, 433.28952f, 270.887f, (byte) 75, 0); //Bridgewatch Post Scout.
						sp(233497, 433.1931f, 439.81213f, 270.88953f, (byte) 75, 0); //Bridgewatch Post Scout.
						sp(233508, 420.13058f, 419.19116f, 262.8201f, (byte) 83, 0); //Asmodian Pathfinder.
						sp(233508, 426.71356f, 389.10214f, 254.76927f, (byte) 119, 0); //Asmodian Pathfinder.
						sp(233508, 431.6784f, 396.48215f, 254.67825f, (byte) 98, 0); //Asmodian Pathfinder.
						sp(233508, 478.8737f, 392.2531f, 239.33661f, (byte) 19, 0); //Asmodian Pathfinder.
						sp(233508, 474.2038f, 396.0856f, 239.33348f, (byte) 19, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Command Center.
			case 233525: //Elyos Command Center Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 8) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831882);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831892, 440.07004f, 768.60376f, 202.6145f, (byte) 118, 0); //Asmodians Command Center Flag.
						sp(831908, 433.28976f, 768.8911f, 203.42834f, (byte) 119, 0); //Asmodians Command Center Teleport Statue.
						sp(233505, 440.07004f, 768.60376f, 202.6145f, (byte) 118, 0); //Asmodians Command Center Officer.
						sp(233497, 454.5969f, 767.52106f, 202.30487f, (byte) 119, 0); //Bridgewatch Post Scout.
						sp(233497, 491.3722f, 765.2371f, 200.16635f, (byte) 119, 0); //Bridgewatch Post Scout.
						sp(233497, 450.8697f, 775.763f, 202.89624f, (byte) 82, 0); //Bridgewatch Post Scout.
						sp(233497, 449.76147f, 760.18384f, 202.88968f, (byte) 34, 0); //Bridgewatch Post Scout.
						sp(233508, 469.97382f, 762.4066f, 201.70393f, (byte) 4, 0); //Asmodian Pathfinder.
						sp(233508, 470.80334f, 770.6527f, 201.70044f, (byte) 114, 0); //Asmodian Pathfinder.
						sp(233508, 519.9691f, 760.84106f, 194.56223f, (byte) 0, 0); //Asmodian Pathfinder.
						sp(233508, 521.1051f, 767.2129f, 194.39792f, (byte) 1, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Headquarters Alpha.
			case 233526: //Elyos Headquarters Alpha Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 9) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831883);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831893, 356.20807f, 473.54718f, 236.18228f, (byte) 106, 0); //Asmodians Headquarters Alpha Flag.
						sp(233506, 356.20807f, 473.54718f, 236.18228f, (byte) 106, 0); //Asmodians Headquarters Alpha Officer.
						sp(702589, 422.98706f, 641.44116f, 214.52452f, (byte) 92, 0); //Beritran Chariot.
						sp(702589, 426.4476f, 617.95264f, 214.52452f, (byte) 32, 0); //Beritran Chariot.
						sp(233497, 344.62454f, 446.85638f, 234.20306f, (byte) 101, 0); //Bridgewatch Post Scout.
						sp(233497, 372.08167f, 455.98224f, 234.25f, (byte) 81, 0); //Bridgewatch Post Scout.
						sp(233497, 357.5201f, 451.77515f, 234.41061f, (byte) 91, 0); //Bridgewatch Post Scout.
						sp(233497, 374.35403f, 469.04745f, 235.10414f, (byte) 13, 0); //Bridgewatch Post Scout.
						sp(233508, 405.99002f, 488.50772f, 233.7788f, (byte) 12, 0); //Asmodian Pathfinder.
						sp(233508, 400.9795f, 495.9016f, 233.7788f, (byte) 12, 0); //Asmodian Pathfinder.
						sp(233508, 362.82736f, 427.99576f, 233.56364f, (byte) 87, 0); //Asmodian Pathfinder.
						sp(233508, 353.3115f, 428.95013f, 233.5629f, (byte) 87, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Headquarters Beta.
			case 233527: //Elyos Headquarters Beta Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 10) {
					if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831884);
						deleteNpc(233517);
						deleteNpc(233528);
						sp(831894, 772.203f, 762.5251f, 198.04073f, (byte) 69, 0); //Asmodians Headquarters Beta Flag.
						sp(233507, 772.203f, 762.5251f, 198.04073f, (byte) 69, 0); //Asmodians Headquarters Beta Officer.
						sp(233497, 755.43805f, 767.4794f, 195.54028f, (byte) 76, 0); //Bridgewatch Post Scout.
						sp(233497, 771.21936f, 743.6165f, 195.54028f, (byte) 76, 0); //Bridgewatch Post Scout.
						sp(233497, 770.72797f, 725.1369f, 194.5f, (byte) 67, 0); //Bridgewatch Post Scout.
						sp(233497, 741.4761f, 771.03613f, 194.625f, (byte) 76, 0); //Bridgewatch Post Scout.
						sp(233497, 741.4577f, 755.0922f, 195.30608f, (byte) 84, 0); //Bridgewatch Post Scout.
						sp(233508, 749.9243f, 740.33624f, 195.2935f, (byte) 76, 0); //Asmodian Pathfinder.
						sp(233508, 715.75085f, 738.7669f, 189.14056f, (byte) 70, 0); //Asmodian Pathfinder.
						sp(233508, 722.4398f, 726.6181f, 189.25f, (byte) 69, 0); //Asmodian Pathfinder.
						sp(233508, 715.70166f, 731.0347f, 189.31033f, (byte) 69, 0); //Asmodian Pathfinder.
						sp(233508, 745.6468f, 724.97f, 194.5f, (byte) 59, 0); //Asmodian Pathfinder.
						sp(233508, 728.9531f, 758.34247f, 194.5f, (byte) 83, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			
		   /**
			* ELYOS
			*/
			//Supply Base Beta.
			case 233498: //Asmodians Supply Base Beta Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 1) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831885);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831875, 393.92688f, 271.82904f, 253.375f, (byte) 107, 0); //Elyos Supply Base Beta Flag.
						sp(233518, 393.92688f, 271.82904f, 253.375f, (byte) 107, 0); //Elyos Supply Base Beta Officer.
						sp(233517, 453.74094f, 264.6635f, 246.40028f, (byte) 36, 0); //Elyos Garrison Scout.
						sp(233517, 438.01596f, 276.6177f, 246.375f, (byte) 118, 0); //Elyos Garrison Scout.
						sp(233517, 428.56836f, 270.10815f, 246.45772f, (byte) 117, 0); //Elyos Garrison Scout.
						sp(233517, 429.64246f, 260.12732f, 246.7947f, (byte) 15, 0); //Elyos Garrison Scout.
						sp(233528, 452.7445f, 284.59048f, 245.72272f, (byte) 34, 0); //Elyos Pathfinder.
						sp(233528, 447.10617f, 286.24292f, 245.49513f, (byte) 19, 0); //Elyos Pathfinder.
						sp(233528, 410.85626f, 258.3087f, 252.87367f, (byte) 7, 0); //Elyos Pathfinder.
						sp(233528, 410.04175f, 269.14273f, 253.05838f, (byte) 116, 0); //Elyos Pathfinder.
				    }
				}
			break;
			//Military Supply Base.
			case 233499: //Asmodians Military Supply Base Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 2) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831886);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831876, 259.81546f, 740.3553f, 201.36577f, (byte) 23, 0); //Elyos Military Supply Base Flag.
						sp(233519, 259.81546f, 740.3553f, 201.36577f, (byte) 23, 0); //Elyos Military Supply Base Officer.
						sp(233517, 308.43594f, 701.484f, 209.6052f, (byte) 92, 0); //Elyos Garrison Scout.
						sp(233517, 288.6053f, 735.61084f, 205.65054f, (byte) 97, 0); //Elyos Garrison Scout.
						sp(233517, 297.2353f, 715.4191f, 206.38576f, (byte) 102, 0); //Elyos Garrison Scout.
						sp(233517, 262.56683f, 760.37726f, 201.4094f, (byte) 106, 0); //Elyos Garrison Scout.
						sp(233528, 258.03357f, 751.4065f, 201.31758f, (byte) 23, 0); //Elyos Pathfinder.
						sp(233528, 267.22363f, 747.40356f, 201.35542f, (byte) 25, 0); //Elyos Pathfinder.
						sp(233528, 312.15384f, 678.8196f, 212.68584f, (byte) 102, 0); //Elyos Pathfinder.
						sp(233528, 318.22308f, 678.7845f, 213.0f, (byte) 86, 0); //Elyos Pathfinder.
				    }
				}
			break;
			//Supply Base Alpha.
			case 233500: //Asmodians Supply Base Alpha Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 3) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831887);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831877, 574.5787f, 505.92386f, 217.75f, (byte) 8, 0); //Elyos Supply Base Alpha Flag.
						sp(233520, 574.5787f, 505.92386f, 217.75f, (byte) 8, 0); //Elyos Supply Base Alpha Officer.
						sp(233517, 574.4876f, 487.7919f, 217.75f, (byte) 6, 0); //Elyos Garrison Scout.
						sp(233517, 605.71234f, 499.11005f, 217.85703f, (byte) 39, 0); //Elyos Garrison Scout.
						sp(233517, 582.6051f, 496.10278f, 217.75f, (byte) 109, 0); //Elyos Garrison Scout.
						sp(233517, 591.69977f, 498.41666f, 217.76877f, (byte) 99, 0); //Elyos Garrison Scout.
						sp(233517, 587.89545f, 513.49084f, 217.75f, (byte) 8, 0); //Elyos Garrison Scout.
						sp(233528, 606.38684f, 522.7315f, 217.92072f, (byte) 16, 0); //Elyos Pathfinder.
						sp(233528, 610.4952f, 517.9505f, 218.02477f, (byte) 16, 0); //Elyos Pathfinder.
						sp(233528, 602.3727f, 479.15283f, 218.125f, (byte) 101, 0); //Elyos Pathfinder.
						sp(233528, 594.52313f, 476.07288f, 218.0f, (byte) 99, 0); //Elyos Pathfinder.
				    }
				}
			break;
			//Artillery Base.
			case 233501: //Asmodians Artillery Base Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 4) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831888);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831878, 604.0888f, 878.7746f, 192.95142f, (byte) 88, 0); //Elyos Artillery Base Flag.
						sp(233521, 604.0888f, 878.7746f, 192.95142f, (byte) 88, 0); //Elyos Artillery Base Officer.
						sp(233517, 606.8558f, 854.07117f, 192.43098f, (byte) 83, 0); //Elyos Garrison Scout.
						sp(233517, 598.023f, 854.6677f, 192.43138f, (byte) 95, 0); //Elyos Garrison Scout.
						sp(233517, 595.8862f, 869.7282f, 193.1967f, (byte) 102, 0); //Elyos Garrison Scout.
						sp(233528, 608.3503f, 870.7595f, 193.10478f, (byte) 87, 0); //Elyos Pathfinder.
						sp(233528, 596.8866f, 834.11206f, 188.75f, (byte) 88, 0); //Elyos Pathfinder.
						sp(233528, 605.67865f, 832.65125f, 188.68343f, (byte) 88, 0); //Elyos Pathfinder.
				    }
				}
			break;
			//Sentry Post Alpha.
			case 233502: //Asmodians Sentry Post Alpha Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 5) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831889);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831879, 555.3127f, 416.20398f, 222.77223f, (byte) 19, 0); //Elyos Sentry Post Alpha Flag.
						sp(831905, 567.2498f, 423.46344f, 222.75f, (byte) 54, 0); //Elyos Sentry Post Alpha Teleport Statue.
						sp(233522, 555.3127f, 416.20398f, 222.77223f, (byte) 19, 0); //Elyos Sentry Post Alpha Officer.
						sp(233517, 560.5613f, 435.62216f, 222.75f, (byte) 95, 0); //Elyos Garrison Scout.
						sp(233517, 565.85754f, 416.52362f, 222.75f, (byte) 36, 0); //Elyos Garrison Scout.
						sp(233528, 553.7597f, 429.06116f, 222.69131f, (byte) 6, 0); //Elyos Pathfinder.
						sp(233528, 572.70245f, 433.73148f, 222.28357f, (byte) 18, 0); //Elyos Pathfinder.
						sp(233528, 567.48376f, 438.24005f, 221.95679f, (byte) 18, 0); //Elyos Pathfinder.
				    }
				}
			break;
			//Sentry Post Beta.
			case 233503: //Asmodians Sentry Post Beta Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 6) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831890);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831880, 819.66156f, 604.6976f, 239.70569f, (byte) 82, 0); //Elyos Sentry Post Beta Flag.
						sp(831916, 803.916f, 598.6077f, 239.5659f, (byte) 80, 0); //Fortress Wall Bomber.
						sp(831917, 806.1366f, 597.3211f, 239.5659f, (byte) 81, 0); //Fortress Bomber.
						sp(831918, 808.4106f, 596.16705f, 239.5659f, (byte) 81, 0); //Fortress Blanket Bomber.
						sp(233523, 819.66156f, 604.6976f, 239.70569f, (byte) 82, 0); //Elyos Sentry Post Beta Officer.
						sp(233517, 810.6133f, 577.9746f, 239.5f, (byte) 113, 0); //Elyos Garrison Scout.
						sp(233517, 831.00385f, 597.151f, 239.5f, (byte) 82, 0); //Elyos Garrison Scout.
						sp(233517, 803.56793f, 611.3292f, 239.5659f, (byte) 95, 0); //Elyos Garrison Scout.
						sp(233517, 822.6201f, 586.7958f, 239.55705f, (byte) 90, 0); //Elyos Garrison Scout.
						sp(233528, 806.08026f, 590.5061f, 239.5659f, (byte) 98, 0); //Elyos Pathfinder.
						sp(233528, 821.4298f, 560.6091f, 239.2033f, (byte) 81, 0); //Elyos Pathfinder.
						sp(233528, 827.238f, 557.0582f, 239.19095f, (byte) 80, 0); //Elyos Pathfinder.
						sp(233528, 809.4041f, 539.61743f, 229.25f, (byte) 74, 0); //Elyos Pathfinder.
				    }
				}
			break;
			//Holy Grounds.
			case 233504: //Asmodians Holy Grounds Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 7) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831891);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831881, 449.6276f, 448.86804f, 270.74738f, (byte) 78, 0); //Elyos Holy Grounds Flag.
						sp(831914, 460.60193f, 455.62781f, 270.94815f, (byte) 0, 203); //Siege Base Bomber.
						sp(831915, 457.12491f, 459.35645f, 270.94815f, (byte) 0, 202); //Siege Base Blanket Bomber.
						sp(233524, 449.6276f, 448.86804f, 270.74738f, (byte) 78, 0); //Elyos Holy Grounds Officer.
						sp(233517, 452.27325f, 385.31363f, 246.42839f, (byte) 0, 0); //Elyos Garrison Scout.
						sp(233517, 421.9427f, 404.8145f, 258.16705f, (byte) 100, 0); //Elyos Garrison Scout.
						sp(233517, 439.18732f, 433.28952f, 270.887f, (byte) 75, 0); //Elyos Garrison Scout.
						sp(233517, 433.1931f, 439.81213f, 270.88953f, (byte) 75, 0); //Elyos Garrison Scout.
						sp(233528, 420.13058f, 419.19116f, 262.8201f, (byte) 83, 0); //Elyos Pathfinder.
						sp(233528, 426.71356f, 389.10214f, 254.76927f, (byte) 119, 0); //Elyos Pathfinder.
						sp(233528, 431.6784f, 396.48215f, 254.67825f, (byte) 98, 0); //Elyos Pathfinder.
						sp(233528, 478.8737f, 392.2531f, 239.33661f, (byte) 19, 0); //Elyos Pathfinder.
						sp(233528, 474.2038f, 396.0856f, 239.33348f, (byte) 19, 0); //Elyos Pathfinder.
				    }
				}
			break;
			//Command Center.
			case 233505: //Asmodians Command Center Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 8) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831892);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831882, 440.07004f, 768.60376f, 202.6145f, (byte) 118, 0); //Elyos Command Center Flag.
						sp(831906, 433.28976f, 768.8911f, 203.42834f, (byte) 119, 0); //Elyos Command Center Teleport Statue.
						sp(233525, 440.07004f, 768.60376f, 202.6145f, (byte) 118, 0); //Elyos Command Center Officer.
						sp(233517, 454.5969f, 767.52106f, 202.30487f, (byte) 119, 0); //Elyos Garrison Scout.
						sp(233517, 491.3722f, 765.2371f, 200.16635f, (byte) 119, 0); //Elyos Garrison Scout.
						sp(233517, 450.8697f, 775.763f, 202.89624f, (byte) 82, 0); //Elyos Garrison Scout.
						sp(233517, 449.76147f, 760.18384f, 202.88968f, (byte) 34, 0); //Elyos Garrison Scout.
						sp(233528, 469.97382f, 762.4066f, 201.70393f, (byte) 4, 0); //Elyos Pathfinder.
						sp(233528, 470.80334f, 770.6527f, 201.70044f, (byte) 114, 0); //Elyos Pathfinder.
						sp(233528, 519.9691f, 760.84106f, 194.56223f, (byte) 0, 0); //Elyos Pathfinder.
						sp(233528, 521.1051f, 767.2129f, 194.39792f, (byte) 1, 0); //Elyos Pathfinder.
				    }
				}
			break;
			//Headquarters Alpha.
			case 233506: //Asmodians Headquarters Alpha Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 9) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831893);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831883, 356.20807f, 473.54718f, 236.18228f, (byte) 106, 0); //Elyos Headquarters Alpha Flag.
						sp(233526, 356.20807f, 473.54718f, 236.18228f, (byte) 106, 0); //Elyos Headquarters Alpha Officer.
						sp(701624, 422.98706f, 641.44116f, 214.52452f, (byte) 92, 0); //Beritran Chariot.
						sp(701624, 426.4476f, 617.95264f, 214.52452f, (byte) 32, 0); //Beritran Chariot.
						sp(233517, 344.62454f, 446.85638f, 234.20306f, (byte) 101, 0); //Elyos Garrison Scout.
						sp(233517, 372.08167f, 455.98224f, 234.25f, (byte) 81, 0); //Elyos Garrison Scout.
						sp(233517, 357.5201f, 451.77515f, 234.41061f, (byte) 91, 0); //Elyos Garrison Scout.
						sp(233517, 374.35403f, 469.04745f, 235.10414f, (byte) 13, 0); //Elyos Garrison Scout.
						sp(233528, 405.99002f, 488.50772f, 233.7788f, (byte) 12, 0); //Elyos Pathfinder.
						sp(233528, 400.9795f, 495.9016f, 233.7788f, (byte) 12, 0); //Elyos Pathfinder.
						sp(233528, 362.82736f, 427.99576f, 233.56364f, (byte) 87, 0); //Elyos Pathfinder.
						sp(233528, 353.3115f, 428.95013f, 233.5629f, (byte) 87, 0); //Elyos Pathfinder.
				    }
				}
			break;
			//Headquarters Beta.
			case 233507: //Asmodians Headquarters Beta Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 10) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831894);
						deleteNpc(233497);
						deleteNpc(233508);
						sp(831884, 772.203f, 762.5251f, 198.04073f, (byte) 69, 0); //Elyos Headquarters Beta Flag.
						sp(233527, 772.203f, 762.5251f, 198.04073f, (byte) 69, 0); //Elyos Headquarters Beta Officer.
						sp(233517, 755.43805f, 767.4794f, 195.54028f, (byte) 76, 0); //Elyos Garrison Scout.
						sp(233517, 771.21936f, 743.6165f, 195.54028f, (byte) 76, 0); //Elyos Garrison Scout.
						sp(233517, 770.72797f, 725.1369f, 194.5f, (byte) 67, 0); //Elyos Garrison Scout.
						sp(233517, 741.4761f, 771.03613f, 194.625f, (byte) 76, 0); //Elyos Garrison Scout.
						sp(233517, 741.4577f, 755.0922f, 195.30608f, (byte) 84, 0); //Elyos Garrison Scout.
						sp(233528, 749.9243f, 740.33624f, 195.2935f, (byte) 76, 0); //Elyos Pathfinder.
						sp(233528, 715.75085f, 738.7669f, 189.14056f, (byte) 70, 0); //Elyos Pathfinder.
						sp(233528, 722.4398f, 726.6181f, 189.25f, (byte) 69, 0); //Elyos Pathfinder.
						sp(233528, 715.70166f, 731.0347f, 189.31033f, (byte) 69, 0); //Elyos Pathfinder.
						sp(233528, 745.6468f, 724.97f, 194.5f, (byte) 59, 0); //Elyos Pathfinder.
						sp(233528, 728.9531f, 758.34247f, 194.5f, (byte) 83, 0); //Elyos Pathfinder.
				    }
				}
			break;
			
		   /**
			* BALAUR
			*/
			//Supply Base Beta.
			case 233550: //Pashid Supply Base Beta Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 1) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831895);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831875, 393.92688f, 271.82904f, 253.375f, (byte) 107, 0); //Elyos Supply Base Beta Flag.
						sp(233518, 393.92688f, 271.82904f, 253.375f, (byte) 107, 0); //Elyos Supply Base Beta Officer.
						sp(233517, 453.74094f, 264.6635f, 246.40028f, (byte) 36, 0); //Elyos Garrison Scout.
						sp(233517, 438.01596f, 276.6177f, 246.375f, (byte) 118, 0); //Elyos Garrison Scout.
						sp(233517, 428.56836f, 270.10815f, 246.45772f, (byte) 117, 0); //Elyos Garrison Scout.
						sp(233517, 429.64246f, 260.12732f, 246.7947f, (byte) 15, 0); //Elyos Garrison Scout.
						sp(233528, 452.7445f, 284.59048f, 245.72272f, (byte) 34, 0); //Elyos Pathfinder.
						sp(233528, 447.10617f, 286.24292f, 245.49513f, (byte) 19, 0); //Elyos Pathfinder.
						sp(233528, 410.85626f, 258.3087f, 252.87367f, (byte) 7, 0); //Elyos Pathfinder.
						sp(233528, 410.04175f, 269.14273f, 253.05838f, (byte) 116, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831895);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831885, 393.92688f, 271.82904f, 253.375f, (byte) 107, 0); //Asmodians Supply Base Beta Flag.
						sp(233498, 393.92688f, 271.82904f, 253.375f, (byte) 107, 0); //Asmodians Supply Base Beta Officer.
						sp(233497, 453.74094f, 264.6635f, 246.40028f, (byte) 36, 0); //Bridgewatch Post Scout.
						sp(233497, 438.01596f, 276.6177f, 246.375f, (byte) 118, 0); //Bridgewatch Post Scout.
						sp(233497, 428.56836f, 270.10815f, 246.45772f, (byte) 117, 0); //Bridgewatch Post Scout.
						sp(233497, 429.64246f, 260.12732f, 246.7947f, (byte) 15, 0); //Bridgewatch Post Scout.
						sp(233508, 452.7445f, 284.59048f, 245.72272f, (byte) 34, 0); //Asmodian Pathfinder.
						sp(233508, 447.10617f, 286.24292f, 245.49513f, (byte) 19, 0); //Asmodian Pathfinder.
						sp(233508, 410.85626f, 258.3087f, 252.87367f, (byte) 7, 0); //Asmodian Pathfinder.
						sp(233508, 410.04175f, 269.14273f, 253.05838f, (byte) 116, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Military Supply Base.
			case 233551: //Pashid Military Supply Base Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 2) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831896);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831876, 259.81546f, 740.3553f, 201.36577f, (byte) 23, 0); //Elyos Military Supply Base Flag.
						sp(233519, 259.81546f, 740.3553f, 201.36577f, (byte) 23, 0); //Elyos Military Supply Base Officer.
						sp(233517, 308.43594f, 701.484f, 209.6052f, (byte) 92, 0); //Elyos Garrison Scout.
						sp(233517, 288.6053f, 735.61084f, 205.65054f, (byte) 97, 0); //Elyos Garrison Scout.
						sp(233517, 297.2353f, 715.4191f, 206.38576f, (byte) 102, 0); //Elyos Garrison Scout.
						sp(233517, 262.56683f, 760.37726f, 201.4094f, (byte) 106, 0); //Elyos Garrison Scout.
						sp(233528, 258.03357f, 751.4065f, 201.31758f, (byte) 23, 0); //Elyos Pathfinder.
						sp(233528, 267.22363f, 747.40356f, 201.35542f, (byte) 25, 0); //Elyos Pathfinder.
						sp(233528, 312.15384f, 678.8196f, 212.68584f, (byte) 102, 0); //Elyos Pathfinder.
						sp(233528, 318.22308f, 678.7845f, 213.0f, (byte) 86, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831896);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831886, 259.81546f, 740.3553f, 201.36577f, (byte) 23, 0); //Asmodians Military Supply Base Flag.
						sp(233499, 259.81546f, 740.3553f, 201.36577f, (byte) 23, 0); //Asmodians Military Supply Base Officer.
						sp(233497, 308.43594f, 701.484f, 209.6052f, (byte) 92, 0); //Bridgewatch Post Scout.
						sp(233497, 288.6053f, 735.61084f, 205.65054f, (byte) 97, 0); //Bridgewatch Post Scout.
						sp(233497, 297.2353f, 715.4191f, 206.38576f, (byte) 102, 0); //Bridgewatch Post Scout.
						sp(233497, 262.56683f, 760.37726f, 201.4094f, (byte) 106, 0); //Bridgewatch Post Scout.
						sp(233508, 258.03357f, 751.4065f, 201.31758f, (byte) 23, 0); //Asmodian Pathfinder.
						sp(233508, 267.22363f, 747.40356f, 201.35542f, (byte) 25, 0); //Asmodian Pathfinder.
						sp(233508, 312.15384f, 678.8196f, 212.68584f, (byte) 102, 0); //Asmodian Pathfinder.
						sp(233508, 318.22308f, 678.7845f, 213.0f, (byte) 86, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Supply Base Alpha.
			case 233552: //Pashid Supply Base Alpha Officer Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 3) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831897);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831877, 574.5787f, 505.92386f, 217.75f, (byte) 8, 0); //Elyos Supply Base Alpha Flag.
						sp(233520, 574.5787f, 505.92386f, 217.75f, (byte) 8, 0); //Elyos Supply Base Alpha Officer.
						sp(233517, 574.4876f, 487.7919f, 217.75f, (byte) 6, 0); //Elyos Garrison Scout.
						sp(233517, 605.71234f, 499.11005f, 217.85703f, (byte) 39, 0); //Elyos Garrison Scout.
						sp(233517, 582.6051f, 496.10278f, 217.75f, (byte) 109, 0); //Elyos Garrison Scout.
						sp(233517, 591.69977f, 498.41666f, 217.76877f, (byte) 99, 0); //Elyos Garrison Scout.
						sp(233517, 587.89545f, 513.49084f, 217.75f, (byte) 8, 0); //Elyos Garrison Scout.
						sp(233528, 606.38684f, 522.7315f, 217.92072f, (byte) 16, 0); //Elyos Pathfinder.
						sp(233528, 610.4952f, 517.9505f, 218.02477f, (byte) 16, 0); //Elyos Pathfinder.
						sp(233528, 602.3727f, 479.15283f, 218.125f, (byte) 101, 0); //Elyos Pathfinder.
						sp(233528, 594.52313f, 476.07288f, 218.0f, (byte) 99, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831897);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831887, 574.5787f, 505.92386f, 217.75f, (byte) 8, 0); //Asmodians Supply Base Alpha Flag.
						sp(233500, 574.5787f, 505.92386f, 217.75f, (byte) 8, 0); //Asmodians Supply Base Alpha Officer.
						sp(233497, 574.4876f, 487.7919f, 217.75f, (byte) 6, 0); //Bridgewatch Post Scout.
						sp(233497, 605.71234f, 499.11005f, 217.85703f, (byte) 39, 0); //Bridgewatch Post Scout.
						sp(233497, 582.6051f, 496.10278f, 217.75f, (byte) 109, 0); //Bridgewatch Post Scout.
						sp(233497, 591.69977f, 498.41666f, 217.76877f, (byte) 99, 0); //Bridgewatch Post Scout.
						sp(233497, 587.89545f, 513.49084f, 217.75f, (byte) 8, 0); //Bridgewatch Post Scout.
						sp(233508, 606.38684f, 522.7315f, 217.92072f, (byte) 16, 0); //Asmodian Pathfinder.
						sp(233508, 610.4952f, 517.9505f, 218.02477f, (byte) 16, 0); //Asmodian Pathfinder.
						sp(233508, 602.3727f, 479.15283f, 218.125f, (byte) 101, 0); //Asmodian Pathfinder.
						sp(233508, 594.52313f, 476.07288f, 218.0f, (byte) 99, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Artillery Base.
			case 233553: //Pashid Artillery Base Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 4) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831898);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831878, 604.0888f, 878.7746f, 192.95142f, (byte) 88, 0); //Elyos Artillery Base Flag.
						sp(233521, 604.0888f, 878.7746f, 192.95142f, (byte) 88, 0); //Elyos Artillery Base Officer.
						sp(233517, 606.8558f, 854.07117f, 192.43098f, (byte) 83, 0); //Elyos Garrison Scout.
						sp(233517, 598.023f, 854.6677f, 192.43138f, (byte) 95, 0); //Elyos Garrison Scout.
						sp(233517, 595.8862f, 869.7282f, 193.1967f, (byte) 102, 0); //Elyos Garrison Scout.
						sp(233528, 608.3503f, 870.7595f, 193.10478f, (byte) 87, 0); //Elyos Pathfinder.
						sp(233528, 596.8866f, 834.11206f, 188.75f, (byte) 88, 0); //Elyos Pathfinder.
						sp(233528, 605.67865f, 832.65125f, 188.68343f, (byte) 88, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831898);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831888, 604.0888f, 878.7746f, 192.95142f, (byte) 88, 0); //Asmodians Artillery Base Flag.
						sp(233501, 604.0888f, 878.7746f, 192.95142f, (byte) 88, 0); //Asmodians Artillery Base Officer.
						sp(233497, 606.8558f, 854.07117f, 192.43098f, (byte) 83, 0); //Bridgewatch Post Scout.
						sp(233497, 598.023f, 854.6677f, 192.43138f, (byte) 95, 0); //Bridgewatch Post Scout.
						sp(233497, 595.8862f, 869.7282f, 193.1967f, (byte) 102, 0); //Bridgewatch Post Scout.
						sp(233508, 608.3503f, 870.7595f, 193.10478f, (byte) 87, 0); //Asmodian Pathfinder.
						sp(233508, 596.8866f, 834.11206f, 188.75f, (byte) 88, 0); //Asmodian Pathfinder.
						sp(233508, 605.67865f, 832.65125f, 188.68343f, (byte) 88, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Sentry Post Alpha.
			case 233554: //Pashid Sentry Post Alpha Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 5) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831899);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831879, 555.3127f, 416.20398f, 222.77223f, (byte) 19, 0); //Elyos Sentry Post Alpha Flag.
						sp(831905, 567.2498f, 423.46344f, 222.75f, (byte) 54, 0); //Elyos Sentry Post Alpha Teleport Statue.
						sp(233522, 555.3127f, 416.20398f, 222.77223f, (byte) 19, 0); //Elyos Sentry Post Alpha Officer.
						sp(233517, 560.5613f, 435.62216f, 222.75f, (byte) 95, 0); //Elyos Garrison Scout.
						sp(233517, 565.85754f, 416.52362f, 222.75f, (byte) 36, 0); //Elyos Garrison Scout.
						sp(233528, 553.7597f, 429.06116f, 222.69131f, (byte) 6, 0); //Elyos Pathfinder.
						sp(233528, 572.70245f, 433.73148f, 222.28357f, (byte) 18, 0); //Elyos Pathfinder.
						sp(233528, 567.48376f, 438.24005f, 221.95679f, (byte) 18, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831899);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831889, 555.3127f, 416.20398f, 222.77223f, (byte) 19, 0); //Asmodians Sentry Post Alpha Flag.
						sp(831907, 567.2498f, 423.46344f, 222.75f, (byte) 54, 0); //Asmodians Sentry Post Alpha Teleport Statue.
						sp(233502, 555.3127f, 416.20398f, 222.77223f, (byte) 19, 0); //Asmodians Sentry Post Alpha Officer.
						sp(233497, 560.5613f, 435.62216f, 222.75f, (byte) 95, 0); //Bridgewatch Post Scout.
						sp(233497, 565.85754f, 416.52362f, 222.75f, (byte) 36, 0); //Bridgewatch Post Scout.
						sp(233508, 553.7597f, 429.06116f, 222.69131f, (byte) 6, 0); //Asmodian Pathfinder.
						sp(233508, 572.70245f, 433.73148f, 222.28357f, (byte) 18, 0); //Asmodian Pathfinder.
						sp(233508, 567.48376f, 438.24005f, 221.95679f, (byte) 18, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Sentry Post Beta.
			case 233555: //Pashid Sentry Post Beta Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 6) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831900);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831880, 819.66156f, 604.6976f, 239.70569f, (byte) 82, 0); //Elyos Sentry Post Beta Flag.
						sp(831916, 803.916f, 598.6077f, 239.5659f, (byte) 80, 0); //Fortress Wall Bomber.
						sp(831917, 806.1366f, 597.3211f, 239.5659f, (byte) 81, 0); //Fortress Bomber.
						sp(831918, 808.4106f, 596.16705f, 239.5659f, (byte) 81, 0); //Fortress Blanket Bomber.
						sp(233523, 819.66156f, 604.6976f, 239.70569f, (byte) 82, 0); //Elyos Sentry Post Beta Officer.
						sp(233517, 810.6133f, 577.9746f, 239.5f, (byte) 113, 0); //Elyos Garrison Scout.
						sp(233517, 831.00385f, 597.151f, 239.5f, (byte) 82, 0); //Elyos Garrison Scout.
						sp(233517, 803.56793f, 611.3292f, 239.5659f, (byte) 95, 0); //Elyos Garrison Scout.
						sp(233517, 822.6201f, 586.7958f, 239.55705f, (byte) 90, 0); //Elyos Garrison Scout.
						sp(233528, 806.08026f, 590.5061f, 239.5659f, (byte) 98, 0); //Elyos Pathfinder.
						sp(233528, 821.4298f, 560.6091f, 239.2033f, (byte) 81, 0); //Elyos Pathfinder.
						sp(233528, 827.238f, 557.0582f, 239.19095f, (byte) 80, 0); //Elyos Pathfinder.
						sp(233528, 809.4041f, 539.61743f, 229.25f, (byte) 74, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831900);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831890, 819.66156f, 604.6976f, 239.70569f, (byte) 82, 0); //Asmodians Sentry Post Beta Flag.
						sp(831911, 803.916f, 598.6077f, 239.5659f, (byte) 80, 0); //Fortress Wall Bomber.
						sp(831912, 806.1366f, 597.3211f, 239.5659f, (byte) 81, 0); //Fortress Bomber.
						sp(831913, 808.4106f, 596.16705f, 239.5659f, (byte) 81, 0); //Fortress Blanket Bomber.
						sp(233503, 819.66156f, 604.6976f, 239.70569f, (byte) 82, 0); //Asmodians Sentry Post Beta Officer.
						sp(233497, 810.6133f, 577.9746f, 239.5f, (byte) 113, 0); //Bridgewatch Post Scout.
						sp(233497, 831.00385f, 597.151f, 239.5f, (byte) 82, 0); //Bridgewatch Post Scout.
						sp(233497, 803.56793f, 611.3292f, 239.5659f, (byte) 95, 0); //Bridgewatch Post Scout.
						sp(233497, 822.6201f, 586.7958f, 239.55705f, (byte) 90, 0); //Bridgewatch Post Scout.
						sp(233508, 806.08026f, 590.5061f, 239.5659f, (byte) 98, 0); //Asmodian Pathfinder.
						sp(233508, 821.4298f, 560.6091f, 239.2033f, (byte) 81, 0); //Asmodian Pathfinder.
						sp(233508, 827.238f, 557.0582f, 239.19095f, (byte) 80, 0); //Asmodian Pathfinder.
						sp(233508, 809.4041f, 539.61743f, 229.25f, (byte) 74, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Holy Grounds.
			case 233556: //Pashid Holy Grounds Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 7) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831901);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831881, 449.6276f, 448.86804f, 270.74738f, (byte) 78, 0); //Elyos Holy Grounds Flag.
						sp(831914, 460.60193f, 455.62781f, 270.94815f, (byte) 0, 203); //Siege Base Bomber.
						sp(831915, 457.12491f, 459.35645f, 270.94815f, (byte) 0, 202); //Siege Base Blanket Bomber.
						sp(233524, 449.6276f, 448.86804f, 270.74738f, (byte) 78, 0); //Elyos Holy Grounds Officer.
						sp(233517, 452.27325f, 385.31363f, 246.42839f, (byte) 0, 0); //Elyos Garrison Scout.
						sp(233517, 421.9427f, 404.8145f, 258.16705f, (byte) 100, 0); //Elyos Garrison Scout.
						sp(233517, 439.18732f, 433.28952f, 270.887f, (byte) 75, 0); //Elyos Garrison Scout.
						sp(233517, 433.1931f, 439.81213f, 270.88953f, (byte) 75, 0); //Elyos Garrison Scout.
						sp(233528, 420.13058f, 419.19116f, 262.8201f, (byte) 83, 0); //Elyos Pathfinder.
						sp(233528, 426.71356f, 389.10214f, 254.76927f, (byte) 119, 0); //Elyos Pathfinder.
						sp(233528, 431.6784f, 396.48215f, 254.67825f, (byte) 98, 0); //Elyos Pathfinder.
						sp(233528, 478.8737f, 392.2531f, 239.33661f, (byte) 19, 0); //Elyos Pathfinder.
						sp(233528, 474.2038f, 396.0856f, 239.33348f, (byte) 19, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831901);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831891, 449.6276f, 448.86804f, 270.74738f, (byte) 78, 0); //Asmodians Holy Grounds Flag.
						sp(831909, 460.60193f, 455.62781f, 270.94815f, (byte) 0, 203); //Siege Base Bomber.
						sp(831910, 457.12491f, 459.35645f, 270.94815f, (byte) 0, 202); //Siege Base Blanket Bomber.
						sp(233504, 449.6276f, 448.86804f, 270.74738f, (byte) 78, 0); //Asmodians Holy Grounds Officer.
						sp(233497, 452.27325f, 385.31363f, 246.42839f, (byte) 0, 0); //Bridgewatch Post Scout.
						sp(233497, 421.9427f, 404.8145f, 258.16705f, (byte) 100, 0); //Bridgewatch Post Scout.
						sp(233497, 439.18732f, 433.28952f, 270.887f, (byte) 75, 0); //Bridgewatch Post Scout.
						sp(233497, 433.1931f, 439.81213f, 270.88953f, (byte) 75, 0); //Bridgewatch Post Scout.
						sp(233508, 420.13058f, 419.19116f, 262.8201f, (byte) 83, 0); //Asmodian Pathfinder.
						sp(233508, 426.71356f, 389.10214f, 254.76927f, (byte) 119, 0); //Asmodian Pathfinder.
						sp(233508, 431.6784f, 396.48215f, 254.67825f, (byte) 98, 0); //Asmodian Pathfinder.
						sp(233508, 478.8737f, 392.2531f, 239.33661f, (byte) 19, 0); //Asmodian Pathfinder.
						sp(233508, 474.2038f, 396.0856f, 239.33348f, (byte) 19, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Command Center.
			case 233557: //Pashid Command Center Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 8) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831902);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831882, 440.07004f, 768.60376f, 202.6145f, (byte) 118, 0); //Elyos Command Center Flag.
						sp(831906, 433.28976f, 768.8911f, 203.42834f, (byte) 119, 0); //Elyos Command Center Teleport Statue.
						sp(233525, 440.07004f, 768.60376f, 202.6145f, (byte) 118, 0); //Elyos Command Center Officer.
						sp(233517, 454.5969f, 767.52106f, 202.30487f, (byte) 119, 0); //Elyos Garrison Scout.
						sp(233517, 491.3722f, 765.2371f, 200.16635f, (byte) 119, 0); //Elyos Garrison Scout.
						sp(233517, 450.8697f, 775.763f, 202.89624f, (byte) 82, 0); //Elyos Garrison Scout.
						sp(233517, 449.76147f, 760.18384f, 202.88968f, (byte) 34, 0); //Elyos Garrison Scout.
						sp(233528, 469.97382f, 762.4066f, 201.70393f, (byte) 4, 0); //Elyos Pathfinder.
						sp(233528, 470.80334f, 770.6527f, 201.70044f, (byte) 114, 0); //Elyos Pathfinder.
						sp(233528, 519.9691f, 760.84106f, 194.56223f, (byte) 0, 0); //Elyos Pathfinder.
						sp(233528, 521.1051f, 767.2129f, 194.39792f, (byte) 1, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831902);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831892, 440.07004f, 768.60376f, 202.6145f, (byte) 118, 0); //Asmodians Command Center Flag.
						sp(831908, 433.28976f, 768.8911f, 203.42834f, (byte) 119, 0); //Asmodians Command Center Teleport Statue.
						sp(233505, 440.07004f, 768.60376f, 202.6145f, (byte) 118, 0); //Asmodians Command Center Officer.
						sp(233497, 454.5969f, 767.52106f, 202.30487f, (byte) 119, 0); //Bridgewatch Post Scout.
						sp(233497, 491.3722f, 765.2371f, 200.16635f, (byte) 119, 0); //Bridgewatch Post Scout.
						sp(233497, 450.8697f, 775.763f, 202.89624f, (byte) 82, 0); //Bridgewatch Post Scout.
						sp(233497, 449.76147f, 760.18384f, 202.88968f, (byte) 34, 0); //Bridgewatch Post Scout.
						sp(233508, 469.97382f, 762.4066f, 201.70393f, (byte) 4, 0); //Asmodian Pathfinder.
						sp(233508, 470.80334f, 770.6527f, 201.70044f, (byte) 114, 0); //Asmodian Pathfinder.
						sp(233508, 519.9691f, 760.84106f, 194.56223f, (byte) 0, 0); //Asmodian Pathfinder.
						sp(233508, 521.1051f, 767.2129f, 194.39792f, (byte) 1, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Headquarters Alpha.
			case 233558: //Pashid Headquarters Alpha Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 9) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831903);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831883, 356.20807f, 473.54718f, 236.18228f, (byte) 106, 0); //Elyos Headquarters Alpha Flag.
						sp(233526, 356.20807f, 473.54718f, 236.18228f, (byte) 106, 0); //Elyos Headquarters Alpha Officer.
						sp(701624, 422.98706f, 641.44116f, 214.52452f, (byte) 92, 0); //Beritran Chariot.
						sp(701624, 426.4476f, 617.95264f, 214.52452f, (byte) 32, 0); //Beritran Chariot.
						sp(233517, 344.62454f, 446.85638f, 234.20306f, (byte) 101, 0); //Elyos Garrison Scout.
						sp(233517, 372.08167f, 455.98224f, 234.25f, (byte) 81, 0); //Elyos Garrison Scout.
						sp(233517, 357.5201f, 451.77515f, 234.41061f, (byte) 91, 0); //Elyos Garrison Scout.
						sp(233517, 374.35403f, 469.04745f, 235.10414f, (byte) 13, 0); //Elyos Garrison Scout.
						sp(233528, 405.99002f, 488.50772f, 233.7788f, (byte) 12, 0); //Elyos Pathfinder.
						sp(233528, 400.9795f, 495.9016f, 233.7788f, (byte) 12, 0); //Elyos Pathfinder.
						sp(233528, 362.82736f, 427.99576f, 233.56364f, (byte) 87, 0); //Elyos Pathfinder.
						sp(233528, 353.3115f, 428.95013f, 233.5629f, (byte) 87, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831903);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						sp(831893, 356.20807f, 473.54718f, 236.18228f, (byte) 106, 0); //Asmodians Headquarters Alpha Flag.
						sp(233506, 356.20807f, 473.54718f, 236.18228f, (byte) 106, 0); //Asmodians Headquarters Alpha Officer.
						sp(702589, 422.98706f, 641.44116f, 214.52452f, (byte) 92, 0); //Beritran Chariot.
						sp(702589, 426.4476f, 617.95264f, 214.52452f, (byte) 32, 0); //Beritran Chariot.
						sp(233497, 344.62454f, 446.85638f, 234.20306f, (byte) 101, 0); //Bridgewatch Post Scout.
						sp(233497, 372.08167f, 455.98224f, 234.25f, (byte) 81, 0); //Bridgewatch Post Scout.
						sp(233497, 357.5201f, 451.77515f, 234.41061f, (byte) 91, 0); //Bridgewatch Post Scout.
						sp(233497, 374.35403f, 469.04745f, 235.10414f, (byte) 13, 0); //Bridgewatch Post Scout.
						sp(233508, 405.99002f, 488.50772f, 233.7788f, (byte) 12, 0); //Asmodian Pathfinder.
						sp(233508, 400.9795f, 495.9016f, 233.7788f, (byte) 12, 0); //Asmodian Pathfinder.
						sp(233508, 362.82736f, 427.99576f, 233.56364f, (byte) 87, 0); //Asmodian Pathfinder.
						sp(233508, 353.3115f, 428.95013f, 233.5629f, (byte) 87, 0); //Asmodian Pathfinder.
				    }
				}
			break;
			//Headquarters Beta.
			case 233559: //Pashid Headquarters Beta Officer.
				point = 400;
				despawnNpc(npc);
				if (ironWallBase == 10) {
					if (race.equals(Race.ELYOS)) {
						deleteNpc(831904);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						deleteNpc(233561);
						sp(831884, 772.203f, 762.5251f, 198.04073f, (byte) 69, 0); //Elyos Headquarters Beta Flag.
						sp(233527, 772.203f, 762.5251f, 198.04073f, (byte) 69, 0); //Elyos Headquarters Beta Officer.
						sp(233517, 755.43805f, 767.4794f, 195.54028f, (byte) 76, 0); //Elyos Garrison Scout.
						sp(233517, 771.21936f, 743.6165f, 195.54028f, (byte) 76, 0); //Elyos Garrison Scout.
						sp(233517, 770.72797f, 725.1369f, 194.5f, (byte) 67, 0); //Elyos Garrison Scout.
						sp(233517, 741.4761f, 771.03613f, 194.625f, (byte) 76, 0); //Elyos Garrison Scout.
						sp(233517, 741.4577f, 755.0922f, 195.30608f, (byte) 84, 0); //Elyos Garrison Scout.
						sp(233528, 749.9243f, 740.33624f, 195.2935f, (byte) 76, 0); //Elyos Pathfinder.
						sp(233528, 715.75085f, 738.7669f, 189.14056f, (byte) 70, 0); //Elyos Pathfinder.
						sp(233528, 722.4398f, 726.6181f, 189.25f, (byte) 69, 0); //Elyos Pathfinder.
						sp(233528, 715.70166f, 731.0347f, 189.31033f, (byte) 69, 0); //Elyos Pathfinder.
						sp(233528, 745.6468f, 724.97f, 194.5f, (byte) 59, 0); //Elyos Pathfinder.
						sp(233528, 728.9531f, 758.34247f, 194.5f, (byte) 83, 0); //Elyos Pathfinder.
				    } else if (race.equals(Race.ASMODIANS)) {
						deleteNpc(831904);
						deleteNpc(233543);
						deleteNpc(233547);
						deleteNpc(233548);
						deleteNpc(233549);
						deleteNpc(233561);
						sp(831894, 772.203f, 762.5251f, 198.04073f, (byte) 69, 0); //Asmodians Headquarters Beta Flag.
						sp(233507, 772.203f, 762.5251f, 198.04073f, (byte) 69, 0); //Asmodians Headquarters Beta Officer.
						sp(233497, 755.43805f, 767.4794f, 195.54028f, (byte) 76, 0); //Bridgewatch Post Scout.
						sp(233497, 771.21936f, 743.6165f, 195.54028f, (byte) 76, 0); //Bridgewatch Post Scout.
						sp(233497, 770.72797f, 725.1369f, 194.5f, (byte) 67, 0); //Bridgewatch Post Scout.
						sp(233497, 741.4761f, 771.03613f, 194.625f, (byte) 76, 0); //Bridgewatch Post Scout.
						sp(233497, 741.4577f, 755.0922f, 195.30608f, (byte) 84, 0); //Bridgewatch Post Scout.
						sp(233508, 749.9243f, 740.33624f, 195.2935f, (byte) 76, 0); //Asmodian Pathfinder.
						sp(233508, 715.75085f, 738.7669f, 189.14056f, (byte) 70, 0); //Asmodian Pathfinder.
						sp(233508, 722.4398f, 726.6181f, 189.25f, (byte) 69, 0); //Asmodian Pathfinder.
						sp(233508, 715.70166f, 731.0347f, 189.31033f, (byte) 69, 0); //Asmodian Pathfinder.
						sp(233508, 745.6468f, 724.97f, 194.5f, (byte) 59, 0); //Asmodian Pathfinder.
						sp(233508, 728.9531f, 758.34247f, 194.5f, (byte) 83, 0); //Asmodian Pathfinder.
				    }
				}
			break;
        }
        updateScore(mostPlayerDamage, npc, point, false);
    }
	
    @Override
    public void handleUseItemFinish(Player player, Npc npc) {
		switch (npc.getNpcId()) {
			case 831909: //Siege Base Bomber A.
			case 831910: //Siege Base Blanket Bomber A.
			case 831914: //Siege Base Bomber E.
			case 831915: //Siege Base Blanket Bomber E.
                despawnNpc(npc);
				//Bombardment has been activated on the siege base.\nBombing will begin soon.
				sendMsgByRace(1402109,  Race.PC_ALL, 0);
				sp(855240, 440.48346f, 648.9064f, 213.875f, (byte) 82, 5000);
                sp(855240, 435.1033f, 637.4064f, 214.52452f, (byte) 81, 5500);
                sp(855240, 426.12326f, 628.37146f, 214.52452f, (byte) 62, 6000);
                sp(855240, 410.35355f, 626.63544f, 214.52452f, (byte) 62, 6500);
                sp(855240, 394.76648f, 640.9771f, 214.52452f, (byte) 44, 7000);
                sp(855240, 398.99435f, 611.30725f, 214.52452f, (byte) 83, 7500);
            break;
			case 831911: //Fortress Wall Bomber A.
			case 831912: //Fortress Bomber A.
			case 831913: //Fortress Blanket Bomber A.
			case 831916: //Fortress Wall Bomber E.
			case 831917: //Fortress Bomber E.
			case 831918: //Fortress Blanket Bomber E.
                despawnNpc(npc);
				//Bombardment has been activated on the siege base.\nBombing will begin soon.
				sendMsgByRace(1402110,  Race.PC_ALL, 0);
				sp(855240, 612.48193f, 246.57852f, 227.24548f, (byte) 33, 5000);
                sp(855240, 607.7852f, 273.68695f, 226.78299f, (byte) 33, 5500);
                sp(855240, 609.47485f, 295.29547f, 226.25f, (byte) 28, 6000);
                sp(855240, 611.2592f, 316.33997f, 226.25f, (byte) 22, 6500);
                sp(855240, 619.06146f, 338.56107f, 225.94135f, (byte) 22, 7000);
                sp(855240, 623.8632f, 352.7094f, 225.85753f, (byte) 14, 7500);
                sp(855240, 637.295f, 366.08438f, 228.58621f, (byte) 15, 8000);
                sp(855240, 649.25397f, 381.53574f, 228.625f, (byte) 14, 8500);
                sp(855240, 638.361f, 393.87704f, 226.625f, (byte) 44, 9000);
                sp(855240, 624.7944f, 409.59f, 226.625f, (byte) 44, 9500);
                sp(855240, 619.216f, 426.68207f, 226.61574f, (byte) 30, 10000);
                sp(855240, 640.5313f, 435.39978f, 226.62898f, (byte) 6, 10500);
                sp(855240, 653.0702f, 420.92535f, 226.99039f, (byte) 103, 11000);
                sp(855240, 667.27106f, 404.47855f, 228.23833f, (byte) 103, 11500);
                sp(855240, 681.4431f, 404.91577f, 229.27058f, (byte) 7, 12000);
                sp(855240, 699.13165f, 412.75574f, 230.8985f, (byte) 6, 12500);
                sp(855240, 713.01105f, 416.59003f, 231.0f, (byte) 3, 13000);
                sp(855240, 730.56836f, 415.3179f, 230.96448f, (byte) 118, 13500);
            break;
        }
    }
	
	private void removeItems(Player player) {
		Storage storage = player.getInventory();
		storage.decreaseByItemId(185000137, storage.getItemCountByItemId(185000137)); //Aetheric Power Crystal.
		storage.decreaseByItemId(182006996, storage.getItemCountByItemId(182006996)); //Case Shot.
		storage.decreaseByItemId(182006997, storage.getItemCountByItemId(182006997)); //Armor-Piercing Shot.
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
        ironWallWarfrontReward.clear();
        isInstanceDestroyed = true;
        stopInstanceTask();
        doors.clear();
    }
	
    protected void openFirstDoors() {
        openDoor(2);
		openDoor(17);
		openDoor(26);
		openDoor(35);
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
        ironWallTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        ironWallTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
        ironWallTask.add(ThreadPoolManager.getInstance().schedule(new Runnable() {
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
	
    private void stopInstanceTask() {
        for (FastList.Node<Future<?>> n = ironWallTask.head(), end = ironWallTask.tail(); (n = n.getNext()) != end; ) {
            if (n.getValue() != null) {
                n.getValue().cancel(true);
            }
        }
    }
	
    @Override
    public InstanceReward<?> getInstanceReward() {
        return ironWallWarfrontReward;
    }
	
    @Override
    public void onExitInstance(Player player) {
        TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
    }
	
    @Override
    public void onLeaveInstance(Player player) {
		//"Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
		IronWallWarfrontPlayerReward playerReward = ironWallWarfrontReward.getPlayerReward(player.getObjectId());
		playerReward.endBoostMoraleEffect(player);
		removeItems(player);
    }
	
    @Override
    public void onPlayerLogin(Player player) {
        ironWallWarfrontReward.sendPacket(10, player.getObjectId());
    }
}