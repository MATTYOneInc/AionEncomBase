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

import java.nio.ByteBuffer;

import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * @author Ranastic
 */

public class WrappInfoBlobEntry extends ItemBlobEntry
{
	WrappInfoBlobEntry() {
		super(ItemBlobType.WRAPP_INFO);
	}
	
	@Override
	public void writeThisBlob(ByteBuffer buf) {
		if (ownerItem.getItemTemplate().getWrappableCount() > 0 && ownerItem.isPacked()) {
			writeC(buf, ownerItem.getWrappableCount());
		} else if (!ownerItem.isPacked()) {
			writeC(buf, ownerItem.getWrappableCount() * -1);
		} else {
			writeC(buf, 0);
		}
	}
	
	@Override
	public int getSize() {
		return 1;
	}
}