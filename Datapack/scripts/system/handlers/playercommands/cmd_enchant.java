/*
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
package com.aionemu.gameserver;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Tago modified by Wakizashi and Ney, Maestros, Eloann
 */
public class cmd_enchant extends PlayerCommand {

	public cmd_enchant() {
		super("enchant");
	}

    @Override
    public void execute(Player player, String... params) {
        int enchant = 0;
 
        try {
            enchant = params[0] == null ? enchant : Integer.parseInt(params[0]);
        } catch (Exception ex) {
            onFail(player, "Fail");
            return;
        }
        if(enchant <= 15){
            enchant(player, enchant);
        } else{
            PacketSendUtility.sendMessage(player, "You cannot enchant higher than +15!");
        }
    }

	private void enchant(Player player, int enchant) {
		for (Item targetItem : player.getEquipment().getEquippedItemsWithoutStigma()) {
			if (isUpgradeble(targetItem)) {
				int enchantLevel = 15, maxEnchant = targetItem.getItemTemplate().getMaxEnchantLevel();
				if (maxEnchant > 0 && enchantLevel > maxEnchant) {
					enchantLevel = maxEnchant;
				}

				targetItem.setEnchantLevel(enchantLevel);

				if (targetItem.isEquipped()) {
					player.getGameStats().updateStatsVisually();
				}
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
			}
		}
		PacketSendUtility.sendMessage(player, "All your items have been enchanted to: " + enchant);
	}

	/**
	 * Verify if the item is enchantble and/or socketble
	 */
	public static boolean isUpgradeble(Item item) {
		if (item.getItemTemplate().isNoEnchant()) {
			return false;
		}
		if (item.getItemTemplate().isWeapon()) {
			return true;
		}
		if (item.getItemTemplate().getCategory() == ItemCategory.STIGMA) {
			return false;
		}
		if (item.getEnchantLevel() == 15) {
			return false;
		}
		if (item.getItemTemplate().isArmor()) {
			int at = item.getItemTemplate().getItemSlot();
			if (at == 1 || /* Main Hand */at == 2 || /* Sub Hand */at == 8 || /* Jacket */at == 16 || /* Gloves */at == 32 || /* Boots */at == 2048 || /* Shoulder */at == 4096 || /* Pants */at == 131072
				|| /*
					 * Main Off Hand
					 */at == 262144) /*
									 * Sub Off Hand
									 */ {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "Syntax .enchant : \n" + "  Syntax .enchant <value>.\n" + "Info: This command all your enchanted items on <value>!" + " For example, would enchant all your items to 15 (eq 15.)");
	}
}