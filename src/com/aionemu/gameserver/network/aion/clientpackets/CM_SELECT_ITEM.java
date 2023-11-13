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

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.DisassembleItem;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SELECT_ITEM_ADD;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Rework LightNing (ENCOM)
 */

public class CM_SELECT_ITEM extends AionClientPacket
{

	private int uniqueItemId;
	private int index;
	@SuppressWarnings("unused")
	private int unk;

	/**
	 * @param opcode
	 * @param state
	 * @param restStates
	 */
	public CM_SELECT_ITEM(int opcode, AionConnection.State state, AionConnection.State... restStates)
	{
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl()
	{
		this.uniqueItemId = readD();
		this.unk = readD();
		this.index = readC();

	}

	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		final Item item = player.getInventory().getItemByObjId(this.uniqueItemId);
		if (item == null) {
			return;
		}
		final int nameId = item.getNameId();
		sendPacket(new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), player.getObjectId(), uniqueItemId, item.getItemId(), 0, 1, 1));
		boolean delete = player.getInventory().decreaseByObjectId(uniqueItemId, 1L);
		if (delete) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300423, new DescriptionId(nameId)));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400452, new DescriptionId(nameId)));
			DisassembleItem selectitem = player.getDisassemblyItemLists().get(index);
			sendPacket(new SM_SELECT_ITEM_ADD(uniqueItemId, index));
			ItemService.addItem(player, selectitem.getItemId(), selectitem.getCount());
		}
	}
}
