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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ChargeInfo;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.*;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemAddType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.RndArray;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.google.common.base.Preconditions;
import com.google.common.collect.Collections2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * @author KID
 */
public class ItemService {

	private static final Logger log = LoggerFactory.getLogger("ITEM_LOG");

	public static final ItemUpdatePredicate DEFAULT_UPDATE_PREDICATE = new ItemUpdatePredicate(ItemAddType.ITEM_COLLECT, ItemUpdateType.INC_ITEM_COLLECT);

	public static void loadItemStones(Collection<Item> itemList) {
		if (itemList != null && itemList.size() > 0) {
			DAOManager.getDAO(ItemStoneListDAO.class).load(itemList);
		}
	}

	public static long addItem(Player player, int itemId, long count) {
		return addItem(player, itemId, count, DEFAULT_UPDATE_PREDICATE);
	}

	public static long addItem(Player player, int itemId, long count, ItemUpdatePredicate predicate) {
		return addItem(player, itemId, count, null, predicate, 0, false);
	}

	/**
	 * Add new item based on all sourceItem values
	 */
	public static long addItem(Player player, Item sourceItem) {
		return addItem(player, sourceItem.getItemId(), sourceItem.getItemCount(), sourceItem, DEFAULT_UPDATE_PREDICATE, 0, false);
	}

	public static long addItem(Player player, Item sourceItem, ItemUpdatePredicate predicate) {
		return addItem(player, sourceItem.getItemId(), sourceItem.getItemCount(), sourceItem, predicate, 0, false);
	}

	public static long addItem(Player player, int itemId, long count, Item sourceItem) {
		return addItem(player, itemId, count, sourceItem, DEFAULT_UPDATE_PREDICATE, 0, false);
	}

	public static long addItemAndEnchant(Player player, int itemId, long count, int enchantLevel, ItemUpdatePredicate predicate) {
		return addItem(player, itemId, count, null, predicate, enchantLevel, false);
	}

	public static long addItemAndEnchant(Player player, int itemId, long count, int enchantLevel) {
		return addItem(player, itemId, count,null, DEFAULT_UPDATE_PREDICATE, enchantLevel, false);
	}

	public static long addItemAndEnchant(Player player, int itemId, long count, int enchantLevel, boolean augment) {
		return addItem(player, itemId, count,null, DEFAULT_UPDATE_PREDICATE, enchantLevel, augment);
	}

