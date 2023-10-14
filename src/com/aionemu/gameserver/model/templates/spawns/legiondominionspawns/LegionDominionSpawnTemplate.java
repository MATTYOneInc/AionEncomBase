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
package com.aionemu.gameserver.model.templates.spawns.legiondominionspawns;

import com.aionemu.gameserver.model.legiondominion.LegionDominionModType;
import com.aionemu.gameserver.model.legiondominion.LegionDominionRace;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

public class LegionDominionSpawnTemplate extends SpawnTemplate {
	private int legionDominionId;
	private LegionDominionRace legionDominionRace;
	private LegionDominionModType legionDominionModType;

	public LegionDominionSpawnTemplate(SpawnGroup2 spawnGroup, SpawnSpotTemplate spot) {
		super(spawnGroup, spot);
	}

	public LegionDominionSpawnTemplate(SpawnGroup2 spawnGroup, float x, float y, float z, byte heading, int randWalk,
			String walkerId, int entityId, int fly) {
		super(spawnGroup, x, y, z, heading, randWalk, walkerId, entityId, fly);
	}

	public int getLegionDominionId() {
		return legionDominionId;
	}

	public LegionDominionRace getLegionDominionRace() {
		return legionDominionRace;
	}

	public LegionDominionModType getLegionDominionModType() {
		return legionDominionModType;
	}

	public void setLegionDominionId(int legionDominionId) {
		this.legionDominionId = legionDominionId;
	}

	public void setLegionDominionRace(LegionDominionRace legionDominionRace) {
		this.legionDominionRace = legionDominionRace;
	}

	public void setLegionDominionModType(LegionDominionModType legionDominionModType) {
		this.legionDominionModType = legionDominionModType;
	}

	public final boolean isPeace() {
		return legionDominionModType.equals(LegionDominionModType.PEACE);
	}

	public final boolean isDominion() {
		return legionDominionModType.equals(LegionDominionModType.DOMINION);
	}
}