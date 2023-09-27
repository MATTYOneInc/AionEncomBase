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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import java.util.Map;

/**
 * Packet with macro list.
 *
 * @author -Nemesiss-
 */
public class SM_MACRO_LIST extends AionServerPacket {

	private Player player;

	/**
	 * Constructs new <tt>SM_MACRO_LIST </tt> packet
	 */
	public SM_MACRO_LIST(Player player, boolean secondPart) {
		this.player = player;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(player.getObjectId());// player id

		int size = player.getMacroList().getSize();

		writeC(0x01);
		writeH(-size);

		if (size != 0) {
			for (Map.Entry<Integer, String> entry : player.getMacroList().getMacrosses().entrySet()) {
				writeC(entry.getKey());// order
				writeS(entry.getValue());// xml
			}
		}
	}
}