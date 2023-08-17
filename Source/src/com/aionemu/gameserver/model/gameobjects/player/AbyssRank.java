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
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

public class AbyssRank
{
	Logger log = LoggerFactory.getLogger(AbyssRank.class);
	
	//AP
	private int dailyAP;
	private int weeklyAP;
	private int currentAp;
	//GP
	private int dailyGP;
	private int weeklyGP;
	private int currentGp;
	private AbyssRankEnum rank;
	private int topRanking;
	private PersistentState persistentState;
	private int dailyKill;
	private int weeklyKill;
	private int allKill;
	private int maxRank;
	private int lastKill;
	private int lastAP;
	private int lastGP;
	private long lastUpdate;
	
   /**
	* @param dailyAP
	* @param weeklyAP
	* @param ap
	*
	* @param dailyGP
	* @param weeklyGP
	* @param gp
	*
	* @param rank
	* @param dailyKill
	* @param weeklyKill
	* @param allKill
	* @param maxRank
	* @param lastKill
	* @param lastAP
	* @param lastGP
	* @param lastUpdate
	*/
	public AbyssRank(int dailyAP, int dailyGP, int weeklyAP, int weeklyGP, int ap, int gp, int rank, int topRanking, int dailyKill, int weeklyKill,
		int allKill, int maxRank, int lastKill, int lastAP, int lastGP, long lastUpdate) {
		//AP
		this.dailyAP = dailyAP;
		this.weeklyAP = weeklyAP;
		this.currentAp = ap;
		//GP
		this.dailyGP = dailyGP;
		this.weeklyGP = weeklyGP;
		this.currentGp = gp;
		this.rank = AbyssRankEnum.getRankById(rank);
		this.topRanking = topRanking;
		this.dailyKill = dailyKill;
		this.weeklyKill = weeklyKill;
		this.allKill = allKill;
		this.maxRank = maxRank;
		this.lastKill = lastKill;
		this.lastAP = lastAP;
		this.lastGP = lastGP;
		this.lastUpdate = lastUpdate;
		doUpdate();
	}

	public enum AbyssRankUpdateType {
		PLAYER_ELYOS (1),
		PLAYER_ASMODIANS(2),
		LEGION_ELYOS(4),
		LEGION_ASMODIANS(8);
		
		private int id;
		AbyssRankUpdateType(int id) {
			this.id = id;
		}
		
		public int value() {
			return id;
		}
	}
	
