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
package com.aionemu.gameserver.model.instance.instancereward;

import static ch.lambdaj.Lambda.maxFrom;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.mutable.MutableInt;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.instance.playerreward.LandMarkPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author Rinzler (Encom)
/****/

public class LandMarkReward extends InstanceReward<LandMarkPlayerReward>
{
    private int capPoints;
    private MutableInt asmodiansPoints = new MutableInt(0);
    private MutableInt elyosPoins = new MutableInt(0);
    private MutableInt asmodiansPvpKills = new MutableInt(0);
    private MutableInt elyosPvpKills = new MutableInt(0);
    private Race race;
    private Point3D asmodiansStartPosition;
    private Point3D elyosStartPosition;
    protected WorldMapInstance instance;
    private long instanceTime;
    private int bonusTime;
	private final byte buffId;
	
    public LandMarkReward(Integer mapId, int instanceId, WorldMapInstance instance) {
        super(mapId, instanceId);
        this.instance = instance;
        capPoints = 999999;
        bonusTime = 12000;
		buffId = 15;
        setStartPositions();
    }
	
	public int AbyssReward(boolean isWin, boolean isTimeUp) {
    	int TimeUp = 1993;
    	int Win = 3163;
    	int Loss = 1031;
    	if (isTimeUp) {
    		return isWin ? (Win + TimeUp) : (Loss + TimeUp);
    	} else {
    		return isWin ? Win : Loss;
    	}
    }
	public int GloryReward(boolean isWin, boolean isTimeUp) {
    	int TimeUp = 50;
    	int Win = 150;
    	int Loss = 30;
    	if (isTimeUp) {
    		return isWin ? (Win + TimeUp) : (Loss + TimeUp);
    	} else {
    		return isWin ? Win : Loss;
    	}
    }
	public int ExpReward(boolean isWin, boolean isTimeUp) {
    	int TimeUp = 2000;
    	int Win = 9000;
    	int Loss = 1000;
    	if (isTimeUp) {
    		return isWin ? (Win + TimeUp) : (Loss + TimeUp);
    	} else {
    		return isWin ? Win : Loss;
    	}
    }
	
    public List<LandMarkPlayerReward> sortPoints() {
        return sort(getInstanceRewards(), on(PvPArenaPlayerReward.class).getScorePoints(), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 != null ? o2.compareTo(o1) : -o1.compareTo(o2);
            }
        });
    }
	
    private void setStartPositions() {
        Point3D a = new Point3D(263.45642f, 166.1216f, 79.430855f); //Blood War Room.
        Point3D e = new Point3D(266.275f, 351.1437f, 79.44365f); //Steel War Room.
        asmodiansStartPosition = a;
        elyosStartPosition = e;
    }
	
    public void portToPosition(Player player) {
        if (player.getRace() == Race.ASMODIANS) {
            TeleportService2.teleportTo(player, mapId, instanceId, asmodiansStartPosition.getX(), asmodiansStartPosition.getY(), asmodiansStartPosition.getZ());
        } else {
            TeleportService2.teleportTo(player, mapId, instanceId, elyosStartPosition.getX(), elyosStartPosition.getY(), elyosStartPosition.getZ());
        }
    }
	
    public MutableInt getPointsByRace(Race race) {
        return (race == Race.ELYOS) ? elyosPoins : (race == Race.ASMODIANS) ? asmodiansPoints : null;
    }
	
    public void addPointsByRace(Race race, int points) {
        MutableInt racePoints = getPointsByRace(race);
        racePoints.add(points);
        if (racePoints.intValue() < 0) {
            racePoints.setValue(0);
        }
    }
	
    public MutableInt getPvpKillsByRace(Race race) {
        return (race == Race.ELYOS) ? elyosPvpKills : (race == Race.ASMODIANS) ? asmodiansPvpKills : null;
    }
	
    public void addPvpKillsByRace(Race race, int points) {
        MutableInt racePoints = getPvpKillsByRace(race);
        racePoints.add(points);
        if (racePoints.intValue() < 0) {
            racePoints.setValue(0);
        }
    }
	
    public void setWinnerRace(Race race) {
        this.race = race;
    }
	
    public Race getWinnerRace() {
        return race;
    }
	
    public Race getWinnerRaceByScore() {
        return asmodiansPoints.compareTo(elyosPoins) > 0 ? Race.ASMODIANS : Race.ELYOS;
    }
	
    @Override
    public void clear() {
        super.clear();
    }
	
    public void regPlayerReward(Player player) {
        if (!containPlayer(player.getObjectId())) {
            addPlayerReward(new LandMarkPlayerReward(player.getObjectId(), bonusTime, buffId, player.getRace()));
        }
    }
	
    @Override
    public void addPlayerReward(LandMarkPlayerReward reward) {
        super.addPlayerReward(reward);
    }
	
    @Override
    public LandMarkPlayerReward getPlayerReward(Integer object) {
        return (LandMarkPlayerReward) super.getPlayerReward(object);
    }
	
    public void sendPacket(final int type, final Integer object) {
        instance.doOnAllPlayers(new Visitor<Player>() {
            @Override
            public void visit(Player player) {
                PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(type, getTime(), getInstanceReward(), object));
            }
        });
    }
	
    public int getTime() {
        long result = System.currentTimeMillis() - instanceTime;
        if (result < 90000) {
            return (int) (90000 - result);
        } else if (result < 1200000) { //20-Mins
            return (int) (1200000 - (result - 90000));
        }
        return 0;
    }
	
	public byte getBuffId() {
		return buffId;
	}
	
    public void setInstanceStartTime() {
        this.instanceTime = System.currentTimeMillis();
    }
	
    public int getCapPoints() {
        return capPoints;
    }
	
    public boolean hasCapPoints() {
        return maxFrom(getInstanceRewards()).getPoints() >= capPoints;
    }
}