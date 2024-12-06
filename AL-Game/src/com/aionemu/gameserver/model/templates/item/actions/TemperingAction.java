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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.item.ItemSocketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/**
 * Author Ranastic (Encom) /
 * mod yayaya
 ****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TemperingAction")
public class TemperingAction extends AbstractItemAction {
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (targetItem.getItemTemplate().getMaxAuthorize() == 0) {
			return false;
		}
		
		// if you don't check your inventory and fill it, the plume won't be deleted because it's
        // impossible to drop into full inventory
        // To extract, there must be at least one free cell in the cube.
		if (player.getInventory().isFull()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330081));
			return false;
		}
		
		if (targetItem.getItemTemplate().isAccessory() && targetItem.getAuthorize() >= 15) {
			// %0 cannot be tempered anymore.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE
					.STR_MSG_ITEM_AUTHORIZE_CANT_MORE_AUTHORIZE(new DescriptionId(targetItem.getNameId())));
			return false;
		}
		if (targetItem.getItemTemplate().isPlume() && targetItem.getAuthorize() >= 18) {
			// %0 cannot be tempered anymore.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE
					.STR_MSG_ITEM_AUTHORIZE_CANT_MORE_AUTHORIZE(new DescriptionId(targetItem.getNameId())));
			return false;
		}
		if (targetItem.getItemTemplate().isBracelet() && targetItem.getAuthorize() >= 10) {
			// %0 cannot be tempered anymore.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE
					.STR_MSG_ITEM_AUTHORIZE_CANT_MORE_AUTHORIZE(new DescriptionId(targetItem.getNameId())));
			return false;
		}
		return targetItem.getAuthorize() < targetItem.getItemTemplate().getMaxAuthorize();
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		if (player.isAuthorizeBoost()) {
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
					parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 1500, 0, 0));
		} else {
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(),
					parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 3000, 0, 0));
		}
		final ItemUseObserver observer = new ItemUseObserver() {
			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(),
						parentItem.getObjectId().intValue(), parentItem.getItemTemplate().getTemplateId(), 0, 3, 0));
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
				// You have canceled the tempering of %0.
				PacketSendUtility.sendPacket(player,
						SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_CANCEL(targetItem.getNameId()));
			}
		};
		player.getObserveController().attach(observer);
		final boolean isTemperingSuccess = isTemperingSuccess(player, targetItem);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (player.getInventory().decreaseByItemId(parentItem.getItemId(), 1)) {
					if (!isTemperingSuccess) {
						PacketSendUtility.broadcastPacketAndReceive(player,
								new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(),
										player.getObjectId().intValue(), parentItem.getObjectId().intValue(),
										parentItem.getItemId(), 0, 2, 0));
						if (targetItem.getItemTemplate().isBracelet()) {
							targetItem.setAuthorize(0);
						} else if (targetItem.getItemTemplate().isPlume()) {
							targetItem.setAuthorize(targetItem.getAuthorize());
						}
						// New Tempering 5.8
						else if (parentItem.getItemId() == 166032001 && parentItem.getItemId() == 166032002) {
							targetItem.setAuthorize(targetItem.getAuthorize() - 1);
							// You failed to temper %0.
							PacketSendUtility.sendPacket(player,
									SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_FAILED_NO_PENALTY(targetItem.getNameId()));
							return;
						} else {
							targetItem.setAuthorize(0);
						}
						// Tempering of %0 has failed and the temperance level has decreased to 0.
						PacketSendUtility.sendPacket(player,
								SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_FAILED(targetItem.getNameId()));
					} else {
						PacketSendUtility.broadcastPacketAndReceive(player,
								new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(),
										player.getObjectId().intValue(), parentItem.getObjectId().intValue(),
										parentItem.getItemId(), 0, 1, 0));
						targetItem.setAuthorize(targetItem.getAuthorize() + 1);
						if (targetItem.getItemTemplate().isBracelet()) {
							checkTempering(player, targetItem);
						}
						// You have successfully tempered %0. +%num1 temperance level achieved.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE
								.STR_MSG_ITEM_AUTHORIZE_SUCCEEDED(targetItem.getNameId(), targetItem.getAuthorize()));
					}
					PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
					player.getObserveController().removeObserver(observer);
					if (targetItem.isEquipped()) {
						player.getGameStats().updateStatsVisually();
					}
					ItemPacketService.updateItemAfterInfoChange(player, targetItem);
					if (targetItem.isEquipped()) {
						player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
					} else {
						player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
					}
				}
			}
		}, 3000));
	}

	public void checkTempering(Player player, Item item) {
		if (item.getAuthorize() >= 5 && item.getAuthorize() <= 7) {
			item.setOptionalSocket(1);
		} else if (item.getAuthorize() >= 7 && item.getAuthorize() <= 10) {
			item.setOptionalSocket(2);
		} else if (item.getAuthorize() >= 10) {
			item.setOptionalSocket(3);
		} else {
			if (item.hasManaStones()) {
				ItemSocketService.removeAllManastone(player, item);
			}
			item.setOptionalSocket(0);
		}
	}

	public boolean isTemperingSuccess(Player player, Item item) {
		if (item.getItemTemplate().isBracelet()) {
			if (Rnd.get(1, 100) < EnchantsConfig.ENCHANT_BRACELET) {
				return true;
			} else {
				return false;
			}
		} else if (item.getItemTemplate().isPlume()) {
			if (Rnd.get(1, 100) < EnchantsConfig.ENCHANT_PLUME) {
				return true;
			} else {
				return false;
			}
		} else if (item.getItemTemplate().isAccessory() && !item.getItemTemplate().isPlume()
				|| !item.getItemTemplate().isBracelet()) {
			if (Rnd.get(1, 100) < EnchantsConfig.ENCHANT_ACCESSORY) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}