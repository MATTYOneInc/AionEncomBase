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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ranastic
 */

public class SM_COALESCENCE_RESULT extends AionServerPacket {
	private int itemTemplateId;
	private int itemObjId;
	private int bonusTemplateId;
	private long bonusCount;
	private boolean isBonus;

	public SM_COALESCENCE_RESULT(int itemTemplateId, int itemObjId, int bonusTemplateId, long bonusCount,
			boolean isBonus) {
		this.itemTemplateId = itemTemplateId;
		this.itemObjId = itemObjId;
		this.bonusTemplateId = bonusTemplateId;
		this.bonusCount = bonusCount;
		this.isBonus = isBonus;
	}

	@Override
	protected void writeImpl(AionConnection client) {
		writeD(itemTemplateId);
		writeD(itemObjId);
		writeD(bonusTemplateId);
		writeD(isBonus ? 1 : 0);
		writeQ(bonusCount);
	}
}