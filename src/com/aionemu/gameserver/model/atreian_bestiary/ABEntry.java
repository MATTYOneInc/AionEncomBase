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
package com.aionemu.gameserver.model.atreian_bestiary;

/**
 * @author Ranastic
 */

public class ABEntry {
	private int id;
	private int killCount;
	private int level;
	private int claimReward;

	public ABEntry(int id, int killCount, int level, int claimReward) {
		this.id = id;
		this.killCount = killCount;
		this.level = level;
		this.claimReward = claimReward;
	}

	public int getId() {
		return id;
	}

	public int getKillCount() {
		return killCount;
	}

	public int getLevel() {
		return level;
	}

	public int claimRewardLevel() {
		return claimReward;
	}
}