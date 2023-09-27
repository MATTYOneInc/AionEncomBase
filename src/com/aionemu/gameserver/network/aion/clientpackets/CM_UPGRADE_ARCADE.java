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
import com.aionemu.gameserver.services.events.ArcadeUpgradeService;

/**
 * @author Ranastic
 */

public class CM_UPGRADE_ARCADE extends AionClientPacket
{
	private int action;
	@SuppressWarnings("unused")
	private int sessionId;
	
	public CM_UPGRADE_ARCADE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		action = readC();
		sessionId = readD();
	}
	
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		} switch(action) {
			case 0:
				ArcadeUpgradeService.getInstance().startArcadeUpgrade(player);
			break;
			case 1:
				ArcadeUpgradeService.getInstance().closeWindow(player);
			break;
			case 2:
				ArcadeUpgradeService.getInstance().tryArcadeUpgrade(player);
			break;
			case 3:
				ArcadeUpgradeService.getInstance().getReward(player);
			break;
			case 4:
				player.getUpgradeArcade().setReTry(true);
				ArcadeUpgradeService.getInstance().tryArcadeUpgrade(player);
			break;
			case 5:
				ArcadeUpgradeService.getInstance().showRewardList(player);
			break;
			default:
			break;
		}
	}
}