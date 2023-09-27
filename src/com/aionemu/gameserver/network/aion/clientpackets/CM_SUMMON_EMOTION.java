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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CM_SUMMON_EMOTION extends AionClientPacket
{
	private static final Logger log = LoggerFactory.getLogger(CM_SUMMON_EMOTION.class);
	
	@SuppressWarnings("unused")
	private int objId;
	
	private int emotionTypeId;
	
	public CM_SUMMON_EMOTION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl() {
		objId = readD();
		emotionTypeId = readC();
	}
	
	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		EmotionType emotionType = EmotionType.getEmotionTypeById(emotionTypeId);
		if (emotionType == EmotionType.UNK) {
			log.error("Unknown emotion type? 0x" + Integer.toHexString(emotionTypeId).toUpperCase());
		}
		Summon summon = player.getSummon();
		if (summon == null) return;
		switch (emotionType) {
			case FLY:
			case LAND:
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, EmotionType.START_EMOTE2));
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
			break;
			case ATTACKMODE:
				summon.setState(CreatureState.WEAPON_EQUIPPED);
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
			break;
			case NEUTRALMODE:
				summon.unsetState(CreatureState.WEAPON_EQUIPPED);
				PacketSendUtility.broadcastPacket(summon, new SM_EMOTION(summon, emotionType));
			break;
		}
	}
}