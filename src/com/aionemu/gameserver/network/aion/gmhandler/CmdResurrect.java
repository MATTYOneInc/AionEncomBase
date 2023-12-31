/**
 * This file is part of Encom.
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
package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RESURRECT;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Alcapwnd
 */
public class CmdResurrect extends AbstractGMHandler {

	public CmdResurrect(Player admin, String params) {
		super(admin, params);
		run();
	}

	public void run() {
		Player t = target != null ? target : admin;
		if (!t.getLifeStats().isAlreadyDead()) {
			return;
		}
		t.setPlayerResActivate(true);
		PacketSendUtility.sendPacket(t, new SM_RESURRECT(admin));
	}
}