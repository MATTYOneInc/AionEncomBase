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
package com.aionemu.gameserver.model.templates.event;

import com.aionemu.gameserver.model.Race;

import javax.xml.bind.annotation.*;

/**
 * @author Rolandas
 */

@XmlType(name = "InventoryDrop")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryDrop
{
	@XmlValue
	private int dropItem;
	
	@XmlAttribute(name = "startlevel", required = false)
	private int startLevel;
	
	@XmlAttribute(name = "endlevel", required = false)
	private int endLevel;
	
	@XmlAttribute(name = "interval", required = true)
	private int interval;
	
	@XmlAttribute(name = "maxCountOfDay", required = false)
	private int maxCountOfDay;
	
	@XmlAttribute(name = "cleanTime", required = false)
	private int cleanTime;
	
	@XmlAttribute
	private Race race = Race.PC_ALL;
	
	public Race getRace() {
		return race;
	}
	
	public int getDropItem() {
		return dropItem;
	}
	
	public int getStartLevel() {
		return startLevel;
	}
	
	public int getEndLevel() {
		return endLevel;
	}
	
	public int getInterval() {
		return interval;
	}
	
	public int getMaxCountOfDay() {
		return maxCountOfDay;
	}
	
	public int getCleanTime() {
		return cleanTime;
	}
}