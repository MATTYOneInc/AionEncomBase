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
package com.aionemu.gameserver.model.templates.housing;

import com.aionemu.gameserver.model.templates.item.ItemQuality;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "house_part")
public class HousePart {

	@XmlAttribute(name = "building_tags", required = true)
	private List<String> buildingTags;

	@XmlAttribute(required = true)
	protected PartType type;
	
	@XmlAttribute(required = true)
	protected ItemQuality quality;

	@XmlAttribute
	protected String name;

	@XmlAttribute(required = true)
	protected int id;

	@XmlTransient
	protected Set<String> tagsSet = new HashSet<String>(1);

	void afterUnmarshal(Unmarshaller u, Object parent) {
		if (buildingTags == null) {
			return;
        }
		for (String tag : buildingTags) {
			tagsSet.add(tag);
        }
		buildingTags.clear();
		buildingTags = null;
	}
	
	public PartType getType() {
		return type;
	}

	public ItemQuality getQuality() {
		return quality;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	
	public Set<String> getTags() {
		return tagsSet;
	}

	public boolean isForBuilding(Building building) {
		return tagsSet.contains(building.getPartsMatchTag());
	}
}