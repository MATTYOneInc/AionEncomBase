/**
 * This file is part of Encom.
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
package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

/**
 * @author Antraxx
 */
public final class CmdWishId extends AbstractGMHandler {

	public CmdWishId(Player admin, String params) {
		super(admin, params);
		run();
	}

	public void run() {
		Player t = target != null ? target : admin;

		String[] p = params.split(" ");
		if (p.length != 2) {
			PacketSendUtility.sendMessage(admin, "not enough parameters");
			return;
		}

		Integer qty = Integer.parseInt(p[0]);
		Integer itemId = Integer.parseInt(p[1]);
		ItemTemplate it = DataManager.ITEM_DATA.getItemTemplate(itemId);
		long count =0;
		if (qty > 0 && itemId > 0) {
			if (it == null) {
				PacketSendUtility.sendMessage(admin, "Item id is incorrect: " + itemId);
			} else {
				if (it.getMaxAuthorize()!=0)
					count = ItemService.addItemAndEnchant(t, it.getTemplateId(),1, qty);
				else if (it.isStackable())
					count = ItemService.addItem(t, itemId, qty);
				else 
					count = ItemService.addItem(t, itemId, 1);
				if (count == 0) {
					PacketSendUtility.sendMessage(admin,
							"You successfully gave " + qty + " x [item:" + itemId + "] to " + t.getName() + ".");
				} else {
					PacketSendUtility.sendMessage(admin, "Item couldn't be added");
				}
			}
		}
	}
}