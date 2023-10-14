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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.decomposable.SelectItem;
import com.aionemu.gameserver.model.templates.decomposable.SelectItems;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_SELECT_ITEM extends AionServerPacket
{
	private int uniqueItemId;
	private List<SelectItem> selsetitems;
	private static final Logger log = LoggerFactory.getLogger(SM_SELECT_ITEM.class);
	
	public SM_SELECT_ITEM(SelectItems selsetitem, int uniqueItemId) {
		this.uniqueItemId = uniqueItemId;
		this.selsetitems = selsetitem.getItems();
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeD(this.uniqueItemId);
		writeD(0x00);
		writeC(this.selsetitems.size());
		for (int slotCount = 0; slotCount < selsetitems.size(); slotCount++) {
			writeC(slotCount);
			SelectItem rt = this.selsetitems.get(slotCount);
			ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(rt.getSelectItemId());
			writeD(rt.getSelectItemId());
			writeD(rt.getCount());
			writeC(itemTemplate.getOptionSlotBonus() > 0 ? 255 : 0);
			writeC(itemTemplate.getMaxEnchantBonus() > 0 ? 255 : 0);
			if ((itemTemplate.isArmor()) || (itemTemplate.isWeapon())) {
				writeC(-1);
			}
			else {
				writeC(0);
			}
			if ((itemTemplate.isCloth()) || (itemTemplate.getOptionSlotBonus() > 0) || (itemTemplate.getMaxEnchantBonus() > 0)) {
				writeC(1);
			}
			else {
				writeC(0);
			}
		}
	}
}