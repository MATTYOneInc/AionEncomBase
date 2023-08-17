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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.network.BannedMacManager;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MAC_INFO;
import org.slf4j.LoggerFactory;


/**
 * In this packet client is sending Mac Address - haha.
 * 
 * @author -Nemesiss-, KID
 */
public class CM_MAC_ADDRESS extends AionClientPacket {
	/**
	 * Mac Addres send by client in the same format as: ipconfig /all [ie:
	 * xx-xx-xx-xx-xx-xx]
	 */
	private String	macAddress;
	private String HardName;
	private int localIP;

	/**
	 * Constructs new instance of <tt>CM_MAC_ADDRESS </tt> packet
	 * 
	 * @param opcode
	 */
	public CM_MAC_ADDRESS(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl() {
		readC();
		short counter = (short)readH();
		for(short i = 0; i < counter; i++)
			readD();
		macAddress = readS();
		HardName = readS();
		localIP = readD();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl() {
		if(BannedMacManager.getInstance().isBanned(macAddress)) {
			//TODO some information packets
			this.getConnection().closeNow();
			LoggerFactory.getLogger(CM_MAC_ADDRESS.class).info("[MAC_AUDIT] "+macAddress+" ("+this.getConnection().getIP()+") was kicked due to mac ban");
			LoggerFactory.getLogger(CM_MAC_ADDRESS.class).info("[Hard_AUDIT] "+HardName+" ("+HardName+")");
		}
		else {
			this.getConnection().setMacAddress(macAddress);
			sendPacket(new SM_MAC_INFO(macAddress, HardName, localIP));
		}
	}
}