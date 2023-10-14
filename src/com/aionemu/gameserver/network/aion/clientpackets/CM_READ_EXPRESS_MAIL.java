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

import java.util.concurrent.Future;

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.spawnengine.VisibleObjectSpawner;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class CM_READ_EXPRESS_MAIL extends AionClientPacket
{
	private int action;
	
	public CM_READ_EXPRESS_MAIL(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		this.action = readC();
	}
	
	@Override
	protected void runImpl() {
		final Player player = getConnection().getActivePlayer();
		boolean haveUnreadExpress = (player.getMailbox().haveUnreadByType(LetterType.EXPRESS) || player.getMailbox().haveUnreadByType(LetterType.BLACKCLOUD));
		switch (this.action) {
			case 0:
				if (player.getPostman() != null) {
					player.getPostman().getController().onDelete();
					player.setPostman(null);
				}
			break;
			case 1:
				if (player.getPostman() != null) {
					//An express courier has already arrived.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_POSTMAN_ALREADY_SUMMONED);
					return;
				} else if (player.isInPrison()) {
					//You cannot call a courier here.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_POSTMAN_UNABLE_POSITION);
					return;
				} else if (player.isFlying()) {
					//You cannot call a courier while flying.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_POSTMAN_UNABLE_IN_FLIGHT);
					return;
				} else if (player.getController().hasTask(TaskId.EXPRESS_MAIL_USE)) {
					//Please wait for a while before you call for the courier again.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_POSTMAN_UNABLE_IN_COOLTIME);
					return;
				} else if (haveUnreadExpress) {
					VisibleObjectSpawner.spawnPostman(player);
					Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
						}
					}, 600000);
					player.getController().addTask(TaskId.EXPRESS_MAIL_USE, task);
				}
			break;
		}
	}
}