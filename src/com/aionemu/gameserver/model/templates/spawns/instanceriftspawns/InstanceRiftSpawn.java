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
package com.aionemu.gameserver.model.templates.spawns.instanceriftspawns;

import com.aionemu.gameserver.model.instancerift.InstanceRiftStateType;
import com.aionemu.gameserver.model.templates.spawns.Spawn;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstanceRiftSpawn")
public class InstanceRiftSpawn
{
	@XmlAttribute(name = "id")
	private int id;
	
	public int getId() {
		return id;
	}
	
	@XmlElement(name = "instance_rift_type")
	private List<InstanceRiftSpawn.InstanceRiftStateTemplate> InstanceRiftStateTemplate;
	
	public List<InstanceRiftStateTemplate> getSiegeModTemplates() {
		return InstanceRiftStateTemplate;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "InstanceRiftStateTemplate")
	public static class InstanceRiftStateTemplate {
	
		@XmlElement(name = "spawn")
		private List<Spawn> spawns;
		
		@XmlAttribute(name = "estate")
		private InstanceRiftStateType instanceRiftType;
		
		public List<Spawn> getSpawns() {
			return spawns;
		}
		
		public InstanceRiftStateType getInstanceRiftType() {
			return instanceRiftType;
		}
	}
}