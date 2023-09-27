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

import com.aionemu.gameserver.model.team.legion.LegionTerritory;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import java.util.Collection;

public class SM_TERRITORY_LIST extends AionServerPacket
{
    int size = 6;
    Collection<LegionTerritory> territoryList;
	
    public SM_TERRITORY_LIST(Collection<LegionTerritory> territoryList) {
        this.territoryList = territoryList;
    }
	
    @Override
    protected void writeImpl(AionConnection con) {
        writeH(territoryList.size());
        for (LegionTerritory territory: territoryList) {
            writeD(territory.getId());
            writeD(territory.getLegionId());
            writeH(territory.getLegionId() > 0 ? 0x8009 : 0);
            writeH(territory.getLegionId() > 0 ? 255 : 0);
            writeH(0);
            writeS(territory.getLegionName());
        }
    }
}