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

import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class InstanceTimeClear extends AbstractItemAction
{
    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
    	return true;
    }
	
    @Override
    public void act(final Player player, final Item parentItem, Item targetItem) {
    }
	
    public void act(final Player player, final Item parentItem, final int SelectedSyncId) {
    	int mapid = DataManager.INSTANCE_COOLTIME_DATA.getWorldId(SelectedSyncId);
    	if (player.getPortalCooldownList().getPortalCooldown(mapid) == 0) {
    		player.getController().cancelTask(TaskId.ITEM_USE);
            player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
    		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_COOL_TIME_INIT);
    		return;
    	}
        PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 1000, 0, 0));
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
                if (parentItem.getActivationCount() > 1) {
                    parentItem.setActivationCount(parentItem.getActivationCount() - 1);
                } else {
                    player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1);
                }
                int mapid = DataManager.INSTANCE_COOLTIME_DATA.getWorldId(SelectedSyncId);
                player.getPortalCooldownList().reduceEntry(mapid);
                PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemId(), 0, 1, 0));
            }
        }, 1000));
    }
}