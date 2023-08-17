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

public enum ATTACK_SPEED
{
	WARRIOR(1500),
	GLADIATOR(1500),
	TEMPLAR(1500),
	SCOUT(1500),
	ASSASSIN(1500),
	RANGER(1500),
	MAGE(1500),
	SORCERER(1500),
	SPIRIT_MASTER(1500),
	PRIEST(1500),
	CLERIC(1500),
	CHANTER(1500),
	//News Class 4.3
	TECHNIST(1500),
	GUNSLINGER(1500),
	MUSE(1500),
	SONGWEAVER(1500),
	//News Class 4.5
	AETHERTECH(1500);
	
	private int value;
	
	private ATTACK_SPEED(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}