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

import com.aionemu.gameserver.controllers.RVController;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import javolution.util.FastMap;

/**
 * @author Sweetkr
 */
public class SM_RIFT_ANNOUNCE extends AionServerPacket {

	private int actionId;
	private RVController rift;
	private FastMap<Integer, Integer> rifts;
	private int objectId;
	private int gelkmaros, inggison;

	/**
	 * Rift announce packet
	 *
	 * @param player
	 */
	public SM_RIFT_ANNOUNCE(FastMap<Integer, Integer> rifts) {
		this.actionId = 0;
		this.rifts = rifts;
	}

	public SM_RIFT_ANNOUNCE(boolean gelkmaros, boolean inggison) {
		this.gelkmaros = gelkmaros ? 1 : 0;
		this.inggison = inggison ? 1 : 0;
		this.actionId = 1;
	}

	/**
	 * Rift announce packet
	 *
	 * @param player
	 */
	public SM_RIFT_ANNOUNCE(RVController rift, boolean isMaster) {
		this.rift = rift;
		this.actionId = isMaster ? 2 : 3;
	}

	/**
	 * Rift despawn
	 *
	 * @param objectId
	 */
	public SM_RIFT_ANNOUNCE(int objectId) {
		this.objectId = objectId;
		this.actionId = 5;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		switch (actionId) {
			case 0:
				writeH(0x57);
				writeC(actionId);
				for (int value : rifts.values()) {
					writeD(value);
				}
			break;
			case 1:
				writeH(0x09);
				writeC(actionId);
				writeD(gelkmaros);
				writeD(inggison);
			break;
			case 2:
				writeH(0x39);
				writeC(actionId);
				writeD(rift.getOwner().getObjectId());
				writeD(rift.getMaxEntries());
				writeD(rift.getRemainTime());
				writeD(rift.getMinLevel());
				writeD(rift.getMaxLevel());
				writeF(rift.getOwner().getX());
				writeF(rift.getOwner().getY());
				writeF(rift.getOwner().getZ());
				writeC(rift.isVortex() ? 1 : 0);
				writeC(rift.isMaster() ? 1 : 0);
				writeD(rift.getOwner().getWorldId());
				writeD(rift.getAbyssPoint());
			break;
			case 3:
				writeH(0x15);
                writeC(actionId);
                writeD(rift.getOwner().getObjectId());
                writeD(rift.getUsedEntries());
                writeD(rift.getRemainTime());
                writeC(rift.isVortex() ? 1 : 0);
                writeC(rift.isMaster() ? 1 : 0);
				writeD(rift.getAbyssPoint());
			break;
			case 4:
				writeH(0x07);
				writeC(actionId);
				writeD(objectId);
				writeC(rift.isVortex() ? 1 : 0);
                writeC(rift.isMaster() ? 1 : 0);
			break;
			case 5:
                writeH(0x05);
                writeC(actionId);
                writeD(0x00);
            break;
		}
	}
}