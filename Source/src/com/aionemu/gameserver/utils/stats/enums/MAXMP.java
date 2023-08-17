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
package com.aionemu.gameserver.utils.stats.enums;

public enum MAXMP
{
	WARRIOR(100),
	GLADIATOR(100),
	TEMPLAR(100),
	SCOUT(100),
	ASSASSIN(100),
	RANGER(100),
	MAGE(100),
	SORCERER(100),
	SPIRIT_MASTER(100),
	PRIEST(100),
	CLERIC(100),
	CHANTER(100),
	//News Class 4.3
	TECHNIST(100),
	GUNSLINGER(100),
	MUSE(100),
	SONGWEAVER(100),
	//News Class 4.5
	AETHERTECH(100);
	
	private int value;
	
	private MAXMP(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}