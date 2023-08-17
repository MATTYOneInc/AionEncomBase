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

public enum EventType
{
	NONE(0, ""),
	CHRISTMAS(1 << 0, "christmas"),
	HALLOWEEN(1 << 1, "halloween"),
	VALENTINE(1 << 2, "valentine"),
	BRAXCAFE(1 << 3, "braxcafe");
	
	private int id;
	private String theme;
	
	private EventType(int id, String theme) {
		this.id = id;
		this.theme = theme;
	}
	
	public int getId() {
		return id;
	}
	
	public String getTheme() {
		return theme;
	}
	public static EventType getEventType(String theme) {
		for (EventType type : values()) {
			if (theme.equals(type.getTheme())) {
				return type;
			}
		}
		return EventType.NONE;
	}
}