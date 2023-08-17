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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Godstone")
public class GodstoneInfo
{
	@XmlAttribute
	private int skillid;
	@XmlAttribute
	private int skilllvl;
	@XmlAttribute
	private int probability;
	@XmlAttribute
	private int probabilityleft;
	@XmlAttribute
	private int breakprob;
	@XmlAttribute
	private int breakcount;
	
	public int getSkillid() {
		return skillid;
	}
	
	public int getSkilllvl() {
		return skilllvl;
	}
	
	public int getProbability() {
		return probability;
	}
	
	public int getProbabilityleft() {
		return probabilityleft;
	}
	
	public int getBreakprob() {
		return breakprob;
	}
	
	public int getBreakcount() {
		return breakcount;
	}
}