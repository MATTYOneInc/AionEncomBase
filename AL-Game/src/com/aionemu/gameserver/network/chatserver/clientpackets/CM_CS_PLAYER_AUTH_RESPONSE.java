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
package com.aionemu.gameserver.network.chatserver.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.network.chatserver.CsClientPacket;
import com.aionemu.gameserver.services.ChatService;

public class CM_CS_PLAYER_AUTH_RESPONSE extends CsClientPacket {
	protected static final Logger log = LoggerFactory.getLogger(CM_CS_PLAYER_AUTH_RESPONSE.class);
	private int playerId;
	private byte[] token;

	public CM_CS_PLAYER_AUTH_RESPONSE(int opcode) {
		super(opcode);
	}

	@Override
	protected void readImpl() {
		playerId = readD();
		int tokenLenght = readC();
		token = readB(tokenLenght);
	}

	@Override
	protected void runImpl() {
		ChatService.playerAuthed(playerId, token);
	}
}