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
package com.aionemu.gameserver.model.gameobjects.player;

public enum ReviveType
{
	BIND_REVIVE(0),
	REBIRTH_REVIVE(1),
	ITEM_SELF_REVIVE(2),
	SKILL_REVIVE(3),
	KISK_REVIVE(4),
	INSTANCE_REVIVE(6),
	VORTEX_REVIVE(8),
	START_POINT_REVIVE(11);
	
	private int typeId;
	
	private ReviveType(int typeId) {
		this.typeId = typeId;
	}
	
	public int getReviveTypeId() {
		return typeId;
	}
	
	public static ReviveType getReviveTypeById(int id, Player pl) {
		for (ReviveType rt : values()) {
			if (rt.typeId == id) {
				return rt;
			}
		}
		throw new IllegalArgumentException("Unsupported revive type: " + id);
	}
}