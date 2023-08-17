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
package com.aionemu.gameserver.model.templates.vortex;

import com.aionemu.gameserver.model.Race;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Vortex")
public class VortexTemplate
{
	@XmlAttribute(name = "id")
	protected int id;
	
	@XmlAttribute(name = "defends_race")
	protected Race dRace;
	
	@XmlAttribute(name = "offence_race")
	protected Race oRace;
	
	@XmlElement(name = "home_point")
	protected HomePoint home;
	
	@XmlElement(name = "resurrection_point")
	protected ResurrectionPoint resurrection;
	
	@XmlElement(name = "start_point")
	protected StartPoint start;
	
	public int getId() {
		return this.id;
	}
	
	public Race getDefendersRace() {
		return this.dRace;
	}
	
	public Race getInvadersRace() {
		return this.oRace;
	}
	
	public HomePoint getHomePoint() {
		return home;
	}
	
	public ResurrectionPoint getResurrectionPoint() {
		return resurrection;
	}
	
	public StartPoint getStartPoint() {
		return start;
	}
}