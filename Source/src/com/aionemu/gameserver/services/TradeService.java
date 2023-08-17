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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.GoodsListData;
import com.aionemu.gameserver.dataholders.TradeListData;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.limiteditems.LimitedItem;
import com.aionemu.gameserver.model.templates.goods.GoodsList;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.TradeinItem;
import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate.TradeTab;
import com.aionemu.gameserver.model.trade.TradeItem;
import com.aionemu.gameserver.model.trade.TradeList;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemFactory;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerLimitService;
import com.aionemu.gameserver.services.trade.PricesService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.OverfowException;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.SafeMath;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TradeService
{
	private static final Logger log = LoggerFactory.getLogger(TradeService.class);
	private static final TradeListData tradeListData = DataManager.TRADE_LIST_DATA;
	private static final GoodsListData goodsListData = DataManager.GOODSLIST_DATA;
	
	public static boolean performBuyFromShop(Npc npc, Player player, TradeList tradeList) {
		if (!RestrictionsManager.canTrade(player)) {
			return false;
		} if (player.getInventory().isFull()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
			return false;
		} if (!validateBuyItems(npc, tradeList, player)) {
			PacketSendUtility.sendMessage(player, "Some items are not allowed to be sold by this npc.");
			return false;
		}
		Storage inventory = player.getInventory();
		int tradeModifier = tradeListData.getTradeListTemplate(npc.getNpcId()).getSellPriceRate();
		if (!tradeList.calculateBuyListPrice(player, tradeModifier)) {
			return false;
		} if (!tradeList.calculateRewardBuyListPrice(player)) {
			return false;
		}
		int freeSlots = inventory.getFreeSlots();
		if (freeSlots < tradeList.size()) {
			return false;
		}
		long tradeListPrice = tradeList.getRequiredKinah();
		LimitedItem item = null;
		for (TradeItem tradeItem : tradeList.getTradeItems()) {
			item = LimitedItemTradeService.getInstance().getLimitedItem(tradeItem.getItemId(), npc.getNpcId());
			if (item != null) {
				if (item.getBuyLimit() == 0 && item.getDefaultSellLimit() != 0) {
					item.getBuyCount().putIfAbsent(player.getObjectId(), 0);
					if (item.getSellLimit() - tradeItem.getCount() < 0) {
						return false;
					}
					item.setSellLimit(item.getSellLimit() - (int) tradeItem.getCount());
				} else if (item.getBuyLimit() != 0 && item.getDefaultSellLimit() == 0) {
					item.getBuyCount().putIfAbsent(player.getObjectId(), 0);
					if (item.getBuyLimit() - tradeItem.getCount() < 0) {
						return false;
					} if (item.getBuyCount().containsKey(player.getObjectId())) {
						if (item.getBuyCount().get(player.getObjectId()) < item.getBuyLimit()) {
							item.getBuyCount().put(player.getObjectId(), item.getBuyCount().get(player.getObjectId()) + (int) tradeItem.getCount());
						} else {
							return false;
						}
					}
				} else if (item.getBuyLimit() != 0 && item.getDefaultSellLimit() != 0) {
					item.getBuyCount().putIfAbsent(player.getObjectId(), 0);
					if (item.getBuyLimit() - tradeItem.getCount() < 0 || item.getSellLimit() - tradeItem.getCount() < 0) {
						return false;
					} if (item.getBuyCount().containsKey(player.getObjectId())) {
						if (item.getBuyCount().get(player.getObjectId()) < item.getBuyLimit()) {
							item.getBuyCount().put(player.getObjectId(), item.getBuyCount().get(player.getObjectId()) + (int) tradeItem.getCount());
						} else {
							return false;
						}
					}
					item.setSellLimit(item.getSellLimit() - (int) tradeItem.getCount());
				}
			}
			Map<Integer, Long> requiredItems = tradeList.getRequiredItems();
            for (Integer itemId: requiredItems.keySet()) {
                if (!player.getInventory().decreaseByItemId(itemId, requiredItems.get(itemId))) {
                    AuditLogger.info(player, "Possible hack. Not " + "removed items on buy in shop.");
                    return false;
                }
            }
			long count = ItemService.addItem(player, tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount());
			if (count != 0) {
				log.warn(String.format("CHECKPOINT: itemservice couldnt add all items on buy: %d %d %d %d", player.getObjectId(), tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount(), count));
				inventory.decreaseKinah(tradeListPrice);
				return false;
			}
		}
		inventory.decreaseKinah(tradeListPrice);
		return true;
	}
	
	public static boolean performBuyFromAbyssShop(Npc npc, Player player, TradeList tradeList) {
		if (!RestrictionsManager.canTrade(player)) {
			return false;
		} if (player.getInventory().isFull()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
			return false;
		} if (!validateBuyItems(npc, tradeList, player)) {
			PacketSendUtility.sendMessage(player, "Some items are not allowed to be selled from this npc");
			return false;
		}
		Storage inventory = player.getInventory();
		int freeSlots = inventory.getFreeSlots();
		if (!tradeList.calculateAbyssBuyListPrice(player)) {
			return false;
		} if (tradeList.getRequiredAp() < 0) {
			AuditLogger.info(player, "Posible client hack. tradeList.getRequiredAp() < 0");
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300927));
			return false;
		} if (freeSlots < tradeList.size()) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300762));
			return false;
		}
		AbyssPointsService.addAp(player, -tradeList.getRequiredAp());
		LimitedItem item = null;
		for (TradeItem tradeItem : tradeList.getTradeItems()) {
			item = LimitedItemTradeService.getInstance().getLimitedItem(tradeItem.getItemId(), npc.getNpcId());
			if (item != null) {
				if (item.getBuyLimit() == 0 && item.getDefaultSellLimit() != 0) {
					item.getBuyCount().putIfAbsent(player.getObjectId(), 0);
					if (item.getSellLimit() - tradeItem.getCount() < 0) {
						return false;
					}
					item.setSellLimit(item.getSellLimit() - (int) tradeItem.getCount());
				} else if (item.getBuyLimit() != 0 && item.getDefaultSellLimit() == 0) {
					item.getBuyCount().putIfAbsent(player.getObjectId(), 0);
					if (item.getBuyLimit() - tradeItem.getCount() < 0) {
						return false;
					} if (item.getBuyCount().containsKey(player.getObjectId())) {
						if (item.getBuyCount().get(player.getObjectId()) < item.getBuyLimit()) {
							item.getBuyCount().put(player.getObjectId(), item.getBuyCount().get(player.getObjectId()) + (int) tradeItem.getCount());
						} else {
							return false;
						}
					}
				} else if (item.getBuyLimit() != 0 && item.getDefaultSellLimit() != 0) {
					item.getBuyCount().putIfAbsent(player.getObjectId(), 0);
					if (item.getBuyLimit() - tradeItem.getCount() < 0 || item.getSellLimit() - tradeItem.getCount() < 0) {
						return false;
					} if (item.getBuyCount().containsKey(player.getObjectId())) {
						if (item.getBuyCount().get(player.getObjectId()) < item.getBuyLimit()) {
							item.getBuyCount().put(player.getObjectId(), item.getBuyCount().get(player.getObjectId()) + (int) tradeItem.getCount());
						} else {
							return false;
						}
					}
					item.setSellLimit(item.getSellLimit() - (int) tradeItem.getCount());
				}
			}
			Map<Integer, Long> requiredItems = tradeList.getRequiredItems();
			for (Integer itemId: requiredItems.keySet()) {
				if (!player.getInventory().decreaseByItemId(itemId, requiredItems.get(itemId))) {
					AuditLogger.info(player, "Possible hack. Not removed items on buy in abyss shop.");
					return false;
				}
			}
			long count = ItemService.addItem(player, tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount());
			if (count != 0) {
				log.warn(String.format("CHECKPOINT: itemservice couldnt add all items on buy: %d %d %d %d", player.getObjectId(), tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount(), count));
				return false;
			} if (tradeItem.getCount() > 1) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300785, new DescriptionId(tradeItem.getItemTemplate().getNameId()), tradeItem.getCount()));
			} else {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300784, new DescriptionId(tradeItem.getItemTemplate().getNameId())));
			}
		}
		return true;
	}
	
	public static boolean performBuyFromRewardShop(Npc npc, Player player, TradeList tradeList) {
		if (!RestrictionsManager.canTrade(player)) {
			return false;
		} if (player.getInventory().isFull()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
			return false;
		} if (!validateBuyItems(npc, tradeList, player)) {
			PacketSendUtility.sendMessage(player, "Some items are not allowed to be selled from this npc");
			return false;
		}
		Storage inventory = player.getInventory();
		int freeSlots = inventory.getFreeSlots();
		if (!tradeList.calculateRewardBuyListPrice(player)) {
			return false;
		} if (freeSlots < tradeList.size()) {
			return false;
		}
		LimitedItem item = null;
		for (TradeItem tradeItem : tradeList.getTradeItems()) {
			item = LimitedItemTradeService.getInstance().getLimitedItem(tradeItem.getItemId(), npc.getNpcId());
			if (item != null) {
				if (item.getBuyLimit() == 0 && item.getDefaultSellLimit() != 0) {
					item.getBuyCount().putIfAbsent(player.getObjectId(), 0);
					if (item.getSellLimit() - tradeItem.getCount() < 0) {
						return false;
					}
					item.setSellLimit(item.getSellLimit() - (int) tradeItem.getCount());
				} else if (item.getBuyLimit() != 0 && item.getDefaultSellLimit() == 0) {
					item.getBuyCount().putIfAbsent(player.getObjectId(), 0);
					if (item.getBuyLimit() - tradeItem.getCount() < 0) {
						return false;
					} if (item.getBuyCount().containsKey(player.getObjectId())) {
						if (item.getBuyCount().get(player.getObjectId()) < item.getBuyLimit()) {
							item.getBuyCount().put(player.getObjectId(), item.getBuyCount().get(player.getObjectId()) + (int) tradeItem.getCount());
						} else {
							return false;
						}
					}
				} else if (item.getBuyLimit() != 0 && item.getDefaultSellLimit() != 0) {
					item.getBuyCount().putIfAbsent(player.getObjectId(), 0);
					if (item.getBuyLimit() - tradeItem.getCount() < 0 || item.getSellLimit() - tradeItem.getCount() < 0) {
						return false;
					} if (item.getBuyCount().containsKey(player.getObjectId())) {
						if (item.getBuyCount().get(player.getObjectId()) < item.getBuyLimit()) {
							item.getBuyCount().put(player.getObjectId(), item.getBuyCount().get(player.getObjectId()) + (int) tradeItem.getCount());
						} else {
							return false;
						}
					}
					item.setSellLimit(item.getSellLimit() - (int) tradeItem.getCount());
				}
			}
            // Map<Integer, Long> requiredItems = tradeList.getRequiredItems();
            // for (Integer itemId : requiredItems.keySet()) {
            // if (!player.getInventory().decreaseByItemId(itemId, requiredItems.get(itemId))) {
            // AuditLogger.info(player, "Possible hack. Not removed items on buy in rewardshop.");
            // return false;
            // }
            // }
			long count = ItemService.addItem(player, tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount());
			if (count != 0) {
				log.warn(String.format("CHECKPOINT: itemservice couldnt add all items on buy: %d %d %d %d", player.getObjectId(), tradeItem.getItemTemplate().getTemplateId(), tradeItem.getCount(), count));
				return false;
			}
		}
		return true;
	}

	private static boolean validateBuyItems(Npc npc, TradeList tradeList, Player player) {
		TradeListTemplate tradeListTemplate = tradeListData.getTradeListTemplate(npc.getObjectTemplate().getTemplateId());
		Set<Integer> allowedItems = new HashSet<Integer>();
		for (TradeTab tradeTab : tradeListTemplate.getTradeTablist()) {
			GoodsList goodsList = goodsListData.getGoodsListById(tradeTab.getId());
			if (goodsList != null && goodsList.getItemIdList() != null) {
				allowedItems.addAll(goodsList.getItemIdList());
			}
		} for (TradeItem tradeItem : tradeList.getTradeItems()) {
			if (tradeItem.getCount() < 1) {
				AuditLogger.info(player, "BUY packet hack item count < 1!");
				return false;
			} if (!allowedItems.contains(tradeItem.getItemId())) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean performSellToShop(Player player, TradeList tradeList) {
		Storage inventory = player.getInventory();
		long kinahReward = 0;
		List<Item> items = new ArrayList<Item>();
		if (!RestrictionsManager.canTrade(player)) {
			return false;
		} for (TradeItem tradeItem : tradeList.getTradeItems()) {
			Item item = inventory.getItemByObjId(tradeItem.getItemId());
			if (item == null) {
				return false;
			} if (!item.isSellable()) {
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300344, new DescriptionId(item.getNameId())));
				return false;
			}
			Item repurchaseItem = null;
			long sellReward = PricesService.getKinahForSell(item.getItemTemplate().getPrice(), player.getRace());
			long realReward = sellReward * tradeItem.getCount();
			if (!PlayerLimitService.updateSellLimit(player, realReward)) {
				break;
			} if (item.getItemCount() - tradeItem.getCount() < 0) {
				AuditLogger.info(player, "Trade exploit, sell item count big");
				return false;
			} else if (item.getItemCount() - tradeItem.getCount() == 0) {
				inventory.delete(item);
				repurchaseItem = item;
			} else if (item.getItemCount() - tradeItem.getCount() > 0) {
				repurchaseItem = ItemFactory.newItem(item.getItemId(), tradeItem.getCount());
				inventory.decreaseItemCount(item, tradeItem.getCount());
			} else {
				return false;
			}
			kinahReward += realReward;
			repurchaseItem.setRepurchasePrice(realReward);
			items.add(repurchaseItem);
		}
		RepurchaseService.getInstance().addRepurchaseItems(player, items);
		inventory.increaseKinah(kinahReward);
		return true;
	}
	
	public static boolean performBuyFromTradeInTrade(Player player, int npcObjectId, int itemId, int count, int TradeinListCount, int TradeinItemObjectId1, int TradeinItemObjectId2, int TradeinItemObjectId3) {
		if (!RestrictionsManager.canTrade(player)) {
			return false;
		} if (player.getInventory().isFull()) {
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
			return false;
		}
		VisibleObject visibleObject = player.getKnownList().getObject(npcObjectId);
		if (visibleObject == null || !(visibleObject instanceof Npc) || MathUtil.getDistance(visibleObject, player) > 10) {
			return false;
		}
		int npcId = ((Npc) visibleObject).getNpcId();
		TradeListTemplate tradeInList = tradeListData.getTradeInListTemplate(npcId);
		if (tradeInList == null) {
			return false;
		}
		boolean valid = false;
		for (TradeTab tab : tradeInList.getTradeTablist()) {
			GoodsList goodList = goodsListData.getGoodsInListById(tab.getId());
			if (goodList.getItemIdList().contains(itemId)) {
				valid = true;
				break;
			}
		} if (!valid) {
			return false;
		}
		ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		if (itemTemplate.getMaxStackCount() < count) {
			return false;
		} try {
			for (TradeinItem treadInList : itemTemplate.getTradeinList().getTradeinItem()) {
				if (player.getInventory().getItemCountByItemId(treadInList.getId()) < SafeMath.multSafe(treadInList.getPrice(), count)) {
					return false;
				}
			} for (TradeinItem treadInList : itemTemplate.getTradeinList().getTradeinItem()) {
				if (!player.getInventory().decreaseByItemId(treadInList.getId(), SafeMath.multSafe(treadInList.getPrice(), count))) {
					return false;
				}
			}
		} catch (OverfowException e) {
			AuditLogger.info(player, "OverfowException using tradeInTrade " + e.getMessage());
			return false;
		}
		ItemService.addItem(player, itemId, count);
		return true;
	}
	
	/**
	 * Purchase List AP 4.3
	 **/
	public static boolean performSellForAPToShop(Player player, TradeList tradeList, TradeListTemplate purchaseTemplate) {
		if (!RestrictionsManager.canTrade(player)) {
			return false;
		}
		Storage inventory = player.getInventory();
		for (TradeItem tradeItem : tradeList.getTradeItems()) {
			int itemObjectId = tradeItem.getItemId();
			long count = tradeItem.getCount();
			Item item = inventory.getItemByObjId(itemObjectId);
			if (item == null) {
				return false;
			}
			int itemId = item.getItemId();
			boolean valid = false;
			for (TradeTab tab : purchaseTemplate.getTradeTablist()) {
				GoodsList goodList = goodsListData.getGoodsPurchaseListById(tab.getId());
				if (goodList.getItemIdList().contains(itemId)) {
					valid = true;
					break;
				}
			} if (!valid) {
				return false;
			} if (inventory.decreaseByObjectId(itemObjectId, count)) {
				AbyssPointsService.addAp(player, item.getItemTemplate().getAcquisition().getRequiredAp() * (int) count);
			}
		}
		return true;
	}
	
	/**
	 * Purchase List KINAH 4.3
	 **/
	public static boolean performSellForKinahToShop(Player player, TradeList tradeList, TradeListTemplate purchaseTemplate) {
		if (!RestrictionsManager.canTrade(player)) {
			return false;
		}
		Storage inventory = player.getInventory();
		for (TradeItem tradeItem : tradeList.getTradeItems()) {
			int itemObjectId = tradeItem.getItemId();
			long count = tradeItem.getCount();
			Item item = inventory.getItemByObjId(itemObjectId);
			if (item == null) {
				return false;
			}
			long purchaseListPrice = PricesService.getKinahForSell(item.getItemTemplate().getPrice(), player.getRace());
			int itemId = item.getItemId();
			boolean valid = false;
			for (TradeTab tab : purchaseTemplate.getTradeTablist()) {
				GoodsList goodList = goodsListData.getGoodsPurchaseListById(tab.getId());
				if (goodList.getItemIdList().contains(itemId)) {
					valid = true;
					break;
				}
			} if (!valid) {
				return false;
			} if (inventory.decreaseByObjectId(itemObjectId, count)) {
			    inventory.increaseKinah(purchaseListPrice);
			}
		}
		return true;
	}
	
	public static TradeListData getTradeListData() {
		return tradeListData;
	}
	
	public static GoodsListData getGoodsListData() {
		return goodsListData;
	}
}