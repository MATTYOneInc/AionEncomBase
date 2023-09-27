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

import com.aionemu.gameserver.GameServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Falke_34
 * @Rework Xnemonix
 */
public class SM_CREATIVITY_POINTS_APPLY extends AionServerPacket {

	Logger log = LoggerFactory.getLogger(SM_CREATIVITY_POINTS_APPLY.class);

	private int type;
	private int size;
	private int id;
	private int slotPoint;

	public SM_CREATIVITY_POINTS_APPLY(int type, int size) {
		this.type = type;
		this.size = size;
	}

	public SM_CREATIVITY_POINTS_APPLY(int type) {
		this.type = type;
	}

	public SM_CREATIVITY_POINTS_APPLY(int type, int id, int slotPoint) {
		this.id = id;
		this.slotPoint = slotPoint;
	}

	public SM_CREATIVITY_POINTS_APPLY(int type, int size, int id, int slotPoint) {
		this.type = type;
		this.size = size;
		this.id = id;
		this.slotPoint = slotPoint;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(0x01);
		writeH(0x01); //No Loop should only return 1
		switch (type) {
			case 0:
				writeD(id);
				writeH(slotPoint);
				break;
			case 1:
				writeD(id);
				writeH(slotPoint);
			break;
		}
	}
}