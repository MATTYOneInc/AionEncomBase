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

public enum HEALTH {
	WARRIOR(110), GLADIATOR(115), TEMPLAR(100), SCOUT(100), ASSASSIN(100), RANGER(90), MAGE(90), SORCERER(90),
	SPIRIT_MASTER(90), PRIEST(95), CLERIC(110), CHANTER(105),

	// News Class 4.3
	TECHNIST(100), GUNSLINGER(105), MUSE(95), SONGWEAVER(100),
	// News Class 4.5
	AETHERTECH(100);

	private int value;

	private HEALTH(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}