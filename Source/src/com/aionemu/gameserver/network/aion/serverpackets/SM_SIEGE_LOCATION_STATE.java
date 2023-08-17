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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_SIEGE_LOCATION_STATE extends AionServerPacket {

	private int locationId;
	private int state;

	public SM_SIEGE_LOCATION_STATE(SiegeLocation location) {
		locationId = location.getLocationId();
		state = (location.isVulnerable() ? 1 : 0);
	}

	public SM_SIEGE_LOCATION_STATE(int locationId, int state) {
		this.locationId = locationId;
		this.state = state;
	}

	protected void writeImpl(AionConnection con) {
		writeD(locationId);
		writeC(state);
	}
}