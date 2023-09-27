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
package com.aionemu.gameserver.model.skill;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;

import java.sql.Timestamp;

/**
 * @author ATracer
 */
public abstract class SkillEntry {

	protected final int skillId;
	protected int skillLevel;
	protected int skinId;
	protected Timestamp activeSkinTime;
	protected int expireTime;
	protected boolean isActivated;

	SkillEntry(int skillId, int skillLevel, int skinId, Timestamp activeSkinTime, int expireTime, boolean isActivated) {
		this.skillId = skillId;
		this.skillLevel = skillLevel;
		this.skinId = skinId;
		this.activeSkinTime = activeSkinTime;
		this.expireTime = expireTime;
		this.isActivated = isActivated;
	}

	public final int getSkillId() {
		return skillId;
	}

	public final int getSkillLevel() {
		return skillLevel;
	}
	
	public final int getSkinId() {
		return skinId;
	}
	
	public final Timestamp getSkinActiveTime() {
		return activeSkinTime;
	}
	
	public void setSkinActiveTime(Timestamp activeSkinTime) {
		this.activeSkinTime = activeSkinTime;
	}
	
	public final int getSkinExpireTime() {
		return expireTime;
	}

	public final String getSkillName() {
		return DataManager.SKILL_DATA.getSkillTemplate(getSkillId()).getName();
	}

	public void setSkillLvl(int skillLevel) {
		this.skillLevel = skillLevel;
	}

	public final SkillTemplate getSkillTemplate() {
		return DataManager.SKILL_DATA.getSkillTemplate(getSkillId());
	}

	public void setSkinId(int skinId) {
		this.skinId = skinId;
	}
	
	public void setSkinExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}

	public boolean isActivated(){
		return isActivated;
	}

	public void setActivated(boolean activated){
		this.isActivated = activated;
	}
}