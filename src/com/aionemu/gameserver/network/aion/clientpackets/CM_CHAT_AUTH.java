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

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHAT_INIT;
import com.aionemu.gameserver.network.chatserver.ChatServer;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_CHAT_AUTH extends AionClientPacket
{
	public CM_CHAT_AUTH(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		@SuppressWarnings("unused")
		int objectId = readD();
		@SuppressWarnings("unused")
		byte[] macAddress = readB(6);
	}
	
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (GSConfig.ENABLE_CHAT_SERVER) {
			if (!player.isInPrison()) {
				ChatServer.getInstance().sendPlayerLoginRequst(player);
			}
		} else {
			PacketSendUtility.sendPacket(player, new SM_CHAT_INIT(new byte[0]));
		}
	}
}