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
package com.aionemu.gameserver.dataholders;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Object of this class is containing info about experience that are required for each level that player can obtain.
 * 
 * @author Luno
 */
@XmlRootElement(name = "player_experience_table")
@XmlAccessorType(XmlAccessType.NONE)
public class PlayerExperienceTable {

	/** Exp table */
	@XmlElement(name = "exp")
	private long[] experience;

	/**
	 * Returns the number of experience that player have at the beginning of given level.<br>
	 * For example at lv 1 it's 0
	 * 
	 * @param level
	 * @return count of experience. If <tt>level</tt> parameter is higher than the max level that player can gain, then
	 *         IllegalArgumentException is thrown.
	 */
	public long getStartExpForLevel(int level) {
		if (level > experience.length) {
			throw new IllegalArgumentException("The given level is higher than possible max");
        }
		return level == 0 ? 0 : experience[level - 1];
	}

	public int getLevelForExp(long expValue) {
		int level = 0;
		for (int i = experience.length; i > 0; i--) {
			if (expValue >= experience[(i - 1)]) {
				level = i;
				break;
			}
		}
		if (getMaxLevel() <= level) {
			return getMaxLevel()-1;
		}
		return level;
	}

	/**
	 * Max possible level,that player can obtain.
	 * 
	 * @return max level.
	 */
	public int getMaxLevel() {
		return experience == null ? 0 : experience.length;
	}
}