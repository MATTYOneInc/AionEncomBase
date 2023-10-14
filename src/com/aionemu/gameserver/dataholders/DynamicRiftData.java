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
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.dynamicrift.DynamicRiftLocation;
import com.aionemu.gameserver.model.templates.dynamicrift.DynamicRiftTemplate;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dynamic_rift")
public class DynamicRiftData {
	@XmlElement(name = "dynamic_location")
	private List<DynamicRiftTemplate> dynamicRiftTemplates;

	@XmlTransient
	private FastMap<Integer, DynamicRiftLocation> dynamicRift = new FastMap<Integer, DynamicRiftLocation>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (DynamicRiftTemplate template : dynamicRiftTemplates) {
			dynamicRift.put(template.getId(), new DynamicRiftLocation(template));
		}
	}

	public int size() {
		return dynamicRift.size();
	}

	public FastMap<Integer, DynamicRiftLocation> getDynamicRiftLocations() {
		return dynamicRift;
	}
}