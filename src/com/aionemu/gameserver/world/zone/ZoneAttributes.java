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
package com.aionemu.gameserver.world.zone;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * @author Rolandas
 */
@XmlType(name = "ZoneAttributes")
@XmlEnum(String.class)
public enum ZoneAttributes {
	BIND(1 << 0),
	RECALL(1 << 1),
	GLIDE(1 << 2),
	FLY(1 << 3),
	RIDE(1 << 4),
	FLY_RIDE(1 << 5),

	@XmlEnumValue("PVP")
	PVP_ENABLED(1 << 6), // Only for PvP type zones
	@XmlEnumValue("DUEL_SAME_RACE")
	DUEL_SAME_RACE_ENABLED(1 << 7), // Only for Duel type zones
	@XmlEnumValue("DUEL_OTHER_RACE")
	DUEL_OTHER_RACE_ENABLED(1 << 8); // Only for Duel type zones

	private int id;

	private ZoneAttributes(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	public static Integer fromList(List<ZoneAttributes> flagValues) {
		Integer result = 0;
		for (ZoneAttributes attribute : ZoneAttributes.values()) {
			if (flagValues.contains(attribute)) {
				result |= attribute.getId();
			}
		}
		return result;
	}
}