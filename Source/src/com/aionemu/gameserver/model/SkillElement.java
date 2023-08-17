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
package com.aionemu.gameserver.model;

import com.aionemu.gameserver.model.stats.container.StatEnum;

/**
 * @author xavier
 */
public enum SkillElement {

	NONE(0),
	FIRE(1),
	WATER(2),
	WIND(3),
	EARTH(4),
	LIGHT(5),
	DARK(6);

	private int element;

	private SkillElement(int id) {
		this.element = id;
	}

	public int getElementId() {
		return element;
	}
	
	public static StatEnum getResistanceForElement(SkillElement element) {
		switch (element) {
			case FIRE:
				return StatEnum.FIRE_RESISTANCE;
			case WATER:
				return StatEnum.WATER_RESISTANCE;
			case WIND:
				return StatEnum.WIND_RESISTANCE;
			case EARTH:
				return StatEnum.EARTH_RESISTANCE;
			case LIGHT:
				return StatEnum.ELEMENTAL_RESISTANCE_LIGHT;
			case DARK:
				return StatEnum.ELEMENTAL_RESISTANCE_DARK;
		}
		return null;
	}
}