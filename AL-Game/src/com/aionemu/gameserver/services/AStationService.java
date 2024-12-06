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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.AStationConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SERVER_IDS;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.services.transfers.AStation;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldType;

/**
 * @author Ranastic
 */

public class AStationService {
	private static final AStationService instance = new AStationService();
	private Logger log = LoggerFactory.getLogger(AStationService.class);
	private Map<Integer, Player> accountsOnAStation = new HashMap<Integer, Player>(1);

	public static AStationService getInstance() {
		return instance;
	}

	public void checkAuthorizationRequest(Player player) {
		int level = AStationConfig.A_STATION_MAX_LEVEL;
		if (player.getLevel() > level) {
			return;
		}
		PacketSendUtility.sendPacket(player,
				new SM_SERVER_IDS(new AStation(AStationConfig.A_STATION_SERVER_ID, true, 1, level)));
	}

	public void handleMoveThere(Player player) {
		TeleportService2.moveAStation(player, AStationConfig.A_STATION_SERVER_ID, false);
	}

	public void handleMoveBack(Player player) {
		TeleportService2.moveAStation(player, AStationConfig.A_STATION_SERVER_ID, true);
	}

	public void checkAStationMove(Player player, int accId, boolean back) {
		if (back) {
			accountsOnAStation.remove(accId);
			player.setOnAStation(false);
			PacketSendUtility.sendYellowMessage(player, "You joined the standard server!");
			aStationBonus(player, true);
		} else {
			if (accountsOnAStation.containsKey(accId)) {
				accountsOnAStation.remove(accId);
				handleMoveBack(player);
				PacketSendUtility.sendYellowMessage(player,
						"You got teleported back to the normal server because you tried to enter the fast track server twice!");
			}
			if (accountsOnAStation.containsKey(accId) && !accountsOnAStation.containsValue(player)) {
				handleMoveBack(player);
				PacketSendUtility.sendYellowMessage(player,
						"You got teleported back to the normal server because something went wrong!");
			}
			accountsOnAStation.put(accId, player);
			player.setOnAStation(true);
			PacketSendUtility.sendYellowMessage(player, "You joined the fast track server!");
			aStationBonus(player, false);
		}
	}

	public void aStationBonus(Player player, boolean off) {
	}

	public boolean isPvPZone(WorldType wt) {
		return wt == WorldType.BALAUREA || wt == WorldType.PANESTERRA || wt == WorldType.ABYSS;
	}

	public boolean isNormalZone(WorldType wt) {
		return wt == WorldType.ASMODAE || wt == WorldType.ELYSEA || wt == WorldType.NONE;
	}
}