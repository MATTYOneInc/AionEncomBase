package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeNpcType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_SELL_ITEM extends AionServerPacket {

	private int targetObjectId;
    private int sellPercentage;
	private TradeListTemplate tradeListTemplate;
	@SuppressWarnings("unused")
	private byte action = 0x01;

	public SM_SELL_ITEM(int targetObjectId, int sellPercentage) {
		this.sellPercentage = sellPercentage;
		this.targetObjectId = targetObjectId;
	}

	public SM_SELL_ITEM(int targetObjectId, TradeListTemplate tradeListTemplate) {
		this.targetObjectId = targetObjectId;
		this.tradeListTemplate = tradeListTemplate;
		this.sellPercentage = tradeListTemplate.getBuyPriceRate();
		if (tradeListTemplate.getTradeNpcType() == TradeNpcType.ABYSS) {
			this.action = 0x02;
		}
	}

	@Override
	protected void writeImpl(AionConnection con) {
		if ((this.tradeListTemplate != null) && (this.tradeListTemplate.getNpcId() != 0) && (this.tradeListTemplate.getCount() != 0)) {
			writeD(this.targetObjectId);
			writeC(this.tradeListTemplate.getTradeNpcType().index());
			writeD(this.sellPercentage);
			writeH(256);
			writeH(this.tradeListTemplate.getCount());
			for (TradeListTemplate.TradeTab tradeTabl : this.tradeListTemplate.getTradeTablist())
				writeD(tradeTabl.getId());
		}
		else {
			writeD(this.targetObjectId);
			writeD(5121);
			writeD(65792);
			writeC(0);
		}
	}
}
