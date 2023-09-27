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
package com.aionemu.gameserver.model.templates.spawns.outpostspawns;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.templates.spawns.Spawn;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Wnkrz on 27/08/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OutpostSpawn")
public class OutpostSpawn
{
    @XmlAttribute(name = "id")
    private int id;
	
    @XmlAttribute(name = "world")
    private int world;
	
    @XmlElement(name = "simple_race")
    private List<SimpleRaceTemplate> simpleRaceTemplates;
	
    public int getId() {
        return id;
    }
	
    public int getWorldId() {
        return world;
    }
	
    public List<SimpleRaceTemplate> getOutpostRaceTemplates() {
        return simpleRaceTemplates;
    }
	
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "OutpostRaceTemplate")
    public static class SimpleRaceTemplate {
        @XmlAttribute(name = "race")
        private Race race;
		
        public Race getBaseRace() {
            return race;
        }
		
        @XmlElement(name = "spawn")
        private List<Spawn> spawns;
		
        public List<Spawn> getSpawns() {
            return spawns;
        }
    }
}