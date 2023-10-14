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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Dr.Nism
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "charge")
public class ChargeTemplate {

	@XmlAttribute(name = "skill_id")
	private int skill_id;

	@XmlAttribute(name = "time")
	private int time;

	/**
	 * @return the Id
	 */
	public int getSkillId() {
		return skill_id;
	}

	/**
	 * @return the Time
	 */
	public int getTime() {
		return time;
	}
}