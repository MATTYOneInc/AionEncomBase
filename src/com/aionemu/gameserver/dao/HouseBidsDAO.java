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
package com.aionemu.gameserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.house.PlayerHouseBid;

import java.sql.Timestamp;
import java.util.Set;

/**
 * @author Rolandas
 */
public abstract class HouseBidsDAO implements DAO {

	@Override
	public final String getClassName() {
		return HouseBidsDAO.class.getName();
	}

	public abstract Set<PlayerHouseBid> loadBids();

	public abstract boolean addBid(int playerId, int houseId, long bidOffer, Timestamp time);

	public abstract void changeBid(int playerId, int houseId, long newBidOffer, Timestamp time);

	public abstract void deleteHouseBids(int houseId);

}