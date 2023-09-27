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

public enum MAIN_HAND_ATTACK
{
	WARRIOR(20),
	GLADIATOR(20),
	TEMPLAR(20),
	SCOUT(20),
	ASSASSIN(20),
	RANGER(18),
	MAGE(14),
	SORCERER(14),
	SPIRIT_MASTER(14),
	PRIEST(18),
	CLERIC(18),
	CHANTER(20),
	//News Class 4.3
	TECHNIST(18),
	GUNSLINGER(18),
	MUSE(14),
	SONGWEAVER(14),
	//News Class 4.5
	AETHERTECH(20);
	
	private int value;
	
	private MAIN_HAND_ATTACK(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}