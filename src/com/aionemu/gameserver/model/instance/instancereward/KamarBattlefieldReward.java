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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.instance.playerreward.KamarBattlefieldPlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.apache.commons.lang.mutable.MutableInt;

import java.util.Comparator;
import java.util.List;

import static ch.lambdaj.Lambda.*;

/****/
/** Author Rinzler (Encom)
/****/

public class KamarBattlefieldReward extends InstanceReward<KamarBattlefieldPlayerReward>
{
    private int capPoints;
    private MutableInt asmodiansPoints = new MutableInt(3800);
    private MutableInt elyosPoins = new MutableInt(3800);
    private MutableInt asmodiansPvpKills = new MutableInt(0);
    private MutableInt elyosPvpKills = new MutableInt(0);
    private Race race;
    private Point3D asmodiansStartPosition;
    private Point3D elyosStartPosition;
    protected WorldMapInstance instance;
    private long instanceTime;
    private int bonusTime;
	private final byte buffId;
	
    public KamarBattlefieldReward(Integer mapId, int instanceId, WorldMapInstance instance) {
        super(mapId, instanceId);
        this.instance = instance;
        capPoints = 30000;
        bonusTime = 12000;
		buffId = 10;
        setStartPositions();
    }
	
	public int AbyssReward(boolean isWin, boolean isVargaKilled) {
    	int VargaKilled = 1993;
    	int Win = 3163;
    	int Loss = 1031;
    	if (isVargaKilled) {
    		return isWin ? (Win + VargaKilled) : (Loss + VargaKilled);
    	} else {
    		return isWin ? Win : Loss;
    	}
    }
	public int GloryReward(boolean isWin, boolean isVargaKilled) {
    	int VargaKilled = 50;
    	int Win = 150;
    	int Loss = 30;
    	if (isVargaKilled) {
    		return isWin ? (Win + VargaKilled) : (Loss + VargaKilled);
    	} else {
    		return isWin ? Win : Loss;
    	}
    }
	public int ExpReward(boolean isWin, boolean isVargaKilled) {
    	int VargaKilled = 20000;
    	int Win = 10000;
    	int Loss = 5000;
    	if (isVargaKilled) {
    		return isWin ? (Win + VargaKilled) : (Loss + VargaKilled);
    	} else {
    		return isWin ? Win : Loss;
    	}
    }
	
    public List<KamarBattlefieldPlayerReward> sortPoints() {
        return sort(getInstanceRewards(), on(PvPArenaPlayerReward.class).getScorePoints(), new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 != null ? o2.compareTo(o1) : -o1.compareTo(o2);
            }
        });
    }
	
    private void setStartPositions() {
        Point3D a = new Point3D(1535.671f, 1573.9156f, 612.4217f); //Habrok Boarding Site.
        Point3D b = new Point3D(1205.2655f, 1350.9125f, 612.91205f); //Griffoen Boarding Site.
        asmodiansStartPosition = a;
        elyosStartPosition = b;
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
            addPlayerReward(new KamarBattlefieldPlayerReward(player.getObjectId(), bonusTime, buffId, player.getRace()));
        }
    }
	
    @Override
    public void addPlayerReward(KamarBattlefieldPlayerReward reward) {
        super.addPlayerReward(reward);
    }
	
    @Override
    public KamarBattlefieldPlayerReward getPlayerReward(Integer object) {
        return (KamarBattlefieldPlayerReward) super.getPlayerReward(object);
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
        } else if (result < 1800000) { //30-Mins
            return (int) (1800000 - (result - 90000));
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