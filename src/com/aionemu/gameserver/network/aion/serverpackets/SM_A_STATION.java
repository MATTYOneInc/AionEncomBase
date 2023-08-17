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

import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ranastic
 */

public class SM_A_STATION extends AionServerPacket
{
    private boolean isFirst = false;
    private int currentServer = 0;
    private int newServerId = 0;
	
    public SM_A_STATION(int currentServer, int newServerId, boolean first) {
        this.currentServer = currentServer;
        this.newServerId = newServerId;
        this.isFirst = first;
    }
	
    @Override
    protected void writeImpl(AionConnection con) {
        Player player = con.getActivePlayer();
        writeD(newServerId);
        writeD(currentServer);
        writeD(player.getObjectId());
        if (isFirst) {
            writeD(NetworkConfig.GAMESERVER_ID);
        } else {
            writeD(newServerId);
        }
        writeD(0);
        writeD(0);
    }
}