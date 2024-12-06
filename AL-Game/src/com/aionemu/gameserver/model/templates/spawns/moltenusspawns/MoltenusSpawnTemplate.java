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
package com.aionemu.gameserver.model.templates.spawns.moltenusspawns;

import com.aionemu.gameserver.model.moltenus.MoltenusStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

/**
 * @author Rinzler (Encom)
 */

public class MoltenusSpawnTemplate extends SpawnTemplate {
	private int id;
	private MoltenusStateType moltenusType;

	public MoltenusSpawnTemplate(SpawnGroup2 spawnGroup, SpawnSpotTemplate spot) {
		super(spawnGroup, spot);
	}

	public MoltenusSpawnTemplate(SpawnGroup2 spawnGroup, float x, float y, float z, byte heading, int randWalk,
			String walkerId, int entityId, int fly) {
		super(spawnGroup, x, y, z, heading, randWalk, walkerId, entityId, fly);
	}

	public int getId() {
		return id;
	}

	public MoltenusStateType getMStateType() {
		return moltenusType;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMStateType(MoltenusStateType moltenusType) {
		this.moltenusType = moltenusType;
	}

	public final boolean isFight() {
		return moltenusType.equals(MoltenusStateType.FIGHT);
	}

	public final boolean isPeace() {
		return moltenusType.equals(MoltenusStateType.PEACE);
	}
}