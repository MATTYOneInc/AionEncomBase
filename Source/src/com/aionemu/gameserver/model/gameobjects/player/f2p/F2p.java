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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.F2pDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.taskmanager.tasks.ExpireTimerTask;

/****/
/** Author Ranastic (Encom)
/****/

public class F2p
{
	private Player owner;
	private F2pAccount f2pAccount;
	
	public F2p(Player owner) {
		this.owner = owner;
	}
	
	public void add(F2pAccount f2pacc, boolean isNew) {
		f2pAccount = f2pacc;
		f2pacc.setActive(true);
		if (isNew) {
			if (f2pacc.getExpireTime() != 0) {
				ExpireTimerTask.getInstance().addTask(f2pacc, owner);
			}
			DAOManager.getDAO(F2pDAO.class).storeF2p(owner.getObjectId().intValue(), f2pacc.getExpireTime());
		}
	}

	public void update(F2pAccount f2pacc, boolean isNew) {
		f2pAccount = f2pacc;
		f2pacc.setActive(true);
		if (isNew) {
			if (f2pacc.getExpireTime() != 0) {
				ExpireTimerTask.getInstance().addTask(f2pacc, owner);
			}
			DAOManager.getDAO(F2pDAO.class).storeF2p(owner.getObjectId().intValue(), f2pacc.getExpireTime());
		}
	}
	
	public F2pAccount getF2pAccount() {
		return f2pAccount;
	}
	
	public boolean remove() {
		if (f2pAccount != null) {
			f2pAccount.setActive(false);
			DAOManager.getDAO(F2pDAO.class).deleteF2p(owner.getObjectId().intValue());
			owner.getEquipment().checkRankLimitItems();
			return true;
		}
		return false;
	}
}