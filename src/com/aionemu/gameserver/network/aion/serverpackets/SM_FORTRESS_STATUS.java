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

import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.SiegeService;

import java.util.Map;

public class SM_FORTRESS_STATUS extends AionServerPacket
{
	@Override
	protected void writeImpl(AionConnection con) {
		Map<Integer, FortressLocation> fortresses = SiegeService.getInstance().getFortresses();
		Influence inf = Influence.getInstance();
		writeC(1);
		writeD(SiegeService.getInstance().getSecondsBeforeHourEnd());
		writeF(inf.getGlobalElyosInfluence());
		writeF(inf.getGlobalAsmodiansInfluence());
		writeF(inf.getGlobalBalaursInfluence());
		writeH(6);
		//========[ABYSS]========
		writeD(400010000);
		writeF(inf.getAbyssElyosInfluence());
		writeF(inf.getAbyssAsmodiansInfluence());
		writeF(inf.getAbyssBalaursInfluence());
		//========[BELUS]========
		writeD(400020000);
		writeF(inf.getBelusElyosInfluence());
		writeF(inf.getBelusAsmodiansInfluence());
		writeF(inf.getBelusBalaursInfluence());
		//========[ASPIDA]=======
		writeD(400040000);
		writeF(inf.getAspidaElyosInfluence());
		writeF(inf.getAspidaAsmodiansInfluence());
		writeF(inf.getAspidaBalaursInfluence());
		//=======[ATANATOS]======
		writeD(400050000);
		writeF(inf.getAtanatosElyosInfluence());
		writeF(inf.getAtanatosAsmodiansInfluence());
		writeF(inf.getAtanatosBalaursInfluence());
		//=======[DISILLON]======
		writeD(400060000);
		writeF(inf.getDisillonElyosInfluence());
		writeF(inf.getDisillonAsmodiansInfluence());
		writeF(inf.getDisillonBalaursInfluence());
		//======[KALDOR]=========
		writeD(600090000);
        writeF(inf.getKaldorElyosInfluence());
        writeF(inf.getKaldorAsmodiansInfluence());
        writeF(inf.getKaldorBalaursInfluence());
		writeH(fortresses.size());
		for (FortressLocation fortress : fortresses.values()) {
			writeD(fortress.getLocationId());
			writeC(fortress.getNextState());
		}
	}
}