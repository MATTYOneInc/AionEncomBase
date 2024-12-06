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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Created by Ace on 14/02/2017.
 */

public class SM_FLAG_INFO extends AionServerPacket {
	int count;
	private Creature _npc;
	private NpcTemplate npcTemplate;
	private int npcId;

	public SM_FLAG_INFO(int count, Npc npc) {
		this.count = count;
		this._npc = npc;
		npcId = npc.getNpcId();
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(count);
		if (_npc != null) {
			writeD(npcId);
			writeD(_npc.getObjectId());
			writeD(_npc.getLifeStats().getCurrentHp());
			writeD(_npc.getLifeStats().getMaxHp());
			writeF(_npc.getX());
			writeF(_npc.getY());
			writeF(_npc.getZ());
		} else {
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeF(0);
			writeF(0);
			writeF(0);
		}
	}
}