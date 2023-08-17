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
package com.aionemu.gameserver.model.templates.spawns.zorshivdredgionspawns;

import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionStateType;

/**
 * @author Rinzler (Encom)
 */

public class ZorshivDredgionSpawnTemplate extends SpawnTemplate
{
	private int id;
	private ZorshivDredgionStateType zorshivDredgionType;
	
	public ZorshivDredgionSpawnTemplate(SpawnGroup2 spawnGroup, SpawnSpotTemplate spot) {
		super(spawnGroup, spot);
	}
	
	public ZorshivDredgionSpawnTemplate(SpawnGroup2 spawnGroup, float x, float y, float z, byte heading, int randWalk, String walkerId, int entityId, int fly) {
		super(spawnGroup, x, y, z, heading, randWalk, walkerId, entityId, fly);
	}
	
	public int getId() {
		return id;
	}
	
	public ZorshivDredgionStateType getZStateType() {
		return zorshivDredgionType;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setZStateType(ZorshivDredgionStateType zorshivDredgionType) {
		this.zorshivDredgionType = zorshivDredgionType;
	}
	
	public final boolean isLanding() {
		return zorshivDredgionType.equals(ZorshivDredgionStateType.LANDING);
	}
	
	public final boolean isPeace() {
		return zorshivDredgionType.equals(ZorshivDredgionStateType.PEACE);
	}
}