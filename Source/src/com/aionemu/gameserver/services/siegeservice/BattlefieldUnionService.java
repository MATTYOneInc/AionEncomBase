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
package com.aionemu.gameserver.services.siegeservice;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BATTLEFIELD_UNION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BATTLEFIELD_UNION_REGISTER;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * Created by wanke on 17/02/2017.
 */

public class BattlefieldUnionService
{
    private static final BattlefieldUnionService instance = new BattlefieldUnionService();
	
    public int size = 0;
    public int maxSize = 24;
    public int requestId = 0;
	public int activeSiegeId;
	
    public void onEnterWorld(Player player) {
        PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION(getSiegeActive(), true, getSize(), getMaxSize()));
    }
	
	public int getSiegeActive() {
        if (SiegeService.getInstance().isSiegeInProgress(1011)) {
            activeSiegeId = 1011;
        } else if (SiegeService.getInstance().isSiegeInProgress(1131)) {
            activeSiegeId = 1131;
        } else if (SiegeService.getInstance().isSiegeInProgress(1132)) {
            activeSiegeId = 1132;
        } else if (SiegeService.getInstance().isSiegeInProgress(1141)) {
            activeSiegeId = 1141;
        } else if (SiegeService.getInstance().isSiegeInProgress(1221)) {
            activeSiegeId = 1231;
        } else if (SiegeService.getInstance().isSiegeInProgress(1231)) {
            activeSiegeId = 1231;
        } else if (SiegeService.getInstance().isSiegeInProgress(1241)) {
            activeSiegeId = 1241;
        } else if (SiegeService.getInstance().isSiegeInProgress(7011)) {
            activeSiegeId = 7011;
        }
        return activeSiegeId;
    }
	
    public void onSiegeStart(final int fortressId) {
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            public void visit(Player player) {
                PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION(fortressId, true, getSize(), getMaxSize()));
            }
        });
    }
	
	public void onSiegeFinish(final int fortressId) {
        World.getInstance().doOnAllPlayers(new Visitor<Player>() {
            public void visit(Player player) {
                PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION(fortressId, false, getSize(), getMaxSize()));
            }
        });
    }
	
    public void onRegister(Player player, int requestId, int activeSiegeId) {
        boolean register = false;
        PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION(activeSiegeId, true, getSize() + 1, getMaxSize()));
        PacketSendUtility.sendPacket(player, new SM_BATTLEFIELD_UNION_REGISTER(requestId, true));
        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404004));
        PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1404005));
    }
	
	public int getSize() {
        return size;
    }
	
    public int getMaxSize() {
        return maxSize;
    }
	
    public int getrequestId() {
        return requestId;
    }
	
    public static BattlefieldUnionService getInstance() {
        return instance;
    }
}