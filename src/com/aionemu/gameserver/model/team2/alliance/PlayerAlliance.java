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
package com.aionemu.gameserver.model.team2.alliance;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.TeamType;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.team2.league.League;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.google.common.base.Preconditions;

public class PlayerAlliance extends TemporaryPlayerTeam<PlayerAllianceMember> {
	private final Map<Integer, PlayerAllianceGroup> groups = new HashMap<Integer, PlayerAllianceGroup>();
	private final List<Integer> viceCaptainIds = new CopyOnWriteArrayList<Integer>();
	private int allianceReadyStatus;
	private TeamType type;
	private League league;
	private int killCount = 0;
	private int bgIndex = -1;

	public PlayerAlliance(PlayerAllianceMember leader, TeamType type) {
		super(IDFactory.getInstance().nextId());
		this.type = type;
		initializeTeam(leader);
		for (int groupId = 1000; groupId <= 1003; groupId++) {
			groups.put(groupId, new PlayerAllianceGroup(this, groupId));
		}
	}

	@Override
	public void addMember(PlayerAllianceMember member) {
		super.addMember(member);
		PlayerAllianceGroup openAllianceGroup = getOpenAllianceGroup();
		openAllianceGroup.addMember(member);
	}

	@Override
	public void removeMember(PlayerAllianceMember member) {
		super.removeMember(member);
		member.getPlayerAllianceGroup().removeMember(member);
	}

	@Override
	public boolean isFull() {
		return size() == 24;
	}

	@Override
	public int getMinExpPlayerLevel() {
		return 0;
	}

	@Override
	public int getMaxExpPlayerLevel() {
		return 0;
	}

	public PlayerAllianceGroup getOpenAllianceGroup() {
		lock();
		try {
			for (int groupId = 1000; groupId <= 1003; groupId++) {
				PlayerAllianceGroup playerAllianceGroup = groups.get(groupId);
				if (!playerAllianceGroup.isFull()) {
					return playerAllianceGroup;
				}
			}
		} finally {
			unlock();
		}
		throw new IllegalStateException("All alliance groups are full.");
	}

	public PlayerAllianceGroup getAllianceGroup(Integer allianceGroupId) {
		PlayerAllianceGroup allianceGroup = groups.get(allianceGroupId);
		Preconditions.checkNotNull(allianceGroup, "No such alliance group " + allianceGroupId);
		return allianceGroup;
	}

	public final List<Integer> getViceCaptainIds() {
		return viceCaptainIds;
	}

	public final boolean isViceCaptain(Player player) {
		return viceCaptainIds.contains(player.getObjectId());
	}

	public final boolean isSomeCaptain(Player player) {
		return isLeader(player) || isViceCaptain(player);
	}

	public int getAllianceReadyStatus() {
		return allianceReadyStatus;
	}

	public void setAllianceReadyStatus(int allianceReadyStatus) {
		this.allianceReadyStatus = allianceReadyStatus;
	}

	public final League getLeague() {
		return league;
	}

	public final void setLeague(League league) {
		this.league = league;
	}

	public final boolean isInLeague() {
		return this.league != null;
	}

	public final int groupSize() {
		return groups.size();
	}

	public final Collection<PlayerAllianceGroup> getGroups() {
		return groups.values();
	}

	public TeamType getTeamType() {
		return type;
	}

	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}

	public int getKillCount() {
		return killCount;
	}

	public void setBgIndex(int bgIndex) {
		this.bgIndex = bgIndex;
	}

	public int getBgIndex() {
		return bgIndex;
	}
}