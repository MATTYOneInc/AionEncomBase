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

import java.util.Iterator;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MEGAPHONE_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author Ranastic
 */

public class CM_MEGAPHONE_MESSAGE extends AionClientPacket {
	private String chatMessage;
	private int itemObjectId;
	private boolean isAll = false;

	public CM_MEGAPHONE_MESSAGE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		chatMessage = readS();
		itemObjectId = readD();
	}

	@Override
	protected void runImpl() {
		final Player activePlayer = getConnection().getActivePlayer();
		if (activePlayer == null) {
			return;
		}
		Item item = activePlayer.getInventory().getItemByObjId(itemObjectId);
		if (item == null) {
			return;
		}
		if ((item.getItemId() >= 188910000) && (item.getItemId() <= 188910009)) {
			this.isAll = true;
		}
		if ((item.getItemId() >= 188930000) && (item.getItemId() <= 188930008)) {
			this.isAll = true;
		}
		boolean deleteItem = activePlayer.getInventory().decreaseByObjectId(this.itemObjectId, 1);
		if (!deleteItem) {
			return;
		}
		Iterator<Player> players = World.getInstance().getPlayersIterator();
		while (players.hasNext()) {
			Player player = (Player) players.next();
			if (this.isAll) {
				PacketSendUtility.sendPacket(player,
						new SM_MEGAPHONE_MESSAGE(activePlayer, this.chatMessage, item.getItemId(), this.isAll));
			} else if (activePlayer.getRace() == player.getRace()) {
				PacketSendUtility.sendPacket(player,
						new SM_MEGAPHONE_MESSAGE(activePlayer, this.chatMessage, item.getItemId(), this.isAll));
			}
		}
	}
}