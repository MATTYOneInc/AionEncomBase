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

import com.aionemu.gameserver.model.beritra.BeritraLocation;
import com.aionemu.gameserver.model.templates.beritra.BeritraTemplate;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "beritra_invasion")
public class BeritraData
{
	@XmlElement(name = "beritra_location")
	private List<BeritraTemplate> beritraTemplates;
	
	@XmlTransient
	private FastMap<Integer, BeritraLocation> beritra = new FastMap<Integer, BeritraLocation>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (BeritraTemplate template : beritraTemplates) {
			beritra.put(template.getId(), new BeritraLocation(template));
		}
	}
	
	public int size() {
		return beritra.size();
	}
	
	public FastMap<Integer, BeritraLocation> getBeritraLocations() {
		return beritra;
	}
}