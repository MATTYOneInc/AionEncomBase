/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
import com.aionemu.gameserver.model.gameobjects.player.ReviveType;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.player.PlayerReviveService;

/**
 * @author Ranastic (Encom)
 */

public class CM_REVIVE extends AionClientPacket {

	private int reviveId;
	
	public CM_REVIVE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		reviveId = readC();
	}
	
	@Override
	protected void runImpl() {
		Player activePlayer = getConnection().getActivePlayer();
		if (!activePlayer.getLifeStats().isAlreadyDead()) {
			return;
		}
		ReviveType reviveType = ReviveType.getReviveTypeById(reviveId, activePlayer);
		switch (reviveType) {
			case BIND_REVIVE:
			case VORTEX_REVIVE:
				PlayerReviveService.bindRevive(activePlayer);
			break;
			case REBIRTH_REVIVE:
				PlayerReviveService.rebirthRevive(activePlayer);
			break;
			case ITEM_SELF_REVIVE:
				PlayerReviveService.itemSelfRevive(activePlayer);
			break;
			case SKILL_REVIVE:
				PlayerReviveService.skillRevive(activePlayer);
			break;
			case KISK_REVIVE:
				PlayerReviveService.kiskRevive(activePlayer);
			break;
			case INSTANCE_REVIVE:
				PlayerReviveService.instanceRevive(activePlayer);
			break;
			case START_POINT_REVIVE:
				PlayerReviveService.startPositionRevive(activePlayer);
			break;
			default:
				break;
		}
	}
}