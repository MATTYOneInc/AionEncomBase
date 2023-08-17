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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_ACCOUNT_PROPERTIES extends AionServerPacket
{
	public SM_ACCOUNT_PROPERTIES() {
	}
	
	private boolean isGM;
    private int accountType;
    private int purchaseType;
    private int time;
	
	public SM_ACCOUNT_PROPERTIES(boolean isGM) {
        this.isGM = isGM;
    }
	
    public SM_ACCOUNT_PROPERTIES(boolean isGM, int accountType, int purchaseType, int time) {
		this.isGM = isGM;
        this.accountType = accountType;
        this.purchaseType = purchaseType;
        this.time = time;
    }
	
	@Override
    protected void writeImpl(AionConnection con) {
        writeH(this.isGM ? 3 : 0);
        writeH(0);
        writeD(0);
        writeD(0);
        writeD(this.isGM ? 32768 : 0);
        writeD(0);
        writeC(0);
        writeD(31);
        writeD(0);
        writeD(purchaseType); //Purchase Type.
        writeD(accountType); //Account Type.
        writeD(0);
        writeD(0);
        writeD(0);
        writeD(time);
        writeB(new byte[32]);
    }
}