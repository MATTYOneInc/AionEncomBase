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
package com.aionemu.gameserver.dao;

import java.util.List;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.ingameshop.IGItem;

import javolution.util.FastMap;

/**
 * @author xTz, KID
 */
public abstract class InGameShopDAO implements DAO {

	public abstract boolean deleteIngameShopItem(int itemId, byte category, byte list, int param);

	public abstract FastMap<Byte, List<IGItem>> loadInGameShopItems();

	public abstract void saveIngameShopItem(int paramInt1, int paramInt2, long paramLong1, long paramLong2,
			byte paramByte1, byte paramByte2, int paramInt3, int paramInt4, byte paramByte3, byte paramByte4,
			String paramString1, String paramString2);

	public abstract boolean increaseSales(int object, int current);

	@Override
	public String getClassName() {
		return InGameShopDAO.class.getName();
	}
}