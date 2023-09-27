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

import com.aionemu.gameserver.model.templates.curingzones.CuringTemplate;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "curingObject" })
@XmlRootElement(name = "curing_objects")
public class CuringObjectsData {

	@XmlElement(name = "curing_object")
	protected List<CuringTemplate> curingObject;

	@XmlTransient
	private List<CuringTemplate> curingObjects = new ArrayList<CuringTemplate>();

	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		for (CuringTemplate template : curingObject) {
			curingObjects.add(template);
		}
	}

	public int size() {
		return curingObjects.size();
	}

	public List<CuringTemplate> getCuringObject() {
		return curingObjects;
	}
}