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

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_EMOTION extends AionServerPacket
{
	private int senderObjectId;
	private EmotionType emotionType;
	private int emotion;
	private int targetObjectId;
	private float speed;
	private int state;
	private int baseAttackSpeed;
	private int currentAttackSpeed;
	private float x;
	private float y;
	private float z;
	private byte heading;
	
	public SM_EMOTION(Creature creature, EmotionType emotionType) {
		this(creature, emotionType, 0, 0);
	}
	
	public SM_EMOTION(Creature creature, EmotionType emotionType, int emotion, int targetObjectId) {
		this.senderObjectId = creature.getObjectId();
		this.emotionType = emotionType;
		this.emotion = emotion;
		this.targetObjectId = targetObjectId;
		this.state = creature.getState();
		Stat2 aSpeed = creature.getGameStats().getAttackSpeed();
		this.baseAttackSpeed = aSpeed.getBase();
		this.currentAttackSpeed = aSpeed.getCurrent();
		this.speed = creature.getGameStats().getMovementSpeedFloat();
	}
	
    public SM_EMOTION(int Objid, EmotionType emotionType, int state) {
		this.senderObjectId = Objid;
		this.emotionType = emotionType;
		this.state = state;
	}
	
	public SM_EMOTION(Player player, EmotionType emotionType, int emotion, float x, float y, float z, byte heading, int targetObjectId) {
		this.senderObjectId = player.getObjectId();
		this.emotionType = emotionType;
		this.emotion = emotion;
		this.x = x;
		this.y = y;
		this.z = z;
		this.heading = heading;
		this.targetObjectId = targetObjectId;
		this.state = player.getState();
		this.speed = player.getGameStats().getMovementSpeedFloat();
		Stat2 aSpeed = player.getGameStats().getAttackSpeed();
		this.baseAttackSpeed = aSpeed.getBase();
		this.currentAttackSpeed = aSpeed.getCurrent();
	}
	
	@Override
	protected void writeImpl(AionConnection con) {
		writeD(senderObjectId);
		writeC(emotionType.getTypeId());
		writeH(state);
		writeF(speed);
		switch (emotionType) {
			case SELECT_TARGET:
			case JUMP:
			case SIT:
			case STAND:
			case LAND_FLYTELEPORT:
			case WINDSTREAM_START_BOOST:
			case WINDSTREAM_END_BOOST:
			case FLY:
			case LAND:
			case ATTACKMODE:
			case NEUTRALMODE:
			case WALK:
			case RUN:
			case OPEN_PRIVATESHOP:
			case CLOSE_PRIVATESHOP:
			case POWERSHARD_ON:
			case POWERSHARD_OFF:
			case ATTACKMODE2:
			case NEUTRALMODE2:
			case START_FEEDING:
			case END_FEEDING:
			case END_SPRINT:
			case WINDSTREAM_END:
			case WINDSTREAM_EXIT:
			case WINDSTREAM_STRAFE:
			case END_DUEL:
			case PET_SNUGGLE:
			case PET_EMOTION_2:
			case PET_EMOTION_3:
			case PET_EMOTION_4:
			break;
			case DIE:
			case START_LOOT:
			case END_LOOT:
			case END_QUESTLOOT:
			case OPEN_DOOR:
				writeD(targetObjectId);
			break;
			case CHAIR_SIT:
			case CHAIR_UP:
				writeF(x);
				writeF(y);
				writeF(z);
				writeC(heading);
			break;
			case START_FLYTELEPORT:
				writeD(emotion);
			break;
			case WINDSTREAM:
				writeD(emotion);
				writeD(targetObjectId);
			break;
	        case RIDE:
	        case RIDE_END:
	            if (targetObjectId != 0) {
	               writeD(targetObjectId);
	            }
	            writeH(0);
	            writeC(0);
	            writeD(0x3F);
	            writeD(0x3F);
	            writeC(0x40);
	        break;
		    case START_SPRINT:
	            writeD(0);
	        break;
		    case RESURRECT:
				writeD(0);
		    break;
			case EMOTE:
				writeD(targetObjectId);
				writeH(emotion);
				writeC(1);
			break;
			case START_EMOTE2:
				writeH(baseAttackSpeed);
				writeH(currentAttackSpeed);
				writeC(0);
			break;
			default:
			if (targetObjectId != 0) {
				writeD(targetObjectId);
			}
		}
	}
}