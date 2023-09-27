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

/**
 * @author zdead
 */
public class AbyssRankingResult {

	private String playerName;
	private int playerAbyssRank;
	private int oldRankPos;
	private int rankPos;
	private int ap;
	private int gp;
	private int title;
	private PlayerClass playerClass;
	private int playerLevel;
	private int playerId;

	private String legionName;
	private int cp;
	private int legionId;
	private int legionLevel;
	private int legionMembers;

	public AbyssRankingResult(String playerName, int playerAbyssRank, int playerId, int ap, int gp,
		int title, PlayerClass playerClass, int playerLevel, String legionName, int oldRankPos, int rankPos) {
		this.playerName = playerName;
		this.playerAbyssRank = playerAbyssRank;
		this.playerId = playerId;
		this.ap = ap;
		this.gp = gp;
		this.title = title;
		this.playerClass = playerClass;
		this.playerLevel = playerLevel;
		this.legionName = legionName;
		this.oldRankPos = oldRankPos;
		this.rankPos = rankPos;
	}

	public AbyssRankingResult(int cp, String legionName, int legionId, int legionLevel, int legionMembers, int oldRankPos, int rankPos) {
		this.oldRankPos = oldRankPos;
		this.rankPos = rankPos;
		this.cp = cp;
		this.legionName = legionName;
		this.legionId = legionId;
		this.legionLevel = legionLevel;
		this.legionMembers = legionMembers;
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getPlayerId() {
		return playerId;
	}

	public int getPlayerAbyssRank() {
		return playerAbyssRank;
	}

	/**
	 * @return the oldRankPos
	 */
	public int getOldRankPos() {
		return oldRankPos;
	}
	
	public int getRankPos() {
		return rankPos;
	}

	public int getPlayerAP() {
		return ap;
	}
	
	public int getPlayerGP() {
		return gp;
	}

	public int getPlayerTitle() {
		return title;
	}

	public int getPlayerLevel() {
		return playerLevel;
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public String getLegionName() {
		return legionName;
	}

	public int getLegionCP() {
		return cp;
	}

	public int getLegionId() {
		return legionId;
	}

	public int getLegionLevel() {
		return legionLevel;
	}

	public int getLegionMembers() {
		return legionMembers;
	}
}