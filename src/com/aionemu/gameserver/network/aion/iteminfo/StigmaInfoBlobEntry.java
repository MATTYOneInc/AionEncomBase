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
import com.aionemu.gameserver.model.templates.item.Stigma;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

import java.nio.ByteBuffer;

/**
 * This blob contains stigma info.
 * 
 * @author -Nemesiss-
 * @modified Rolandas
 */
public class StigmaInfoBlobEntry extends ItemBlobEntry {

	StigmaInfoBlobEntry() {
		super(ItemBlobType.STIGMA_INFO);
	}

	@Override
	public void writeThisBlob(ByteBuffer buf) {
		Item item = ownerItem;
		Stigma stigma = item.getItemTemplate().getStigma();

		writeD(buf, stigma.getSkills().get(0).getSkillId());// skill id 1
		if (stigma.getSkills().size() >= 2) {
			writeD(buf, stigma.getSkills().get(1).getSkillId());// skill id 2
		}
		else {
			writeD(buf, 0);
        }
		writeD(buf, stigma.getShard());

		skip(buf, 192);
		writeH(buf, 0x1);	// unk
		writeH(buf, 0);
		skip(buf, 96);
		writeH(buf, 0);		// unk
	}

	@Override
	public int getSize() {
		return 8 + 4 + 192 + 4 + 96 + 2;
	}
}