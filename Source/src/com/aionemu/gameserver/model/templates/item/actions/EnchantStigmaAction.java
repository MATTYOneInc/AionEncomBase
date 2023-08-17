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

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.StigmaService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Wnkrz on 25/08/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EnchantStigmaAction")
public class EnchantStigmaAction extends AbstractItemAction
{
    @XmlAttribute(name = "count")
    private int count;
	
    @XmlAttribute(name = "min_level")
    private Integer min_level;
	
    @XmlAttribute(name = "max_level")
    private Integer max_level;
	
    @XmlAttribute(name = "stigma_only")
    private boolean stigma_only;
	
    @XmlAttribute(name = "chance")
    private float chance;
	
    @Override
    public boolean canAct(Player player, Item parentItem, Item targetItem) {
        if (parentItem == null || targetItem == null) {
            //The item cannot be found.
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
            return false;
        } if (targetItem.getEnchantLevel() >= 10) {
            //You cannot enchant %0 any further.
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_IT_CAN_NOT_BE_ENCHANTED_MORE_TIME(targetItem.getNameId()));
            return false;
        } if (targetItem.isEquipped()) {
            PacketSendUtility.sendBrightYellowMessageOnCenter(player, "You can not enchant a stigma stone equipped.");
            return false;
        } if (player.getInventory().getKinah() < getStigmaByQuality(parentItem)) {
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_NOT_ENOUGH_MONEY);
            return false;
        }
        return true;
    }
	
    @Override
    public void act(final Player player, final Item parentItem, final Item targetItem) {
        if (!canAct(player, parentItem, targetItem)) {
            return;
        }
        final boolean isSuccess = Rnd.chance(EnchantsConfig.ENCHANT_STIGMA);
        final int parentItemId = parentItem.getItemId();
        final int parntObjectId = parentItem.getObjectId();
        final int parentNameId = parentItem.getNameId();
        final int nameId = targetItem.getNameId();
        PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItemId, 3000, 0, 0), true);
        final ItemUseObserver observer = new ItemUseObserver() {
            @Override
            public void abort() {
                player.getController().cancelTask(TaskId.ITEM_USE);
                player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_CANCELED(new DescriptionId(parentNameId)));
                //Stigma enchantment of %0 has been cancelled.
                PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_ENCHANT_CANCEL(new DescriptionId(parentNameId)));
                PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parntObjectId, parentItemId, 0, 2, 0), true);
                player.getObserveController().removeObserver(this);
            }
        };
        player.getObserveController().attach(observer);
        player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    player.getObserveController().removeObserver(observer);
                    player.getInventory().decreaseKinah(getStigmaByQuality(parentItem));
                    PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parntObjectId, parentItemId, 0, 1, 1), true);
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_ENCHANT_SUCCESS(new DescriptionId(parentNameId)));
                    player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1);
                    targetItem.setEnchantLevel(targetItem.getEnchantLevel() + 1);
                    targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
                    PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
                    player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
                } else {
                    player.getObserveController().removeObserver(observer);
                    player.getInventory().decreaseKinah(getStigmaByQuality(parentItem));
                    PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parntObjectId, parentItemId, 0, 2, 1), true);
                    PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_ENCHANT_FAIL(new DescriptionId(parentNameId)));
                    targetItem.setEnchantLevel(0);
					targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
                    PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
                }
            }
        }, 3000));
    }
	
    public static int getStigmaByQuality(Item item) {
        int price = 0;
        switch (item.getItemTemplate().getItemQuality()) {
            case RARE:
                price = 423;
            break;
            case LEGEND:
                price = 1271;
            break;
            case UNIQUE:
                price = 3813;
            break;
            default:
            break;
        }
        return price;
    }
}