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
package com.aionemu.gameserver.skillengine.model;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "skill")
public class SkillLearnTemplate {

	@XmlAttribute(name = "classId", required = true)
	private PlayerClass classId = PlayerClass.ALL;
	
	@XmlAttribute(name = "skillId", required = true)
	private int skillId;
	
	@XmlAttribute(name = "skillLevel", required = true)
	private int skillLevel;
	
	@XmlAttribute(name = "name", required = true)
	private String name;
	
	@XmlAttribute(name = "race", required = true)
	private Race race;
	
	@XmlAttribute(name = "minLevel", required = true)
	private int minLevel;
	
	@XmlAttribute(name = "skill_group")
	private String skill_group;
	
	@XmlAttribute
    private boolean autoLearn;
	
	@XmlAttribute
	private boolean stigma = false;
	
	public PlayerClass getClassId() {
		return classId;
	}
	
	public int getSkillId() {
		return skillId;
	}
	
	public int getSkillLevel() {
		return skillLevel;
	}
	
	public String getName() {
		return name;
	}
	
	public int getMinLevel() {
		return minLevel;
	}
	
	public Race getRace() {
		return race;
	}
	
	public String getSkillGroup() {
		return skill_group;
	}
	
	public boolean isAutoLearn() {
        return autoLearn;
    }
	
	public boolean isStigma() {
		return stigma;
	}
}