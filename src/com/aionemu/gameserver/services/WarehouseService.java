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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.model.items.storage.StorageType;
import com.aionemu.gameserver.model.templates.WarehouseExpandTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_WAREHOUSE_INFO;
import com.aionemu.gameserver.utils.PacketSendUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class WarehouseService
{
	private static final Logger log = LoggerFactory.getLogger(WarehouseService.class);
	
	private static final int MIN_EXPAND = 0;
	private static final int MAX_EXPAND = 11;
	
	public static void expandWarehouse(final Player player, Npc npc) {
		final WarehouseExpandTemplate expandTemplate = DataManager.WAREHOUSEEXPANDER_DATA.getWarehouseExpandListTemplate(npc.getNpcId());
		if (expandTemplate == null) {
			log.error("Warehouse Expand Template could not be found for Npc ID: " + npc.getObjectTemplate().getTemplateId());
			return;
		} if (npcCanExpandLevel(expandTemplate, player.getWarehouseSize() + 1) && validateNewSize(player.getWarehouseSize() + 1)) {
			if (validateNewSize(player.getWarehouseSize() + 1)) {
				final int price = getPriceByLevel(expandTemplate, player.getWarehouseSize() + 1);
				RequestResponseHandler responseHandler = new RequestResponseHandler(npc) {
					@Override
					public void acceptRequest(Creature requester, Player responder) {
						if (player.getInventory().getKinah() < price) {
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300831));
							return;
						}
						expand(responder);
						player.getInventory().decreaseKinah(price);
					}
					@Override
					public void denyRequest(Creature requester, Player responder) {
					}
				};
				boolean result = player.getResponseRequester().putRequest(900686, responseHandler);
				if (result) {
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(900686, 0, 0, String.valueOf(price)));
				}
			}
		} else {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300432));
		}
	}
	
	public static void expand(Player player) {
		if (!canExpand(player)) {
			return;
		}
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300433, "8"));
		player.setWarehouseSize(player.getWarehouseSize() + 1);
		sendWarehouseInfo(player, false);
	}
	
	private static boolean validateNewSize(int level) {
		if (level < MIN_EXPAND || level > MAX_EXPAND) {
			return false;
		}
		return true;
	}
	
	public static boolean canExpand(Player player) {
		return validateNewSize(player.getWarehouseSize() + 1);
	}
	
	private static boolean npcCanExpandLevel(WarehouseExpandTemplate clist, int level) {
		if (!clist.contains(level)) {
		    return false;
		}
		return true;
	}
	
	private static int getPriceByLevel(WarehouseExpandTemplate clist, int level) {
		return clist.get(level).getPrice();
	}
	
	public static void sendWarehouseInfo(Player player, boolean sendAccountWh) {
		List<Item> items = player.getStorage(StorageType.REGULAR_WAREHOUSE.getId()).getItems();
		int whSize = player.getWarehouseSize();
		int itemsSize = items.size();
		boolean firstPacket = true;
		if (itemsSize != 0) {
			int index = 0;
			while (index + 10 < itemsSize) {
				PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(items.subList(index, index + 10), StorageType.REGULAR_WAREHOUSE.getId(), whSize, firstPacket, player));
				index += 10;
				firstPacket = false;
			}
			PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(items.subList(index, itemsSize), StorageType.REGULAR_WAREHOUSE.getId(), whSize, firstPacket, player));
		}
		PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(null, StorageType.REGULAR_WAREHOUSE.getId(), whSize, false, player));
		if (sendAccountWh) {
			PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(player.getStorage(StorageType.ACCOUNT_WAREHOUSE.getId()).getItemsWithKinah(), StorageType.ACCOUNT_WAREHOUSE.getId(), 0, true, player));
		}
		PacketSendUtility.sendPacket(player, new SM_WAREHOUSE_INFO(null, StorageType.ACCOUNT_WAREHOUSE.getId(), 0, false, player));
	}
}