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
import com.aionemu.gameserver.model.templates.challenge.ChallengeType;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.ChallengeTaskService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_CHALLENGE_LIST extends AionClientPacket {
	public CM_CHALLENGE_LIST(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	int action;
	int taskOwner;
	int ownerType;
	int playerId;
	int dateSince;

	@Override
	protected void readImpl() {
		action = readC();
		taskOwner = readD();
		ownerType = readC();
		playerId = readD();
		dateSince = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (ownerType == 1) {
			if (player.getLegion() == null) {
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GUILD_LEAVE_I_AM_NOT_BELONG_TO_GUILD);
				return;
			}
			ChallengeTaskService.getInstance().showTaskList(player, ChallengeType.LEGION, taskOwner);
		} else {
			ChallengeTaskService.getInstance().showTaskList(player, ChallengeType.TOWN, taskOwner);
		}
	}
}