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

import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HEADING_UPDATE;
import com.aionemu.gameserver.services.DialogService;
import com.aionemu.gameserver.services.player.PlayerMailboxState;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class CM_CLOSE_DIALOG extends AionClientPacket
{
	private int targetObjectId;
	
	public CM_CLOSE_DIALOG(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		targetObjectId = readD();
	}
	
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		final VisibleObject obj = player.getKnownList().getObject(targetObjectId);
		final AionConnection client = getConnection();
		if(obj == null) {
			return;
		} if (obj instanceof Npc) {
			Npc npc = (Npc)obj;
			npc.getAi2().onCreatureEvent(AIEventType.DIALOG_FINISH, player);
			DialogService.onCloseDialog(npc, player);
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					client.sendPacket(new SM_HEADING_UPDATE(targetObjectId, (byte)obj.getHeading()));
				}
			}, 1200);
		} if (player.getMailbox().mailBoxState != 0) {
			player.getMailbox().mailBoxState = PlayerMailboxState.CLOSED;
		}
	}
}