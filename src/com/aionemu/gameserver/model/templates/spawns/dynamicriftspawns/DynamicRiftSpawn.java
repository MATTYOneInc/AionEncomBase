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
package com.aionemu.gameserver.model.templates.spawns.dynamicriftspawns;

import com.aionemu.gameserver.model.dynamicrift.DynamicRiftStateType;
import com.aionemu.gameserver.model.templates.spawns.Spawn;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DynamicRiftSpawn")
public class DynamicRiftSpawn
{
	@XmlAttribute(name = "id")
	private int id;
	
	public int getId() {
		return id;
	}
	
	@XmlElement(name = "dynamic_rift_type")
	private List<DynamicRiftSpawn.DynamicRiftStateTemplate> DynamicRiftStateTemplate;
	
	public List<DynamicRiftStateTemplate> getSiegeModTemplates() {
		return DynamicRiftStateTemplate;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "DynamicRiftStateTemplate")
	public static class DynamicRiftStateTemplate {
	
		@XmlElement(name = "spawn")
		private List<Spawn> spawns;
		
		@XmlAttribute(name = "dstate")
		private DynamicRiftStateType dynamicRiftType;
		
		public List<Spawn> getSpawns() {
			return spawns;
		}
		
		public DynamicRiftStateType getDynamicRiftType() {
			return dynamicRiftType;
		}
	}
}