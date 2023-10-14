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

import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author Ranastic (Encom)
 */

public class CM_TITLE_SET extends AionClientPacket {
	private int titleId;

	public CM_TITLE_SET(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		titleId = readH();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (titleId != 0xFFFF) {
			if (!player.getTitleList().contains(titleId)
					&& !player.havePermission(MembershipConfig.TITLES_ADDITIONAL_ENABLE)) {
				return;
			}
		}
		player.getTitleList().setDisplayTitle(titleId);
	}
}