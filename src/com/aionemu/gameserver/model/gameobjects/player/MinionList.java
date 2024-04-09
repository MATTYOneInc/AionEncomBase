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
package com.aionemu.gameserver.model.gameobjects.player;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerMinionsDAO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MINIONS;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

public class MinionList {

	private final Player player;
	private int lastUsedMinionsId;
	private FastMap<Integer, MinionCommonData> minions = new FastMap<>();

	MinionList(Player player) {
		this.player = player;
		loadMinions();
	}

	public void loadMinions() {
		List<MinionCommonData> playerMinions = DAOManager.getDAO(PlayerMinionsDAO.class).getPlayerMinions(player);
		MinionCommonData lastUsedMinions = null;
		for (MinionCommonData minion : playerMinions) {
			if (minion.getExpireTime() > 0) {
				ExpireTimerTask.getInstance().addTask(minion, player);
			}
			minions.put(minion.getObjectId(), minion);
			if (lastUsedMinions == null || minion.getDespawnTime().after(lastUsedMinions.getDespawnTime())) {
				lastUsedMinions = minion;
			}
		}

		if (lastUsedMinions != null) {
			lastUsedMinionsId = lastUsedMinions.getMinionId();
		}
	}


	public Collection<MinionCommonData> getMinions() {
		return minions.values();
	}

	/**
	 * @param objectId
	 * @return
	 */
	public MinionCommonData getMinion(int objectId) {
		return minions.get(objectId);
	}

	public MinionCommonData getLastUsedMinion() {
		return getMinion(lastUsedMinionsId);
	}

	public void setLastUsedMinionsId(int lastUsedMinionId) {
		this.lastUsedMinionsId = lastUsedMinionId;
	}

	/**
	 * @param player
	 * @param petId
	 * @param name
	 * @return
	 */
	public MinionCommonData addMinion(Player player, int petId, String name, int expireTime) {
		return addMinion(player, petId, System.currentTimeMillis(), name, expireTime);
	}

	public MinionCommonData addMinion(Player player, int petId, long birthday, String name, int expireTime) {
		MinionCommonData minionCommonData = new MinionCommonData(petId, player.getObjectId(), expireTime);
		minionCommonData.setName(name);
		minionCommonData.setBirthday(new Timestamp(birthday));
		minionCommonData.setDespawnTime(new Timestamp(System.currentTimeMillis()));
		DAOManager.getDAO(PlayerMinionsDAO.class).insertPlayerMinions(minionCommonData, name);
		minions.put(minionCommonData.getObjectId(), minionCommonData);
		return minionCommonData;
	}

	/**
	 * @param minionId
	 * @return
	 */
	public boolean hasMinion(int minionId) {
		return minions.containsKey(minionId);
	}

	/**
	 * @param objtId
	 */
	public void deleteMinion(int objtId) {
		if (hasMinion(objtId)) {
			minions.remove(objtId);
		}
	}
}