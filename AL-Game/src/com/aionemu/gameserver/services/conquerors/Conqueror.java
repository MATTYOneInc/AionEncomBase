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
package com.aionemu.gameserver.services.conquerors;

import com.aionemu.gameserver.model.gameobjects.player.Player;

public class Conqueror {
	private Player owner;
	private int killerRank;
	public int victims;

	public Conqueror(Player owner) {
		this.owner = owner;
	}

	public void refreshOwner(Player player) {
		owner = player;
	}

	public Player getOwner() {
		return owner;
	}

	public void setRank(int rank) {
		killerRank = rank;
	}

	public int getRank() {
		return killerRank;
	}
}