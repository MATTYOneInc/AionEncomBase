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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.TradeService;

public class CM_BUY_TRADE_IN_TRADE extends AionClientPacket
{
	private int sellerObjId;
    private int BuyMask; 
	private int itemId;
	private int count;
	private int TradeinListCount; 
    private int TradeinItemObjectId1;
    private int TradeinItemObjectId2;
	private int TradeinItemObjectId3;
	
	public CM_BUY_TRADE_IN_TRADE(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		sellerObjId = readD();
        BuyMask = readC();
		itemId = readD();
		count = readD();
        TradeinListCount = readH();
        switch (TradeinListCount) {
			case 1:
				TradeinItemObjectId1 = readD();
		    break;
			case 2:
				TradeinItemObjectId1 = readD();
				TradeinItemObjectId2 = readD();
			break;
			case 3:
				TradeinItemObjectId1 = readD();
				TradeinItemObjectId2 = readD();
				TradeinItemObjectId3 = readD();
			break;
	    }
	}
	
	@Override
	protected void runImpl() {
		Player player = this.getConnection().getActivePlayer();
		if (count < 1) {
			return;
		}
        TradeService.performBuyFromTradeInTrade(player, sellerObjId, itemId, count, TradeinListCount, TradeinItemObjectId1, TradeinItemObjectId2, TradeinItemObjectId3);
	}
}