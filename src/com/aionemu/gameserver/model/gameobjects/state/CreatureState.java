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
package com.aionemu.gameserver.model.gameobjects.state;

/**
 * @author ATracer, Sweetkr
 */
public enum CreatureState
{
    ACTIVE(1),
    FLYING(2),
    FLIGHT_TELEPORT(2),
    RESTING(4),
    DEAD(7),
    CHAIR(6),
    FLOATING_CORPSE(8),
    PRIVATE_SHOP(10),
    LOOTING(12),
    WEAPON_EQUIPPED(32),
    WALKING(64),
    NPC_IDLE(64),
    POWERSHARD(128),
    TREATMENT(256),
    GLIDING(512);

	/**
	 * Standing, path flying, free flying, riding, sitting, sitting on chair, dead, fly dead, private shop, looting, fly
	 * looting, default
	 */

	private int id;

	private CreatureState(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}