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
package com.aionemu.gameserver.model.templates.spawns.nightmarecircusspawns;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusStateType;
import com.aionemu.gameserver.model.templates.spawns.Spawn;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NightmareCircusSpawn")
public class NightmareCircusSpawn
{
	@XmlAttribute(name = "id")
	private int id;
	
	public int getId() {
		return id;
	}
	
	@XmlElement(name = "nightmare_circus_type")
	private List<NightmareCircusSpawn.NightmareCircusStateTemplate> NightmareCircusStateTemplate;
	
	public List<NightmareCircusStateTemplate> getSiegeModTemplates() {
		return NightmareCircusStateTemplate;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "NightmareCircusStateTemplate")
	public static class NightmareCircusStateTemplate {
	
		@XmlElement(name = "spawn")
		private List<Spawn> spawns;
		
		@XmlAttribute(name = "nstate")
		private NightmareCircusStateType nightmareCircusType;
		
		public List<Spawn> getSpawns() {
			return spawns;
		}
		
		public NightmareCircusStateType getNightmareCircusType() {
			return nightmareCircusType;
		}
	}
}