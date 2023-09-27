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
package com.aionemu.gameserver.services.events.thievesguildservice;

/**
 * @author Rinzler (Encom)
 */
public enum ThievesType {
	
	NONE(0), // Нет
	BRONZE(1),
	SILVER(2),
	GOLD(3),
	PLATINUM(4),
	MITHRIL(5),
	SERAMIUM(6);

	private int id;

	private ThievesType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public static ThievesType getThievesType(int id) {
		for (ThievesType type : values()) {
			if (id == type.getId()) {
				return type;
			}
		}
		return ThievesType.NONE;
	}
}