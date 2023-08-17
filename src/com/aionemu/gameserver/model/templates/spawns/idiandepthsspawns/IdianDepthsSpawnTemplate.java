/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.model.templates.spawns.idiandepthsspawns;

import com.aionemu.gameserver.model.idiandepths.IdianDepthsStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

/**
 * @author Rinzler (Encom)
 */

public class IdianDepthsSpawnTemplate extends SpawnTemplate
{
	private int id;
	private IdianDepthsStateType idianDepthsType;
	
	public IdianDepthsSpawnTemplate(SpawnGroup2 spawnGroup, SpawnSpotTemplate spot) {
		super(spawnGroup, spot);
	}
	
	public IdianDepthsSpawnTemplate(SpawnGroup2 spawnGroup, float x, float y, float z, byte heading, int randWalk, String walkerId, int entityId, int fly) {
		super(spawnGroup, x, y, z, heading, randWalk, walkerId, entityId, fly);
	}
	
	public int getId() {
		return id;
	}
	
	public IdianDepthsStateType getIStateType() {
		return idianDepthsType;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setIStateType(IdianDepthsStateType idianDepthsType) {
		this.idianDepthsType = idianDepthsType;
	}
	
	public final boolean isIdianDepthsOpen() {
		return idianDepthsType.equals(IdianDepthsStateType.OPEN);
	}
	
	public final boolean isIdianDepthsClosed() {
		return idianDepthsType.equals(IdianDepthsStateType.CLOSED);
	}
}