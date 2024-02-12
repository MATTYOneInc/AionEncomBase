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
package com.aionemu.gameserver.model.templates.item.actions;

import java.util.Iterator;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnchantItemAction")
public class EnchantItemAction extends AbstractItemAction {
	@XmlAttribute(name = "count")
	private int count;

	@XmlAttribute(name = "min_level")
	private Integer min_level;

	@XmlAttribute(name = "max_level")
	private Integer max_level;

	@XmlAttribute(name = "manastone_only")
	private boolean manastone_only;

	@XmlAttribute(name = "chance")
	private float chance;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		int EnchantKinah = EnchantService.EnchantKinah(targetItem);
		if (isSupplementAction()) {
			return false;
		}
		if (targetItem.getItemTemplate().isEstima() && targetItem.getEnchantLevel() >= 10) {
			// You cannot add Essence.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GIVE_CP_ENCHANT_CANNOT);
			return false;
		}
		if (parentItem == null || targetItem == null) {
			// The item cannot be found.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		if (!targetItem.isAmplified() && parentItem.getItemTemplate().isEnchantmentStone()
				&& player.getInventory().getKinah() < EnchantKinah) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
			return false;
		}
		if (targetItem.isAmplified() && parentItem.getItemTemplate().isAmplificationStone()
				&& player.getInventory().getKinah() < EnchantKinah) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
			return false;
		}
		if (!player.isGM()) { // If the player is not a GM
    	if (targetItem.getEnchantLevel() >= 15 || (targetItem.getEnchantLevel() == 15 && targetItem.getItemTemplate().getArmorType() == ArmorType.WING)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_IT_CAN_NOT_BE_ENCHANTED_MORE_TIME(targetItem.getNameId()));
        	return false;
			}
		}
		else { // If the player is GM
		if (targetItem.getEnchantLevel() >= 255 || (targetItem.getEnchantLevel() == 255 && !parentItem.getItemTemplate().isManaStone() && !(targetItem.getItemTemplate().getArmorType() == ArmorType.WING))) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_IT_CAN_NOT_BE_ENCHANTED_MORE_TIME(targetItem.getNameId()));
        	return false;
			}
		}
		int msID = parentItem.getItemTemplate().getTemplateId() / 1000000;
		int tID = targetItem.getItemTemplate().getTemplateId() / 1000000;
		int wID = targetItem.getItemTemplate().getTemplateId() / 1000000;
		if ((msID != 167 && msID != 166) || tID >= 120 && wID != 187) {
			return false;
		}
		if ((targetItem.canAmplification()) && parentItem.getItemTemplate().isAmplificationStone()
				&& targetItem.getEnchantLevel() == targetItem.getItemTemplate().getMaxEnchantLevel()
				&& !targetItem.isAmplified()) {
			// %0 can be enchanted after amplification.
			PacketSendUtility.sendPacket(player,
					SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_ENCHANT_CANNOT_01(new DescriptionId(targetItem.getNameId())));
			return false;
		}
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		act(player, parentItem, targetItem, null, 1);
	}

	public void act(final Player player, final Item parentItem, final Item targetItem, final Item supplementItem,
			final int targetWeapon) {
		if ((supplementItem != null)
				&& (!checkSupplementLevel(player, supplementItem.getItemTemplate(), targetItem.getItemTemplate()))) {
			return;
		}
		if (player.getInventory().getKinah() < EnchantService.EnchantKinah(targetItem)) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
			return;
		}
		int enchantCast = 0;
		if (player.getGameStats().getStat(StatEnum.ENCHANT_BOOST, 0).getCurrent() != 0) {
			enchantCast = EnchantsConfig.ENCHANT_SPEED / 2 - (EnchantsConfig.ENCHANT_SPEED
					* player.getGameStats().getStat(StatEnum.ENCHANT_BOOST, 0).getCurrent() / 100);
		} else {
			enchantCast = EnchantsConfig.ENCHANT_SPEED;
		}
		final int currentEnchant = targetItem.getEnchantLevel();
		final boolean isSuccess = isSuccess(player, parentItem, targetItem, supplementItem, targetWeapon);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
				parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), enchantCast, 0, 0));
		final ItemUseObserver Enchant = new ItemUseObserver() {
			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(),
						targetItem.getObjectId().intValue(), targetItem.getItemTemplate().getTemplateId(), 0, 3, 0));
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
				// You have cancelled the enchanting of %0.
				PacketSendUtility.sendPacket(player,
						SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_CANCELED(targetItem.getItemTemplate().getNameId()));
			}
		};
		player.getObserveController().attach(Enchant);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(Enchant);
				ItemTemplate itemTemplate = parentItem.getItemTemplate();
				if (itemTemplate.isEnchantmentStone() || itemTemplate.isAmplificationStone()) {
					EnchantService.enchantItemAct(player, parentItem, targetItem, supplementItem, currentEnchant,
							isSuccess);
				} else if (targetItem.getItemTemplate().isCpStones()) {
					EnchantService.estimaEnchant(player, parentItem, targetItem);
				} else {
					EnchantService.socketManastoneAct(player, parentItem, targetItem, supplementItem, targetWeapon,
							isSuccess);
				}
				PacketSendUtility.broadcastPacketAndReceive(player,
						new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(),
								parentItem.getItemTemplate().getTemplateId(), 0, isSuccess ? 1 : 2, 384));
				if (CustomConfig.ENABLE_ENCHANT_ANNOUNCE) {
					if (itemTemplate.isEnchantmentStone() || itemTemplate.isAmplificationStone()) {
						Iterator<Player> iter = World.getInstance().getPlayersIterator();
						while (iter.hasNext()) {
							Player player2 = iter.next();
							if (targetItem.getEnchantLevel() == 15 && isSuccess) {
								if (player2.getRace() == player.getRace()) {
									// %0 has succeeded in enchanting %1 to Level 15.
									PacketSendUtility.sendPacket(player2,
											SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_ITEM_SUCCEEDED_15(player.getName(),
													targetItem.getItemTemplate().getNameId()));
								}
							}
							if (targetItem.getEnchantLevel() == 20 && isSuccess) {
								if (player2.getRace() == player.getRace()) {
									// %0 has succeeded in enchanting %1 to Level 20.
									PacketSendUtility.sendPacket(player2,
											SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_ITEM_SUCCEEDED_20(player.getName(),
													targetItem.getItemTemplate().getNameId()));
								}
							}
						}
					}
				}
			}
		}, enchantCast));
	}

	private boolean isSuccess(final Player player, final Item parentItem, final Item targetItem,
			final Item supplementItem, final int targetWeapon) {
		if (parentItem.getItemTemplate() != null) {
			ItemTemplate itemTemplate = parentItem.getItemTemplate();
			if (itemTemplate.isEnchantmentStone() || itemTemplate.isAmplificationStone()) {
				return EnchantService.enchantItem(player, parentItem, targetItem, supplementItem);
			}
			return EnchantService.socketManastone(player, parentItem, targetItem, supplementItem, targetWeapon);
		}
		return false;
	}

	public int getCount() {
		return count;
	}

	public int getMaxLevel() {
		return max_level != null ? max_level : 0;
	}

	public int getMinLevel() {
		return min_level != null ? min_level : 0;
	}

	public boolean isManastoneOnly() {
		return manastone_only;
	}

	public float getChance() {
		return chance;
	}

	boolean isSupplementAction() {
		return getMinLevel() > 0 || getMaxLevel() > 0 || getChance() > 0 || isManastoneOnly();
	}

	private boolean checkSupplementLevel(final Player player, final ItemTemplate supplementTemplate,
			final ItemTemplate targetItemTemplate) {
		if (supplementTemplate.getCategory() != ItemCategory.ENCHANTMENT) {
			int minEnchantLevel = targetItemTemplate.getLevel();
			int maxEnchantLevel = targetItemTemplate.getLevel();
			EnchantItemAction action = supplementTemplate.getActions().getEnchantAction();
			if (action != null) {
				if (action.getMinLevel() != 0) {
					minEnchantLevel = action.getMinLevel();
				}
				if (action.getMaxLevel() != 0) {
					maxEnchantLevel = action.getMaxLevel();
				}
			}
			if (minEnchantLevel <= targetItemTemplate.getLevel() && maxEnchantLevel >= targetItemTemplate.getLevel()) {
				return true;
			}
			// You cannot use those Supplements.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_ENCHANT_ASSISTANT_NO_RIGHT_ITEM);
			return false;
		}
		return true;
	}
}