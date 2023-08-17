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
package com.aionemu.gameserver.model.templates.item.actions;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.IdianStone;
import com.aionemu.gameserver.model.items.RandomBonusResult;
import com.aionemu.gameserver.model.templates.item.bonuses.StatBonusType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/****/
/** Author Rinzler (Encom)
/****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IdianAction")
public class IdianAction extends AbstractItemAction
{
	@XmlAttribute(name = "setId")
	protected int polishSetId;
	
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		int idianKinah = 68647;
		if (parentItem == null || targetItem == null) {
			//The item cannot be found.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		} if (parentItem.getItemTemplate().getLevel() > targetItem.getItemTemplate().getLevel()) {
			//You cannot socket Idian to the selected item. The Idian's level is too high.
	        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_POLISH_WRONG_LEVEL);
			return false;
		} if (targetItem.hasRetuning()) {
			//You need to tune your equipment before socketing Idian.
	        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_POLISH_NEED_IDENTIFY);
	        return false;
        } if (player.getInventory().getKinah() < idianKinah) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
			return false;
		} if (player.getInventory().getKinah() >= idianKinah) {
			player.getInventory().decreaseKinah(idianKinah);
		}
		return !player.isAttackMode() && targetItem.getItemTemplate().isWeapon() && targetItem.getItemTemplate().isCanIdian();
	}
	
	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem) {
		final int parentItemId = parentItem.getItemId();
		final int parntObjectId = parentItem.getObjectId();
		final int parentNameId = parentItem.getNameId();
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItemId, 3000, 0, 0), true);
		final ItemUseObserver observer = new ItemUseObserver() {
			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				//Canceled %0 Idian socketing.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_POLISH_CANCELED(targetItem.getNameId()));
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parntObjectId, parentItemId, 0, 2, 0), true);
				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parntObjectId, parentItemId, 0, 1, 1), true);
				if (!player.getInventory().decreaseByObjectId(parntObjectId, 1)) {
					return;
				}
				RandomBonusResult bonus = DataManager.ITEM_RANDOM_BONUSES.getRandomModifiers(StatBonusType.POLISH, polishSetId);
				if (bonus == null) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_FAILED(new DescriptionId(parentNameId)));
					return;
				}
				//%0's Idian is fully charged.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_POLISH_SUCCEED(targetItem.getItemTemplate().getNameId()));
				IdianStone idianStone = targetItem.getIdianStone();
				if (idianStone!= null) {
					idianStone.onUnEquip(player);
					targetItem.setIdianStone(null);
					idianStone.setPersistentState(PersistentState.DELETED);
					DAOManager.getDAO(ItemStoneListDAO.class).storeIdianStones(idianStone);
				}
				idianStone = new IdianStone(parentItemId, PersistentState.NEW, targetItem, bonus.getTemplateNumber(), 1000000);
				targetItem.setIdianStone(idianStone);
				if (targetItem.isEquipped()) {
					idianStone.onEquip(player);
				}
				PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
			}
		}, 3000));
	}
	
	public int getPolishSetId() {
		return polishSetId;
	}
}