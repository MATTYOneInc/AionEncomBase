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
package com.aionemu.gameserver.model.team.legion;

/**
 * @author Simple
 */
public enum LegionRank {
	/** All Legion Ranks **/
	BRIGADE_GENERAL(0), DEPUTY(1), CENTURION(2), LEGIONARY(3), VOLUNTEER(4);

	private byte rank;

	private LegionRank(int rank) {
		this.rank = (byte) rank;
	}

	/**
	 * Returns client-side id for this
	 * 
	 * @return byte
	 */
	public byte getRankId() {
		return this.rank;
	}
}