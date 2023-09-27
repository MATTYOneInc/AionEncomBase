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
package com.aionemu.gameserver.model.templates.spawns.iuspawns;

import com.aionemu.gameserver.model.iu.IuStateType;
import com.aionemu.gameserver.model.templates.spawns.Spawn;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IuSpawn")
public class IuSpawn
{
	@XmlAttribute(name = "id")
	private int id;
	
	public int getId() {
		return id;
	}
	
	@XmlElement(name = "iu_type")
	private List<IuSpawn.IuStateTemplate> IuStateTemplate;
	
	public List<IuStateTemplate> getSiegeModTemplates() {
		return IuStateTemplate;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "IuStateTemplate")
	public static class IuStateTemplate {
	
		@XmlElement(name = "spawn")
		private List<Spawn> spawns;
		
		@XmlAttribute(name = "iustate")
		private IuStateType iuType;
		
		public List<Spawn> getSpawns() {
			return spawns;
		}
		
		public IuStateType getIuType() {
			return iuType;
		}
	}
}