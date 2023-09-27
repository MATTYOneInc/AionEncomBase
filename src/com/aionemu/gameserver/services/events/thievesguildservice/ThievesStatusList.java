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
package com.aionemu.gameserver.services.events.thievesguildservice;

import java.sql.Timestamp;

/**
 * @author Rinzler (Encom)
 */
public class ThievesStatusList {
	
	private int playerId;
	private int rankId;
	private int thievesCount;
	private Long lastThievesKinah;
	private int prisonCount;
	private String revengeName;
	private int revengeCount;
	private Timestamp revengeDate;
	
	public ThievesStatusList() {
	}

	public ThievesStatusList(int playerId, int rankId, int thievesCount, Long lastThievesKinah, int prisonCount, String revengeName, int revengeCount, Timestamp revengeDate) {
		this.playerId = playerId;
		this.rankId = rankId;
		this.thievesCount = thievesCount;
		this.lastThievesKinah = lastThievesKinah;
		this.prisonCount = prisonCount;
		this.revengeName = revengeName;
		this.revengeCount = revengeCount;
		this.revengeDate = revengeDate;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getRankId() {
		return rankId;
	}

	public void setRankId(int rankId) {
		this.rankId = rankId;
	}

	public int getThievesCount() {
		return thievesCount;
	}

	public void setThievesCount(int thievesCount) {
		this.thievesCount = thievesCount;
	}

	public Long getLastThievesKinah() {
		return lastThievesKinah;
	}

	public void setLastThievesKinah(Long lastThievesKinah) {
		this.lastThievesKinah = lastThievesKinah;
	}

	public int getPrisonCount() {
		return prisonCount;
	}

	public void setPrisonCount(int prisonCount) {
		this.prisonCount = prisonCount;
	}

	public String getRevengeName() {
		return revengeName;
	}

	public void setRevengeName(String revengeName) {
		this.revengeName = revengeName;
	}

	public int getRevengeCount() {
		return revengeCount;
	}

	public void setRevengeCount(int revengeCount) {
		this.revengeCount = revengeCount;
	}

	public Timestamp getRevengeDate() {
		return revengeDate;
	}

	public void setRevengeDate(Timestamp revengeDate) {
		this.revengeDate = revengeDate;
	}
}