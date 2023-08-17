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

import com.aionemu.gameserver.model.templates.springzones.SpringTemplate;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/****/
/** Author Rinzler (Encom)
/****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"springObject"})
@XmlRootElement(name = "spring_objects")
public class SpringObjectsData
{
	@XmlElement(name = "spring_object")
	protected List<SpringTemplate> springObject;
	
	@XmlTransient
	private List<SpringTemplate> springObjects = new ArrayList<SpringTemplate>();
	
	void afterUnmarshal(Unmarshaller unmarshaller, Object parent) {
		for (SpringTemplate template : springObject)
		springObjects.add(template);
	}
	
	public int size() {
		return springObjects.size();
	}
	
	public List<SpringTemplate> getSpringObject() {
		return springObjects;
	}
}