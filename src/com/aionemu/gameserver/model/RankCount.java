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
package com.aionemu.gameserver.model;

public class RankCount {
	private int playerId;
	private int ap;
	private int gp;
	private Race race;

	public RankCount(int playerId, int ap, int gp, Race race) {
		this.playerId = playerId;
		this.ap = ap;
		this.gp = gp;
		this.race = race;
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getPlayerAP() {
		return ap;
	}

	public int getPlayerGP() {
		return gp;
	}

	public Race getPlayerRace() {
		return race;
	}
}