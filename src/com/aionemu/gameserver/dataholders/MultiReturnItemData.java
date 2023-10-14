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

import com.aionemu.gameserver.model.templates.teleport.MultiReturn;
import com.aionemu.gameserver.model.templates.teleport.MultiReturnLocationList;

import gnu.trove.map.hash.TIntObjectHashMap;

/****/
/** Author Rinzler (Encom)
/****/

@XmlRootElement(name = "multi_returns")
@XmlAccessorType(XmlAccessType.FIELD)
public class MultiReturnItemData
{
	@XmlElement(name = "item")
	private List<MultiReturn> ItemList;
	
	private TIntObjectHashMap<List<MultiReturnLocationList>> ItemLocationList = new TIntObjectHashMap<>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		ItemLocationList.clear();
		for (MultiReturn template: ItemList) {
			ItemLocationList.put(template.getId(), template.getMultiReturnList());
		}
	}
	
	public int size() {
		return ItemLocationList.size();
	}
	
	public MultiReturn getMultiReturnById(int id) {
		for (MultiReturn template: ItemList) {
			if (template.getId() == id) {
				return template;
			}
		}
		return null;
	}
	
	public List<MultiReturn> getMultiReturns() {
		return ItemList;
	}
}