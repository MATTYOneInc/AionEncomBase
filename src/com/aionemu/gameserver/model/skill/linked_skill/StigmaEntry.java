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
package com.aionemu.gameserver.model.skill.linked_skill;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

/**
 * @author DrNism
 */
public abstract class StigmaEntry {

	protected final int itemId;
	protected final String itemName;

	StigmaEntry(int itemId, String itemName) {
		this.itemId = itemId;
		this.itemName = itemName;
	}

	public final int getItemId() {
		return itemId;
	}

	public final String getItemName() {
		return DataManager.ITEM_DATA.getItemTemplate(itemId).getName();
	}

	public final ItemTemplate getSkillTemplate() {
		return DataManager.ITEM_DATA.getItemTemplate(getItemId());
	}
}