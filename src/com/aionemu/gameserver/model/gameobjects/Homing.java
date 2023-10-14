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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.HomingGameStats;
import com.aionemu.gameserver.model.stats.container.NpcLifeStats;
import com.aionemu.gameserver.model.templates.item.ItemAttackType;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;

public class Homing extends SummonedObject<Creature>
{
	private int attackCount;
	private int skillId;
	private int activeSkillId;
	
	public Homing(int objId, NpcController controller, SpawnTemplate spawnTemplate, NpcTemplate objectTemplate, byte level, int skillId) {
		super(objId, controller, spawnTemplate, objectTemplate, level);
		this.skillId = skillId;
	}
	
	@Override
	protected void setupStatContainers(byte level) {
		setGameStats(new HomingGameStats(this));
		setLifeStats(new NpcLifeStats(this));
	}
	
	public void setAttackCount(int attackCount) {
		this.attackCount = attackCount;
	}
	
	public int getAttackCount() {
		return attackCount;
	}
	
	@Override
	public boolean isEnemy(Creature creature) {
		return getCreator().isEnemy(creature);
	}
	
	@Override
	public boolean isEnemyFrom(Player player) {
		return getCreator() != null ? getCreator().isEnemyFrom(player) : false;
	}
	
	@Override
	public NpcObjectType getNpcObjectType() {
		return NpcObjectType.HOMING;
	}
	
	@Override
	public String getMasterName() {
		return StringUtils.EMPTY;
	}
	
	@Override
	public ItemAttackType getAttackType() {
		if ((getName().contains("wind")) || (getName().contains("cyclone"))) {
			return ItemAttackType.MAGICAL_WIND;
		}
		return ItemAttackType.PHYSICAL;
	}
	
	public int getSkillId() {
		return skillId;
	}
	
	public int getActiveSkillId() {
		return activeSkillId;
	}
	
	public void setActiveSkillId(int activeSkillId) {
		this.activeSkillId = activeSkillId;
	}
}