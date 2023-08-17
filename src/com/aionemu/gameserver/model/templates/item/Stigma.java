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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rinzler
 */

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Stigma")
public class Stigma
{
	@XmlElement(name = "require_skill")
	protected List<RequireSkill> requireSkill;
	
	@XmlAttribute
	protected List<String> skill;
	
	@XmlAttribute
	protected int shard;
	
	public List<StigmaSkill> getSkills() {
		List<StigmaSkill> list = new ArrayList<StigmaSkill>();
		for (String st : skill) {
			String[] array = st.split(":");
			list.add(new StigmaSkill(Integer.parseInt(array[0]), Integer.parseInt(array[1])));
		}
		return list;
	}
	
	public List<Integer> getSkillIdOnly() {
		List<Integer> ids = new ArrayList<Integer>();
		List<String> skill = this.skill;
		if (skill.size() != 1) {
			String[] tempArray = new String[0];
			for (String parts : skill){
				tempArray = parts.split(":");
				ids.add(Integer.parseInt(tempArray[1]));
			}
			return ids;
		} for (String st : this.skill) {
			String[] array = st.split(":");
			ids.add(Integer.parseInt(array[1]));
		}
		return ids;
	}
	
	public int getShard() {
		return shard;
	}
	
	public List<RequireSkill> getRequireSkill() {
		if (requireSkill == null) {
			requireSkill = new ArrayList<RequireSkill>();
		}
		return this.requireSkill;
	}
	
	public static class StigmaSkill {
		private int skillId;
		private int skillLvl;
		
		public StigmaSkill(int skillLvl, int skillId) {
			this.skillId = skillId;
			this.skillLvl = skillLvl;
		}
		
		public int getSkillLvl() {
			return this.skillLvl;
		}
		
		public int getSkillId() {
			return this.skillId;
		}
	}
}