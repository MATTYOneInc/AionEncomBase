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
package com.aionemu.gameserver.model.gameobjects.base;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.basespawns.BaseSpawnTemplate;

/**
 * @author Ranastic
 */

public class BaseNpc extends Npc
{
	private int baseId;
	
	public BaseNpc(int objId, NpcController controller, BaseSpawnTemplate spawnTemplate, NpcTemplate objectTemplate) {
		super(objId, controller, spawnTemplate, objectTemplate);
		this.baseId = spawnTemplate.getId();
	}
	
	public int getBaseId() {
		return baseId;
	}
	
	@Override
	public BaseSpawnTemplate getSpawn() {
		return (BaseSpawnTemplate) super.getSpawn();
	}
	
	@Override
	public boolean isEnemyFrom(Creature creature) {
		if (creature instanceof BaseNpc) {
			return true;
		} else {
			return super.isEnemyFrom(creature);
		}
	}
}