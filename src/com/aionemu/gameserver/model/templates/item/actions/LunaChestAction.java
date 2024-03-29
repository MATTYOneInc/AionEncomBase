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

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LUNA_SHOP_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class LunaChestAction extends AbstractItemAction {

	@XmlAttribute
	protected int count;

	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (parentItem == null) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		return true;
	}

	@Override
	public void act(final Player player, final Item parentItem, Item targetItem) {
		player.getController().cancelUseItem();
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 1000, 0, 0));
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {

			@Override
			public void run() {
				boolean succ = player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1);
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), 0, parentItem.getObjectId(), parentItem.getItemId(), 0, 1, 0));
				if (succ) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300423, new Object[] { new DescriptionId(parentItem.getItemTemplate().getNameId()) }));
					player.setLunaAccount(player.getLunaAccount() + count);
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GETLUNA(player.getName(), count));
					PacketSendUtility.sendPacket(player, new SM_LUNA_SHOP_LIST(0, player.getLunaAccount()));
				}
			}
		}, 1000));
	}
}