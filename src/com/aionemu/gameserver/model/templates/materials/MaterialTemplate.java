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
package com.aionemu.gameserver.model.templates.materials;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rolandas
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MaterialTemplate", propOrder = { "skills" })
public class MaterialTemplate {

	@XmlElement(name = "skill", required = true)
	protected List<MaterialSkill> skills;

	@XmlAttribute(name = "skill_obstacle")
	protected Integer skillObstacle;

	@XmlAttribute(required = true)
	protected int id;

	public List<MaterialSkill> getSkills() {
		return skills;
	}

	public Integer getSkillObstacle() {
		return skillObstacle;
	}

	public int getId() {
		return id;
	}
}