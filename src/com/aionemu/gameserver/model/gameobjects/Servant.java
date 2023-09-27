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
package com.aionemu.gameserver.model.gameobjects;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import org.apache.commons.lang.StringUtils;

/**
 * @author ATracer
 */
public class Servant extends SummonedObject<Creature>
{
	private NpcObjectType objectType;

	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param level
	 */
	public Servant(int objId, NpcController controller, SpawnTemplate spawnTemplate, NpcTemplate objectTemplate,
		byte level) {
		super(objId, controller, spawnTemplate, objectTemplate, level);
	}

	@Override
	public final boolean isEnemy(Creature creature) {
		return getCreator().isEnemy(creature);
	}

	@Override
	public boolean isEnemyFrom(Player player) {
		return getCreator() != null && getCreator().isEnemyFrom(player);
	}

	@Override
	public NpcObjectType getNpcObjectType() {
		return objectType;
	}

	public void setNpcObjectType(NpcObjectType objectType) {
		this.objectType = objectType;
	}

	@Override
    public String getMasterName() {
		return StringUtils.EMPTY;
	}
}