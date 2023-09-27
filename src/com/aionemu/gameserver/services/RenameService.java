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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.dao.LegionDAO;
import com.aionemu.gameserver.dao.OldNamesDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RENAME;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.World;

import java.util.Iterator;

public class RenameService
{
	public static boolean renamePlayer(Player player, String oldName, String newName, int item) {
    if (!NameRestrictionService.isValidName(newName)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400151));
			return false;
		} if (NameRestrictionService.isForbiddenWord(newName)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400153));
			return false;
		} if (!PlayerService.isFreeName(newName)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400155));
			return false;
		} if (player.getName().equals(newName)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400153));
			return false;
		} if (!CustomConfig.OLD_NAMES_COUPON_DISABLED && PlayerService.isOldName(newName)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400155));
			return false;
		} if ((player.getInventory().getItemByObjId(item).getItemId() != 169670000 &&
		    player.getInventory().getItemByObjId(item).getItemId() != 169670001) ||
			(!player.getInventory().decreaseByObjectId(item, 1))) {
			AuditLogger.info(player, "Try rename youself without coupon.");
			return false;
		} if (!CustomConfig.OLD_NAMES_COUPON_DISABLED) {
			DAOManager.getDAO(OldNamesDAO.class).insertNames(player.getObjectId(), player.getName(), newName);
		    player.getCommonData().setName(newName);
		}
		Iterator<Player> onlinePlayers = World.getInstance().getPlayersIterator();
		while (onlinePlayers.hasNext()) {
			Player p = onlinePlayers.next();
			if (p != null && p.getClientConnection() != null) {
				PacketSendUtility.sendPacket(p, new SM_RENAME(player.getObjectId(), oldName, newName));
			}
		}
		DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
		return true;
	}
	
	public static boolean renameLegion(Player player, String name, int item) {
		if (!player.isLegionMember()) {
			return false;
		} if (!LegionService.getInstance().isValidName(name)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400152));
			return false;
		} if (NameRestrictionService.isForbiddenWord(name)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400160));
			return false;
		} if (DAOManager.getDAO(LegionDAO.class).isNameUsed(name)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400156));
			return false;
		} if (player.getLegion().getLegionName().equals(name)) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400154));
			return false;
		} if ((player.getInventory().getItemByObjId(item).getItemId() != 169680000 &&
		    player.getInventory().getItemByObjId(item).getItemId() != 169680001) ||
			(!player.getInventory().decreaseByObjectId(item, 1))) {
			AuditLogger.info(player, "Try rename legion without coupon.");
			return false;
		}
		LegionService.getInstance().setLegionName(player.getLegion(), name, true);
		return true;
	}
}