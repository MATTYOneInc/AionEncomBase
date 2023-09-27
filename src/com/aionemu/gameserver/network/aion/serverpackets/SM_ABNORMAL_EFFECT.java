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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.skillengine.model.Effect;

import java.util.Collection;

public class SM_ABNORMAL_EFFECT extends AionServerPacket
{
	private int effectedId;
	private int effectType = 1;
	private int abnormals;
	private Collection<Effect> filtered;
	
	public SM_ABNORMAL_EFFECT(Creature effected, int abnormals, Collection<Effect> effects) {
		this.abnormals = abnormals;
		this.effectedId = effected.getObjectId();
		this.filtered = effects;
		if (effected instanceof Player) {
			effectType = 2;
		}
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(effectedId);
		writeC(effectType); //unk
		writeD(0); // unk
		writeD(abnormals); // unk
		writeD(0); // unk
		writeC(0x7F);//unk 5.3
		writeH(filtered.size()); // effects size
		for (Effect effect : filtered) {
			switch(effectType) {
				case 2:
					writeD(effect.getEffectorId());
					writeH(effect.getSkillId());
					writeC(effect.getSkillLevel());
					writeC(effect.getTargetSlot());
					writeD(effect.getRemainingTime());
					writeH(0x00);//unk 5.3
				break;
				case 1:
					writeH(effect.getSkillId());
					writeC(effect.getSkillLevel());
					writeC(effect.getTargetSlot());
					writeD(effect.getRemainingTime());
					writeH(0x00);//unk 5.3
				break;
				default:
					writeH(effect.getSkillId());
					writeC(effect.getSkillLevel());
			}
		}
	}
}