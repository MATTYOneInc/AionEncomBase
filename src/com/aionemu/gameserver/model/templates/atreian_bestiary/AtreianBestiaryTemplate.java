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
package com.aionemu.gameserver.model.templates.atreian_bestiary;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Ranastic
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AtreianBestiaryTemplate")
public class AtreianBestiaryTemplate {

	@XmlAttribute(name = "id")
	private int id;
	
	@XmlAttribute(name = "level")
	private int level;
	
	@XmlAttribute(name = "name")
	private String name;
	
	@XmlAttribute(name = "npc_ids")
	private List<Integer> npc_ids;
	
	@XmlAttribute(name = "type")
	private BookType type;
	
	@XmlElement(name = "achievement")
	private List<AtreianBestiaryAchievementTemplate> achievement;
	
	public int getId() {
		return id;
	}
	
	public int getLevel() {
		return level;
	}
	
	public BookType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}

	public List<Integer> getNpcIds() {
        if (npc_ids == null) {
        	npc_ids = Collections.emptyList();
        }
        return this.npc_ids;
    }
	
	public List<AtreianBestiaryAchievementTemplate> getAtreianBestiaryAchievementTemplate() {
		return achievement;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "AtreianBestiaryAchievementTemplate")
	public static class AtreianBestiaryAchievementTemplate {
	
		@XmlAttribute(name = "condition")
		private int condition;
		
		@XmlAttribute(name = "exp")
		private int exp;
		
		public int getKillCondition() {
			return condition;
		}
		
		public int getRewardExp() {
			return exp;
		}
	}
	
	@XmlType(name = "BookType")
	@XmlEnum
	public enum BookType
	{
		NORMAL(),
		HERO();
	}
}