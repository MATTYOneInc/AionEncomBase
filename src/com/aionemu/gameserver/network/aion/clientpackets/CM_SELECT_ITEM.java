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

import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.decomposable.SelectItem;
import com.aionemu.gameserver.model.templates.decomposable.SelectItems;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SELECT_ITEM_ADD;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Rework LightNing (ENCOM)
 */

public class CM_SELECT_ITEM extends AionClientPacket {

	private int uniqueItemId;
	private int index;
	@SuppressWarnings("unused")
	private int unk;

	/**
	 * @param opcode
	 * @param state
	 * @param restStates
	 */
	public CM_SELECT_ITEM(int opcode, AionConnection.State state, AionConnection.State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		this.uniqueItemId = readD();
		this.unk = readD();
		this.index = readC();

	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		Item item = player.getInventory().getItemByObjId(this.uniqueItemId);
		if (item == null) {
			return;
		}
		sendPacket(new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), player.getObjectId().intValue(), this.uniqueItemId, item.getItemId(), 0, 1, 1));
		boolean delete = player.getInventory().decreaseByObjectId(this.uniqueItemId, 1L);
		if (delete) {
			SelectItems selectitem = DataManager.DECOMPOSABLE_SELECT_ITEM_DATA.getSelectItem(player.getPlayerClass(), player.getRace(), item.getItemId());
			SelectItem st = selectitem.getItems().get(this.index);
			ItemService.addItem(player, st.getSelectItemId(), st.getCount());
			sendPacket(new SM_SELECT_ITEM_ADD(this.uniqueItemId, 0));
		}

	}

}
