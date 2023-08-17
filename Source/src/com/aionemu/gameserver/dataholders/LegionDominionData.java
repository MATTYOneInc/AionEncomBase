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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.model.legiondominion.LegionDominionLocation;
import com.aionemu.gameserver.model.templates.legiondominion.LegionDominionTemplate;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dominion_locations")
public class LegionDominionData
{
	@XmlElement(name = "dominion_location")
	private List<LegionDominionTemplate> legionDominionTemplates;
	
	@XmlTransient
	private FastMap<Integer, LegionDominionLocation> legionDominion = new FastMap<Integer, LegionDominionLocation>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (LegionDominionTemplate template : legionDominionTemplates) {
			legionDominion.put(template.getLegionDominionId(), new LegionDominionLocation(template));
		}
	}
	
	public int size() {
		return legionDominion.size();
	}
	
	public FastMap<Integer, LegionDominionLocation> getLegionDominionLocations() {
		return legionDominion;
	}
}