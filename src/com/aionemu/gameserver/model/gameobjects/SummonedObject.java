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

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
import com.aionemu.gameserver.model.stats.container.SummonedObjectGameStats;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

/**
 * @author ATracer
 */
public class SummonedObject<T extends VisibleObject> extends Npc {

	private byte level;

	/**
	 * Creator of this SummonedObject
	 */
	private T creator;

	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate
	 * @param level
	 */
	public SummonedObject(int objId, NpcController controller, SpawnTemplate spawnTemplate, NpcTemplate objectTemplate,
			byte level) {
		super(objId, controller, spawnTemplate, objectTemplate, level);
		this.level = level;
	}

	@Override
	protected void setupStatContainers(byte level) {
		setGameStats(new SummonedObjectGameStats(this));
		setLifeStats(new NpcLifeStats(this));
	}

	@Override
	public byte getLevel() {
		return this.level;
	}

	@Override
	public T getCreator() {
		return creator;
	}

	public void setCreator(T creator) {
		if (creator instanceof Player) {
			((Player) creator).setSummonedObj(this);
		}
		this.creator = creator;
	}

	@Override
	public String getMasterName() {
		return creator != null ? creator.getName() : StringUtils.EMPTY;
	}

	@Override
	public int getCreatorId() {
		return creator != null ? creator.getObjectId() : 0;
	}

	@Override
	public Creature getActingCreature() {
		if (creator instanceof Creature) {
			return (Creature) getCreator();
		}
		return this;
	}

	@Override
	public Creature getMaster() {
		if (creator instanceof Creature) {
			return (Creature) getCreator();
		}
		return this;
	}

	@Override
	public Race getRace() {
		if (creator instanceof Creature) {
			return creator != null ? ((Creature) creator).getRace() : Race.NONE;
		}
		return super.getRace();
	}
}