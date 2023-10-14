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

import java.util.HashMap;
import java.util.Map.Entry;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_NEARBY_QUESTS extends AionServerPacket
{
	private HashMap<Integer, Integer> nearbyQuestList;
	
	public SM_NEARBY_QUESTS(HashMap<Integer, Integer> nearbyQuestList) {
        this.nearbyQuestList = nearbyQuestList;
    }
	
	@Override
    protected void writeImpl(AionConnection con) {
        if (nearbyQuestList == null || con.getActivePlayer() == null) {
            return;
        }
        writeC(0);
        writeH(-nearbyQuestList.size() & 0xFFFF);
        for (Entry<Integer, Integer> nearbyQuest : nearbyQuestList.entrySet()) {
            if (nearbyQuest.getValue() > 0) {
                writeH(nearbyQuest.getKey());
                writeH(0x02);
            } else {
                writeD(nearbyQuest.getKey());
            }
        }
    }
}