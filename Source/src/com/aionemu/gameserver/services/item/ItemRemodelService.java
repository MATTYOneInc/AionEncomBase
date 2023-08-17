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
package com.aionemu.gameserver.services.item;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPDATE_PLAYER_APPEARANCE;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class ItemRemodelService
{
	public static void remodelItem(Player player, int keepItemObjId, int extractItemObjId) {
		Storage inventory = player.getInventory();
		Item keepItem = inventory.getItemByObjId(keepItemObjId);
		Item extractItem = inventory.getItemByObjId(extractItemObjId);
		int remodelKinah = 14125;
		if (keepItem == null && extractItem == null) {
			return;
		} if (player.getLevel() < 10) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_PC_LEVEL_LIMIT);
			return;
		} if (player.getInventory().getKinah() < remodelKinah) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_NOT_ENOUGH_GOLD(new DescriptionId(keepItem.getItemTemplate().getNameId())));
			return;
		} if (extractItem == null) {
			if (keepItem.getItemTemplate() == keepItem.getItemSkinTemplate()) {
				PacketSendUtility.sendMessage(player, "That item does not have a remodeled skin to remove.");
				return;
			} if (player.getInventory().getKinah() >= remodelKinah) {
				player.getInventory().decreaseKinah(remodelKinah);
			}
			keepItem.setItemSkinTemplate(keepItem.getItemTemplate());
			if (!keepItem.getItemTemplate().isItemDyePermitted()) {
				keepItem.setItemColor(0);
			}
			ItemPacketService.updateItemAfterInfoChange(player, keepItem);
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_SUCCEED(new DescriptionId(keepItem.getItemTemplate().getNameId())));
			return;
		} if ((keepItem.getItemTemplate().getWeaponType() != extractItem.getItemSkinTemplate().getWeaponType())) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHANGE_ITEM_SKIN_NOT_COMPATIBLE(new DescriptionId(keepItem.getItemTemplate().getNameId()), new DescriptionId(extractItem.getItemSkinTemplate().getNameId())));
			return;
		} if (!keepItem.isRemodelable(player)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300478, new DescriptionId(keepItem.getItemTemplate().getNameId())));
			return;
		} if (!extractItem.isRemodelable(player)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300482, new DescriptionId(keepItem.getItemTemplate().getNameId())));
			return;
		} if (player.getInventory().getKinah() >= remodelKinah) {
			player.getInventory().decreaseKinah(remodelKinah);
		}
		player.getInventory().decreaseItemCount(extractItem, 1);
		keepItem.setItemSkinTemplate(extractItem.getItemSkinTemplate());
		keepItem.setItemColor(extractItem.getItemColor());
		keepItem.setItemSkinSkill(extractItem.getItemSkinSkill());
		ItemPacketService.updateItemAfterInfoChange(player, keepItem);
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300483, new DescriptionId(keepItem.getItemTemplate().getNameId())));
	}
	
	public static void systemRemodelItem(Player player, Item keepItem, ItemTemplate template) {
		if (keepItem.getItemSkinSkill() > 0) {
			SkillLearnService.removeSkill(player, keepItem.getItemSkinSkill());
		}
        keepItem.setItemSkinTemplate(template);
        keepItem.setItemSkinSkill(template.getSkinSkill());
		ItemPacketService.updateItemAfterInfoChange(player, keepItem);
        PacketSendUtility.sendPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), player.getEquipment().getEquippedForApparence()));
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300483, new DescriptionId(keepItem.getItemTemplate().getNameId())));
		if (keepItem.getItemSkinSkill() > 0) {
			player.getSkillList().addSkill(player, keepItem.getItemSkinSkill(), 1);
		} 
	}
	
	public static boolean commandViewRemodelItem(Player player, int itemId, int duration) {
        ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(itemId);
        if (template == null) {
            return false;
        }
        Equipment equip = player.getEquipment();
        if (equip == null) {
            return false;
        } for (Item item : equip.getEquippedItemsWithoutStigmaOld()) {
            if (item.getEquipmentSlot() == ItemSlot.MAIN_OFF_HAND.getSlotIdMask() || item.getEquipmentSlot() == ItemSlot.SUB_OFF_HAND.getSlotIdMask()) {
                continue;
            } if (item.getItemTemplate().isWeapon()) {
                if (item.getItemTemplate().getWeaponType() == template.getWeaponType() && item.getItemSkinTemplate().getTemplateId() != itemId) {
                    viewRemodelItem(player, item, template, duration);
                    return true;
                }
            } else if (item.getItemTemplate().isArmor()) {
                if (item.getItemTemplate().getItemSlot() == template.getItemSlot() && item.getItemSkinTemplate().getTemplateId() != itemId) {
                    viewRemodelItem(player, item, template, duration);
                    return true;
                }
            }
        }
        return false;
    }
	
	public static void viewRemodelItem(final Player player, final Item item, ItemTemplate template, int duration) {
        final ItemTemplate oldTemplate = item.getItemSkinTemplate();
        item.setItemSkinTemplate(template);
        PacketSendUtility.sendPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), player.getEquipment().getEquippedForApparence()));
        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300483, new DescriptionId(item.getItemTemplate().getNameId())));
        PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), player.getEquipment().getEquippedItemsWithoutStigma()), true);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                item.setItemSkinTemplate(oldTemplate);
            }
        }, 50);
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.sendPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), player.getEquipment().getEquippedForApparence()));
                PacketSendUtility.broadcastPacket(player, new SM_UPDATE_PLAYER_APPEARANCE(player.getObjectId(), player.getEquipment().getEquippedItemsWithoutStigma()), true);
            }
        }, duration * 1000);
    }
}