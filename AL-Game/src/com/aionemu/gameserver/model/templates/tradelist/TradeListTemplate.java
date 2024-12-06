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
package com.aionemu.gameserver.model.templates.tradelist;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "tradelist_template")
public class TradeListTemplate {
	@XmlAttribute(name = "npc_id", required = true)
	private int npcId;

	@XmlAttribute(name = "npc_type")
	private TradeNpcType tradeNpcType;

	@XmlAttribute(name = "sell_price_rate")
	private int sellPriceRate;

	@XmlAttribute(name = "buy_price_rate")
	private int buyPriceRate;

	@XmlAttribute(name = "ap_buy_price_rate")
	private int apBuyPriceRate;

	@XmlAttribute(name = "ap_sell_price_rate")
	private int apSellPriceRate;

	@XmlElement(name = "tradelist")
	protected List<TradeTab> tradeTablist;

	public TradeListTemplate() {
		tradeNpcType = TradeNpcType.NORMAL;
		sellPriceRate = 100;
		apSellPriceRate = 100;
	}

	public List<TradeTab> getTradeTablist() {
		if (tradeTablist == null) {
			tradeTablist = new ArrayList<TradeTab>();
		}
		return this.tradeTablist;
	}

	public int getNpcId() {
		return npcId;
	}

	public int getCount() {
		return tradeTablist.size();
	}

	public TradeNpcType getTradeNpcType() {
		return tradeNpcType;
	}

	public int getSellPriceRate() {
		return sellPriceRate;
	}

	public int getBuyPriceRate() {
		return buyPriceRate;
	}

	public int getApBuyPriceRate() {
		return apBuyPriceRate;
	}

	public int getApSellPriceRate() {
		return apSellPriceRate;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "Tradelist")
	public static class TradeTab {
		@XmlAttribute
		protected int id;

		public int getId() {
			return id;
		}
	}
}