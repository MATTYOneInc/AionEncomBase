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
import com.aionemu.gameserver.dao.BlockListDAO;
import com.aionemu.gameserver.dao.FriendListDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.BlockedPlayer;
import com.aionemu.gameserver.model.gameobjects.player.Friend;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BLOCK_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BLOCK_RESPONSE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_NOTIFY;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FRIEND_RESPONSE;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.world.World;

public class SocialService
{
	public static boolean addBlockedUser(Player player, Player blockedPlayer, String reason) {
		if (DAOManager.getDAO(BlockListDAO.class).addBlockedUser(player.getObjectId(), blockedPlayer.getObjectId(), reason)) {
			player.getBlockList().add(new BlockedPlayer(blockedPlayer.getCommonData(), reason));
			player.getClientConnection().sendPacket(new SM_BLOCK_RESPONSE(SM_BLOCK_RESPONSE.BLOCK_SUCCESSFUL, blockedPlayer.getName()));
			player.getClientConnection().sendPacket(new SM_BLOCK_LIST());
			return true;
		}
		return false;
	}
	
	public static boolean deleteBlockedUser(Player player, int blockedUserId) {
		if (DAOManager.getDAO(BlockListDAO.class).delBlockedUser(player.getObjectId(), blockedUserId)) {
			player.getBlockList().remove(blockedUserId);
			player.getClientConnection().sendPacket(new SM_BLOCK_RESPONSE(SM_BLOCK_RESPONSE.UNBLOCK_SUCCESSFUL, DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(blockedUserId).getName()));
			player.getClientConnection().sendPacket(new SM_BLOCK_LIST());
			return true;
		}
		return false;
	}
	
	public static boolean setBlockedReason(Player player, BlockedPlayer target, String reason) {
		if (!target.getReason().equals(reason)) {
			if (DAOManager.getDAO(BlockListDAO.class).setReason(player.getObjectId(), target.getObjId(), reason)) {
				target.setReason(reason);
				player.getClientConnection().sendPacket(new SM_BLOCK_LIST());
				return true;
			}
		}
		return false;
	}
	
	public static void makeFriends(Player friend1, Player friend2) {
		DAOManager.getDAO(FriendListDAO.class).addFriends(friend1, friend2);
		friend1.getFriendList().addFriend(new Friend(friend2.getCommonData()));
		friend2.getFriendList().addFriend(new Friend(friend1.getCommonData()));
		friend1.getClientConnection().sendPacket(new SM_FRIEND_LIST());
		friend2.getClientConnection().sendPacket(new SM_FRIEND_LIST());
		friend1.getClientConnection().sendPacket(new SM_FRIEND_RESPONSE(friend2.getName(), SM_FRIEND_RESPONSE.TARGET_ADDED));
		friend2.getClientConnection().sendPacket(new SM_FRIEND_RESPONSE(friend1.getName(), SM_FRIEND_RESPONSE.TARGET_ADDED));
	}
	
	public static void deleteFriend(Player deleter, int exFriend2Id) {
		if (DAOManager.getDAO(FriendListDAO.class).delFriends(deleter.getObjectId(), exFriend2Id)) {
			Player friend2Player = PlayerService.getCachedPlayer(exFriend2Id);
			if (friend2Player == null) {
				friend2Player = World.getInstance().findPlayer(exFriend2Id);
			}
			String friend2Name = friend2Player != null ? friend2Player.getName() : DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(exFriend2Id).getName();
			deleter.getFriendList().delFriend(exFriend2Id);
			deleter.getClientConnection().sendPacket(new SM_FRIEND_LIST());
			deleter.getClientConnection().sendPacket(new SM_FRIEND_RESPONSE(friend2Name, SM_FRIEND_RESPONSE.TARGET_REMOVED));
			if (friend2Player != null) {
				friend2Player.getFriendList().delFriend(deleter.getObjectId());
				if (friend2Player.isOnline()) {
					friend2Player.getClientConnection().sendPacket(new SM_FRIEND_NOTIFY(SM_FRIEND_NOTIFY.DELETED, deleter.getName()));
					friend2Player.getClientConnection().sendPacket(new SM_FRIEND_LIST());
				}
			}
		}
	}
	
	public static void setFriendNote(Player player, Friend friend, String notice) {
		friend.setNote(notice);
		DAOManager.getDAO(FriendListDAO.class).setFriendNote(player.getObjectId(), friend.getOid(), notice);
		player.getClientConnection().sendPacket(new SM_FRIEND_LIST());
	}
}