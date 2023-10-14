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
package com.aionemu.gameserver.model.templates.item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author antness
 */
@XmlType(name = "ResultedItemsCollection")
public class ResultedItemsCollection {

	@XmlElement(name = "item")
	protected ArrayList<ResultedItem> items;
	@XmlElement(name = "random_item")
	protected ArrayList<RandomItem> randomItems;
	@XmlElement(name = "item_set")
	protected ArrayList<ResultedItemSet> item_set;

	public Collection<ResultedItem> getItems() {
		return items != null ? items : Collections.<ResultedItem> emptyList();
	}

	public Collection<ResultedItemSet> getItemSet() {
		return item_set != null ? item_set : Collections.<ResultedItemSet> emptyList();
	}

	public List<RandomItem> getRandomItems() {
		if (randomItems != null) {
			return randomItems;
		}
		else {
			return new ArrayList<RandomItem>();
		}
	}
}