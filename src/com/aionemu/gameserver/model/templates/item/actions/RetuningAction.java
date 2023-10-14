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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TUNE_RESULT;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Ranastic
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RetuningAction")
public class RetuningAction extends AbstractItemAction {
	@XmlAttribute
	UseTarget target;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (target.equals(UseTarget.WEAPON) && !targetItem.getItemTemplate().isWeapon()) {
			return false;
		}
		if (target.equals(UseTarget.ARMOR) && !targetItem.getItemTemplate().isArmor()) {
			return false;
		}
		return targetItem.getRandomCount() < targetItem.getItemTemplate().getRandomBonusCount()
				&& !targetItem.isEquipped();
	}

	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		final int parentItemId = parentItem.getItemId();
		final int parntObjectId = parentItem.getObjectId();
		final int nameId = parentItem.getNameId();
		PacketSendUtility.broadcastPacket(player,
				new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItemId, 3000, 0, 0),
				true);
		final ItemUseObserver observer = new ItemUseObserver() {
			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(nameId)));
				PacketSendUtility.broadcastPacket(player,
						new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parntObjectId, parentItemId, 0, 2, 0), true);
				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				PacketSendUtility.broadcastPacket(player,
						new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parntObjectId, parentItemId, 0, 1, 1), true);
				if (!player.getInventory().decreaseByObjectId(parntObjectId, 1)) {
					return;
				}
				int rndCount = targetItem.getRandomCount();
				if (rndCount >= targetItem.getItemTemplate().getRandomBonusCount() || targetItem.isEquipped()) {
					return;
				}
				targetItem.setRandomStats(null);
				targetItem.setBonusNumber(0);
				targetItem.setRandomCount(++rndCount);
				targetItem.setOptionalSocket(Rnd.get(0, targetItem.getItemTemplate().getOptionSlotBonus()));
				targetItem.setRndBonus();
				targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
				PacketSendUtility.sendPacket(player,
						new SM_TUNE_RESULT(player, targetItem.getObjectId(), parentItemId, targetItem.getItemId()));
				PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401639, new DescriptionId(nameId)));
			}
		}, 3000));
	}
}