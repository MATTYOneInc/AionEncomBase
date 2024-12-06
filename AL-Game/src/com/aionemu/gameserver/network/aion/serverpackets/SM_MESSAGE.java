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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_MESSAGE extends AionServerPacket {
	private Player player;
	private int senderObjectId;
	private String message;
	private String senderName;
	private Race race;
	private ChatType chatType;
	private float x;
	private float y;
	private float z;

	public SM_MESSAGE(Player player, String message, ChatType chatType) {
		this.player = player;
		this.senderObjectId = player.getObjectId();
		this.senderName = player.getName();
		this.message = message;
		this.race = player.getRace();
		this.chatType = chatType;
		this.x = player.getX();
		this.y = player.getY();
		this.z = player.getZ();
	}

	public SM_MESSAGE(int senderObjectId, String senderName, String message, ChatType chatType) {
		this.senderObjectId = senderObjectId;
		this.senderName = senderName;
		this.message = message;
		this.chatType = chatType;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		boolean canRead = true;
		if (race != null) {
			canRead = chatType.isSysMsg() || CustomConfig.SPEAKING_BETWEEN_FACTIONS || player.getAccessLevel() > 0
					|| (con.getActivePlayer() != null && con.getActivePlayer().getAccessLevel() > 0);
		}
		writeC(chatType.toInteger());
		writeC(canRead ? 0 : race.getRaceId() + 1);
		writeD(senderObjectId);
		switch (chatType) {
		case NORMAL:
		case GOLDEN_YELLOW:
		case WHITE:
		case YELLOW:
		case BRIGHT_YELLOW:
		case WHITE_CENTER:
		case YELLOW_CENTER:
		case BRIGHT_YELLOW_CENTER:
			writeH(0x00);
			writeS(message);
			break;
		case SHOUT:
			writeS(senderName);
			writeS(message);
			writeF(x);
			writeF(y);
			writeF(z);
			break;
		case ALLIANCE:
		case GROUP:
		case GROUP_LEADER:
		case LEGION:
		case WHISPER:
		case LEAGUE:
		case LEAGUE_ALERT:
		case CH1:
		case CH2:
		case CH3:
		case CH4:
		case CH5:
		case CH6:
		case CH7:
		case CH8:
		case CH9:
		case CH10:
		case COMMAND:
		case ANNOUNCE:
			writeS(senderName);
			writeS(message);
			break;
		}
	}
}