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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerScripts;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_SCRIPTS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_HOUSE_SCRIPT extends AionClientPacket {
	int address;
	int scriptIndex;
	int totalSize;
	int compressedSize;
	int uncompressedSize;
	byte[] stream;

	public CM_HOUSE_SCRIPT(int opcode, State state, State... restStates) {

		super(opcode, state, restStates);
	}

	protected void readImpl() {
		address = readD();
		scriptIndex = readC();
		totalSize = readH();
		if (totalSize > 0) {
			compressedSize = readD();
			if (compressedSize < 8150) {
				uncompressedSize = readD();
				stream = readB(compressedSize);
			}
		}
	}

	protected void runImpl() {
		Player player = this.getConnection().getActivePlayer();

		if (compressedSize > 8149) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_SCRIPT_OVERFLOW);
		}
		House house = player.getActiveHouse();

		if (house == null) {

			return;
		}
		PlayerScripts scripts = house.getPlayerScripts();

		if (totalSize <= 0) {

			scripts.addScript(scriptIndex, new byte[0], 0);
		}

		else
			scripts.addScript(scriptIndex, stream, uncompressedSize);

		PacketSendUtility.sendPacket(player, new SM_HOUSE_SCRIPTS(address, scripts, scriptIndex, scriptIndex));
	}
}