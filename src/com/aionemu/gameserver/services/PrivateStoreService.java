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
package com.aionemu.gameserver.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.objects.filter.ObjectFilter;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PrivateStore;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.trade.TradeItem;
import com.aionemu.gameserver.model.trade.TradeList;
import com.aionemu.gameserver.model.trade.TradePSItem;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PRIVATE_STORE_NAME;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class PrivateStoreService {

	private static final Logger log = LoggerFactory.getLogger("EXCHANGE_LOG");
	public static void sellStoreItem(Player seller, Player buyer, TradeList tradeList) {

		if (!validateParticipants(seller, buyer))
			return;

		PrivateStore store = seller.getStore();
		tradeList = loadObjIds(seller, tradeList);
		if (tradeList == null)
			return;

		Storage inventory = buyer.getInventory();
		int freeSlots = inventory.getLimit() - inventory.getItemsWithKinah().size() + 1;
		if (freeSlots < tradeList.size()) {
			return; // TODO message
		}

		long price = getTotalPrice(store, tradeList);

		if (price < 0)
			return;

		if (buyer.getInventory().getKinah() >= price) {
			for (TradeItem tradeItem : tradeList.getTradeItems()) {
				Item item = getItemByObjId(seller, tradeItem.getItemId());
				if (item != null) {
					TradePSItem storeItem = store.getTradeItemByObjId(tradeItem.getItemId());

					if (item.getItemCount() < tradeItem.getCount()) {
						PacketSendUtility.sendMessage(buyer, "You cannot buy more than player can sell.");
						return;
					}

					decreaseItemFromPlayer(seller, item, tradeItem);
					ItemService.addItem(buyer, item.getItemId(), tradeItem.getCount(), item);
					if (storeItem.getCount() == tradeItem.getCount()) {
						store.removeItem(storeItem.getItemObjId());
					}

					log.info("[PRIVATE STORE] > [Seller: " + seller.getName() + "] sold [Item: " + item.getItemId() + "][Amount: " + item.getItemCount() + "] to [Buyer: " + buyer.getName() + "] for [Price: " + price + "]");
				}
			}

			decreaseKinahAmount(buyer, price);
			increaseKinahAmount(seller, price);

			if (store.getSoldItems().size() == 0) {
				closePrivateStore(seller);
			}
		}
	}

	public static void openPrivateStore(Player activePlayer, String name) {
		final int senderRace = activePlayer.getRace().getRaceId();
		final Player playerActive = activePlayer;
		if (name != null) {
			activePlayer.getStore().setStoreMessage(name);
			if (CustomConfig.SPEAKING_BETWEEN_FACTIONS) {
				PacketSendUtility.broadcastPacket(playerActive, new SM_PRIVATE_STORE_NAME(playerActive.getObjectId(), name), true);
			}
			else {
				PacketSendUtility.broadcastPacket(playerActive, new SM_PRIVATE_STORE_NAME(playerActive.getObjectId(), name), true, new ObjectFilter<Player>() {

					@Override
					public boolean acceptObject(Player object) {
						return ((senderRace == object.getRace().getRaceId() && !object.getBlockList().contains(playerActive.getObjectId())) || object.isGM());
					}
				});
				PacketSendUtility.broadcastPacket(playerActive, new SM_PRIVATE_STORE_NAME(playerActive.getObjectId(), ""), false, new ObjectFilter<Player>() {

					@Override
					public boolean acceptObject(Player object) {
						return senderRace != object.getRace().getRaceId() && !object.getBlockList().contains(playerActive.getObjectId()) && !object.isGM();
					}
				});
			}
		}
		else {
			PacketSendUtility.broadcastPacket(playerActive, new SM_PRIVATE_STORE_NAME(playerActive.getObjectId(), ""), true);
		}
	}

	public static void addItems(Player activePlayer, TradePSItem[] tradePSItems) {
		if (CreatureState.ACTIVE.getId() != activePlayer.getState()) {
			return;
		}

		if (activePlayer.getStore() == null) {
			createStore(activePlayer);
		}

		PrivateStore store = activePlayer.getStore();

		for (int i = 0; i < tradePSItems.length; i++) {
			Item item = getItemByObjId(activePlayer, tradePSItems[i].getItemObjId());
			if (item != null && item.isTradeable(activePlayer)) {
				if (validateItem(store, item, tradePSItems[i])) {
					store.addItemToSell(tradePSItems[i].getItemObjId(), tradePSItems[i]);
				}
			}
		}
	}

	private static boolean validateItem(PrivateStore store, Item item, TradePSItem psItem) {
		int itemId = psItem.getItemId();
		long itemCount = psItem.getCount();
		if (item.getItemTemplate().getTemplateId() != itemId)
			return false;
		if (itemCount > item.getItemCount() || itemCount < 1)
			return false;

		TradePSItem addedPsItem = store.getTradeItemByObjId(psItem.getItemObjId());
		return addedPsItem == null;

	}

	private static void createStore(Player activePlayer) {
		if (activePlayer.isInState(CreatureState.RESTING)) {
			return;
		}
		activePlayer.setStore(new PrivateStore(activePlayer));
		activePlayer.setState(CreatureState.PRIVATE_SHOP);
		PacketSendUtility.broadcastPacket(activePlayer, new SM_EMOTION(activePlayer, EmotionType.OPEN_PRIVATESHOP, 0, 0), true);
	}

	public static void closePrivateStore(Player activePlayer) {
		activePlayer.setStore(null);
		activePlayer.unsetState(CreatureState.PRIVATE_SHOP);
		PacketSendUtility.broadcastPacket(activePlayer, new SM_EMOTION(activePlayer, EmotionType.CLOSE_PRIVATESHOP, 0, 0), true);
	}

	private static void decreaseItemFromPlayer(Player seller, Item item, TradeItem tradeItem) {
		seller.getInventory().decreaseItemCount(item, tradeItem.getCount());
		seller.getStore().getTradeItemByObjId(item.getObjectId()).decreaseCount(tradeItem.getCount());
	}

	private static void increaseKinahAmount(Player player, long price) {
		player.getInventory().increaseKinah(price);
	}

	private static Item getItemByObjId(Player seller, int itemObjId) {
		return seller.getInventory().getItemByObjId(itemObjId);
	}

	private static long getTotalPrice(PrivateStore store, TradeList tradeList) {
		long totalprice = 0;
		for (TradeItem tradeItem : tradeList.getTradeItems()) {
			TradePSItem item = store.getTradeItemByObjId(tradeItem.getItemId());
			if (item == null) {
				continue;
			}
			totalprice += item.getPrice() * tradeItem.getCount();
		}
		return totalprice;
	}

	private static TradeList loadObjIds(Player seller, TradeList tradeList) {
		PrivateStore store = seller.getStore();
		TradeList newTradeList = new TradeList();

		for (TradeItem tradeItem : tradeList.getTradeItems()) {
			int i = 0;
			for (int itemObjId : store.getSoldItems().keySet()) {
				if (i == tradeItem.getItemId()) {
					newTradeList.addPSItem(itemObjId, tradeItem.getCount());
				}
				i++;
			}
		}

		if (!validateBuyItems(seller, newTradeList)) {
			return null;
		}

		return newTradeList;
	}

	private static boolean validateParticipants(Player itemOwner, Player newOwner) {
		return itemOwner != null && newOwner != null && itemOwner.isOnline() && newOwner.isOnline() && itemOwner.getRace().equals(newOwner.getRace());
	}

	private static boolean validateBuyItems(Player seller, TradeList tradeList) {
		for (TradeItem tradeItem : tradeList.getTradeItems()) {
			Item item = seller.getInventory().getItemByObjId(tradeItem.getItemId());

			if (item == null) {
				return false;
			}
		}
		return true;
	}

	private static void decreaseKinahAmount(Player player, long price) {
		player.getInventory().decreaseKinah(price);
	}
}
