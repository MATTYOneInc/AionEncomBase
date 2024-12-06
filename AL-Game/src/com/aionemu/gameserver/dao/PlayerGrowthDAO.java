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

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * Created by wanke on 26/02/2017.
 */

public abstract class PlayerGrowthDAO implements DAO {
	@Override
	public String getClassName() {
		return PlayerGrowthDAO.class.getName();
	}

	public abstract void load(Player player);

	public abstract boolean add(final int playerId, boolean isFree, int rechargeCount);

	public abstract boolean delete();

	public abstract boolean store(Player player);

	public abstract boolean setGrowthByObjId(final int obj, boolean isFree, int rechargeCount);
}