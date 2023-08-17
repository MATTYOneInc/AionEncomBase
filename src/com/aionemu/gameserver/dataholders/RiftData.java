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

import com.aionemu.gameserver.model.rift.RiftLocation;
import com.aionemu.gameserver.model.templates.rift.RiftTemplate;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Source
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "rift_locations")
public class RiftData {

	@XmlElement(name = "rift_location")
	private List<RiftTemplate> riftTemplates;
	@XmlTransient
	private FastMap<Integer, RiftLocation> rift = new FastMap<Integer, RiftLocation>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (RiftTemplate template : riftTemplates) {
			rift.put(template.getId(), new RiftLocation(template));
		}
	}

	public int size() {
		return rift.size();
	}

	public FastMap<Integer, RiftLocation> getRiftLocations() {
		return rift;
	}
}