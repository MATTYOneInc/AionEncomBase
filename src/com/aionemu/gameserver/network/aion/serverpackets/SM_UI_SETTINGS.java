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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_UI_SETTINGS extends AionServerPacket
{
	private byte[] data;
	private int type;
	
	public SM_UI_SETTINGS(byte[] data, int type) {
		this.data = data;
		this.type = type;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeC(type);
		writeH(0x1C00);
		writeB(data);
		if (0x1C00 > data.length) {
			writeB(new byte[0x1C00 - data.length]);
		}
	}
}