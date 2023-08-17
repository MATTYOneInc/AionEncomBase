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
package com.aionemu.gameserver.model.templates.spawns.zorshivdredgionspawns;

import com.aionemu.gameserver.model.templates.spawns.Spawn;
import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionStateType;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ZorshivDredgionSpawn")
public class ZorshivDredgionSpawn
{
	@XmlAttribute(name = "id")
	private int id;
	
	public int getId() {
		return id;
	}
	
	@XmlElement(name = "zorshiv_dredgion_type")
	private List<ZorshivDredgionSpawn.ZorshivDredgionStateTemplate> ZorshivDredgionStateTemplate;
	
	public List<ZorshivDredgionStateTemplate> getSiegeModTemplates() {
		return ZorshivDredgionStateTemplate;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "ZorshivDredgionStateTemplate")
	public static class ZorshivDredgionStateTemplate {
	
		@XmlElement(name = "spawn")
		private List<Spawn> spawns;
		
		@XmlAttribute(name = "zstate")
		private ZorshivDredgionStateType zorshivDredgionType;
		
		public List<Spawn> getSpawns() {
			return spawns;
		}
		
		public ZorshivDredgionStateType getZorshivDredgionType() {
			return zorshivDredgionType;
		}
	}
}