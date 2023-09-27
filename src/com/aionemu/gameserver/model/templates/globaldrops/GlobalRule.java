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
package com.aionemu.gameserver.model.templates.globaldrops;

import javax.xml.bind.annotation.*;

/**
 * @author Wnkrz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GlobalRule")
public class GlobalRule
{
	@XmlElement(name = "gd_items", required = false)
	protected GlobalDropItems gdItems;
	
	@XmlElement(name = "gd_maps", required = false)
	protected GlobalDropMaps gdMaps;
	
	@XmlElement(name = "gd_races", required = false)
	protected GlobalDropRaces gdRaces;
	
	@XmlElement(name = "gd_tribes", required = false)
	protected GlobalDropTribes gdTribes;
	
	@XmlElement(name = "gd_ratings", required = false)
	protected GlobalDropRatings gdRatings;
	
	@XmlElement(name = "gd_worlds", required = false)
	protected GlobalDropWorlds gdWorlds;
	
	@XmlElement(name = "gd_npcs", required = false)
	protected GlobalDropNpcs gdNpcs;
	
	@XmlElement(name = "gd_zones", required = false)
	protected GlobalDropZones gdZones;
	
	@XmlAttribute(name = "rule_name", required = true)
	protected String ruleName;
	
	@XmlAttribute(name = "min_count")
	protected Long minCount;
	
	@XmlAttribute(name = "max_count")
	protected Long maxCount;
	
	@XmlAttribute(name = "base_chance", required = true)
	protected float chance;
	
	@XmlAttribute(name = "min_diff")
	protected int minDiff;
	
	@XmlAttribute(name = "max_diff")
	protected int maxDiff;
	
	@XmlAttribute(name = "restriction_race")
	protected RestrictionRace restrictionRace;
	
	@XmlAttribute(name = "no_reduction")
	protected boolean noReduction;
	
	public GlobalDropItems getGlobalRuleItems() {
		return gdItems;
	}
	
	public void setItems(GlobalDropItems value) {
		this.gdItems = value;
	}
	
	public GlobalDropWorlds getGlobalRuleWorlds() {
		return gdWorlds;
	}
	
	public void setWorlds(GlobalDropWorlds value) {
		this.gdWorlds = value;
	}
	
	public GlobalDropRaces getGlobalRuleRaces() {
		return gdRaces;
	}
	
	public void setNpcRaces(GlobalDropRaces value) {
		this.gdRaces = value;
	}
	
	public GlobalDropRatings getGlobalRuleRatings() {
		return gdRatings;
	}
	
	public void setNpcRatings(GlobalDropRatings value) {
		this.gdRatings = value;
	}
	
	public GlobalDropMaps getGlobalRuleMaps() {
		return gdMaps;
	}
	
	public void setMaps(GlobalDropMaps value) {
		this.gdMaps = value;
	}
	
	public GlobalDropTribes getGlobalRuleTribes() {
		return gdTribes;
	}
	
	public void setNpcTribes(GlobalDropTribes value) {
		this.gdTribes = value;
	}
	
	public GlobalDropNpcs getGlobalRuleNpcs() {
		return gdNpcs;
	}
	
	public void setNpcs(GlobalDropNpcs value) {
		this.gdNpcs = value;
	}
	
	public GlobalDropZones getGlobalRuleZones() {
		return gdZones;
	}
	
	public void setZones(GlobalDropZones value) {
		this.gdZones = value;
	}
	
	public String getRuleName() {
		return ruleName;
	}
	
	public void setRuleName(String value) {
		this.ruleName = value;
	}
	
	public long getMinCount() {
		if (minCount == null) {
			return 1L;
		} else {
			return minCount;
		}
	}
	
	public void setMinCount(Long value) {
		this.minCount = value;
	}
	
	public long getMaxCount() {
		if (maxCount == null) {
			return 1L;
		} else {
			return maxCount;
		}
	}
	
	public void setMaxCount(Long value) {
		this.maxCount = value;
	}
	
	public float getChance() {
		return chance;
	}
	
	public void setChance(float value) {
		this.chance = value;
	}
	
	public int getMinDiff() {
		return minDiff;
	}
	
	public void setMinDiff(int value) {
		this.minDiff = value;
	}
	
	public int getMaxDiff() {
		return maxDiff;
	}
	
	public void setMaxDiff(int value) {
		this.maxDiff = value;
	}
	
	public RestrictionRace getRestrictionRace() {
		return restrictionRace;
	}
	
	public void setRestrictionRace(RestrictionRace value) {
		this.restrictionRace = value;
	}
	
	public boolean getNoReduction() {
		return noReduction;
	}
	
	public void setNoReduction(boolean value) {
		this.noReduction = value;
	}
	
	@XmlType(name = "race_restriction")
	@XmlEnum
	public enum RestrictionRace {
		ASMODIANS, ELYOS
	}
}