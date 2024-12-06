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
package com.aionemu.gameserver.questEngine.task;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.world.zone.ZoneName;

final class ZoneChecker extends DestinationChecker {

	private final ZoneName zoneName;

	ZoneChecker(Creature follower, ZoneName zoneName) {
		this.follower = follower;
		this.zoneName = zoneName;
	}

	@Override
	boolean check() {
		return follower.isInsideZone(zoneName);
	}
}

final class ZoneChecker2 extends DestinationChecker {

	private final ZoneName zone1, zone2;

	ZoneChecker2(Creature follower, ZoneName zone1, ZoneName zone2) {
		this.follower = follower;
		this.zone1 = zone1;
		this.zone2 = zone2;
	}

	@Override
	boolean check() {
		return follower.isInsideZone(zone1) || follower.isInsideZone(zone2);
	}
}