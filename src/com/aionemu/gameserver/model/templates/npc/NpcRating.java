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
package com.aionemu.gameserver.model.templates.npc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.gameobjects.state.CreatureSeeState;

@XmlType(name = "rating")
@XmlEnum
public enum NpcRating {
	JUNK(CreatureSeeState.NORMAL), NORMAL(CreatureSeeState.NORMAL), ELITE(CreatureSeeState.SEARCH1),
	HERO(CreatureSeeState.SEARCH2), LEGENDARY(CreatureSeeState.SEARCH2);

	private final CreatureSeeState congenitalSeeState;

	private NpcRating(CreatureSeeState congenitalSeeState) {
		this.congenitalSeeState = congenitalSeeState;
	}

	public CreatureSeeState getCongenitalSeeState() {
		return congenitalSeeState;
	}
}