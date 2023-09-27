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

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.SkillSkinTemplate;

@XmlRootElement(name="skill_skins")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillSkinData {

	@XmlElement(name="skill_skin")
	private List<SkillSkinTemplate> sst;
	private TIntObjectHashMap<SkillSkinTemplate> skillskins;
  
	void afterUnmarshal(Unmarshaller u, Object parent) {
		skillskins = new TIntObjectHashMap<SkillSkinTemplate>();
		for (SkillSkinTemplate st : sst) {
			skillskins.put(st.getId(), st);
		}
		sst = null;
	}
  
	public SkillSkinTemplate getSkillSkinTemplate(int skinId) {
		return skillskins.get(skinId);
	}
  
	public int size() {
		return skillskins.size();
	}
}