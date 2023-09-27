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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.IGItem;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastList;

import java.util.Collection;
import java.util.List;

public class SM_IN_GAME_SHOP_LIST extends AionServerPacket
{
	private Player player;
	private int nrList;
	private int salesRanking;
	private TIntObjectHashMap<FastList<IGItem>> allItems = new TIntObjectHashMap<FastList<IGItem>>();
	
	public SM_IN_GAME_SHOP_LIST(Player player, int nrList, int salesRanking) {
		this.player = player;
		this.nrList = nrList;
		this.salesRanking = salesRanking;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		byte category = player.inGameShop.getCategory();
		byte subCategory = player.inGameShop.getSubCategory();
		if (salesRanking == 1) {
			Collection<IGItem> items = InGameShopEn.getInstance().getItems(category);
			int size = 0;
			int tabSize = 9;
			int f = 0;
			for (IGItem a : items) {
				if (subCategory == 2 || a.getSubCategory() == subCategory) {
					if (size == tabSize) {
						tabSize += 9;
						f++;
					}
					FastList<IGItem> template = allItems.get(f);
					if (template == null) {
						template = FastList.newInstance();
						allItems.put(f, template);
					}
					template.add(a);
					size++;
				}
			}
			List<IGItem> inAllItems = allItems.get(nrList);
			writeD(salesRanking);
			writeD(nrList);
			writeD(size > 0 ? tabSize : 0);
			writeH(inAllItems == null ? 0 : inAllItems.size());
			if (inAllItems != null) {
				for (IGItem item : inAllItems) {
					writeD(item.getObjectId());
				}
			}
		} else {
			FastList<Integer> salesRankingItems = InGameShopEn.getInstance().getTopSales(subCategory, category);
			writeD(salesRanking);
			writeD(nrList);
			writeD((InGameShopEn.getInstance().getMaxList(subCategory, category) + 1) * 9);
			writeH(salesRankingItems.size());
			for (int id : salesRankingItems) {
				writeD(id);
			}
			FastList.recycle(salesRankingItems);
		}
	}
}