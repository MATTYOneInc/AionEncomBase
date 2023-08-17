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
package com.aionemu.gameserver.services.protectors;

import com.aionemu.gameserver.model.gameobjects.player.Player;

public class Protector
{
	public int victims;
	private Player owner;
	private int guardType;
	private int guardRank;
	
	public Protector(Player owner) {
		this.owner = owner;
	}
	
	public void refreshOwner(Player player) {
		owner = player;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public void setRank(int rank) {
		guardRank = rank;
	}
	
	public int getRank() {
		return guardRank;
	}
	
	public void setType(int type) {
		guardType = type;
	}
	
	public int getType() {
		return guardType;
	}
}