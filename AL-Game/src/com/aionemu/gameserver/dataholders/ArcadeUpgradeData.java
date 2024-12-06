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

import com.aionemu.gameserver.model.templates.arcadeupgrade.ArcadeTab;
import com.aionemu.gameserver.model.templates.arcadeupgrade.ArcadeTabItem;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * Created by wanke on 17/02/2017.
 */
@XmlRootElement(name = "arcadelist")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArcadeUpgradeData {
	@XmlElement(name = "tab")
	private List<ArcadeTab> arcadeTabTemplate;
	private TIntObjectHashMap<List<ArcadeTabItem>> arcadeItemList = new TIntObjectHashMap<>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		arcadeItemList.clear();
		for (ArcadeTab template : arcadeTabTemplate) {
			arcadeItemList.put(template.getId(), template.getArcadeTabItems());
		}
	}

	public int size() {
		return arcadeItemList.size();
	}

	public List<ArcadeTabItem> getArcadeTabById(int id) {
		return arcadeItemList.get(id);
	}

	public List<ArcadeTab> getArcadeTabs() {
		return arcadeTabTemplate;
	}
}