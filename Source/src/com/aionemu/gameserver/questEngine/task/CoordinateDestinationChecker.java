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
package com.aionemu.gameserver.questEngine.task;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.utils.MathUtil;

final class CoordinateDestinationChecker extends DestinationChecker {

	private final Creature follower;
	private final float x;
	private final float y;
	private final float z;

	/**
	 * @param follower
	 * @param x
	 * @param y
	 * @param z
	 */
	CoordinateDestinationChecker(Creature follower, float x, float y, float z) {
		this.follower = follower;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	boolean check() {
		return MathUtil.isNearCoordinates(follower, x, y, z, 10);
	}
}