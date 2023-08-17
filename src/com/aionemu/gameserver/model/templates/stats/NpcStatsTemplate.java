/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.model.templates.stats;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "npc_stats_template")
public class NpcStatsTemplate extends StatsTemplate
{
	@XmlAttribute(name = "run_speed_fight")
	private float runSpeedFight;
	
	@XmlAttribute(name = "pdef")
	private int pdef;
	
	@XmlAttribute(name = "mdef")
	private int mdef;
	
	@XmlAttribute(name = "mresist")
	private int mresist;
	
	@XmlAttribute(name = "crit")
	private int crit;
	
	@XmlAttribute(name = "accuracy")
	private int accuracy;
	
	@XmlAttribute(name = "power")
	private int power;
	
	@XmlAttribute(name = "maxXp")
	private int maxXp;
	
	public float getRunSpeedFight() {
		return runSpeedFight;
	}
	
	public int getPdef() {
		return pdef;
	}
	
	public float getMdef() {
		return mdef;
	}
	
	public int getMresist() {
		return mresist;
	}
	
	public float getCrit() {
		return crit;
	}
	
	public float getAccuracy() {
		return accuracy;
	}
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public int getMaxXp() {
		return maxXp;
	}
}