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

import com.aionemu.gameserver.skillengine.model.ChargeSkillTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastMap;

/**
 * @author Dr.Nism [Ranastic]
 */
@XmlRootElement(name = "charge_skills")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChargeSkillData {

	@XmlElement(name = "charge_skill")
	private List<ChargeSkillTemplate> chargeSkills;

	private TIntObjectHashMap<ChargeSkillTemplate> ids = new TIntObjectHashMap<ChargeSkillTemplate>();
	private final Map<String, ChargeSkillTemplate> setName = new FastMap<String, ChargeSkillTemplate>().shared();
	private TIntObjectHashMap<ChargeSkillTemplate> firstTemplates = new TIntObjectHashMap<ChargeSkillTemplate>();
	private TIntObjectHashMap<ChargeSkillTemplate> totalTemplates = new TIntObjectHashMap<ChargeSkillTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (ChargeSkillTemplate chargeSkill : chargeSkills) {
			ids.put(chargeSkill.getId(), chargeSkill);
			setName.put(chargeSkill.getChargeSetName(), chargeSkill);
			firstTemplates.put(chargeSkill.getFirstId(), chargeSkill);
			totalTemplates.put(chargeSkill.getFirstId(), chargeSkill);
			totalTemplates.put(chargeSkill.getSecondId(), chargeSkill);
			totalTemplates.put(chargeSkill.getThirdId(), chargeSkill);
		}
		chargeSkills = null;
	}

	public int size() {
		return ids.size();
	}

	public ChargeSkillTemplate getChargeSkillTemplateById(int id) {
		return ids.get(id);
	}

	public ChargeSkillTemplate getChargeSkillTemplateBySetName(String name) {
		return setName.get(name);
	}

	public ChargeSkillTemplate getChargeSkillTemplate1st(int skillId) {
		return firstTemplates.get(skillId);
	}

	public ChargeSkillTemplate getChargeSkillTemplateTotal(int skillId) {
		return totalTemplates.get(skillId);
	}
}