	/**
	 * Add new item based on sourceItem values
	 */
	public static long addItem(Player player, int itemId, long count, Item sourceItem, ItemUpdatePredicate predicate, int enchantLevel, boolean augment) {
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		if (count <= 0 || itemTemplate == null) {
			return 0;
		}
		Preconditions.checkNotNull(itemTemplate, "No item with id " + itemId);
		Preconditions.checkNotNull(predicate, "Predicate is not supplied");
		if (LoggingConfig.LOG_ITEM) {
			log.info("[ITEM] ID/Count"
			+ (LoggingConfig.ENABLE_ADVANCED_LOGGING ? "/Item Name - " + itemTemplate.getTemplateId() + "/" + count + "/"
			+ itemTemplate.getName() : " - " + itemTemplate.getTemplateId() + "/" + count) + " to player " + player.getName());
		}
		Storage inventory = player.getInventory();
		if (itemTemplate.isKinah()) {
			inventory.increaseKinah(count);
			return 0;
		} if (itemTemplate.isStackable()) {
			count = addStackableItem(player, itemTemplate, count, predicate);
		} else {
			count = addNonStackableItem(player, itemTemplate, count, sourceItem, predicate, enchantLevel, augment);
		} if (inventory.isFull(itemTemplate.getExtraInventoryId()) && count > 0) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DICE_INVEN_ERROR);
		}
		return count;
	}

	/**
	 * Add non-stackable item to inventory
	 */
	private static long addNonStackableItem(Player player, ItemTemplate itemTemplate, long count, Item sourceItem, ItemUpdatePredicate predicate, int enchantlevel, boolean augment) {
		Storage inventory = player.getInventory();
		ItemCustomSetTeamplate itemCustomSet = DataManager.ITEM_CUSTOM_SET_DATA.getCustomTemplate(itemTemplate.getItemCustomSet());
		ItemSkillEnhance itemSkillEnhance = DataManager.ITEM_SKILL_ENHANCE_DATA.getSkillEnhance(itemTemplate.getSkillEnhance());
		while (!inventory.isFull(itemTemplate.getExtraInventoryId()) && count > 0) {
			Item newItem = ItemFactory.newItem(itemTemplate.getTemplateId());
			if (newItem.getExpireTime() != 0) {
				ExpireTimerTask.getInstance().addTask(newItem, player);
			} if (sourceItem != null) {
				copyItemInfo(sourceItem, newItem);
			} if (itemTemplate.getMaxEnchantBonus() != 0) {
				newItem.setEnchantBonus(Rnd.get(0, itemTemplate.getMaxEnchantBonus()));
			} if (enchantlevel > 0) {
				enchant(player, enchantlevel, newItem);
			} if(augment){
				chargeItem(player, newItem, 2);
			} if (itemTemplate.getItemCustomSet() != 0) {
				enchant(player, itemCustomSet.getCustomEnchantValue(), newItem);
			} if(itemTemplate.getSkillEnhance() != 0) {
				int skillId = RndArray.get(itemSkillEnhance.getSkillId());
				newItem.setEnhanceSkillId(skillId);
				newItem.setEnhanceEnchantLevel(1);
				newItem.setIsEnhance(true);
			}
			predicate.changeItem(newItem);
			inventory.add(newItem);
			count--;
		}
		return count;
	}

	public static void chargeItem(Player player, Item item, int level) {
		Improvement improvement = item.getImprovement();
		if (improvement == null) {
			return;
		}
		int chargeWay = improvement.getChargeWay();
		int currentCharge = item.getChargePoints();
		switch (level) {
			case 1:
				item.getConditioningInfo().updateChargePoints(ChargeInfo.LEVEL1 - currentCharge);
			break;
			case 2:
				item.getConditioningInfo().updateChargePoints(ChargeInfo.LEVEL2 - currentCharge);
			break;
		}
		if (item.isEquipped()) {
			player.getGameStats().updateStatsVisually();
		}
		ItemPacketService.updateItemAfterInfoChange(player, item);
	}

	/**
	 * Copy some item values like item stones and enchange level
	 */
	private static void copyItemInfo(Item sourceItem, Item newItem) {
		newItem.setOptionalSocket(sourceItem.getOptionalSocket());
		newItem.setEnchantBonus(sourceItem.getEnchantBonus());
		if (sourceItem.hasManaStones()) {
			for (ManaStone manaStone : sourceItem.getItemStones()) {
				ItemSocketService.addManaStone(newItem, manaStone.getItemId());
			}
		} 
		if (sourceItem.getGodStone() != null) {
			newItem.addGodStone(sourceItem.getGodStone().getItemId());
		} 
		if (sourceItem.getEnchantLevel() > 0) {
			newItem.setEnchantLevel(sourceItem.getEnchantLevel());
		} 
		if (sourceItem.getEnchantBonus() > 0) {
			newItem.setEnchantBonus(sourceItem.getEnchantBonus());
		}
		if (sourceItem.isSoulBound()) {
			newItem.setSoulBound(true);
		}
		newItem.setBonusNumber(sourceItem.getBonusNumber());
		newItem.setRandomStats(sourceItem.getRandomStats());
		newItem.setIdianStone(sourceItem.getIdianStone());
		newItem.setRandomCount(sourceItem.getRandomCount());
		newItem.setItemColor(sourceItem.getItemColor());
		newItem.setItemSkinTemplate(sourceItem.getItemSkinTemplate());
		newItem.setIsEnhance(sourceItem.isEnhance());
		newItem.setEnhanceEnchantLevel(sourceItem.getEnhanceEnchantLevel());
		newItem.setEnhanceSkillId(sourceItem.getEnhanceSkillId());
	}

	/**
	 * Add stackable item to inventory
	 */
	private static long addStackableItem(Player player, ItemTemplate itemTemplate, long count, ItemUpdatePredicate predicate) {
		Storage inventory = player.getInventory();
		Collection<Item> items = inventory.getItemsByItemId(itemTemplate.getTemplateId());
		for (Item item : items) {
			if (count == 0) {
				break;
			}
			count = inventory.increaseItemCount(item, count, predicate.getUpdateType(item, true));
		} if (itemTemplate.getArmorType() == ArmorType.SHARD) {
			Equipment equipement = player.getEquipment();
			items = equipement.getEquippedItemsByItemId(itemTemplate.getTemplateId());
			for (Item item : items) {
				if (count == 0) {
					break;
				}
				count = equipement.increaseEquippedItemCount(item, count);
			}
		}

		while (!inventory.isFull(itemTemplate.getExtraInventoryId()) && count > 0) {
			Item newItem = ItemFactory.newItem(itemTemplate.getTemplateId(), count);
			count -= newItem.getItemCount();
			inventory.add(newItem);
		}
		return count;
	}
	
	public static boolean addQuestItems(Player player, List<QuestItems> questItems) {
		return addQuestItems(player, questItems, DEFAULT_UPDATE_PREDICATE);
	}

	public static boolean addQuestItems(Player player, List<QuestItems> questItems, ItemUpdatePredicate predicate) {
		int slotReq = 0, specialSlot = 0;

		for (QuestItems qi : questItems) {
			if (qi.getItemId() != ItemId.KINAH.value() && qi.getCount() != 0) {
				ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(qi.getItemId());
				long stackCount = template.getMaxStackCount();
				long count = qi.getCount() / stackCount;
				if (qi.getCount() % stackCount != 0)
					count++;
				if (template.getExtraInventoryId() > 0) {
					specialSlot += count;
				}
				else {
					slotReq += count;
				}
			}
		}
		Storage inventory = player.getInventory();
		if (slotReq > 0 && inventory.getFreeSlots() < slotReq) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
			return false;
		}
		if (specialSlot > 0 && inventory.getSpecialCubeFreeSlots() < specialSlot) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
			return false;
		}
		for (QuestItems qi : questItems) {
			addItem(player, qi.getItemId(), qi.getCount(), predicate);
		}
		return true;
	}

	public static void releaseItemId(Item item) {
		IDFactory.getInstance().releaseId(item.getObjectId());
	}

	public static void releaseItemIds(Collection<Item> items) {
		Collection<Integer> idIterator = Collections2.transform(items, AionObject.OBJECT_TO_ID_TRANSFORMER);
		IDFactory.getInstance().releaseIds(idIterator);
	}

	public static boolean dropItemToInventory(int playerObjectId, int itemId) {
		return dropItemToInventory(World.getInstance().findPlayer(playerObjectId), itemId);
	}

	public static boolean dropItemToInventory(Player player, int itemId) {
		if (player == null || !player.isOnline()) {
			return false;
		}
		Storage storage = player.getInventory();
		if (storage.getFreeSlots() < 1) {
			List<Item> items = storage.getItemsByItemId(itemId);
			boolean hasFreeStack = false;
			for (Item item : items) {
				if (item.getPersistentState() == PersistentState.DELETED || item.getItemCount() < item.getItemTemplate().getMaxStackCount()) {
					hasFreeStack = true;
					break;
				}
			} if (!hasFreeStack) {
				return false;
			}
		}
		return addItem(player, itemId, 1) == 0;
	}
	
	public static Item newItem(int itemId, long count, String crafterName, int ownerId, long tempItemTime, int tempTradeTime) {
        ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);

        if (itemTemplate == null) {
            log.error("Item was not populated correctly. Item template is missing for item id: " + itemId);
            return null;
        }

        // Outside expire time has higher priority
        if (tempItemTime <= 0) {
            tempItemTime = itemTemplate.getExpireTime() * 60;
        }

        if (tempTradeTime <= 0) {
            tempTradeTime = itemTemplate.getTempExchangeTime() * 60;
        }

        int maxStackCount = (int) itemTemplate.getMaxStackCount();

        if ((count > maxStackCount) && (maxStackCount != 0) && !itemTemplate.isKinah()) {
            count = maxStackCount;
        }

        // TODO if Item object will contain ownerId - item can be saved to DB before return
        Item temp = new Item(IDFactory.getInstance().nextId(), itemTemplate, count, false, 0);

        if (itemTemplate.isWeapon() || itemTemplate.isArmor()) {
            temp.setOptionalSocket(Rnd.get(0, itemTemplate.getOptionSlotBonus()));
        }
        if (itemTemplate.getMaxEnchantBonus() != 0) {
			temp.setEnchantBonus(Rnd.get(0, itemTemplate.getMaxEnchantBonus()));
		}

        return temp;
    }
	
	public static boolean checkRandomTemplate(int randomItemId) {
		ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(randomItemId);
		return template != null;
	}
	
	public static class ItemUpdatePredicate {
		private final ItemUpdateType itemUpdateType;
		private final ItemAddType itemAddType;

		public ItemUpdatePredicate(ItemAddType itemAddType, ItemUpdateType itemUpdateType) {
			this.itemUpdateType = itemUpdateType;
			this.itemAddType = itemAddType;
		}

		public ItemUpdatePredicate() {
			this(ItemAddType.ITEM_COLLECT, ItemUpdateType.INC_ITEM_COLLECT);
		}

		public ItemUpdateType getUpdateType(Item item, boolean isIncrease) {
			if (item.getItemTemplate().isKinah()) {
				return ItemUpdateType.getKinahUpdateTypeFromAddType(itemAddType, isIncrease);
			}
			return itemUpdateType;
		}

		public ItemAddType getAddType() {
			return itemAddType;
		}

		public boolean changeItem(Item item) {
			return true;
		}
	}
	
	public static void makeUpgradeItem(Item sourceItem, Item newItem) {
		if (sourceItem.hasManaStones()) {
			for (ManaStone manaStone : sourceItem.getItemStones()) {
				ItemSocketService.addManaStone(newItem, manaStone.getItemId());
			}
		} if (sourceItem.getGodStone() != null) {
			newItem.addGodStone(sourceItem.getGodStone().getItemId());
		} if (sourceItem.getEnchantLevel() > 0) {
			newItem.setEnchantLevel(sourceItem.getEnchantLevel() - 5);
		} if (sourceItem.getAuthorize() > 0 && sourceItem.getItemTemplate().isWeapon()) {
			newItem.setAuthorize(sourceItem.getAuthorize() - 5);
		} if (sourceItem.getAuthorize() > 0 && sourceItem.getItemTemplate().isPlume()) {
			newItem.setAuthorize(0);
		} if (sourceItem.isSoulBound()) {
			newItem.setSoulBound(true);
		} if (sourceItem.isAmplified()) {
			newItem.setEnchantLevel(sourceItem.getItemTemplate().getMaxEnchantLevel() + sourceItem.getItemTemplate().getMaxEnchantBonus());
			newItem.setAmplification(false);
		}
		newItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	private static void enchant(Player player, int enchant, Item item) {
		if (isUpgradable(item)) {
			if (item.getEnchantLevel() == enchant) {
				return;
			} if (enchant > 25) {
				enchant = 25;
			} if (enchant < 0) {
				enchant = 0;
			}
			item.setEnchantLevel(enchant);
			if (item.isEquipped()) {
				player.getGameStats().updateStatsVisually();
			}
			ItemPacketService.updateItemAfterInfoChange(player, item);
		}
	}
	
	public static boolean isUpgradable(Item item) {
		if (item.getItemTemplate().isNoEnchant() && !item.getItemTemplate().isStigma()) {
			return false;
		} if (item.getItemTemplate().isWeapon()) {
			return true;
		} if (item.getItemTemplate().isStigma()) {
			return true;
		} if (item.getItemTemplate().isArmor()) {
			int at = item.getItemTemplate().getItemSlot();
			if (at == 1 || /* Main Hand */
			    at == 2 || /* Sub Hand */
			    at == 8 || /* Jacket */
			    at == 16 || /* Gloves */
			    at == 32 || /* Boots */
			    at == 2048 || /* Shoulder */
			    at == 4096 || /* Pants */
				at == 32768 || /* Wing */
			    at == 131072 || /* Main Off Hand */
			    at == 262144) { /* Sub Off Hand */
				return true;
			}
		}
		return false;
	}
}