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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.templates.decomposable.DecomposableSelectItem;
import com.aionemu.gameserver.model.templates.decomposable.SelectItem;
import com.aionemu.gameserver.model.templates.decomposable.SelectItems;

@XmlRootElement(name = "decomposable_select_items")
@XmlAccessorType(XmlAccessType.FIELD)
public class DecomposableSelectItemsData {
	@XmlElement(name = "decomposable_select_item", required = true)
	protected List<DecomposableSelectItem> selectItems;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap<Integer, HashMap<PlayerClass, SelectItems>> selectItemData = new HashMap();

	@SuppressWarnings({ "unchecked", "rawtypes" })
	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (Iterator i$ = this.selectItems.iterator(); i$.hasNext();) {
			DecomposableSelectItem item;
			item = (DecomposableSelectItem) i$.next();
			if (item.getItems() != null) {
				if (!this.selectItemData.containsKey(Integer.valueOf(item.getItemId()))) {
					this.selectItemData.put(Integer.valueOf(item.getItemId()), new HashMap());
				}
				for (SelectItems its : item.getItems()) {
					((HashMap) this.selectItemData.get(Integer.valueOf(item.getItemId()))).put(its.getPlayerClass(),
							its);
				}
			}
		}
		this.selectItems.clear();
		this.selectItems = null;
	}

	public SelectItems getSelectItem(PlayerClass playerClass, Race race, int itemid) {
		if (this.selectItemData.containsKey(Integer.valueOf(itemid))) {
			SelectItems selectedItems;
			Iterator j;
			SelectItem select;
			if (((HashMap) this.selectItemData.get(Integer.valueOf(itemid))).containsKey(playerClass)) {
				selectedItems = new SelectItems();
				j = ((SelectItems) ((HashMap) this.selectItemData.get(Integer.valueOf(itemid))).get(playerClass))
						.getItems().iterator();
				while (j.hasNext()) {
					select = (SelectItem) j.next();
					if ((select.getRace() == Race.PC_ALL) || (select.getRace() == race)) {
						selectedItems.addItem(select);
					}
				}
				return selectedItems;
			}
			if (((HashMap) this.selectItemData.get(Integer.valueOf(itemid))).containsKey(PlayerClass.ALL)) {
				selectedItems = new SelectItems();
				j = ((SelectItems) ((HashMap) this.selectItemData.get(Integer.valueOf(itemid))).get(PlayerClass.ALL))
						.getItems().iterator();
				while (j.hasNext()) {
					select = (SelectItem) j.next();
					if ((select.getRace() == Race.PC_ALL) || (select.getRace() == race)) {
						selectedItems.addItem(select);
					}
				}
				return selectedItems;
			}
			return null;
		}
		return null;
	}

	public int size() {
		return selectItemData.size();
	}
}