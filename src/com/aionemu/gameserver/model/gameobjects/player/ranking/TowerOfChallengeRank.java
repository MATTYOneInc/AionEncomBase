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
package com.aionemu.gameserver.model.gameobjects.player.ranking;

import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * Created by Wnkrz on 24/07/2017.
 */

public class TowerOfChallengeRank
{
    private int rank;
    private int bestRank;
    private int lowRank;
    private int currentTime;
    private int lastTime;
    private int bestTime;
    private PersistentState persistentState;
	
    public TowerOfChallengeRank(int rank, int bestRank, int low_rank, int current_time, int last_time, int best_time){
        this.rank = rank;
        this.bestRank = bestRank;
        this.lowRank = low_rank;
        this.currentTime = current_time;
        this.lastTime = last_time;
        this.bestTime = best_time;
    }
	
    public int getRank() {
        return rank;
    }
    public int getBestRank() {
        return bestRank;
    }
    public int getLowRank() {
        return lowRank;
    }
    public int getCurrentTime() {
        return currentTime;
    }
    public int getLastTime() {
        return lastTime;
    }
    public int getBestTime() {
        return bestTime;
    }
    public void setRank(int r) {
        this.rank = r;
    }
    public void setBestRank(int r) {
        this.bestRank = r;
    }
    public void  setLowRank(int r) {
        this.lowRank = r;
    }
    public void setCurrentTime(int r) {
        this.currentTime = r;
    }
    public void setLastTime(int r) {
        this.lastTime = r;
    }
    public void setBestTime(int r) {
        this.bestTime = r;
    }
	
    public PersistentState getPersistentState() {
        return persistentState;
    }
	
    public void setPersistentState(PersistentState persistentState) {
        if (persistentState != PersistentState.UPDATE_REQUIRED || this.persistentState != PersistentState.NEW) {
            this.persistentState = persistentState;
		}
    }
}