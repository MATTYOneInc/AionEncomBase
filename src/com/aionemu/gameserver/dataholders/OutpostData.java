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

import com.aionemu.gameserver.model.outpost.OutpostLocation;
import com.aionemu.gameserver.model.templates.outpost.OutpostTemplate;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by Wnkrz on 27/08/2017.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "outpost_locations")
public class OutpostData
{
    @XmlElement(name = "outpost_location")
    private List<OutpostTemplate> outpostTemplates;
    @XmlTransient
    private FastMap<Integer, OutpostLocation> out = new FastMap<Integer, OutpostLocation>();
	
    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (OutpostTemplate template : outpostTemplates) {
            out.put(template.getId(), new OutpostLocation(template));
        }
    }
	
    public int size() {
        return out.size();
    }
	
    public FastMap<Integer, OutpostLocation> getOutpostLocations() {
        return out;
    }
}