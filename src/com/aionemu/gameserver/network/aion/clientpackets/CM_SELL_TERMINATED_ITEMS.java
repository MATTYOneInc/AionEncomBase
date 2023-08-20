package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.Acquisition;
import com.aionemu.gameserver.model.trade.TradeList;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.TradeService;

public class CM_SELL_TERMINATED_ITEMS extends AionClientPacket
{
	private int amount;
	private int itemId;
	private long count;
	private TradeList tradeListKinah;
	private TradeList tradeListAp;

	public CM_SELL_TERMINATED_ITEMS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		Player player = getConnection().getActivePlayer();
		tradeListKinah = new TradeList(0);
		tradeListAp = new TradeList(1);
		amount = readH(); // total no of items

		for (int i = 0; i < amount; i++) {
			itemId = readD();
			Item items = player.getInventory().getItemByObjId(itemId);
			count = player.getInventory().getItemCountByItemId(items.getItemId());

			Acquisition aquisition = player.getInventory().getItemByObjId(itemId).getItemTemplate().getAcquisition();

			if (aquisition != null) {
				tradeListAp.addSellItem(itemId, count);
			}
			else {
				tradeListKinah.addSellItem(itemId, count);
			}
		}
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (tradeListAp.size() > 0 && tradeListKinah.size() == 0) {
			TradeService.performSellBrokenAPItems(player, tradeListAp); // Sell for Ap
		}
		else if (tradeListKinah.size() > 0 && tradeListAp.size() == 0) {
			TradeService.performSellToShop(player, tradeListKinah); // Sell for Kinah
		}
		else if (tradeListAp.size() > 0 && tradeListKinah.size() > 0) {
			TradeService.performSellToShop(player, tradeListKinah);
			TradeService.performSellBrokenAPItems(player, tradeListAp);
		}
	}
}
