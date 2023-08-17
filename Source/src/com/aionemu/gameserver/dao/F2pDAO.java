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
import com.aionemu.gameserver.model.gameobjects.player.Player;

/****/
/** Author Ranastic (Encom)
/** Rework Ace
 */
/****/
public abstract class F2pDAO implements DAO
{
	public abstract void loadF2pInfo(Player player);
	public abstract boolean storeF2p(int playerId, int time);
	public abstract boolean updateF2p(int playerId, int time);
	public abstract boolean deleteF2p(int playerId);
	
	public String getClassName() {
		return F2pDAO.class.getName();
	}
}