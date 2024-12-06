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
package com.aionemu.gameserver.dataholders;

import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.skillengine.model.SkinSkillTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastMap;

/**
 * @author Ranastic
 */
@XmlRootElement(name = "skin_skills")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkinSkillData {

	@XmlElement(name = "skin_skill")
	private List<SkinSkillTemplate> tlist;

	@XmlTransient
	private TIntObjectHashMap<SkinSkillTemplate> skinSkillData = new TIntObjectHashMap<SkinSkillTemplate>();

	private final Map<String, SkinSkillTemplate> string = new FastMap<String, SkinSkillTemplate>().shared();

	void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject) {
		for (SkinSkillTemplate skinSkill : tlist) {
			skinSkillData.put(skinSkill.getId(), skinSkill);
			string.put(skinSkill.getGroup().toUpperCase(), skinSkill);
		}
	}

	public int size() {
		return skinSkillData.size();
	}

	public SkinSkillTemplate getSkinSkillById(int id) {
		return skinSkillData.get(id);
	}

	public SkinSkillTemplate getSkinSkillByGroupName(String name) {
		return string.get(name);
	}
}