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

import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;

/**
 * @author ATracer
 */
public class PlayerAllianceGroup extends TemporaryPlayerTeam<PlayerAllianceMember> {

	private final PlayerAlliance alliance;

	public PlayerAllianceGroup(PlayerAlliance alliance, Integer objId) {
		super(objId);
		this.alliance = alliance;
	}

	@Override
	public void addMember(PlayerAllianceMember member) {
		super.addMember(member);
		member.setPlayerAllianceGroup(this);
		member.setAllianceId(getTeamId());
	}

	@Override
	public void removeMember(PlayerAllianceMember member) {
		super.removeMember(member);
		member.setPlayerAllianceGroup(null);
	}

	@Override
	public boolean isFull() {
		return size() == 6;
	}

	@Override
	public int getMinExpPlayerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMaxExpPlayerLevel() {
		// TODO Auto-generated method stub
		return 0;
	}

	public PlayerAlliance getAlliance() {
		return alliance;
	}
}