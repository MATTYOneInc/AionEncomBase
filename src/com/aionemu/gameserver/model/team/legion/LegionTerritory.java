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

public class LegionTerritory {
	int territoryId = 0;
	int legionId = 0;
	String legionName = "";

	public LegionTerritory(int id) {
		this.territoryId = id;
	}

	public LegionTerritory() {
	}

	public int getId() {
		return territoryId;
	}

	public void setTerritoryId(int terretoryId) {
		this.territoryId = terretoryId;
	}

	public int getLegionId() {
		return legionId;
	}

	public void setLegionId(int legionId) {
		this.legionId = legionId;
	}

	public String getLegionName() {
		return legionName;
	}

	public void setLegionName(String legionName) {
		this.legionName = legionName;
	}
}