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
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUIT_RESPONSE;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.services.player.PlayerLeaveWorldService;

public class CM_QUIT extends AionClientPacket
{
	private boolean logout;
	
	public CM_QUIT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		logout = readC() == 1;
	}
	
	@Override
	protected void runImpl() {
		AionConnection client = getConnection();
		Player player = null;
		if (client.getState() == State.IN_GAME) {
			player = client.getActivePlayer();
			if (!logout) {
				LoginServer.getInstance().aionClientDisconnected(client.getAccount().getId());
			}
			PlayerLeaveWorldService.startLeaveWorld(player);
			client.setActivePlayer(null);
		} if (logout) {
			if (player != null && player.isInEditMode()) {
				sendPacket(new SM_QUIT_RESPONSE(true));
				player.setEditMode(false);
			} else {
				sendPacket(new SM_QUIT_RESPONSE());
			}
		} else {
			client.close(new SM_QUIT_RESPONSE(), false);
		}
	}
}