	/**
	 * Add AP to a player (current player AP + added AP)
	 * 
	 * @param additionalAp
	 */
	public void addAp(int additionalAp, Player player) {
		dailyAP += additionalAp;
		if (dailyAP < 0) {
			dailyAP = 0;
		}
		weeklyAP += additionalAp;
		if (weeklyAP < 0) {
			weeklyAP = 0;
		}
		currentAp += additionalAp;
		if (currentAp < 0) {
			currentAp = 0;
		}
		AbyssRankEnum newRank = AbyssRankEnum.getRankForAp(currentAp);
		if (player.getAbyssRank().getRank().getId() >= 1 && player.getAbyssRank().getRank().getId() <= 9) {
			if (newRank.getId() > 9) {
				newRank = AbyssRankEnum.GRADE1_SOLDIER;
			}
			setRank(newRank);
		}
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * Add GP to a player (current player GP + added GP)
	 * 
	 * @param additionalGp
	 */
	public void addGp(int additionalGp) {
		dailyGP += additionalGp;
		if (dailyGP < 0) {
			dailyGP = 0;
		}
		weeklyGP += additionalGp;
		if (weeklyGP < 0) {
			weeklyGP = 0;
		}
		currentGp += additionalGp;
		if (currentGp < 0) {
			currentGp = 0;
		}
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	/**
	 * @return The daily <Abyss Point> count
	 */
	public int getDailyAP() {
		return dailyAP;
	}
	
	/**
	 * @return The weekly <Abyss Point> count
	 */
	public int getWeeklyAP() {
		return weeklyAP;
	}
	
	/**
	 * @return The all time <Abyss Point> count
	 */
	public int getAp() {
		return currentAp;
	}
	
	/**
	 * @return The daily <Glory Point> count
	 */
	public int getDailyGP() {
		return dailyGP;
	}
	
	/**
	 * @return The weekly <Glory Point> count
	 */
	public int getWeeklyGP() {
		return weeklyGP;
	}
	
	/**
	 * @return The all time <Glory Point> count
	 */
	public int getGp() {
		return currentGp;
	}
	
	/**
	 * @return the rank
	 */
	public AbyssRankEnum getRank() {
		return rank;
	}
	
	/**
	 * @return The top ranking of the current rank
	 */
	public int getTopRanking() {
		return topRanking;
	}
	
	/**
	 * @param topRanking
	 */
	public void setTopRanking(int topRanking) {
		this.topRanking = topRanking;
	}
	
	/**
	 * @return The daily count kill
	 */
	public int getDailyKill() {
		return dailyKill;
	}
	
	/**
	 * @return The weekly count kill
	 */
	public int getWeeklyKill() {
		return weeklyKill;
	}
	
	/**
	 * @return all Kill
	 */
	public int getAllKill() {
		return allKill;
	}
	
	/**
	 * Add one kill to a player
	 */
	public void updateKillCounts() {
		this.dailyKill += 1;
		this.weeklyKill += 1;
		this.allKill += 1;
	}
	
	/**
	 * @return max Rank
	 */
	public int getMaxRank() {
		return maxRank;
	}
	
	/**
	 * @return The last week count kill
	 */
	public int getLastKill() {
		return lastKill;
	}
	
	/**
	 * @return The last week <Abyss Point> count
	 */
	public int getLastAP() {
		return lastAP;
	}
	
	/**
	 * @return The last week <Glory Point> count
	 */
	public int getLastGP() {
		return lastGP;
	}
	
	/**
	 * @param rank
	 * the rank to set
	 */
	public void setRank(AbyssRankEnum rank) {
		if (rank.getId() > this.maxRank) {
			this.maxRank = rank.getId();
		}
		this.rank = rank;
		this.topRanking = rank.getQuota();
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}

	/**
	 * Add one kill to a player
	 */
	public void setAllKill() {
		this.dailyKill += 1;
		this.weeklyKill += 1;
		this.allKill += 1;
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
	
	/**
	 * @return The last update of the AbyssRank
	 */
	public long getLastUpdate() {
		return lastUpdate;
	}
	
	/**
	 * Make an update for the daily/weekly/last kill & ap counts
	 */
	public void doUpdate() {
		boolean needUpdate = false;
		Calendar lastCal = Calendar.getInstance();
		lastCal.setTimeInMillis(lastUpdate);
		Calendar curCal = Calendar.getInstance();
		curCal.setTimeInMillis(System.currentTimeMillis());
		// Checking the day - month & year are checked to prevent if a player come back after 1 month, the same day
		if (lastCal.get(Calendar.DAY_OF_MONTH) != curCal.get(Calendar.DAY_OF_MONTH)
			|| lastCal.get(Calendar.MONTH) != curCal.get(Calendar.MONTH)
			|| lastCal.get(Calendar.YEAR) != curCal.get(Calendar.YEAR)) {
			this.dailyAP = 0;
			this.dailyGP = 0;
			this.dailyKill = 0;
			needUpdate = true;
		}
		// Checking the week - year is checked to prevent if a player come back after 1 year, the same week
		if (lastCal.get(Calendar.WEEK_OF_YEAR) != curCal.get(Calendar.WEEK_OF_YEAR)
			|| lastCal.get(Calendar.YEAR) != curCal.get(Calendar.YEAR)) {
			this.lastKill = this.weeklyKill;
			this.lastAP = this.weeklyAP;
			this.lastGP = this.weeklyGP;
			this.weeklyKill = 0;
			this.weeklyAP = 0;
			this.weeklyGP = 0;
			needUpdate = true;
		}
		//For offline changed ranks
		if (rank.getId() > maxRank) {
            maxRank = rank.getId();
            needUpdate = true;
        }
		// Finally, update the the last update
		this.lastUpdate = System.currentTimeMillis();
		if (needUpdate) {
			setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
	}
}