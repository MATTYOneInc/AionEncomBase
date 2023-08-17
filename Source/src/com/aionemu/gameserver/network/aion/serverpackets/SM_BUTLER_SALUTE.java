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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ranastic
 */

public class SM_BUTLER_SALUTE extends AionServerPacket
{
	private int playerObjId;
	private int isInside;
	private int unk1;
	private int unk2;
	private int unk3;
	private int unk4;
	
	public SM_BUTLER_SALUTE(int unk1, int unk2, int unk3, int unk4, int playerObjId, int isInside) {
		this.unk1 = unk1;
		this.unk2 = unk2;
		this.unk3 = unk3;
		this.unk4 = unk4;
		this.playerObjId = playerObjId;
		this.isInside = isInside;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(unk1);
		writeC(unk2);
		writeD(unk3);
		writeC(unk4);
		writeD(playerObjId);
		writeC(isInside);
	}
}