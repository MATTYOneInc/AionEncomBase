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

import com.aionemu.commons.utils.ExitCode;
import com.aionemu.gameserver.network.chatserver.ChatServerConnection.State;
import com.aionemu.gameserver.network.chatserver.CsClientPacket;
import com.aionemu.gameserver.network.chatserver.serverpackets.SM_CS_AUTH;
import com.aionemu.gameserver.services.ChatService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class CM_CS_AUTH_RESPONSE extends CsClientPacket {
	protected static final Logger log = LoggerFactory.getLogger(CM_CS_AUTH_RESPONSE.class);
	private int response;
	private byte[] ip;
	private int port;

	public CM_CS_AUTH_RESPONSE(int opcode) {
		super(opcode);
	}

	@Override
	protected void readImpl() {
		response = readC();
		ip = readB(4);
		port = readH();
	}

	@Override
	protected void runImpl() {
		switch (response) {
		case 0: // Authed
			log.info("GameServer authed successfully IP : " + (ip[0] & 0xFF) + "." + (ip[1] & 0xFF) + "."
					+ (ip[2] & 0xFF) + "." + (ip[3] & 0xFF) + " Port: " + port);
			getConnection().setState(State.AUTHED);
			ChatService.setIp(ip);
			ChatService.setPort(port);
			break;
		case 1: // Not Authed
			log.error("GameServer is not authenticated at ChatServer side");
			System.exit(ExitCode.CODE_ERROR);
			break;
		case 2: // Already Registered
			log.info("GameServer is already registered at ChatServer side! trying again...");
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					CM_CS_AUTH_RESPONSE.this.getConnection().sendPacket(new SM_CS_AUTH());
				}
			}, 10000);
			break;
		}
	}
}