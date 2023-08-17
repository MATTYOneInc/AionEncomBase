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
import javolution.util.FastList;

/**
 * @author Rinzler
 */ 

public class SM_DISPUTE_LAND extends AionServerPacket
{
	boolean active;
	FastList<Integer> worlds;
    
	public SM_DISPUTE_LAND(FastList<Integer> worlds, boolean active) {
        this.worlds = worlds;
        this.active = active;
    }
	
	@Override
    protected void writeImpl(AionConnection con) {
        writeH(worlds.size());
        for (int world: worlds) {
            writeD(active ? 0x02 : 0x01);
            writeD(world);
            writeQ(0x00);
            writeQ(0x00);
            writeQ(0x00);
            writeQ(0x00);
        }
    }
}