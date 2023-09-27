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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.AssemblyItem;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AssemblyItemAction")
public class AssemblyItemAction extends AbstractItemAction
{
	@XmlAttribute
	private int item;
	
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		AssemblyItem assemblyItem = getAssemblyItem();
		if (assemblyItem == null) {
			return false;
		} for (Integer itemId: assemblyItem.getParts()) {
			if (player.getInventory().getFirstItemByItemId(itemId) == null) {
				return false;
			}
		}
		return true;
	}
	
	public static void removeItems(Player player, int itemId, long itemCount) {
		if (!player.getInventory().decreaseByItemId(itemId, itemCount)) {
		}
	}
	
	@Override
	public void act(final Player player, final Item parentItem, Item targetItem) {
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 3000, 0, 0), true);
		final ItemUseObserver observer = new ItemUseObserver() {
			@Override
			public void abort() {
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(parentItem.getItemTemplate().getNameId())));
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 2, 0), true);
				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				player.getObserveController().removeObserver(observer);
				player.getController().cancelTask(TaskId.ITEM_USE);
				AssemblyItem assemblyItem = getAssemblyItem();
				int itemType = 0;
				for (Integer itemId: assemblyItem.getParts()) {
					if (!player.getInventory().decreaseByItemId(itemId, assemblyItem.getPartsNum())) {
						return;
					}
					player.getInventory().decreaseByItemId(itemId, 1);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, 1, 0), true);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401122));
					if (assemblyItem.getProcAssembly() != 0) {
						if (Rnd.get(1, 100) < 15) {
							itemType = 2;
						} else {
							itemType = 1;
						}
					} else {
						itemType = 1;
					}
				} switch(itemType) {
					case 0:
					break;
					case 1:
						ItemService.addItem(player, assemblyItem.getId(), 1);
					break;
					case 2:
						ItemService.addItem(player, assemblyItem.getProcAssembly(), 1);
					break;
				}
			}
		}, 3000));
	}
	
	public AssemblyItem getAssemblyItem() {
		return DataManager.ASSEMBLY_ITEM_DATA.getAssemblyItem(item);
	}
}