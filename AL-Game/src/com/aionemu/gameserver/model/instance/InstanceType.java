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
package com.aionemu.gameserver.model.instance;

public enum InstanceType {
	LF1, SCORE, ARENA, NORMAL, INVASION, DREADGION, ARENA_PVP, TOURNAMENT, ARENA_TEAM, TIME_ATTACK, BATTLEFIELD;

	public boolean isDarkPoetaInstance() {
		return this.equals(InstanceType.LF1);
	}

	public boolean isScoreInstance() {
		return this.equals(InstanceType.SCORE);
	}

	public boolean isArenaInstance() {
		return this.equals(InstanceType.ARENA);
	}

	public boolean isNormalInstance() {
		return this.equals(InstanceType.NORMAL);
	}

	public boolean isInvasionInstance() {
		return this.equals(InstanceType.INVASION);
	}

	public boolean isDreadgionInstance() {
		return this.equals(InstanceType.DREADGION);
	}

	public boolean isArenaPvPInstance() {
		return this.equals(InstanceType.ARENA_PVP);
	}

	public boolean isTournamentInstance() {
		return this.equals(InstanceType.TOURNAMENT);
	}

	public boolean isArenaTeamInstance() {
		return this.equals(InstanceType.ARENA_TEAM);
	}

	public boolean isTimeAttackInstance() {
		return this.equals(InstanceType.TIME_ATTACK);
	}

	public boolean isBattlefieldInstance() {
		return this.equals(InstanceType.BATTLEFIELD);
	}
}