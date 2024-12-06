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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_FORTRESS_INFO extends AionServerPacket {

	private int locationId;
	private boolean teleportStatus;
	private int unk;

	public SM_FORTRESS_INFO(int locationId, boolean teleportStatus) {
		this.locationId = locationId;
		this.teleportStatus = teleportStatus;
	}

	protected void writeImpl(AionConnection con) {
		writeD(1);// 4.3 protocol changed
		writeD(locationId);
		writeD(unk);// 4.3 protocol changed
		writeC(teleportStatus ? 1 : 0);
	}
}