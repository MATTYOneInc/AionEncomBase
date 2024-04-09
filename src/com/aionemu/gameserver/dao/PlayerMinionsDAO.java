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
import com.aionemu.gameserver.model.gameobjects.player.MinionCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.minion.MinionDopingBag;

/**
 * @author Falke_34
 */
public abstract class PlayerMinionsDAO implements DAO {

	@Override
	public final String getClassName() {
		return PlayerMinionsDAO.class.getName();
	}

	public abstract void insertPlayerMinions(MinionCommonData minionCommonData, String minionName);

	public abstract void removePlayerMinions(Player player, int minionId, String minionName);

	public abstract void updateMinionsName(MinionCommonData minionCommonData, String OldName);

	public abstract List<MinionCommonData> getPlayerMinions(Player player);

	public abstract void lockMinions(Player player, int minionId, int isLocked);

	public abstract void evolutionMinion(Player player, int oldMinionId, int newMinionId, MinionCommonData petCommonData);

	public abstract void updateMinionsGrowthPoint(MinionCommonData minionCommonData);

	public abstract void saveDopingBag(Player player, MinionCommonData petCommonData, MinionDopingBag bag);

}