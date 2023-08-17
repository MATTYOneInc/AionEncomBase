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
package com.aionemu.gameserver.utils.stats;

import java.util.NoSuchElementException;

public enum DropRewardEnum
{
	MINUS_10(-10, 0),
	MINUS_9(-9, 39),
	MINUS_8(-8, 79),
	MINUS_7(-7, 100);
	
	private int dropRewardPercent;
	private int levelDifference;
	
	private DropRewardEnum(int levelDifference, int dropRewardPercent) {
		this.levelDifference = levelDifference;
		this.dropRewardPercent = dropRewardPercent;
	}
	
	public int rewardPercent() {
		return dropRewardPercent;
	}
	
	public static int dropRewardFrom(int levelDifference) {
		if (levelDifference < MINUS_10.levelDifference) {
			return MINUS_10.dropRewardPercent;
		} if (levelDifference > MINUS_7.levelDifference) {
			return MINUS_7.dropRewardPercent;
		} for (DropRewardEnum dropReward : values()) {
			if (dropReward.levelDifference == levelDifference) {
				return dropReward.dropRewardPercent;
			}
		}
		throw new NoSuchElementException("Drop reward for such level difference was not found");
	}
}