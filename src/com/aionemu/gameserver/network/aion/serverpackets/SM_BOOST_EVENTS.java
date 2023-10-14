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

import java.util.Map;

import com.aionemu.gameserver.model.templates.event.BoostEvents;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Created by wanke on 01/02/2017.
 */

public class SM_BOOST_EVENTS extends AionServerPacket
{
    private Map<Integer, BoostEvents> boostEvents;

    private int buffId;
    private int buffValue;
    long eventStartTime;
    long eventEndTime;
	
    public SM_BOOST_EVENTS(int buffId, int buffValue, long eventStartTime, long eventEndTime) {
        this.buffId = buffId;
        this.buffValue = buffValue;
        this.eventStartTime = eventStartTime;
        this.eventEndTime = eventEndTime;
    }
	
    @Override
    protected void writeImpl(AionConnection con) {
        writeH(9); //buff Count dont tuch
        writeC(buffId); //buff Id
        writeC(1); //enabledCount
        writeD((int) eventStartTime); //start
        writeD(0);
        writeD((int) eventEndTime); //end
        writeD(0);
        writeD(buffValue); //boost value
        writeQ(-1);
        writeD(0);
        writeD(0);
    }
}