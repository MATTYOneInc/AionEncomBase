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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_0x14F;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ACCOUNT_PROPERTIES;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHARACTER_LIST;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CM_CHARACTER_LIST extends AionClientPacket
{
	private static Logger log = LoggerFactory.getLogger(CM_CHARACTER_LIST.class);
	
	private int playOk2;
	
	public CM_CHARACTER_LIST(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		playOk2 = readD();
	}
	
	@Override
	protected void runImpl() {
		boolean isGM = (getConnection()).getAccount().getAccessLevel() >= AdminConfig.GM_PANEL;
		sendPacket(new SM_ACCOUNT_PROPERTIES(isGM));
		sendPacket(new SM_0x14F());
		sendPacket(new SM_CHARACTER_LIST(0, playOk2));
		sendPacket(new SM_CHARACTER_LIST(2, playOk2));
	}
}