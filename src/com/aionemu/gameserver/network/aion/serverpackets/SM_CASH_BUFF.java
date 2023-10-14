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

public class SM_CASH_BUFF extends AionServerPacket {
	int type;

	public SM_CASH_BUFF(int type) {
		this.type = type;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(type); // 버프 시작
		switch (type) {
		case 1:
			writeH(0);
			break;
		case 2:
			writeH(1); // buff number
			writeC(2); // 버프 시작
			writeH(3000); // 버프 아이디
			writeH(0); // 0x00
			writeD(388306); // temps
			break;
		}
	}
}