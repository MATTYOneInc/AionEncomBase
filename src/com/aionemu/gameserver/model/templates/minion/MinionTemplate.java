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
package com.aionemu.gameserver.model.templates.minion;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.templates.BoundRadius;
import com.aionemu.gameserver.model.templates.stats.ModifiersTemplate;

/**
 * @author Falke_34
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "minion")
public class MinionTemplate {

	@XmlElement(name = "physical_attr")
	protected List<MinionStatAttribute> physicalAttr;

	@XmlElement(name = "magical_attr")
	protected List<MinionStatAttribute> magicalAttr;

	@XmlAttribute(name = "id", required = true)
	private int id;
	@XmlAttribute(name = "name", required = true)
	private String name;
	@XmlAttribute(name = "nameid", required = true)
	private int nameId;
	@XmlAttribute(name = "group_set", required = true)
	private String groupSet;
	@XmlAttribute(name = "tier_grade", required = true)
	private String tierGrade;
	@XmlAttribute(name = "star_grade", required = true)
	private int starGrade;
	@XmlAttribute(name = "is_only_feeds", required = true)
	private boolean isOnlyFeeds;
	@XmlAttribute(name = "use_func_option", required = true)
	private boolean useFuncOption;
	@XmlAttribute(name = "skill01", required = true)
	private int skill1;
	@XmlAttribute(name = "skill01_energy", required = true)
	private int skill1Energy;
	@XmlAttribute(name = "skill02", required = true)
	private int skill2;
	@XmlAttribute(name = "skill02_energy", required = true)
	private int skill2Energy;
	@XmlAttribute(name = "iserect", required = true)
	private int isErect;
	@XmlAttribute(name = "walk", required = true)
	private float walk;
	@XmlAttribute(name = "run", required = true)
	private float run;
	@XmlAttribute(name = "height", required = true)
	private float height;
	@XmlAttribute(name = "alti", required = true)
	private float alti;
	@XmlAttribute(name = "max_growth_value", required = true)
	private int maxGrowthValue;
	@XmlAttribute(name = "growth_pt", required = true)
	private int growthPt;
	@XmlAttribute(name = "growth_cost", required = true)
	private int growthCost;
	@XmlAttribute(name = "evolve_item", required = true)
	private int evolveItem;
	@XmlAttribute(name = "evolve_item_num", required = true)
	private int evolveItemNum;
	@XmlAttribute(name = "evolve_cost", required = true)
	private long evolveCost;

	public List<MinionStatAttribute> getPhysicalAttr() {
		if (physicalAttr == null) {
			physicalAttr = new ArrayList<>();
		}
		return physicalAttr;
	}

	public List<MinionStatAttribute> getMagicalAttr() {
		if (magicalAttr == null) {
			magicalAttr = new ArrayList<>();
		}
		return magicalAttr;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getNameId() {
		return nameId;
	}

	public String getGroupSet() {
		return groupSet;
	}

	public String getTierGrade() {
		return tierGrade;
	}

	public int getStarGrade() {
		return starGrade;
	}

	public boolean isOnlyFeeds() {
		return isOnlyFeeds;
	}

	public boolean isUseFuncOption() {
		return useFuncOption;
	}

	public int getSkill1() {
		return skill1;
	}

	public int getSkill1Energy() {
		return skill1Energy;
	}

	public int getSkill2() {
		return skill2;
	}

	public int getSkill2Energy() {
		return skill2Energy;
	}

	public int getIsErect() {
		return isErect;
	}

	public float getWalk() {
		return walk;
	}

	public float getRun() {
		return run;
	}

	public float getHeight() {
		return height;
	}

	public float getAlti() {
		return alti;
	}

	public int getMaxGrowthValue() {
		return maxGrowthValue;
	}

	public int getGrowthPt() {
		return growthPt;
	}

	public int getGrowthCost() {
		return growthCost;
	}

	public int getEvolveItem() {
		return evolveItem;
	}

	public int getEvolveItemNum() {
		return evolveItemNum;
	}

	public long getEvolveCost() {
		return evolveCost;
	}

	public boolean isKerub(){
		if (getGroupSet().equalsIgnoreCase("TESTFAMILIARGROUP02") ||
				getGroupSet().equalsIgnoreCase("TESTFAMILIARGROUP09")||
				getGroupSet().equalsIgnoreCase("TESTFAMILIARGROUP10") ||
				getGroupSet().equalsIgnoreCase("TESTFAMILIARGROUP11")){
			return true;
		}
		return false;
	}

}