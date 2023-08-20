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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.LinkedHashMap;

import com.aionemu.gameserver.model.trade.TradePSItem;

public class PrivateStore {

	private Player owner;
	private LinkedHashMap<Integer, TradePSItem> items;
	private String storeMessage;

	public PrivateStore(Player owner) {
		this.owner = owner;
		this.items = new LinkedHashMap<Integer, TradePSItem>();
	}

	public Player getOwner() {
		return owner;
	}

	public LinkedHashMap<Integer, TradePSItem> getSoldItems() {
		return items;
	}

	public void addItemToSell(int itemObjId, TradePSItem tradeItem) {
		items.put(itemObjId, tradeItem);
	}

	public void removeItem(int itemObjId) {
		if (items.containsKey(itemObjId)) {
			LinkedHashMap<Integer, TradePSItem> newItems = new LinkedHashMap<Integer, TradePSItem>();
			for (int itemObjIds : items.keySet()) {
				if (itemObjId != itemObjIds) {
					newItems.put(itemObjIds, items.get(itemObjIds));
				}
			}
			this.items = newItems;
		}
	}

	public TradePSItem getTradeItemByObjId(int itemObjId) {
		return items.get(itemObjId);
	}

	public void setStoreMessage(String storeMessage) {
		this.storeMessage = storeMessage;
	}

	public String getStoreMessage() {
		return storeMessage;
	}
}
