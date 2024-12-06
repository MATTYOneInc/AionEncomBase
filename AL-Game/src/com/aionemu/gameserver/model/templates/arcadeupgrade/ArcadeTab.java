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
package com.aionemu.gameserver.model.templates.arcadeupgrade;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by wanke on 17/02/2017.
 */
@XmlType(name = "ArcadeTab")
public class ArcadeTab {
	@XmlAttribute(name = "id")
	private int id;

	@XmlElement(name = "item")
	private List<ArcadeTabItem> arcadeTabItem;

	public int getId() {
		return id;
	}

	public List<ArcadeTabItem> getArcadeTabItems() {
		return arcadeTabItem;
	}
}