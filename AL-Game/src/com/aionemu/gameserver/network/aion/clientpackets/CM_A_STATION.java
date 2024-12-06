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
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.AStationService;

public class CM_A_STATION extends AionClientPacket {
	private int action;

	public CM_A_STATION(int opcode, State state, State... states) {
		super(opcode, state, states);
	}

	@Override
	protected void readImpl() {
		action = readH();
		readH();
		readD();
		readD();
		readD();
	}

	@Override
	protected void runImpl() {
		Player requested = getConnection().getActivePlayer();
		switch (action) {
		case 1:
			AStationService.getInstance().handleMoveThere(requested);
			break;
		case 2:
			AStationService.getInstance().handleMoveBack(requested);
			break;
		}
	}
}