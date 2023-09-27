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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CM_S_REP_WEB_SESSIONKEY extends AionClientPacket {

	private static Logger log = LoggerFactory.getLogger(CM_S_REP_WEB_SESSIONKEY.class);
	
	private int unk;
	private String text;
	
	public CM_S_REP_WEB_SESSIONKEY(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		unk = readC();
		text = readS();
	}

	@Override
	protected void runImpl() {
		log.info(text);
	}
}