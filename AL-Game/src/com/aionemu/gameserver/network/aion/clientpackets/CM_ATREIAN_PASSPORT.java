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
import com.aionemu.gameserver.services.events.AtreianPassportService;

/**
 * @author Ghostfur (Aion-Unique)
 */
public class CM_ATREIAN_PASSPORT extends AionClientPacket {

	@SuppressWarnings("unused")
	private int passportId;

	/**
	 * @param opcode
	 * @param state
	 * @param restStates
	 */
	public CM_ATREIAN_PASSPORT(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aionemu.commons.network.packet.BaseClientPacket#readImpl()
	 */
	@Override
	protected void readImpl() {
		passportId = readD();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.aionemu.commons.network.packet.BaseClientPacket#runImpl()
	 */
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}
		AtreianPassportService.getInstance().getReward(player, 8); // 8 = active PassportID (Todo Config ?)
	}
}