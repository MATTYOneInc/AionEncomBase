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
package com.aionemu.gameserver.questEngine.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @author Mr. Poke
 */
@XmlEnum
public enum ConditionOperation {

	EQUAL, GREATER, GREATER_EQUAL, LESSER, LESSER_EQUAL, NOT_EQUAL, IN, NOT_IN;

	public String value() {
		return name();
	}

	public static ConditionOperation fromValue(String v) {
		return valueOf(v);
	}
}