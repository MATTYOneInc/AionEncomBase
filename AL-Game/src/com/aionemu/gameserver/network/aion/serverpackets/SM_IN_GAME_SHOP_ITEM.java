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

public class SM_IN_GAME_SHOP_ITEM extends AionServerPacket {
	private IGItem item;

	public SM_IN_GAME_SHOP_ITEM(Player player, int objectItem) {
		item = InGameShopEn.getInstance().getIGItem(objectItem);
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(item.getObjectId());
		writeQ(item.getItemPrice());
		writeH(0);
		writeD(item.getItemId());
		writeQ(item.getItemCount());
		writeD(0);
		writeD(item.getGift());
		writeD(item.getItemType());
		writeD(0);
		writeC(0);
		writeH(0);
		writeS(item.getTitleDescription());
		writeS(item.getItemDescription());
	}
}