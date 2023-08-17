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
package com.aionemu.gameserver.model.instance.playerreward;

/****/
/** Author Rinzler (Encom)
/****/

public class CruciblePlayerReward extends InstancePlayerReward
{
	private int insignia;
	private int spawnPosition;
	private boolean isRewarded = false;
	private boolean isPlayerLeave = false;
	private boolean isPlayerDefeated = false;
	
	public CruciblePlayerReward(Integer object) {
		super(object);
	}
	
	public boolean isRewarded() {
		return isRewarded;
	}
	
	public void setRewarded() {
		isRewarded = true;
	}
	
	public void setInsignia(int insignia) {
		this.insignia = insignia;
	}
	
	public int getInsignia() {
		return insignia;
	}
	
	public void setSpawnPosition(int spawnPosition) {
		this.spawnPosition = spawnPosition;
	}
	
	public int getSpawnPosition() {
		return spawnPosition;
	}
	
	public boolean isPlayerLeave() {
		return isPlayerLeave;
	}
	
	public void setPlayerLeave() {
		isPlayerLeave = true;
	}
	
	public void setPlayerDefeated(boolean value) {
		isPlayerDefeated = value;
	}
	
	public boolean isPlayerDefeated() {
		return isPlayerDefeated;
	}
}