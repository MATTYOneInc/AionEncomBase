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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.siegeservice.BattlefieldUnionService;

/**
 * Created by wanke on 14/02/2017.
 */

public class CM_BATTLEFIELD_UNION_REGISTER extends AionClientPacket
{
    private int requestId;
    private int fortressId;
	
    public CM_BATTLEFIELD_UNION_REGISTER(int opcode, AionConnection.State state, AionConnection.State... restStates) {
        super(opcode, state, restStates);
    }
	
    @Override
    protected void readImpl() {
        requestId = readD();
    }
	
    @Override
    protected void runImpl() {
        if (SiegeService.getInstance().isSiegeInProgress(1011)) {
            BattlefieldUnionService.getInstance().onRegister(getConnection().getActivePlayer(), requestId, 1011);
        } else if (SiegeService.getInstance().isSiegeInProgress(1131)) {
            BattlefieldUnionService.getInstance().onRegister(getConnection().getActivePlayer(), requestId, 1131);
        } else if (SiegeService.getInstance().isSiegeInProgress(1132)) {
            BattlefieldUnionService.getInstance().onRegister(getConnection().getActivePlayer(), requestId, 1132);
        } else if (SiegeService.getInstance().isSiegeInProgress(1141)) {
            BattlefieldUnionService.getInstance().onRegister(getConnection().getActivePlayer(), requestId, 1141);
        } else if (SiegeService.getInstance().isSiegeInProgress(1221)) {
            BattlefieldUnionService.getInstance().onRegister(getConnection().getActivePlayer(), requestId, 1221);
        } else if (SiegeService.getInstance().isSiegeInProgress(1231)) {
            BattlefieldUnionService.getInstance().onRegister(getConnection().getActivePlayer(), requestId, 1231);
        } else if (SiegeService.getInstance().isSiegeInProgress(1241)) {
            BattlefieldUnionService.getInstance().onRegister(getConnection().getActivePlayer(), requestId, 1241);
        } else if (SiegeService.getInstance().isSiegeInProgress(7011)) {
            BattlefieldUnionService.getInstance().onRegister(getConnection().getActivePlayer(), requestId, 7011);
        }
    }
}