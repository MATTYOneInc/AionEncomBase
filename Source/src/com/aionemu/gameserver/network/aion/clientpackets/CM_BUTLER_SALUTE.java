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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_BUTLER_SALUTE;

/**
 * @author Ranastic
 */

public class CM_BUTLER_SALUTE extends AionClientPacket
{
	private int playerObjId;
	private int isInside;
	private int unk1;
	private int unk2;
	private int unk3;
	private int unk4;
	
	public CM_BUTLER_SALUTE(int opcode, State state, State... states) {
        super(opcode, state, states);
    }
	
    @Override
    protected void readImpl() {
    	unk1 = readD();
    	unk2 = readC();
    	unk3 = readD();
    	unk4 = readC();
    	playerObjId = readD();
    	isInside = readC();
    }
	
    @Override
    protected void runImpl() {
    	sendPacket(new SM_BUTLER_SALUTE(unk1, unk2, unk3, unk4, playerObjId, isInside));
    }
}