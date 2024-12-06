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
package com.aionemu.gameserver.network.aion;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.packet.BaseServerPacket;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.network.Crypt;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * Base class for every GS -> Aion Server Packet.
 * 
 * @author -Nemesiss-
 */
public abstract class AionServerPacket extends BaseServerPacket {

	private static final Logger log = LoggerFactory.getLogger(AionServerPacket.class);

	/**
	 * Constructs new server packet
	 */
	protected AionServerPacket() {
		super();
		setOpcode(ServerPacketsOpcodes.getOpcode(getClass()));
	}

	/**
	 * Write packet opcodec and two additional bytes
	 * 
	 * @param buf
	 * @param value
	 */
	private final void writeOP(int value) {
		/** obfuscate packet id */
		int op = Crypt.encodeOpcodec(value);
		buf.putShort((short) (op));
		/** put static server packet code */
		buf.put(Crypt.staticServerPacketCode);
		/** for checksum? */
		buf.putShort((short) (~op));
	}

	public final void write(AionConnection con) {
		write(con, buf);
	}

	/**
	 * Write and encrypt this packet data for given connection, to given buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	public final void write(AionConnection con, ByteBuffer buffer) {
		if (con.getState().equals(AionConnection.State.IN_GAME)
				&& con.getActivePlayer().getPlayerAccount().getAccessLevel() == 5 && NetworkConfig.DISPLAY_PACKETS) {
			if (!this.getPacketName().equals("SM_MESSAGE")) {
				PacketSendUtility.sendMessage(con.getActivePlayer(),
						"0x" + Integer.toHexString(this.getOpcode()).toUpperCase() + " : " + this.getPacketName());
			}
		}
		this.setBuf(buffer);
		buf.putShort((short) 0);
		writeOP(getOpcode());
		writeImpl(con);
		buf.flip();
		buf.putShort((short) buf.limit());
		ByteBuffer b = buf.slice();
		buf.position(0);
		con.encrypt(b);
	}

	/**
	 * Write data that this packet represents to given byte buffer.
	 * 
	 * @param con
	 * @param buf
	 */
	protected void writeImpl(AionConnection con) {

	}

	public final ByteBuffer getBuf() {
		return this.buf;
	}

	/**
	 * Write String to buffer
	 * 
	 * @param text
	 * @param size
	 */
	protected final void writeS(String text, int size) {
		if (text == null) {
			buf.put(new byte[size]);
		} else {
			final int len = text.length();
			for (int i = 0; i < len; i++) {
				buf.putChar(text.charAt(i));
			}
			buf.put(new byte[size - (len * 2)]);
		}
	}

	protected void writeNameId(int nameId) {
		writeH(0x24);
		writeD(nameId);
		writeH(0x00);
	}
}