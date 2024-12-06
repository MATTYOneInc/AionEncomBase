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

import java.util.Map;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Packet with macro list.
 *
 * @author -Nemesiss-
 */
public class SM_MACRO_LIST extends AionServerPacket {

	private Player player;
	private int packet;

	/**
	 * Constructs new <tt>SM_MACRO_LIST </tt> packet
	 */
	public SM_MACRO_LIST(Player player, int packet) {
		this.player = player;
		this.packet = packet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(player.getObjectId());// player id

		Map<Integer, String> macrosToSend = player.getMacroList().getMarcosPart(packet);

		int size = macrosToSend.size();

		if (packet == 1) {
			writeC(1);
		} else {
			writeC(0);
			size *= -1;
		}

		writeH(size);

		if (size != 0) {
			for (Map.Entry<Integer, String> entry : macrosToSend.entrySet()) {
				writeC(entry.getKey());// order
				writeS(entry.getValue());// xml
			}
		}
	}
}
