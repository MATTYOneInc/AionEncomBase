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

public enum POWER
{
	WARRIOR(110),
	GLADIATOR(110),
	TEMPLAR(110),
	SCOUT(100),
	ASSASSIN(110),
	RANGER(90),
	MAGE(90),
	SORCERER(90),
	SPIRIT_MASTER(90),
	PRIEST(95),
	CLERIC(105),
	CHANTER(110),

	//News Class 4.3
	TECHNIST(100), //GOOD
	GUNSLINGER(100), //GOOD
	MUSE(95), //GOOD
	SONGWEAVER(90), // 95?
	AETHERTECH(110); //115?

	private int value;
	
	private POWER(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}