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
package com.aionemu.gameserver.questEngine.handlers.models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Monster")
public class Monster {
	@XmlAttribute(name = "var", required = true)
	protected int var;

	@XmlAttribute(name = "start_var")
	protected Integer startVar;

	@XmlAttribute(name = "end_var", required = true)
	protected int endVar;

	@XmlAttribute(name = "npc_ids", required = true)
	protected List<Integer> npcIds;

	public int getVar() {
		return var;
	}

	public Integer getStartVar() {
		return startVar;
	}

	public int getEndVar() {
		return endVar;
	}

	public List<Integer> getNpcIds() {
		return npcIds;
	}
}