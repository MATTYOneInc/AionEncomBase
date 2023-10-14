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
package com.aionemu.gameserver.model.templates.decomposable;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.PlayerClass;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="SelectItems")
public class SelectItems
{
	@XmlAttribute(name="player_class")
	private PlayerClass playerClass = PlayerClass.ALL;
	
	@XmlElement(name="item")
	private List<SelectItem> items;
	
	public PlayerClass getPlayerClass() {
		return this.playerClass;
	}
	
	public List<SelectItem> getItems() {
		return this.items;
	}

	public void addItem(SelectItem selected){
		if (this.items == null) {
			this.items = new ArrayList();
		}
		this.items.add(selected);
	}
}