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
package com.aionemu.gameserver.model.templates.teleport;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/****/
/** Author Rinzler (Encom)
/****/

@XmlType(name = "MultiReturn")
public class MultiReturn {

	@XmlAttribute(name = "id")
	private int id;
	
	@XmlElement(name = "loc")
	private List<MultiReturnLocationList> MultiReturnList;
	
	public int getId() {
		return id;
	}
	
	public MultiReturnLocationList getReturnDataById(int id) {
		if (MultiReturnList != null) {
			return MultiReturnList.get(id);
		}
		return null;
	}
	
	public List<MultiReturnLocationList> getMultiReturnList() {
		return MultiReturnList;
	}
}