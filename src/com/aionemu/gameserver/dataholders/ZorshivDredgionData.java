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

import com.aionemu.gameserver.model.templates.zorshivdredgion.ZorshivDredgionTemplate;
import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionLocation;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "zorshiv_dredgion")
public class ZorshivDredgionData
{
	@XmlElement(name = "zorshiv_location")
	private List<ZorshivDredgionTemplate> zorshivDredgionTemplates;
	
	@XmlTransient
	private FastMap<Integer, ZorshivDredgionLocation> zorshivDredgion = new FastMap<Integer, ZorshivDredgionLocation>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (ZorshivDredgionTemplate template : zorshivDredgionTemplates) {
			zorshivDredgion.put(template.getId(), new ZorshivDredgionLocation(template));
		}
	}
	
	public int size() {
		return zorshivDredgion.size();
	}
	
	public FastMap<Integer, ZorshivDredgionLocation> getZorshivDredgionLocations() {
		return zorshivDredgion;
	}
}