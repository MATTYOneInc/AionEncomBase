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
package com.aionemu.gameserver.model.gameobjects.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.player.FriendList.Status;
import com.aionemu.gameserver.world.WorldPosition;

public class Friend
{
	private static final Logger log = LoggerFactory.getLogger(Friend.class);
	private PlayerCommonData pcd;
	private String friendNote = "";

	public Friend(PlayerCommonData pcd) {
		this.pcd = pcd;
	}

	public Status getStatus() {
		if (pcd.getPlayer() == null || !pcd.isOnline()) {
			return FriendList.Status.OFFLINE;
		}
		return pcd.getPlayer().getFriendList().getStatus();
	}

	public void setPCD(PlayerCommonData pcd) {
		this.pcd = pcd;
	}

	public String getName() {
		return pcd.getName();
	}

	public int getLevel() {
		return pcd.getLevel();
	}

	public String getNote() {
		return pcd.getNote();
	}

	public PlayerClass getPlayerClass() {
		return pcd.getPlayerClass();
	}

	public int getMapId() {
		WorldPosition position = pcd.getPosition();
		if (position == null) {
			log.warn("Null friend position: {}", pcd.getPlayerObjId());
			return 0;
		}
		return position.getMapId();
	}

	public int getLastOnlineTime() {
		if (pcd.getLastOnline() == null || isOnline()) {
			return 0;
		}
		return (int) (pcd.getLastOnline().getTime() / 1000);
	}

	public int getOid() {
		return pcd.getPlayerObjId();
	}

	public Player getPlayer() {
		return pcd.getPlayer();
	}

	public boolean isOnline() {
		return pcd.isOnline();
	}

	public String getFriendNote() {
		return friendNote;
	}

	public void setNote(String note) {
		friendNote = note;
	}
}