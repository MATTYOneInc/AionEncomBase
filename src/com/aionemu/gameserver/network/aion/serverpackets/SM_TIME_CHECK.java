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

import java.sql.Timestamp;

/**
 * I have no idea wtf is this
 * 
 * @author -Nemesiss-
 */
public class SM_TIME_CHECK extends AionServerPacket {

	// Don't be fooled with empty class :D
	// This packet is just sending opcode, without any content

	// 1.5.x sending 8 bytes

	private int nanoTime;
	private int time;
	private Timestamp dateTime;

	public SM_TIME_CHECK(int nanoTime) {
		this.dateTime = new Timestamp((new java.util.Date()).getTime());
		this.nanoTime = nanoTime;
		this.time = (int) dateTime.getTime();
	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(time);
		writeD(nanoTime);

	}
}