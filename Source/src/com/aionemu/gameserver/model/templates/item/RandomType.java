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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum RandomType
{
	ENCHANTMENT,
	MANASTONE,
	MANASTONE_COMMON_GRADE_10(10),
	MANASTONE_COMMON_GRADE_20(20),
	MANASTONE_COMMON_GRADE_30(30),
	MANASTONE_COMMON_GRADE_40(40),
	MANASTONE_COMMON_GRADE_50(50),
	MANASTONE_COMMON_GRADE_60(60),
	MANASTONE_COMMON_GRADE_65(65),
	MANASTONE_RARE_GRADE_10(10),
	MANASTONE_RARE_GRADE_20(20),
	MANASTONE_RARE_GRADE_30(30),
	MANASTONE_RARE_GRADE_40(40),
	MANASTONE_RARE_GRADE_50(50),
	MANASTONE_RARE_GRADE_60(60),
	MANASTONE_RARE_GRADE_65(65),
	MANASTONE_LEGEND_GRADE_10(10),
	MANASTONE_LEGEND_GRADE_20(20),
	MANASTONE_LEGEND_GRADE_30(30),
	MANASTONE_LEGEND_GRADE_40(40),
	MANASTONE_LEGEND_GRADE_50(50),
	MANASTONE_LEGEND_GRADE_60(60),
	MANASTONE_LEGEND_GRADE_65(65),
	ANCIENT_ITEMS,
	CHUNK_EARTH,
	CHUNK_ROCK, 
	CHUNK_SAND, 
	CHUNK_GEMSTONE,
	SCROLLS,
	POTION;
	
	private int level;
	
	private RandomType() {
	}
	
	private RandomType(int level) {
		this.level = level;
	}
	
	public int getLevel() {
		return level;
	}
}