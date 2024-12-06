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
package com.aionemu.gameserver.services.item;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.stats.listeners.ItemEquipmentListener;
import com.aionemu.gameserver.model.templates.item.GodstoneInfo;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class ItemSocketService {
	private static final Logger log = LoggerFactory.getLogger(ItemSocketService.class);

	public static ManaStone addManaStone(Item item, int itemId) {
		if (item == null) {
			return null;
		}
		Set<ManaStone> manaStones = item.getItemStones();
		if (manaStones.size() >= item.getSockets(false)) {
			return null;
		}
		ItemCategory manastoneCategory = DataManager.ITEM_DATA.getItemTemplate(itemId).getCategory();
		int specialSlotCount = item.getItemTemplate().getSpecialSlots();
		if (manastoneCategory == ItemCategory.SPECIAL_MANASTONE && specialSlotCount == 0) {
			return null;
		}
		int specialSlotsOccupied = 0;
		int normalSlotsOccupied = specialSlotCount;
		int maxSlot = specialSlotCount;
		HashSet<Integer> allSlots = new HashSet<>();
		for (ManaStone ms : manaStones) {
			ItemCategory category = DataManager.ITEM_DATA.getItemTemplate(ms.getItemId()).getCategory();
			if (category == ItemCategory.SPECIAL_MANASTONE) {
				specialSlotsOccupied++;
			}
			if (category == ItemCategory.MANASTONE) {
				normalSlotsOccupied++;
			}
			allSlots.add(ms.getSlot());
			if (maxSlot < ms.getSlot()) {
				maxSlot = ms.getSlot();
			}
		}
		if (specialSlotsOccupied >= specialSlotCount && manastoneCategory == ItemCategory.SPECIAL_MANASTONE) {
			return null;
		}
		int start = manastoneCategory == ItemCategory.SPECIAL_MANASTONE ? 0 : specialSlotCount;
		int end = manastoneCategory == ItemCategory.SPECIAL_MANASTONE ? specialSlotCount : manaStones.size();
		int nextSlot = start;
		boolean slotFound = false;
		for (; nextSlot < end; nextSlot++) {
			if (!allSlots.contains(nextSlot)) {
				slotFound = true;
				break;
			}
		}
		if (!slotFound) {
			if (specialSlotCount == 0 && manastoneCategory == ItemCategory.MANASTONE) {
				nextSlot = manaStones.size();
			}
			if (specialSlotCount > 0 && manastoneCategory == ItemCategory.SPECIAL_MANASTONE) {
				nextSlot = manaStones.size();
			}
			if (specialSlotCount > 0 && manastoneCategory == ItemCategory.MANASTONE) {
				nextSlot = normalSlotsOccupied;
			}
		}
		if (nextSlot >= item.getSockets(false)) {
			return null;
		}
		ManaStone stone = new ManaStone(item.getObjectId(), itemId, nextSlot, PersistentState.NEW);
		manaStones.add(stone);
		return stone;
	}

	public static ManaStone addManaStone(Item item, int itemId, int slotId) {
		if (item == null) {
			return null;
		}
		Set<ManaStone> manaStones = item.getItemStones();
		if (manaStones.size() >= Item.MAX_BASIC_STONES) {
			return null;
		}
		ManaStone stone = new ManaStone(item.getObjectId(), itemId, slotId, PersistentState.NEW);
		manaStones.add(stone);
		return stone;
	}

	public static void copyManaStones(Item source, Item target) {
		if (source.hasManaStones()) {
			for (ManaStone manaStone : source.getItemStones()) {
				target.getItemStones().add(new ManaStone(target.getObjectId(), manaStone.getItemId(),
						manaStone.getSlot(), PersistentState.NEW));
			}
			for (ManaStone manaStone : source.getFusionStones()) {
				target.getFusionStones().add(new ManaStone(target.getObjectId(), manaStone.getItemId(),
						manaStone.getSlot(), PersistentState.NEW));
			}
		}
	}

	public static void copyFusionStones(Item source, Item target) {
		if (source.hasManaStones()) {
			for (ManaStone manaStone : source.getItemStones()) {
				target.getFusionStones().add(new ManaStone(target.getObjectId(), manaStone.getItemId(),
						manaStone.getSlot(), PersistentState.NEW));
			}
		}
	}

	public static ManaStone addFusionStone(Item item, int itemId) {
		if (item == null) {
			return null;
		}
		Set<ManaStone> manaStones = item.getFusionStones();
		if (manaStones.size() >= item.getSockets(true)) {
			return null;
		}
		ItemCategory manastoneCategory = DataManager.ITEM_DATA.getItemTemplate(itemId).getCategory();
		int specialSlotCount = item.getFusionedItemTemplate().getSpecialSlots();
		if (manastoneCategory == ItemCategory.SPECIAL_MANASTONE && specialSlotCount == 0) {
			return null;
		}
		int specialSlotsOccupied = 0;
		int normalSlotsOccupied = specialSlotCount;
		int maxSlot = specialSlotCount;
		HashSet<Integer> allSlots = new HashSet<>();
		for (ManaStone ms : manaStones) {
			ItemCategory category = DataManager.ITEM_DATA.getItemTemplate(ms.getItemId()).getCategory();
			if (category == ItemCategory.SPECIAL_MANASTONE) {
				specialSlotsOccupied++;
			}
			if (category == ItemCategory.MANASTONE) {
				normalSlotsOccupied++;
			}
			allSlots.add(ms.getSlot());
			if (maxSlot < ms.getSlot()) {
				maxSlot = ms.getSlot();
			}
		}
		if (specialSlotsOccupied >= specialSlotCount && manastoneCategory == ItemCategory.SPECIAL_MANASTONE) {
			return null;
		}
		int start = manastoneCategory == ItemCategory.SPECIAL_MANASTONE ? 0 : specialSlotCount;
		int end = manastoneCategory == ItemCategory.SPECIAL_MANASTONE ? specialSlotCount : manaStones.size();
		int nextSlot = start;
		boolean slotFound = false;
		for (; nextSlot < end; nextSlot++) {
			if (!allSlots.contains(nextSlot)) {
				slotFound = true;
				break;
			}
		}
		if (!slotFound) {
			if (specialSlotCount == 0 && manastoneCategory == ItemCategory.MANASTONE) {
				nextSlot = manaStones.size();
			}
			if (specialSlotCount > 0 && manastoneCategory == ItemCategory.SPECIAL_MANASTONE) {
				nextSlot = specialSlotsOccupied;
			}
			if (specialSlotCount > 0 && manastoneCategory == ItemCategory.MANASTONE) {
				nextSlot = normalSlotsOccupied;
			}
		}
		if (nextSlot >= item.getSockets(true)) {
			return null;
		}
		ManaStone stone = new ManaStone(item.getObjectId(), itemId, nextSlot, PersistentState.NEW);
		manaStones.add(stone);
		Set<ManaStone> itemStones = item.getFusionStones();
		DAOManager.getDAO(ItemStoneListDAO.class).storeFusionStones(itemStones);
		return stone;
	}

	public static ManaStone addFusionStone(Item item, int itemId, int slotId) {
		if (item == null) {
			return null;
		}
		Set<ManaStone> fusionStones = item.getFusionStones();
		if (fusionStones.size() > item.getSockets(true)) {
			return null;
		}
		ManaStone stone = new ManaStone(item.getObjectId(), itemId, slotId, PersistentState.NEW);
		fusionStones.add(stone);
		return stone;
	}

	public static void removeManastone(Player player, int itemObjId, int slotNum) {
		Storage inventory = player.getInventory();
		Item item = inventory.getItemByObjId(itemObjId);
		if (item == null) {
			item = player.getEquipment().getEquippedItemByObjId(itemObjId);
			if (item == null) {
				log.warn("Item not found during manastone remove");
				return;
			}
		}
		if (!item.hasManaStones()) {
			log.warn("Item stone list is empty");
			return;
		}
		Set<ManaStone> itemStones = item.getItemStones();
		int specialSlotCount = item.getItemTemplate().getSpecialSlots();
		for (ManaStone ms : itemStones) {
			if (item.isEquipped()) {
				ItemEquipmentListener.removeStoneStats1(item, ms, player.getGameStats());
			}

			if (ms.getSlot() == slotNum) {
				ms.setPersistentState(PersistentState.DELETED);
				DAOManager.getDAO(ItemStoneListDAO.class).storeManaStones(Collections.singleton(ms));
				itemStones.remove(ms);
				break;
			}
			if (ms.getSlot() > specialSlotCount) {
				ms.setPersistentState(PersistentState.DELETED);
				DAOManager.getDAO(ItemStoneListDAO.class).storeManaStones(Collections.singleton(ms));
				itemStones.remove(ms);
				break;
			}
			if (ms.getSlot() > slotNum && ms.getSlot() < specialSlotCount) {
				ms.setPersistentState(PersistentState.DELETED);
				DAOManager.getDAO(ItemStoneListDAO.class).storeManaStones(Collections.singleton(ms));
				itemStones.remove(ms);
				break;
			}
		}
		ItemPacketService.updateItemAfterInfoChange(player, item);
	}

	public static void removeFusionstone(Player player, int itemObjId, int slotNum) {
		Storage inventory = player.getInventory();
		Item item = inventory.getItemByObjId(itemObjId);
		if (item == null) {
			item = player.getEquipment().getEquippedItemByObjId(itemObjId);
			if (item == null) {
				log.warn("Item not found during manastone remove");
				return;
			}
		}
		if (!item.hasFusionStones()) {
			log.warn("Item stone list is empty");
			return;
		}
		Set<ManaStone> itemStones = item.getFusionStones();
		int specialSlotCount = item.getFusionedItemTemplate().getSpecialSlots();
		for (ManaStone ms : itemStones) {
			if (item.isEquipped()) {
				ItemEquipmentListener.removeStoneStats1(item, ms, player.getGameStats());
			}
			if (ms.getSlot() == slotNum) {
				ms.setPersistentState(PersistentState.DELETED);
				DAOManager.getDAO(ItemStoneListDAO.class).storeFusionStones(Collections.singleton(ms));
				itemStones.remove(ms);
				break;
			}
			if (ms.getSlot() > specialSlotCount) {
				ms.setPersistentState(PersistentState.DELETED);
				DAOManager.getDAO(ItemStoneListDAO.class).storeFusionStones(Collections.singleton(ms));
				itemStones.remove(ms);
				break;
			}
			if (ms.getSlot() > slotNum && ms.getSlot() < specialSlotCount) {
				ms.setPersistentState(PersistentState.DELETED);
				DAOManager.getDAO(ItemStoneListDAO.class).storeFusionStones(Collections.singleton(ms));
				itemStones.remove(ms);
				break;
			}
		}
		ItemPacketService.updateItemAfterInfoChange(player, item);
	}

	public static void removeAllManastone(Player player, Item item) {
		if (item == null) {
			log.warn("Item not found during manastone remove");
			return;
		}
		if (!item.hasManaStones()) {
			return;
		}
		Set<ManaStone> itemStones = item.getItemStones();
		for (ManaStone ms : itemStones) {
			ms.setPersistentState(PersistentState.DELETED);
		}
		DAOManager.getDAO(ItemStoneListDAO.class).storeManaStones(itemStones);
		itemStones.clear();
		ItemPacketService.updateItemAfterInfoChange(player, item);
	}

	public static void removeAllFusionStone(Player player, Item item) {
		if (item == null) {
			log.warn("Item not found during manastone remove");
			return;
		}
		if (!item.hasFusionStones()) {
			return;
		}
		Set<ManaStone> fusionStones = item.getFusionStones();
		for (ManaStone ms : fusionStones) {
			ms.setPersistentState(PersistentState.DELETED);
		}
		DAOManager.getDAO(ItemStoneListDAO.class).storeFusionStones(fusionStones);
		fusionStones.clear();
		ItemPacketService.updateItemAfterInfoChange(player, item);
	}

	public static void socketGodstone(final Player player, int weaponId, int stoneId) {
		final Item weaponItem = player.getInventory().getItemByObjId(weaponId);
		final Item godstone = player.getInventory().getItemByObjId(stoneId);
		final int godStoneItemId = godstone.getItemTemplate().getTemplateId();
		if (weaponItem == null) {
			PacketSendUtility.sendPacket(player,
					SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_PROC_CANNOT_GIVE_PROC_TO_EQUIPPED_ITEM);
			return;
		}
		if (!weaponItem.canSocketGodstone()) {
			PacketSendUtility.sendPacket(player,
					SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_PROC_NOT_ADD_PROC(new DescriptionId(weaponItem.getNameId())));
		}
		if (player.getInventory().getKinah() < getPriceByQuality(godstone)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
			return;
		}
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(godStoneItemId);
		GodstoneInfo godstoneInfo = itemTemplate.getGodstoneInfo();
		if (godstoneInfo == null) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_PROC_NO_PROC_GIVE_ITEM);
			log.warn("Godstone info missing for itemid " + godStoneItemId);
			return;
		}
		if (!player.getInventory().decreaseByObjectId(stoneId, 1)) {
			return;
		}
		player.getInventory().decreaseKinah(getPriceByQuality(godstone));
		player.getController().cancelUseItem();
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
				godstone.getObjectId(), godstone.getItemTemplate().getTemplateId(), 5000, 0, 0));
		final ItemUseObserver Enchant = new ItemUseObserver() {
			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(),
						weaponItem.getObjectId().intValue(), weaponItem.getItemTemplate().getTemplateId(), 0, 3, 0));
				ItemPacketService.updateItemAfterInfoChange(player, weaponItem);
				PacketSendUtility.sendPacket(player,
						SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_CANCELED(weaponItem.getItemTemplate().getNameId()));
			}
		};
		player.getObserveController().attach(Enchant);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(Enchant);
				weaponItem.addGodStone(godStoneItemId);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE
						.STR_GIVE_ITEM_PROC_ENCHANTED_TARGET_ITEM(new DescriptionId(weaponItem.getNameId())));
				ItemPacketService.updateItemAfterInfoChange(player, weaponItem);
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
						godstone.getObjectId(), godstone.getItemTemplate().getTemplateId(), 0, 1, 384));
			}
		}, 5000));
	}

	private static int getPriceByQuality(Item item) {
		int price = 0;
		switch (item.getItemTemplate().getItemQuality()) {
		case RARE:
			price = 842;
			break;
		case LEGEND:
			price = 2542;
			break;
		case UNIQUE:
			price = 7627;
			break;
		case EPIC:
			price = 22882;
			break;
		default:
			break;
		}
		return price;
	}

	@SuppressWarnings("null")
	public static void amplification(final Player player, int itemId, int toolUniqueId,
			final int enchantmentStoneObjectId) {
		final Item currentItem = player.getInventory().getItemByObjId(itemId);
		final Item toolItem = player.getInventory().getItemByObjId(toolUniqueId);
		final Item enchantStone = player.getInventory().getItemByObjId(enchantmentStoneObjectId);
		final int toolItemId = toolItem.getItemTemplate().getTemplateId();
		final int toolObjectId = toolItem.getObjectId();
		if (currentItem == null) {
			PacketSendUtility.sendPacket(player,
					SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_NO_TARGET_ITEM(new DescriptionId(currentItem.getNameId())));
			return;
		}
		if (currentItem.isEquipped()) {
			PacketSendUtility.sendMessage(player, "Can't use on equiped item!");
			return;
		}
		if (currentItem.isPacked()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_CANNOT_03);
			return;
		}
		if (currentItem.isAmplified() && enchantStone.getItemTemplate().isEnchantmentStone()) {
			PacketSendUtility.sendPacket(player,
					SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_ENCHANT_CANNOT_02(new DescriptionId(enchantStone.getNameId())));
			return;
		}
		if (currentItem != null & currentItem.isAmplified()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_ALREADY);
			return;
		}
		if (currentItem != null & currentItem.getEnchantLevel() < currentItem.getItemTemplate().getMaxEnchantLevel()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_CANNOT_02);
			return;
		}
		if (currentItem != null & !currentItem.canAmplification()) {
			PacketSendUtility.sendPacket(player,
					SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_CANNOT_01(new DescriptionId(currentItem.getNameId())));
			return;
		}
		PacketSendUtility.broadcastPacket(player,
				new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), toolObjectId, toolItemId, 5000, 0, 0),
				true);
		final ItemUseObserver observer = new ItemUseObserver() {
			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(toolItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.broadcastPacket(player,
						new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), toolObjectId, toolItemId, 0, 2, 0),
						true);
				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				PacketSendUtility.broadcastPacket(player,
						new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), toolObjectId, toolItemId, 0, 1, 1),
						true);
				if (!player.getInventory().decreaseByObjectId(toolObjectId, 1))
					return;
				if (!player.getInventory().decreaseByObjectId(enchantmentStoneObjectId, 1))
					return;
				currentItem.setAmplification(true);
				player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
				currentItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
				ItemPacketService.updateItemAfterInfoChange(player, currentItem);
				PacketSendUtility.sendPacket(player,
						SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_SUCCEED(new DescriptionId(currentItem.getNameId())));
			}
		}, 5000));
	}
}