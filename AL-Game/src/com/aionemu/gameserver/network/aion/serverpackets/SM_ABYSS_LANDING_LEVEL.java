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
 * Created by wanke on 16/05/2017.
 */

public class SM_ABYSS_LANDING_LEVEL extends AionServerPacket {
	private int id;
	private int level;
	private int newLevel;

	public SM_ABYSS_LANDING_LEVEL(int id, int level, int newLevel) {
		this.id = id;
		this.level = level;
		this.newLevel = newLevel;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeC(id);
		writeC(level);
		writeC(newLevel);
	}
}