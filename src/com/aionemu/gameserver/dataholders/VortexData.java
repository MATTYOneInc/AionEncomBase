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

import com.aionemu.gameserver.model.templates.vortex.VortexTemplate;
import com.aionemu.gameserver.model.vortex.VortexLocation;
import javolution.util.FastMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * @author Source
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dimensional_vortex")
public class VortexData {

	@XmlElement(name = "vortex_location")
	private List<VortexTemplate> vortexTemplates;
	@XmlTransient
	private FastMap<Integer, VortexLocation> vortex = new FastMap<Integer, VortexLocation>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (VortexTemplate template : vortexTemplates) {
			vortex.put(template.getId(), new VortexLocation(template));
		}
	}

	public int size() {
		return vortex.size();
	}

	public VortexLocation getVortexLocation(int invasionWorldId) {
		for (VortexLocation loc : vortex.values()) {
			if (loc.getInvasionWorldId() == invasionWorldId) {
				return loc;
			}
		}
		return null;
	}

	public FastMap<Integer, VortexLocation> getVortexLocations() {
		return vortex;
	}
}