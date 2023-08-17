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
package com.aionemu.gameserver.configs.ingameshop;

import com.aionemu.commons.utils.xml.JAXBUtil;
import com.aionemu.gameserver.model.templates.ingameshop.IGCategory;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "in_game_shop")
public class InGameShopProperty {

	@XmlElement(name = "category", required = true)
	private List<IGCategory> categories;

	public List<IGCategory> getCategories() {
		if (categories == null) {
			categories = new ArrayList<IGCategory>();
		}
		return categories;
	}

	public int size() {
		return getCategories().size();
	}

	public void clear() {
		if (categories != null) {
			categories.clear();
		}
	}

	public static InGameShopProperty load() {
		InGameShopProperty ing = null;
		try {
			String xml = FileUtils.readFileToString(new File("./config/ingameshop/in_game_shop.xml"), "UTF-8");
			ing = (InGameShopProperty) JAXBUtil.deserialize(xml, InGameShopProperty.class);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to initialize ingameshop", e);
		}
		return ing;
	}
}