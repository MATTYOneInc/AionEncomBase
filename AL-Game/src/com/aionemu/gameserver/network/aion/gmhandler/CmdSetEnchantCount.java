/**
 * This file is part of Aion 5.8 Community Project.
 *
 *  Aion 5.8 Community Project is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion 5.8 Community Project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser Public License
 *  along with Aion 5.8 Community Project.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * @author Angry Catster
 */
public class CmdSetEnchantCount extends AbstractGMHandler {

	public CmdSetEnchantCount(Player admin, String params) {
		super(admin, params);
		run();
	}

	private void run() {
		String[] p = params.split(" ");
		Integer objid = Integer.parseInt(p[0]);
		Integer enchlvl = Integer.parseInt(p[1]);
		Integer authorizelvl = Integer.parseInt(p[2]);
		
		Storage inventory = admin.getInventory();
		Equipment equip = admin.getEquipment();
		
		Item targetItem = inventory.getItemByObjId(objid);
		if (targetItem==null)
			targetItem = equip.getEquippedItemByObjId(objid);
		if ((targetItem != null)){
			if ((targetItem.getItemTemplate().getMaxEnchantLevel()!=0) && (targetItem.getEnchantLevel()<=254))
				targetItem.setEnchantLevel(targetItem.getEnchantLevel() + enchlvl);
			else if ((targetItem.getItemTemplate().getMaxAuthorize()!=0))
				targetItem.setAuthorize(targetItem.getAuthorize() + authorizelvl);

			equip.setPersistentState(PersistentState.UPDATE_REQUIRED);
			inventory.setPersistentState(PersistentState.UPDATE_REQUIRED);
			admin.getGameStats().updateStatsAndSpeedVisually();
			ItemPacketService.updateItemAfterInfoChange(admin, targetItem);
		}
	}
}