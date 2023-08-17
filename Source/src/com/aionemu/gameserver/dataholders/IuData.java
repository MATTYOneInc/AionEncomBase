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

import com.aionemu.gameserver.model.iu.IuLocation;
import com.aionemu.gameserver.model.templates.iu.IuTemplate;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "iu")
public class IuData
{
	@XmlElement(name = "iu_location")
	private List<IuTemplate> iuTemplates;
	
	@XmlTransient
	private FastMap<Integer, IuLocation> iu = new FastMap<Integer, IuLocation>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (IuTemplate template : iuTemplates) {
			iu.put(template.getId(), new IuLocation(template));
		}
	}
	
	public int size() {
		return iu.size();
	}
	
	public FastMap<Integer, IuLocation> getIuLocations() {
		return iu;
	}
}