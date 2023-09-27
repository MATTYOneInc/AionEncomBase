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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ranastic
 */
public class SM_MAC_INFO extends AionServerPacket {

	private String macAddress;
	private String hardName;
	private int localIP;
	
	public SM_MAC_INFO(String macAddress, String hardName, int localIP) {
		this.macAddress = macAddress;
		this.hardName = hardName;
		this.localIP = localIP;
	}
	
    @Override
    protected void writeImpl(AionConnection con) {
    	writeS(macAddress);
    	writeS(hardName);
        writeD(localIP);
    }
}