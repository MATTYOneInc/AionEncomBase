/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.ranking.Arena6V6Ranking;
import com.aionemu.gameserver.model.gameobjects.player.ranking.ArenaOfTenacityRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.GoldArenaRank;
import com.aionemu.gameserver.model.gameobjects.player.ranking.TowerOfChallengeRank;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Created by Wnkrz on 24/07/2017.
 */
public class SM_MY_HISTORY extends AionServerPacket {

    private int tableId;
    private GoldArenaRank gold;
    private TowerOfChallengeRank tower;
    private Arena6V6Ranking arena6v6;
    private ArenaOfTenacityRank arenaOfTenacity;
	
    public SM_MY_HISTORY(int tableId, GoldArenaRank ranking){
        this.tableId = tableId;
        this.gold = ranking;
    }
	
    public SM_MY_HISTORY(int tableId, TowerOfChallengeRank ranking){
        this.tableId = tableId;
        this.tower = ranking;
    }
	
    public SM_MY_HISTORY(int tableId, Arena6V6Ranking ranking){
        this.tableId = tableId;
        this.arena6v6 = ranking;
    }
	
    public SM_MY_HISTORY(int tableId, ArenaOfTenacityRank ranking){
        this.tableId = tableId;
        this.arenaOfTenacity = ranking;
    }
	
    protected void writeImpl(AionConnection paramAionConnection){
        writeD(tableId);
        switch (tableId){
            case 1: //Hall Of Tenacity
                writeD(gold.getRank()); //actual Rank
                writeD(gold.getPoints()); //current Points
                writeD(0);
                writeD(0);
                writeD(gold.getBestRank());//lasted rank
                writeD(gold.getLastPoints()); //last Points
                writeD(gold.getLowPoints()); //low points
                writeD(gold.getHighPoints()); //hight points
			    return;
            case 541: //Arena Of Discipline
                writeD(arenaOfTenacity.getRank()); //actual Rank
                writeD(arenaOfTenacity.getPoints()); //current Points
                writeD(1);
                writeD(arenaOfTenacity.getPossitionMatch()); //possition match
                writeD(arenaOfTenacity.getBestRank());//lasted rank
                writeD(arenaOfTenacity.getLastPoints()); //last Points
                writeD(arenaOfTenacity.getLowPoints()); //low points
                writeD(arenaOfTenacity.getHighPoints()); //hight points
                return;
            case 2: //Crucible Spire.
                writeD(tower.getRank()); //actual Rank
                writeD(tower.getCurrentTime() ); //actual Time
                writeD(0);
                writeD(0);
                writeD(tower.getBestRank());//lasted rank
                writeD(tower.getLastTime() ); //last time
                writeD(tower.getLowRank() ); //low rank
                writeD(tower.getBestTime() ); //best time
                return;
            case 3: //Grand Arena Training Camp
                writeD(arena6v6.getRank()); //actual Rank
                writeD(arena6v6.getPoints()); //current Points
                writeD(1);
                writeD(arena6v6.getPossitionMatch()); //possition match
                writeD(arena6v6.getBestRank());//lasted rank
                writeD(arena6v6.getLastPoints()); //last Points
                writeD(arena6v6.getLowPoints()); //low points
                writeD(arena6v6.getHighPoints()); //hight points
                return;
            default:
                writeD(0); //actual Rank
                writeD(0); //current Points
                writeD(0);
                writeD(0);
                writeD(0);//lasted rank
                writeD(0); //last Points
                writeD(0); //low points
                writeD(0); //hight points
            return;
        }
    }
}