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
package com.aionemu.gameserver.services;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EVERGALE_CANYON;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Created by Wnkrz on 11/08/2017.
 */

public class WindyGorgeService
{
    public void onLogin(Player player){
        PacketSendUtility.sendPacket(player, new SM_EVERGALE_CANYON(2));
        PacketSendUtility.sendPacket(player, new SM_EVERGALE_CANYON(4));
    }
	
    public static final WindyGorgeService getInstance() {
        return SingletonHolder.instance;
    }
	
    @SuppressWarnings("synthetic-access")
    private static class SingletonHolder {
        protected static final WindyGorgeService instance = new WindyGorgeService();
    }
}