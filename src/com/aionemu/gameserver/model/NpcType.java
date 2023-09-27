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

public enum NpcType
{
	ATTACKABLE(0),
	PEACE(2),
	AGGRESSIVE(8),
	INVULNERABLE(10),
	NON_ATTACKABLE(38),
	UNKNOWN(54);
	
	private int someClientSideId;
	
	private NpcType(int id) {
		this.someClientSideId = id;
	}
	
	public int getId() {
		return someClientSideId;
	}
}