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
 * This packet show casting spell animation.
 * 
 * @author alexa026
 * @author rhys2002
 */
public class SM_CASTSPELL extends AionServerPacket {

	private final int attackerObjectId;
	private final int spellId;
	private final int level;
	private final int targetType;
	private final int duration;
	private int targetObjectId;
	private float x;
	private float y;
	private float z;
	private int skinId;

	public SM_CASTSPELL(int attackerObjectId, int spellId, int level, int targetType, int targetObjectId, int duration, int skinId) {
		this.attackerObjectId = attackerObjectId;
		this.spellId = spellId;
		this.level = level;
		this.targetType = targetType;
		this.targetObjectId = targetObjectId;
		this.duration = duration;
		this.skinId = skinId;
	}

	public SM_CASTSPELL(int attackerObjectId, int spellId, int level, int targetType, float x, float y, float z, int duration, int skinId) {
		this(attackerObjectId, spellId, level, targetType, 0, duration,  skinId);
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(attackerObjectId);
		writeH(spellId);
		writeC(level);
		
		writeC(targetType);
		switch (targetType) {
			case 0:
			case 3:
			case 4:
				writeD(targetObjectId);
				break;
			case 1:
				writeF(x);
				writeF(y);
				writeF(z);
				break;
			case 2:
				writeF(x);
				writeF(y);
				writeF(z);
				writeD(0);//unk1
				writeD(0);//unk2
				writeD(0);//unk3
				writeD(0);//unk4
				writeD(0);//unk5
				writeD(0);//unk6
				writeD(0);//unk7
				writeD(0);//unk8
		}
		writeH(duration);//unk
		writeC(0x00);//unk
		writeF((float) 0.8);
		//SkinID Skill Animation
		writeH(skinId);
		if (duration > 0) {
			writeC(0x01);//unk
		}
		else {
			writeC(0x00);
		}
	}
}