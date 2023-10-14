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

import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_EXP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/** Author Rinzler (Encom)
/****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PurifierExpAction")
public class PurifierExpAction extends AbstractItemAction
{
	@XmlAttribute(name = "extraction_cost")
    protected int extractionCost;
	
    @XmlAttribute(name = "percent")
    protected boolean isPercent;
	
    @XmlAttribute(name = "extraction_reward")
    protected int extractionReward;
	
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem) {
		if (parentItem == null) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		} if (player.getCommonData().getExp() == 0) {
            return false;
        } if (player.getInventory().isFull()) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WAREHOUSE_FULL_INVENTORY);
			return false;
        }
		return true;
    }
	
	@Override
    public void act(final Player player, final Item parentItem, Item targetItem) {
        PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemTemplate().getTemplateId(), 3000, 0, 0));
        player.getController().cancelTask(TaskId.ITEM_USE);
        final ItemUseObserver observer = new ItemUseObserver() {
            public void abort() {
                player.getController().cancelTask(TaskId.ITEM_USE);
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_DECOMPOSE_ITEM_CANCELED(parentItem.getNameId()));
                PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemTemplate().getTemplateId(), 0, 2, 0));
                player.getObserveController().removeObserver(this);
            }
        };
        player.getObserveController().attach(observer);
        player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
            public void run() {
                player.getObserveController().removeObserver(observer);
                int expExtraction = 0;
                if (isPercent) {
                    expExtraction = (int) (player.getCommonData().getExpNeed() / 100) * extractionCost;
                } else {
                    expExtraction = extractionCost;
                }
                player.getCommonData().setExp(player.getCommonData().getExp() - expExtraction, false);
                ItemService.addItem(player, extractionReward, 1);
                player.getInventory().decreaseByItemId(parentItem.getItemId(), 1);
                PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemTemplate().getTemplateId(), 0, 1, 0));
            }
        }, 3000));
        PacketSendUtility.sendPacket(player, new SM_STATUPDATE_EXP(player.getCommonData().getExpShown(), player.getCommonData().getExpRecoverable(), player.getCommonData().getExpNeed(), player.getCommonData().getCurrentReposteEnergy(), player.getCommonData().getMaxReposteEnergy()));
    }
}