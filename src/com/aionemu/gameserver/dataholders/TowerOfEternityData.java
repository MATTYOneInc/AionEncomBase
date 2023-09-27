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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.towerofeternity.TowerOfEternityLocation;
import com.aionemu.gameserver.model.templates.towerofeternity.TowerOfEternityTemplate;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Wnkrz on 22/08/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "tower_of_eternity")
public class TowerOfEternityData
{
    @XmlElement(name = "tower_location")
    private List<TowerOfEternityTemplate> towerOfEternityTemplates;
	
    @XmlTransient
    private FastMap<Integer, TowerOfEternityLocation> towerOfEternity = new FastMap<Integer, TowerOfEternityLocation>();
	
    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (TowerOfEternityTemplate template : towerOfEternityTemplates) {
            towerOfEternity.put(template.getId(), new TowerOfEternityLocation(template));
        }
    }
	
    public int size() {
        return towerOfEternity.size();
    }
	
    public FastMap<Integer, TowerOfEternityLocation> getTowerOfEternityLocations() {
        return towerOfEternity;
    }
}