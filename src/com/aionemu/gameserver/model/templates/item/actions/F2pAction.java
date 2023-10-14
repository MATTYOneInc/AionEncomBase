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

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.F2pService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/** Author Rinzler & Ranastic (Encom)
/****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "F2pAction")
public class F2pAction extends AbstractItemAction
{
	@XmlAttribute
	protected String pack;
	
	@XmlAttribute
	protected Integer minutes;
	
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (parentItem == null) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		} if (player.getF2p() != null && player.getF2p().getF2pAccount() != null && player.getF2p().getF2pAccount().getActive()) {
			PacketSendUtility.sendWarnMessageOnCenter(player, "You cannot accumulate 2 <Gold Pack> at the same time.");
			return false;
		}
		return true;
	}
	
	public void act(final Player player, final Item parentItem, Item targetItem) {
		player.getController().cancelUseItem();
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemTemplate().getTemplateId(), 1000, 0, 0));
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				boolean succ = player.getInventory().decreaseByObjectId(parentItem.getObjectId().intValue(), 1);
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemId(), 0, 1, 0));
				if (succ) {
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300423, new Object[] { new DescriptionId(parentItem.getItemTemplate().getNameId()) }));
					F2pService.getInstance().onAddF2p(player, minutes);
				}
			}
		}, 1000));
	}
}