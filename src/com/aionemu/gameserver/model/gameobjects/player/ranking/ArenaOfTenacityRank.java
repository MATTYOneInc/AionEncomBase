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
package com.aionemu.gameserver.model.gameobjects.player.ranking;

import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * Created by Wnkrz on 24/07/2017.
 */
public class ArenaOfTenacityRank {
    //rank
    private int rank;
    private int bestRank;

    //competiton Points
    private int points;
    private int lastPoints;
    private int highPoints;
    private int lowPoints;

    private int possitionMatch;

    private PersistentState persistentState;

    public ArenaOfTenacityRank(int rank, int bestRank, int points, int lastPoints, int highPoints, int lowPoints, int possitionMatch){
        this.rank = rank;
        this.bestRank = bestRank;
        this.points = points;
        this.lastPoints = lastPoints;
        this.highPoints = highPoints;
        this.lowPoints = lowPoints;
        this.possitionMatch = possitionMatch;
    }

    public int getRank(){
        return rank;
    }

    public int getBestRank(){
        return bestRank;
    }

    public int getPoints(){
        return points;
    }

    public int getLastPoints(){
        return lastPoints;
    }

    public int getHighPoints(){
        return highPoints;
    }

    public int getLowPoints() {
        return lowPoints;
    }

    public int getPossitionMatch(){
        return possitionMatch;
    }

    public void setRank(int rank){
        this.rank = rank;
    }

    public void setBestRank(int rank){
        this.bestRank = rank;
    }

    public void setPoints(int pts){
        this.points = pts;
    }

    public void setLastPoints(int pts){
        this.lastPoints = pts;
    }

    public void setHighPoints(int pts){
        this.highPoints = pts;
    }

    public void setLowPoints(int pts) {
        this.lowPoints = pts;
    }

    public void setPossitionMatch(int pos){
        this.possitionMatch = pos;
    }

    /**
     * @return the persistentState
     */
    public PersistentState getPersistentState() {
        return persistentState;
    }

    /**
     * @param persistentState
     *          the persistentState to set
     */
    public void setPersistentState(PersistentState persistentState) {
        if (persistentState != PersistentState.UPDATE_REQUIRED || this.persistentState != PersistentState.NEW) {
            this.persistentState = persistentState;
		}
    }
}