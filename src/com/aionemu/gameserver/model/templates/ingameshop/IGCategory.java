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
package com.aionemu.gameserver.model.templates.ingameshop;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IGCategory")
public class IGCategory {

	@XmlElement(name = "sub_category")
	protected List<IGSubCategory> subCategories;

	@XmlAttribute(required = true)
	protected int id;

	@XmlAttribute(required = true)
	protected String name;

	public List<IGSubCategory> getSubCategories() {
		if (subCategories == null) {
			subCategories = new ArrayList<IGSubCategory>();
		}
		return subCategories;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}