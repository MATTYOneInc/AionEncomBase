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
package com.aionemu.gameserver.model.items;

import com.aionemu.gameserver.dataholders.loadingutils.adapters.NpcEquipmentList;
import com.aionemu.gameserver.dataholders.loadingutils.adapters.NpcEquippedGearAdapter;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * @author Luno
 */
@XmlJavaTypeAdapter(NpcEquippedGearAdapter.class)
public class NpcEquippedGear implements Iterable<Entry<ItemSlot, ItemTemplate>> {

	private Map<ItemSlot, ItemTemplate> items;
	private short mask;

	private NpcEquipmentList v;

	public NpcEquippedGear(NpcEquipmentList v) {
		this.v = v;
	}

	/**
	 * @return short
	 */
	public short getItemsMask() {
		if (items == null) {
			init();
		}
		return mask;
	}

	@Override
	public Iterator<Entry<ItemSlot, ItemTemplate>> iterator() {
		if (items == null) {
			init();
		}
		return items.entrySet().iterator();
	}

	/**
	 * Here NPC equipment mask is initialized. All NPC slot masks should be lower than 65536
	 */
	public void init() {
		synchronized (this) {
			if (items == null) {
				items = new TreeMap<ItemSlot, ItemTemplate>();
				for (ItemTemplate item : v.items) {
					ItemSlot[] itemSlots = ItemSlot.getSlotsFor(item.getItemSlot());
					for (ItemSlot itemSlot : itemSlots) {
						if (items.get(itemSlot) == null) {
							items.put(itemSlot, item);
							mask |= itemSlot.getSlotIdMask();
							break;
						}
					}
				}
			}
			v = null;
		}
	}

	/**
	 * @param itemSlot
	 * @return
	 */
	public ItemTemplate getItem(ItemSlot itemSlot) {
		return items != null ? items.get(itemSlot) : null;
	}
}