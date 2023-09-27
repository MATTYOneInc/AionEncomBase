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

import com.aionemu.gameserver.model.items.IdianStone;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

import java.nio.ByteBuffer;

/**
 * @author Ranastic
 */

public class IdianInfoBlobEntry extends ItemBlobEntry
{
	IdianInfoBlobEntry() {
		super(ItemBlobType.IDIAN_INFO);
	}
	
	@Override
	public void writeThisBlob(ByteBuffer buf) {
		IdianStone stone = ownerItem.getIdianStone();
		writeD(buf, stone == null ? 0 : stone.getPolishCharge());
	}
	
	@Override
	public int getSize() {
		return 4;
	}
}