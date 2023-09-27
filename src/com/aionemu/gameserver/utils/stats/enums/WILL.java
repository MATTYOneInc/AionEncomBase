/*
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
package com.aionemu.gameserver.utils.stats.enums;

public enum WILL
{
	WARRIOR(90),
	GLADIATOR(90),
	TEMPLAR(105),
	SCOUT(90),
	ASSASSIN(90),
	RANGER(110),
	MAGE(115),
	SORCERER(110),
	SPIRIT_MASTER(115),
	PRIEST(110),
	CLERIC(110),
	CHANTER(110),
	//News Class 4.3
	TECHNIST(90),
	GUNSLINGER(100),
	MUSE(110),
	SONGWEAVER(110),
	//News Class 4.5
	AETHERTECH(90);
	
	private int value;
	
	private WILL(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}