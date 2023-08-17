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
package com.aionemu.gameserver.model.atreian_bestiary;

import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * @author Ranastic
 */

public class PlayerABEntry extends ABEntry
{
	private PersistentState persistentState;
	
	public PlayerABEntry(int id, int killCount, int level, int claimReward, PersistentState persistentState) {
		super(id, killCount, level, claimReward);
		this.persistentState = persistentState;
	}
	
	public PersistentState getPersistentState() {
		return persistentState;
	}
	
	public void setPersistentState(PersistentState persistentState) {
		switch (persistentState) {
			case DELETED:
				if (this.persistentState == PersistentState.NEW) {
					this.persistentState = PersistentState.NOACTION;
				} else {
					this.persistentState = PersistentState.DELETED;
				}
			break;
			case UPDATE_REQUIRED:
				if (this.persistentState != PersistentState.NEW) {
					this.persistentState = PersistentState.UPDATE_REQUIRED;
				}
			break;
			case NOACTION:
			break;
			default:
			this.persistentState = persistentState;
		}
	}
}