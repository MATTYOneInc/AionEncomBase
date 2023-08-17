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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemDeleteType;

/**
 * @author Ranastic
 */

public class CM_SELL_TERMINATED_ITEMS extends AionClientPacket
{
	private int size;
	private int itemObjId;
	private static final Logger log = LoggerFactory.getLogger(CM_SELL_TERMINATED_ITEMS.class);
	
	public CM_SELL_TERMINATED_ITEMS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		Player player = getConnection().getActivePlayer();
		if (player == null) {
			return;
		}
		Storage inventory = player.getInventory();
		size = readH();
		for (int i = 0; i < size; i++) {
			itemObjId = readD();
			Item item = inventory.getItemByObjId(itemObjId);
			inventory.delete(item, ItemDeleteType.DISCARD);
		}
		inventory.increaseKinah(size * 5000);
	}
	
	@Override
	protected void runImpl() {
	}
}