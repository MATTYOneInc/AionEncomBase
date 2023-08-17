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

import com.aionemu.gameserver.model.gameobjects.player.PlayerScripts;
import com.aionemu.gameserver.model.house.PlayerScript;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import java.util.Map;

public class SM_HOUSE_SCRIPTS extends AionServerPacket {

	private int address;
	private PlayerScripts scripts;
	int from;
	int to;

	public SM_HOUSE_SCRIPTS(int address, PlayerScripts scripts, int from, int to) {
		this.address = address;
		this.scripts = scripts;
		this.from = from;
		this.to = to;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(address);
		writeH(to - from + 1);
		Map<Integer, PlayerScript> scriptMap = scripts.getScripts();
		for (int position = from; position <= to; position++) {
			writeC(position);
			PlayerScript script = scriptMap.get(position);
			byte[] bytes = script.getCompressedBytes();
			if (bytes == null) {
				writeH(-1);
			}
			else if (bytes.length == 0) {
				writeH(0);
			}
			else {
				writeH(bytes.length + 8);
				writeD(bytes.length);
				writeD(script.getUncompressedSize());
				writeB(bytes);
			}
		}
	}
}