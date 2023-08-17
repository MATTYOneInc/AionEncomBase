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
package com.aionemu.gameserver.model.atreian_bestiary;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerABDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ranastic
 */

public final class PlayerABList implements ABList<Player>
{
	private final Map<Integer, PlayerABEntry> entry;
	
	public PlayerABList() {
		this.entry = new HashMap<Integer, PlayerABEntry>(0);
	}
	
	public PlayerABList(List<PlayerABEntry> entries) {
		this();
		for (PlayerABEntry e : entries) {
			entry.put(e.getId(), e);
		}
	}
	
	public PlayerABEntry[] getAllAB() {
		List<PlayerABEntry> allCp = new ArrayList<PlayerABEntry>();
		allCp.addAll(entry.values());
		return allCp.toArray(new PlayerABEntry[allCp.size()]);
	}
	
	public PlayerABEntry[] getBasicAB() {
		return entry.values().toArray(new PlayerABEntry[entry.size()]);
	}
	
	@Override
	public boolean add(Player player, int id, int killCount, int level, int claimReward) {
		return add(player, id, killCount, level, claimReward, PersistentState.NEW);
	}
	
	private synchronized boolean add(Player player, int id, int killCount, int level, int claimReward, PersistentState state) {
		entry.put(id, new PlayerABEntry(id, killCount, level, claimReward, state));
		DAOManager.getDAO(PlayerABDAO.class).store(player.getObjectId(), id, killCount, level, claimReward);
		return true;
	}
	
	@Override
	public synchronized boolean remove(Player player, int id) {
		PlayerABEntry entries = entry.get(id);
		if (entries != null) {
			entries.setPersistentState(PersistentState.DELETED);
			entry.remove(id);
			DAOManager.getDAO(PlayerABDAO.class).delete(player.getObjectId(), id);
		}
		return entry != null;
	}
	
	@Override
	public int size() {
		return entry.size();
	}
}