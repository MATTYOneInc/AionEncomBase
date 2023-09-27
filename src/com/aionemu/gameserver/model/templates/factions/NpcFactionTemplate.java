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
package com.aionemu.gameserver.model.templates.factions;

import com.aionemu.gameserver.model.Race;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NpcFaction")
public class NpcFactionTemplate
{
	@XmlAttribute(name = "id", required = true)
	protected int id;
	
	@XmlAttribute(name = "name")
	protected String name;
	
	@XmlAttribute(name = "nameId")
	protected int nameId;
	
	@XmlAttribute(name = "category")
	protected FactionCategory category;
	
	@XmlAttribute(name = "minlevel")
	protected Integer minlevel;
	
	@XmlAttribute(name = "maxlevel")
	protected int maxlevel = 99;
	
	@XmlAttribute(name = "auto_join")
	protected Integer autoJoin;
	
	@XmlAttribute(name = "auto_quit")
	protected int autoQuit = 40;
	
	@XmlAttribute(name = "race")
	protected Race race;
	
	@XmlAttribute(name = "npcid")
	protected int npcId;
	
	@XmlAttribute(name = "skill_points")
	protected int skillPoints;
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getNameId() {
		return nameId;
	}
	
	public FactionCategory getCategory() {
		return category;
	}
	
	public int getMinLevel() {
		return minlevel;
	}
	
	public int getMaxLevel() {
		return maxlevel;
	}
	
	public Race getRace() {
		return race;
	}
	
	public boolean isMentor(){
		return category == FactionCategory.MENTOR;
	}
	
	public int getNpcId() {
		return npcId;
	}
	
	public int getSkillPoints() {
		return skillPoints;
	}
	
	public int getAutoJoin() {
		return autoJoin;
	}
	
	public int getAutoQuit() {
		return autoQuit;
	}
}