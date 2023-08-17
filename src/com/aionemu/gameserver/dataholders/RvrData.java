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

import com.aionemu.gameserver.model.rvr.RvrLocation;
import com.aionemu.gameserver.model.templates.rvr.RvrTemplate;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "rvr")
public class RvrData
{
	@XmlElement(name = "rvr_location")
	private List<RvrTemplate> rvrTemplates;
	
	@XmlTransient
	private FastMap<Integer, RvrLocation> rvr = new FastMap<Integer, RvrLocation>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (RvrTemplate template : rvrTemplates) {
			rvr.put(template.getId(), new RvrLocation(template));
		}
	}
	
	public int size() {
		return rvr.size();
	}
	
	public FastMap<Integer, RvrLocation> getRvrLocations() {
		return rvr;
	}
}