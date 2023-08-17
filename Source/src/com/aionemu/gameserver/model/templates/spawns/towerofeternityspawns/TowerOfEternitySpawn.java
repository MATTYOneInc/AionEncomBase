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
package com.aionemu.gameserver.model.templates.spawns.towerofeternityspawns;

import com.aionemu.gameserver.model.towerofeternity.TowerOfEternityStateType;
import com.aionemu.gameserver.model.templates.spawns.Spawn;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Wnkrz on 23/08/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TowerOfEternitySpawn")
public class TowerOfEternitySpawn
{
    @XmlAttribute(name = "id")
    private int id;
	
    public int getId() {
        return id;
    }
	
    @XmlElement(name = "tower_of_eternity_type")
    private List<TowerOfEternitySpawn.TowerOfEternityStateTemplate> TowerOfEternityStateTemplate;
	
    public List<TowerOfEternityStateTemplate> getSiegeModTemplates() {
        return TowerOfEternityStateTemplate;
    }
	
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "TowerOfEternityStateTemplate")
    public static class TowerOfEternityStateTemplate {
		
        @XmlElement(name = "spawn")
        private List<Spawn> spawns;
		
        @XmlAttribute(name = "tstate")
        private TowerOfEternityStateType towerOfEternityType;
		
        public List<Spawn> getSpawns() {
            return spawns;
        }
		
        public TowerOfEternityStateType getTowerOfEternityType() {
            return towerOfEternityType;
        }
    }
}