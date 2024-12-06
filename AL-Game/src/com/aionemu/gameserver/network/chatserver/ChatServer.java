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
package com.aionemu.gameserver.network.chatserver;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.Dispatcher;
import com.aionemu.commons.network.NioServer;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_AUTH;
import com.aionemu.gameserver.network.chatserver.serverpackets.SM_CS_PLAYER_LOGOUT;
import com.aionemu.gameserver.network.factories.CsPacketHandlerFactory;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public class ChatServer {
	private static final Logger log = LoggerFactory.getLogger(ChatServer.class);
	private ChatServerConnection chatServer;
	private NioServer nioServer;
	private boolean serverShutdown = false;

	public static final ChatServer getInstance() {
		return SingletonHolder.instance;
	}

	private ChatServer() {
	}

	public void setNioServer(NioServer nioServer) {
		this.nioServer = nioServer;
	}

	public ChatServerConnection connect() {
		SocketChannel sc;
		for (;;) {
			chatServer = null;
			log.info("Connecting to ChatServer: " + NetworkConfig.CHAT_ADDRESS);
			try {
				sc = SocketChannel.open(NetworkConfig.CHAT_ADDRESS);
				sc.configureBlocking(false);
				Dispatcher d = nioServer.getReadWriteDispatcher();
				CsPacketHandlerFactory csPacketHandlerFactory = new CsPacketHandlerFactory();
				chatServer = new ChatServerConnection(sc, d, csPacketHandlerFactory.getPacketHandler());
				d.register(sc, SelectionKey.OP_READ, chatServer);
				chatServer.initialized();
				return chatServer;
			} catch (Exception e) {
				log.info("Cant connect to ChatServer: " + e.getMessage());
			}
			try {
				Thread.sleep(10 * 1000);
			} catch (Exception e) {
			}
		}
	}

	public void chatServerDown() {
		log.warn("Connection with ChatServer lost...");
		chatServer = null;
		if (!serverShutdown) {
			ThreadPoolManager.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					connect();
				}
			}, 5000);
		}
	}

	public void sendPlayerLoginRequst(Player player) {
		if (chatServer != null) {
			chatServer
					.sendPacket(new SM_CS_PLAYER_AUTH(player.getObjectId(), player.getAcountName(), player.getName()));
		}
	}

	public void sendPlayerLogout(Player player) {
		if (chatServer != null) {
			chatServer.sendPacket(new SM_CS_PLAYER_LOGOUT(player.getObjectId()));
		}
	}

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder {
		protected static final ChatServer instance = new ChatServer();
	}
}