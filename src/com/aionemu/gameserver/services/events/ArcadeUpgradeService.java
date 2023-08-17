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
package com.aionemu.gameserver.services.events;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerUpgradeArcade;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.arcadeupgrade.ArcadeTab;
import com.aionemu.gameserver.model.templates.arcadeupgrade.ArcadeTabItem;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPGRADE_ARCADE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import java.util.List;

/**
 * @author Rinzler (Encom)
 */
public class ArcadeUpgradeService
{
    private final int frenzyTime = 90;
	
    public ArcadeUpgradeService() {
    }
	
    public void onEnterWorld(Player player) {
        PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(true));
    }
	
    public static ArcadeTabItem getRewardItem(Player player) {
        PlayerUpgradeArcade arcade = player.getUpgradeArcade();
        int frenzyLevel = arcade.getFrenzyLevel();
        boolean isFrenzy = arcade.isFrenzy();
        int rewardLevel = 0;
		if (frenzyLevel >= 1 && frenzyLevel <= 2) {
            rewardLevel = 1;
        } else if (frenzyLevel >= 3 && frenzyLevel <= 4) {
            rewardLevel = 2;
        } else if (frenzyLevel >= 5 && frenzyLevel <= 6) {
            rewardLevel = 3;
        } else if (frenzyLevel >= 7 && frenzyLevel <= 8) {
            rewardLevel = 4;
        }
        List<ArcadeTabItem> items = DataManager.ARCADE_UPGRADE_DATA.getArcadeTabById(rewardLevel);
        int count = (items.size() -1) - (isFrenzy ? 0 : 4);
        return items.get(Rnd.get(0, count));
    }
	
    public static void getSpecialRewardItem(Player player) {
        PlayerUpgradeArcade arcade = player.getUpgradeArcade();
        List<ArcadeTabItem> items = DataManager.ARCADE_UPGRADE_DATA.getArcadeTabById(4);
        ArcadeTabItem item = items.get(Rnd.get(0, items.size()));
        int itemCount = arcade.isFrenzy() ? item.getNormalCount() : item.getFrenzyCount();
        if (itemCount == 0) {
            itemCount = item.getFrenzyCount();
        }
        PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(11));
		//You've reached Frenzy four times and received %1 %0!
        PacketSendUtility.sendPacket(player, itemCount > 1 ? SM_SYSTEM_MESSAGE.STR_MSG_GACHA_FEVER_ITEM_REWARD_MULTI(item.getItemId(), itemCount) : SM_SYSTEM_MESSAGE.STR_MSG_GACHA_FEVER_ITEM_REWARD(item.getItemId()));
        ItemService.addItem(player, item.getItemId(), itemCount);
        arcade.setFrenzy(false);
    }
	
    public static ArcadeUpgradeService getInstance() {
        return SingletonHolder.instance;
    }
	
    public void closeWindow(Player player) {
        PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(2));
    }
	
    public void startArcadeUpgrade(Player player) {
        PlayerUpgradeArcade arcade = player.getUpgradeArcade();
        if (arcade == null) {
            arcade = new PlayerUpgradeArcade();
        }
        arcade.reset();
        PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(arcade.getFrenzyPoints(), arcade.getFrenzyCount()));
    }
	
    public void showRewardList(Player player) {
        PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(10));
    }
	
    public List<ArcadeTab> getTabs() {
        return DataManager.ARCADE_UPGRADE_DATA.getArcadeTabs();
    }
	
    public void tryArcadeUpgrade(final Player player) {
        if (!EventsConfig.ENABLE_EVENT_ARCADE) {
            return;
        }
        PlayerUpgradeArcade arcade = player.getUpgradeArcade();
        Storage localStorage = player.getInventory();
		if (player.getInventory().isFull()) {
			//Your cube is full.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
			return;
		} if ((arcade.getFrenzyLevel() == 1) && (!localStorage.decreaseByItemId(186000389, 1L))) {
			//You do not have enough %0s.
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GACHA_ITEM_CHECK);
            return;
        } if (arcade.isReTry() && (!localStorage.decreaseByItemId(186000389, 2L))) {
			//You do not have enough %0s.
            PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GACHA_ITEM_CHECK);
            return;
        } if (arcade.isFailed() || arcade.getFrenzyLevel() == 1) {
            arcade.setFrenzyPoints(arcade.getFrenzyPoints() + 8);
            arcade.setFailed(false);
        } if (arcade.getFrenzyPoints() >= 100 && !arcade.isFrenzy()) {
            getFrenzyArcade(player, arcade);
        } if (Rnd.chance(EventsConfig.EVENT_ARCADE_CHANCE)) {
            getPlaySuccesArcade(player, arcade);
        } else {
            getPlayFailedArcade(player, arcade);
        }
    }
	
    public void getPlaySuccesArcade (final Player player, final PlayerUpgradeArcade arcade) {
        arcade.setFrenzyLevel(arcade.getFrenzyLevel() + 1);
        PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(3, true, arcade.getFrenzyPoints()));
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(player, 4, arcade.getFrenzyLevel()));
            }
        }, 3000);
    }
	
    public void getPlayFailedArcade (final Player player, final PlayerUpgradeArcade arcade) {
        PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(3, false, arcade.getFrenzyPoints()));
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(player, 5, arcade.isReTry() ? arcade.getFailedLevel() : 1));
                if (arcade.getFrenzyLevel() < 8 && !arcade.isReTry()) {
                    arcade.setFrenzyLevel(1);
                } else {
                    arcade.setReTry(true);
                    arcade.setFailedLevel(arcade.getFrenzyLevel());
                }
                PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(player, 5, arcade.isReTry() ? arcade.getFailedLevel() : 1));
                arcade.setFailed(true);
            }
        }, 3000);
    }
	
    public void getFrenzyArcade(final Player player, final PlayerUpgradeArcade arcade) {
        if (arcade.getFrenzyCount() < 4) {
            arcade.setFrenzyCount(arcade.getFrenzyCount() + 1);
        } if (arcade.getFrenzyCount() == 4) {
            arcade.setFrenzy(true);
            getSpecialRewardItem(player);
        }
		//Upgrade Frenzy!
        PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GACHA_FEVERTIME_START);
        PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(7, frenzyTime, arcade.getFrenzyCount()));
        ThreadPoolManager.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                PlayerUpgradeArcade arcade = player.getUpgradeArcade();
                PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(7, 0, arcade.getFrenzyCount()));
                player.getUpgradeArcade().setFrenzy(false);
                if (arcade.getFrenzyCount() >= 4) {
                    arcade.setFrenzyCount(0);
                }
            }
        }, frenzyTime * 1000);
        player.getUpgradeArcade().setFrenzyPoints(0);
        if (arcade.getFrenzyCount() == 4) {
            arcade.setFrenzyCount(0);
        }
    }
	
    public void getReward(Player player) {
        if (!EventsConfig.ENABLE_EVENT_ARCADE) {
            return;
        }
        PlayerUpgradeArcade arcade = player.getUpgradeArcade();
        ArcadeTabItem item = getRewardItem(player);
        int itemCount = arcade.isFrenzy() ? item.getNormalCount() : item.getFrenzyCount();
        if (arcade.isFrenzy()) {
			//You've reached Frenzy four times and received %1 %0!
            PacketSendUtility.sendPacket(player, itemCount >= 1 ? SM_SYSTEM_MESSAGE.STR_MSG_GACHA_FEVER_ITEM_REWARD_MULTI(item.getItemId(), itemCount) : SM_SYSTEM_MESSAGE.STR_MSG_GACHA_FEVER_ITEM_REWARD(item.getItemId()));
        } else {
			//You won %1 of %0 from the Upgrade Arcade.
            PacketSendUtility.sendPacket(player, itemCount >= 1 ? SM_SYSTEM_MESSAGE.STR_MSG_GACHA_ITEM_REWARD_MULTI(item.getItemId(), itemCount) : SM_SYSTEM_MESSAGE.STR_MSG_GACHA_ITEM_REWARD(item.getItemId()));
        } if (itemCount == 0) {
            ItemService.addItem(player, item.getItemId(), item.getFrenzyCount());
        } else {
            ItemService.addItem(player, item.getItemId(), itemCount);
        }
        PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(6, item));
        arcade.reset();
    }
	
    private static class SingletonHolder {
        protected static final ArcadeUpgradeService instance = new ArcadeUpgradeService();
    }
}