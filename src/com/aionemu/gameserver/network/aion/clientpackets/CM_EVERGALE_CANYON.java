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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EVERGALE_CANYON;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Ranastic
 */

public class CM_EVERGALE_CANYON extends AionClientPacket {
	Logger log = LoggerFactory.getLogger(CM_EVERGALE_CANYON.class);

	public int action;

	public CM_EVERGALE_CANYON(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		action = readH();
		switch (action) {
		case 0:
			readD();
			readD();
			readD();
			readD();
			break;
		}
	}

	@Override
	protected void runImpl() {
		switch (action) {
		case 0:
			PacketSendUtility.sendPacket(getConnection().getActivePlayer(), new SM_EVERGALE_CANYON(0));
			break;
		}
	}
}