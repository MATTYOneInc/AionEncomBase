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
package com.aionemu.gameserver.model.instance.playerreward;

import com.aionemu.gameserver.model.autogroup.AGPlayer;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

import java.util.List;

public class HarmonyGroupReward extends PvPArenaPlayerReward
{
	private int id;
	private List<AGPlayer> players;
	
	public HarmonyGroupReward(Integer object, int timeBonus, byte buffId, List<AGPlayer> players) {
		super(object, timeBonus, buffId);
		this.players = players;
		id = IDFactory.getInstance().nextId();
	}
	
	public List<AGPlayer> getAGPlayers() {
		return players;
	}
	
	public boolean containPlayer(Integer object) {
		for (AGPlayer agp : players) {
			if (agp.getObjectId().equals(object)) {
				return true;
			}
		}
		return false;
	}
	
	public AGPlayer getAGPlayer(Integer object) {
		for (AGPlayer agp : players) {
			if (agp.getObjectId().equals(object)) {
				return agp;
			}
		}
		return null;
	}
	
	public int getId() {
        return id;
    }
}