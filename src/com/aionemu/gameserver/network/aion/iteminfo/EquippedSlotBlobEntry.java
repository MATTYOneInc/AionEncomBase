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
package com.aionemu.gameserver.network.aion.iteminfo;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

import java.nio.ByteBuffer;

/**
 * This block is sent for all items that can be equipped. If item is equipped. This block says to which slot it's
 * equipped. If not, then it says 0.
 * 
 * @author -Nemesiss-
 * @modified Rolandas
 */
public class EquippedSlotBlobEntry extends ItemBlobEntry {

	EquippedSlotBlobEntry() {
		super(ItemBlobType.EQUIPPED_SLOT);
	}

	@Override
	public void writeThisBlob(ByteBuffer buf) {
		Item item = ownerItem;

		writeQ(buf, item.isEquipped() ? item.getEquipmentSlot() : 0x00);
	}

	@Override
	public int getSize() {
		return 8;
	}
}