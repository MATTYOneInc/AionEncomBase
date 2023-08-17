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

import com.aionemu.gameserver.model.templates.portal.PortalLoc;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "portalLoc" })
@XmlRootElement(name = "portal_locs")
public class PortalLocData {

	@XmlElement(name = "portal_loc")
	protected List<PortalLoc> portalLoc;

	@XmlTransient
	private TIntObjectHashMap<PortalLoc> portalLocs = new TIntObjectHashMap<PortalLoc>();

	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		for (PortalLoc loc : portalLoc) {
			portalLocs.put(loc.getLocId(), loc);
		}

		portalLoc.clear();
		portalLoc = null;
	}

	public int size() {
		return portalLocs.size();
	}

	public PortalLoc getPortalLoc(int locId) {
		return portalLocs.get(locId);
	}
}