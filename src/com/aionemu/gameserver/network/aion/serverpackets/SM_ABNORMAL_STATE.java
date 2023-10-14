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

import java.util.Collection;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.skillengine.model.Effect;

public class SM_ABNORMAL_STATE extends AionServerPacket
{
	private Collection<Effect> effects;
	private int abnormals;
	
	public SM_ABNORMAL_STATE(Collection<Effect> effects, int abnormals) {
		this.effects = effects;
		this.abnormals = abnormals;
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(abnormals);
		writeD(0);
		writeD(0);//unk 4.5
		writeC(0x7F);//unk 4.5(127)what's that? O.o
		writeH(effects.size());
		for (Effect effect : effects) {
			writeD(effect.getEffectorId());
			writeH(effect.getSkillId());
			writeC(effect.getSkillLevel());
			writeC(effect.getTargetSlot());
			writeD(effect.getRemainingTime());
			writeH(0x00); //unk 5.3
		}
	}
}