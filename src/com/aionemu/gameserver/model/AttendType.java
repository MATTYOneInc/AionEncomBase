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
package com.aionemu.gameserver.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author Alcapwnd
 */
@XmlEnum
public enum AttendType {

	NONE(0), BASIC(1), ANNIVERSARY(2);

	private int id;

	private AttendType(int id) {
		this.id = id;
	}

	public static AttendType getLoginTypeById(int id) {
		for (AttendType attendType : values()) {
			if (attendType.getId() == id) {
				return attendType;
			}
		}
		return AttendType.NONE;
	}

	public int getId() {
		return id;
	}
}