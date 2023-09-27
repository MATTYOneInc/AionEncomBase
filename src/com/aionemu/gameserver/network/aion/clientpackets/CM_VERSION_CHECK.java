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
import com.aionemu.gameserver.network.aion.serverpackets.*;

/**
 * @author -Nemesiss-
 */

public class CM_VERSION_CHECK extends AionClientPacket
{
	private int version;
	@SuppressWarnings("unused")
	private int subversion;
	@SuppressWarnings("unused")
	private int windowsEncoding;
	@SuppressWarnings("unused")
	private int windowsVersion;
	@SuppressWarnings("unused")
	private int windowsSubVersion;
	private int unk;
	
	public CM_VERSION_CHECK(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		version = readH();
		subversion = readH();
		windowsEncoding = readD();
		windowsVersion = readD();
		windowsSubVersion = readD();
		unk = readC();
	}
	
	@Override
	protected void runImpl() {
		sendPacket(new SM_VERSION_CHECK(version));
		sendPacket(new SM_0x125());
		sendPacket(new SM_0x126(unk));
		sendPacket(new SM_UNK_168());
		sendPacket(new SM_BLACKCLOUD_TRADE());
		sendPacket(new SM_CASH_BUFF(2));
	}
}