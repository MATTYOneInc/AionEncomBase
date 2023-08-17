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
package com.aionemu.gameserver.model.gameobjects.player.f2p;

import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author Ranastic (Encom)
/****/

public class F2pAccount implements IExpirable
{
	private int deleteTime = 0;
	private boolean active = false;
	
	public F2pAccount(int deletionTime) {
		deleteTime = deletionTime;
	}
	
	public int getRemainingTime() {
		if (deleteTime == 0) {
			return 0;
		}
		return deleteTime - (int)(System.currentTimeMillis() / 1000L);
	}
	
	public int getExpireTime() {
		return deleteTime;
	}
	
	public boolean getActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void expireEnd(Player player) {
		setActive(false);
		player.getF2p().remove();
		PacketSendUtility.sendBrightYellowMessageOnCenter(player, "<F2p Pack> is expired!!!");
	}
	
	public boolean canExpireNow() {
		return true;
	}
	
	public void expireMessage(Player player, int time) {
		PacketSendUtility.sendBrightYellowMessageOnCenter(player, "<F2p Pack> end!!!");
	}
}