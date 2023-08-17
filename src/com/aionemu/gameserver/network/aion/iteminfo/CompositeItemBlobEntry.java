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
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;

public class CompositeItemBlobEntry extends ItemBlobEntry
{
	CompositeItemBlobEntry() {
		super(ItemBlobType.COMPOSITE_ITEM);
	}
	
	@Override
    public void writeThisBlob(ByteBuffer buf) {
        Item item = ownerItem;
        writeD(buf, item.getFusionedItemId());
        writeFusionStones(buf);
        writeH(buf, 0);
    }
	
	private void writeFusionStones(ByteBuffer buf) {
        Item item = ownerItem;
        int count = 0;
        if (item.hasFusionStones()) {
            Set<ManaStone> itemStones = item.getFusionStones();
            ArrayList<ManaStone> basicStones = new ArrayList<ManaStone>();
            ArrayList<ManaStone> ancientStones = new ArrayList<ManaStone>();
            for (ManaStone itemStone : itemStones) {
                if (itemStone.isBasic()) {
                    basicStones.add(itemStone);
                } else {
                    ancientStones.add(itemStone);
                }
            } if (item.getFusionedItemTemplate().getSpecialSlots() > 0) {
                if (ancientStones.size() > 0) {
                    for (ManaStone ancientStone : ancientStones) {
                        if (count == 6) {
                            break;
                        }
                        writeD(buf, ancientStone.getItemId());
                        count++;
                    }
                } for (int i = count; i < item.getFusionedItemTemplate().getSpecialSlots(); i++) {
                    writeD(buf, 0);
                    count++;
                }
            } for (ManaStone basicFusionStone : basicStones) {
                if (count == 6) {
                    break;
                }
                writeD(buf, basicFusionStone.getItemId());
                count++;
            }
            skip(buf, (6 - count) * 4);
        } else {
            skip(buf, 24);
        }
    }
	
	@Override
	public int getSize() {
		return 30;
	}
}