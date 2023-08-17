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
package com.aionemu.gameserver.network.aion.iteminfo;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

import java.nio.ByteBuffer;

/**
 * This blob is sent for weapons. It keeps info about slots that weapon can be equipped to.
 * 
 * @author -Nemesiss-
 * @modified Rolandas
 */
public class WeaponInfoBlobEntry extends ItemBlobEntry{

	WeaponInfoBlobEntry() {
		super(ItemBlobType.SLOTS_WEAPON);
	}

	@Override
	public void writeThisBlob(ByteBuffer buf) {
		Item item = ownerItem;

		ItemSlot[] slots = ItemSlot.getSlotsFor(item.getItemTemplate().getItemSlot());
		if (slots.length == 1) {
			writeQ(buf, slots[0].getSlotIdMask());
			writeQ(buf, item.hasFusionedItem() ? 0x00 : 0x02);
			return;
		}
		if (item.getItemTemplate().isTwoHandWeapon()) {
			writeQ(buf, slots[0].getSlotIdMask() | slots[1].getSlotIdMask());
			writeQ(buf, 0);
		}
		else {
			writeQ(buf, slots[0].getSlotIdMask());
			writeQ(buf, slots[1].getSlotIdMask());
		}
	}

	@Override
	public int getSize() {
		return 16;
	}
}