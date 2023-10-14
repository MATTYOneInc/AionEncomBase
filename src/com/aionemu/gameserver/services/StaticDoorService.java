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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class StaticDoorService
{
	private static final Logger log = LoggerFactory.getLogger(StaticDoorService.class);
	
	public static StaticDoorService getInstance() {
		return SingletonHolder.instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final StaticDoorService instance = new StaticDoorService();
	}
	
	public void openStaticDoor(final Player player, int doorId) {
		if (player.getAccessLevel() >= 3) {
			PacketSendUtility.sendMessage(player, "Door Id: " + doorId);
		}
		StaticDoor door = player.getPosition().getWorldMapInstance().getDoors().get(doorId);
		if (door == null) {
			log.warn("Not spawned door worldId: "+ player.getWorldId()+" doorId: "+doorId);
			return;
		}
		int keyId = door.getObjectTemplate().getKeyId();
		if (player.getAccessLevel() >= 3) {
			PacketSendUtility.sendMessage(player, "Key Id: " + keyId);
		} if (checkStaticDoorKey(player, doorId, keyId)) {
			door.setOpen(true);
		}
		InstanceService.onOpenDoor(player, doorId);
	}
	
	public boolean checkStaticDoorKey(Player player, int doorId, int keyId) {
		if (player.getAccessLevel() >= AdminConfig.DOORS_OPEN) {
			return true;
		} if (keyId == 0) {
			return true;
		} if (keyId == 1) {
			return false;
		} if (!player.getInventory().decreaseByItemId(keyId, 1)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(false, 1300723, player.getObjectId(), 2));
			return false;
		}
		return true;
	}
}