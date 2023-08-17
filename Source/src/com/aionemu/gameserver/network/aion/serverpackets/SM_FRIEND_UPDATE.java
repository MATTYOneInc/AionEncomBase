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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.Friend;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sent to update a player's status in a friendlist
 * 
 * @author Ben
 */
public class SM_FRIEND_UPDATE extends AionServerPacket {

	private int friendObjId;

	private static Logger log = LoggerFactory.getLogger(SM_FRIEND_UPDATE.class);

	public SM_FRIEND_UPDATE(int friendObjId) {
		this.friendObjId = friendObjId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		Friend f = con.getActivePlayer().getFriendList().getFriend(friendObjId);
		if (f == null) {
			log.debug("Attempted to update friend list status of " + friendObjId + " for " + con.getActivePlayer().getName() + " - object ID not found on friend list");
		}
		else {
			writeS(f.getName());
			writeD(f.getLevel());
			writeD(f.getPlayerClass().getClassId());
			writeC(f.isOnline() ? 1 : 0); // Online status - No idea why this and f.getStatus are used
			writeD(f.getMapId());
			writeD(f.getLastOnlineTime()); // Date friend was last online as a Unix timestamp.
			writeS(f.getNote());
			writeC(f.getStatus().getId());
		}
	}
}