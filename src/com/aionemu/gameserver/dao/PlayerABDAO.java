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
import com.aionemu.gameserver.model.atreian_bestiary.PlayerABList;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Ranastic
 */

public abstract class PlayerABDAO implements DAO {

	@Override
	public String getClassName() {
		return PlayerABDAO.class.getName();
	}
	
	public abstract PlayerABList load(Player paramPlayer);
	public abstract boolean store(int playerObjId, int id, int kill_count, int level, int levelUpable);
	public abstract boolean delete(int playerObjId, int slot);
	public abstract int getKillCountById(int playerObjId, int id);
	public abstract int getLevelById(int playerObjId, int id);
	public abstract int getClaimRewardById(int playerObjId, int id);
}