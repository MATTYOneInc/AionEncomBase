package com.aionemu.gameserver.model.templates.item.actions;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.item.DisassembleItem;
import com.aionemu.gameserver.model.templates.item.DisassembleItemGroups;
import com.aionemu.gameserver.model.templates.item.DisassembleItems;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SELECT_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SELECT_ITEM_ADD;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

/**
 * DisassemblyAction for Aion 2.4 classic (modified for 5.8)
 *
 * @author BeckUp.Media
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DisassemblyAction")
public class DisassemblyAction extends AbstractItemAction
{
	// we have an mode 0 (disassembly) and an mode 1 (disassembly select box)
	@XmlAttribute(name = "mode")
	public int mode;
	private static final Logger log = LoggerFactory.getLogger(DisassemblyAction.class);

	// we can set the speed over config (3000 makes probs)
	private static final int USAGE_DELAY = 2000;

	// we can activate the WhatsInsideTheBox methode
	private static final boolean DISASSEMBLY_DEBUG = true;

	/**
	 * ask if the Player can act with the Item
	 *
	 * @param player
	 * @param parentItem
	 * @param targetItem
	 * @return
	 */
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		// some items you only can open with level > 10 as examle
		int usageLevel = parentItem.getItemTemplate().getRequiredLevel(player.getCommonData().getPlayerClass());
		if (usageLevel > player.getLevel()) {
			// You cannot use %s until you reach level %d.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CANNOT_USE_ITEM_TOO_LOW_LEVEL_MUST_BE_THIS_LEVEL(parentItem.getNameId(), parentItem.getItemTemplate().getLevel()));
			return false;
		}
		// the itemsCollections of all stuff inside the disassembly
		List<DisassembleItemGroups> itemsCollections = DataManager.DISASSEMBLY_ITEMS_DATA.getInfoByItemId(parentItem.getItemId());
		// what if the item is empty, we send an msg to player
		if (itemsCollections == null || itemsCollections.isEmpty()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_INVALID_STANCE(parentItem.getNameId()));
			PacketSendUtility.sendMessage(player, "There is nothing inside, pls open a bug report for this Item: " + parentItem.getItemId());
			return false;
		}
		// player cant use disassembly in combat, attackMode(), maybe a bit hardcore - we can remove it later
		if (player.getController().isInCombat() || player.isAttackMode()) {
			// You cannot extract item while in combat.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_INVALID_STANCE(2800159));
			return false;
		}
		return true;
	}

	/**
	 * Start the ItemAction
	 *
	 * @param player
	 * @param parentItem
	 * @param targetItem
	 */
	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem)
	{
		// todo stream() all this stuff after all is working fine
		// we stop use an item.
		player.getController().cancelUseItem();
		// we get the disassembly item with all possible groups.
		List<DisassembleItemGroups> itemGroupCollection = DataManager.DISASSEMBLY_ITEMS_DATA.getInfoByItemId(parentItem.getItemId());
		// we exclude the min/max level, race and playerclass
		List<DisassembleItemGroups> itemGroupCollectionFiltered = filterGroupsByLevelRaceClass(player, itemGroupCollection);
		// now lets calc the chance for each group
		List<DisassembleItemGroups> finalGroupCollection = calculateGroupChance(itemGroupCollectionFiltered);
		// lets calculate the chance for the items
		final List<DisassembleItem> finalItemCollection;
		// we dont need an itemUseObserver on SelectBox, comes from C_SELECT_ITEM in 5.8 instant
		if (this.mode == 1) { // SelectBox
			// calc the chance (isSelect == true - we dont need to calc, we simply add all items here)
			finalItemCollection = calculateItemchance(player, finalGroupCollection, true);
			// we clear the List (safety mechanic)
			if (player.getDisassemblyItemLists().size() > 0)
				player.getDisassemblyItemLists().clear();
			// we set the List for this Player
			player.setDisassemblyItemLists(finalItemCollection);
			// we send the S packet with all Selectable Items
			PacketSendUtility.sendPacket(player, new SM_SELECT_ITEM(finalItemCollection, parentItem.getObjectId().intValue()));
			return;
		} else { // Normal DisassemblyBox
			// send the S_USE_ITEM packet
			PacketSendUtility.broadcastPacketAndReceive(player,
					new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), USAGE_DELAY,
							0, 0));
			// debug stuff here, it sends an msg if the max value not reach 10000
			if (DISASSEMBLY_DEBUG)
				checkWhatsInsideTheBox(itemGroupCollection, player, parentItem);
			// we calc the chance
			finalItemCollection = calculateItemchance(player, finalGroupCollection, false);
			// observer
			final ItemUseObserver observer = new ItemUseObserver()
			{
				// player abort the action
				@Override
				public void abort()
				{
					player.getController().cancelTask(TaskId.ITEM_USE);
					player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(parentItem.getItemTemplate().getNameId())));
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
							parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 2, 0), true);
					player.getObserveController().removeObserver(this);
				}
			};
			player.getObserveController().attach(observer);
			player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				// player submit the action
				@Override
				public void run()
				{
					player.getObserveController().removeObserver(observer);
					// we check if the action is valid
					boolean isValidAction = checkValidate(player, parentItem);
					if (isValidAction) {
						if (finalItemCollection.size() > 0) {
							for (DisassembleItem item : finalItemCollection) {
								ItemService.addItem(player, item.getItemId(), item.getCount());
							}
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_SUCCEED(parentItem.getNameId()));
						}
					}
					PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
							parentItem.getObjectId(), parentItem.getItemId(), 0, isValidAction ? 1 : 2, 0));
					player.getController().cancelTask(TaskId.ITEM_USE);
				}

				/**
				 * Check if the action is valid - canAct, calc inv slots, remove item from inv
				 *
				 * @param player
				 * @param parentItem
				 * @return
				 */
				boolean checkValidate(Player player, Item parentItem)
				{
					if (!canAct(player, parentItem, targetItem)) {
						return false;
					}
					Storage playerInventory = player.getInventory();
					int invSlotReq = calcUsedSlotsFromAction(finalItemCollection, false);
					int specialSlotreq = calcUsedSlotsFromAction(finalItemCollection, true);
					if (invSlotReq > 0 && playerInventory.getFreeSlots() < invSlotReq) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
						return false;
					}
					if (specialSlotreq > 0 && playerInventory.getSpecialCubeFreeSlots() < specialSlotreq) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DECOMPRESS_INVENTORY_IS_FULL);
						return false;
					}
					if (player.getLifeStats().isAlreadyDead() || !player.isSpawned()) {
						return false;
					}
					if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1)) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_NO_TARGET_ITEM);
						return false;
					}
					if (finalItemCollection.isEmpty()) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_FAILED(parentItem.getNameId()));
						return false;
					}
					return true;
				}
			}, USAGE_DELAY));
		}
	}

	/**
	 * We need to know how many slots it will take to open the item.
	 *
	 * @param finalItemCol
	 * @param specialCube
	 * @return
	 */
	private int calcUsedSlotsFromAction(List<DisassembleItem> finalItemCol, boolean specialCube)
	{
		int maxCount = 0;
		for (DisassembleItem item : finalItemCol) {
			ItemTemplate template = DataManager.ITEM_DATA.getItemTemplate(item.getItemId());
			if (specialCube && template.getExtraInventoryId() > 0) {
				maxCount++;
			} else if (template.getExtraInventoryId() < 1) {
				maxCount++;
			}
		}
		return maxCount;
	}

	/**
	 * We calc the itemChance, for SelectBox we simply add all items
	 *
	 * @param player
	 * @param finalGroups
	 * @return
	 */
	private List<DisassembleItem> calculateItemchance(Player player, List<DisassembleItemGroups> finalGroups, boolean isSelect)
	{
		List<DisassembleItem> newItemCollection = new ArrayList<DisassembleItem>();
		if (isSelect) {
			for (DisassembleItemGroups group : finalGroups) {
				List<DisassembleItems> itemList = group.getGroupItems();
				for (DisassembleItems item : itemList) {
					newItemCollection.add(item.getItem());
				}
			}
			return newItemCollection;
		} else {
			for (DisassembleItemGroups group : finalGroups) {
				List<DisassembleItems> itemList = group.getGroupItems();
				int currentSum = 0;
				int rnd;
				if (itemList.size() == 1 && itemList.get(0).getItemProb() == 10000)
					rnd = 1;
				else
					rnd = Rnd.get(0, 10000);
				for (DisassembleItems item : itemList) {
					currentSum += item.getItemProb();
					if (rnd < currentSum) {
						if (player.isGM())
							PacketSendUtility.sendMessage(player, String.format("Disassembly rnd: %d - hit current value: %d", rnd, currentSum));
						newItemCollection.add(item.getItem());
						break;
					}
				}
			}
			return newItemCollection;
		}
	}

	/**
	 * I use this now to check if an Value is higher or lower as 10000
	 *
	 * @param AllGroups
	 * @param player
	 * @param parentItem
	 */
	private void checkWhatsInsideTheBox(List<DisassembleItemGroups> AllGroups, Player player, Item parentItem)
	{
		int index = 0;
		for (DisassembleItemGroups group : AllGroups) {
			List<DisassembleItems> itemList = group.getGroupItems();
			int probMax = 0;
			for (DisassembleItems item : itemList) {
				probMax += item.getItemProb();
			}
			if (probMax < 10000) {
				PacketSendUtility.sendMessage(player, String.format(
						"Pls do a report. Something is wrong on %d - The Max Value is under 10000 on index %d with only a maxValue from %d"
						, parentItem.getItemId(), index, probMax));
				log.error(String.format(
						"Something is wrong on %d - The Max Value is under 10000 on index %d with only a maxValue from %d"
						, parentItem.getItemId(), index, probMax));
			} else if (probMax > 10000) {
				PacketSendUtility.sendMessage(player, String.format(
						"Pls do a report. Something is wrong on %d - The Max Value is above 10000 on index %d with only a maxValue from %d"
						, parentItem.getItemId(), index, probMax));
				log.error(String.format(
						"Something is wrong on %d - The Max Value is above 10000 on index %d with only a maxValue from %d"
						, parentItem.getItemId(), index, probMax));
			}
			index++;
		}
	}

	/**
	 * we have groups inside with a value below 1000, so we need to calc how many groups the player get
	 *
	 * @param filteredList
	 * @return
	 */
	private List<DisassembleItemGroups> calculateGroupChance(List<DisassembleItemGroups> filteredList)
	{
		List<DisassembleItemGroups> newCollection = new ArrayList<DisassembleItemGroups>();
		for (DisassembleItemGroups group : filteredList) {
			int rnd = Rnd.get(0, 1000);
			if (rnd < group.getGroupProb())
				newCollection.add(group);
		}
		return newCollection;
	}

	/**
	 * We need to check the level, race and playerclass and excludet the wrong groups
	 *
	 * @param player
	 * @param collection
	 * @return
	 */
	private List<DisassembleItemGroups> filterGroupsByLevelRaceClass(Player player, List<DisassembleItemGroups> collection)
	{
		int playerLevel = player.getLevel();
		Race playerRace = player.getRace();
		PlayerClass pClass = player.getPlayerClass();
		List<DisassembleItemGroups> newCollection = new ArrayList<DisassembleItemGroups>();
		for (DisassembleItemGroups group : collection) {
			if (group.getRace() != Race.PC_ALL && group.getRace() != playerRace)
				continue;
			if (group.getPlayerClassList() != null && !group.getPlayerClassList().contains(pClass))
				continue;
			if (group.getMinLevel() > playerLevel)
				continue;
			if (group.getMaxLevel() > 0 && group.getMaxLevel() < playerLevel)
				continue;
			newCollection.add(group);
		}
		return newCollection;
	}
}
