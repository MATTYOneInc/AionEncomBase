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

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.teleport.HotspotlocationTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Rinzler (Encom)
 */

@XmlRootElement(name = "hotspot_location")
@XmlAccessorType(XmlAccessType.FIELD)
public class HotspotLocationData {
	@XmlElement(name = "hotspot_template")
	private List<HotspotlocationTemplate> hslist;

	private TIntObjectHashMap<HotspotlocationTemplate> lochslistData = new TIntObjectHashMap<HotspotlocationTemplate>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (HotspotlocationTemplate loc : hslist) {
			lochslistData.put(loc.getLocId(), loc);
		}
	}

	public int size() {
		return lochslistData.size();
	}

	public HotspotlocationTemplate getHotspotlocationTemplate(int id) {
		return lochslistData.get(id);
	}
}