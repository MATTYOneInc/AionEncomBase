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
package com.aionemu.gameserver.model.templates.spawns.legiondominionspawns;

import com.aionemu.gameserver.model.legiondominion.LegionDominionModType;
import com.aionemu.gameserver.model.legiondominion.LegionDominionRace;
import com.aionemu.gameserver.model.templates.spawns.Spawn;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LegionDominionSpawn")
public class LegionDominionSpawn
{
	@XmlElement(name = "legion_dominion_race")
	private List<LegionDominionRaceTemplate> legionDominionRaceTemplates;
	
	@XmlAttribute(name = "legion_id")
	private int legionDominionId;
	
	public int getLegionDominionId() {
		return legionDominionId;
	}
	
	public List<LegionDominionRaceTemplate> getLegionDominionRaceTemplates() {
		return legionDominionRaceTemplates;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "LegionDominionRaceTemplate")
	public static class LegionDominionRaceTemplate {
	
		@XmlElement(name = "legion_mod")
		private List<LegionDominionModTemplate> LegionDominionModTemplates;
		
		@XmlAttribute(name = "race")
		private LegionDominionRace race;
		
		public LegionDominionRace getLegionDominionRace() {
			return race;
		}
		
		public List<LegionDominionModTemplate> getLegionDominionModTemplates() {
			return LegionDominionModTemplates;
		}
		
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "LegionDominionModTemplate")
		public static class LegionDominionModTemplate {
			@XmlElement(name = "spawn")
			private List<Spawn> spawns;
			
			@XmlAttribute(name = "mod")
			private LegionDominionModType legionDominionMod;
			
			public List<Spawn> getSpawns() {
				return spawns;
			}
			
			public LegionDominionModType getLegionDominionModType() {
				return legionDominionMod;
			}
		}
	}
}