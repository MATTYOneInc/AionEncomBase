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
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.common.events.TeamCommand;
import com.aionemu.gameserver.model.team2.common.service.PlayerTeamCommandService;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * Developer's note please dont remove Duoc goi ra khi trong group va su dung
 * assign group leader khi doi tuong dang offline
 * 
 * @author Lyahim, ATracer, Simple, xTz
 */

public class CM_GROUP_PLAYER_STATUS_INFO extends AionClientPacket {

	private int commandCode;
	private int playerObjId;
	private int allianceGroupId;
	private int secondObjectId;

	public CM_GROUP_PLAYER_STATUS_INFO(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		commandCode = readC();
		playerObjId = readD();
		allianceGroupId = readD();
		secondObjectId = readD();
	}

	@Override
	protected void runImpl() {
		Player activePlayer = getConnection().getActivePlayer();
		TeamCommand command = TeamCommand.getCommand(commandCode);
		switch (command) {
		case GROUP_SET_LFG:
			activePlayer.setLookingForGroup(playerObjId == 2);
			break;
		case ALLIANCE_CHANGE_GROUP:
			PlayerAllianceService.changeMemberGroup(activePlayer, playerObjId, secondObjectId, allianceGroupId);
			break;
		default:
			PlayerTeamCommandService.executeCommand(activePlayer, command, playerObjId);
		}
	}
}