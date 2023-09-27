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
import com.aionemu.gameserver.model.templates.item.ItemSkillEnhance;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.RndArray;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * Created by wanke on 01/03/2017.
 */

public class EnhanceAction extends AbstractItemAction
{
    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        return true;
    }
	
    @Override
    public void act(final Player player, final Item parentItem, final Item targetItem) {
        final int parentItemId = parentItem.getItemId();
        final int parntObjectId = parentItem.getObjectId();
        final int parentNameId = parentItem.getNameId();
        final int nameId = targetItem.getNameId();
        final ItemSkillEnhance itemSkillEnhance = DataManager.ITEM_SKILL_ENHANCE_DATA.getSkillEnhance(targetItem.getItemTemplate().getSkillEnhance());
        final int skillId = RndArray.get(itemSkillEnhance.getSkillId());
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItemId, 3000, 0, 0), true);
        final ItemUseObserver observer = new ItemUseObserver() {
            @Override
            public void abort() {
                player.getController().cancelTask(TaskId.ITEM_USE);
                player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(parentNameId)));
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
                targetItem.setEnhanceEnchantLevel(targetItem.getEnhanceEnchantLevel() + 1);
                player.getSkillList().addSkill(player, targetItem.getEnhanceSkillId(), targetItem.getEnhanceEnchantLevel() + 1);
                PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
            }
        }, 3000));
    }
}