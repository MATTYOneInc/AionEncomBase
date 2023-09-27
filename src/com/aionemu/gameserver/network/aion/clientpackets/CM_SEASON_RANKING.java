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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.network.aion.serverpackets.SM_SEASON_RANKING;
import com.aionemu.gameserver.services.ranking.SeasonRankingUpdateService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

import java.util.List;

/**
 * Created by Wnkrz on 24/07/2017.
 */

public class CM_SEASON_RANKING extends AionClientPacket
{
    private static Logger log = LoggerFactory.getLogger(CM_SEASON_RANKING.class);
    private int tableId;
    private int serverSwitch;
	
    public CM_SEASON_RANKING(int opcode, State state, State... restStates) {
        super(opcode, state, restStates);
    }
	
    @Override
    protected void readImpl() {
        tableId = readD();
        serverSwitch = readC();
    }
	
    @Override
    protected void runImpl() {
        List<SM_SEASON_RANKING> results = SeasonRankingUpdateService.getInstance().getPlayers(tableId);
        for (SM_SEASON_RANKING packet: results) {
            sendPacket(packet);
		}
    }
}