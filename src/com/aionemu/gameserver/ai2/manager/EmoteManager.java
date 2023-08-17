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
package com.aionemu.gameserver.ai2.manager;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class EmoteManager
{
	public static final void emoteStartAttacking(Npc owner) {
		Creature target = (Creature) owner.getTarget();
		owner.unsetState(CreatureState.WALKING);
		if (!owner.isInState(CreatureState.WEAPON_EQUIPPED)) {
			owner.setState(CreatureState.WEAPON_EQUIPPED);
			PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, target.getObjectId()));
			PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.ATTACKMODE, 0, target.getObjectId()));
		}
	}
	
	public static final void emoteStopAttacking(Npc owner) {
		owner.unsetState(CreatureState.WEAPON_EQUIPPED);
		if (owner.getTarget() != null && owner.getTarget() instanceof Player) {
			PacketSendUtility.sendPacket((Player) owner.getTarget(), SM_SYSTEM_MESSAGE.STR_UI_COMBAT_NPC_RETURN(owner.getObjectTemplate().getNameId()));
		}
	}
	
	public static final void emoteStartFollowing(Npc owner) {
		owner.unsetState(CreatureState.WALKING);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.NEUTRALMODE, 0, 0));
	}
	
	public static final void emoteStartWalking(Npc owner) {
		owner.setState(CreatureState.WALKING);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.WALK));
	}
	
	public static final void emoteStopWalking(Npc owner) {
		owner.unsetState(CreatureState.WALKING);
	}
	
	public static final void emoteStartReturning(Npc owner) {
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.NEUTRALMODE, 0, 0));
	}
	
	public static final void emoteStartIdling(Npc owner) {
		owner.setState(CreatureState.WALKING);
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.START_EMOTE2, 0, 0));
		PacketSendUtility.broadcastPacket(owner, new SM_EMOTION(owner, EmotionType.NEUTRALMODE, 0, 0));
	}
}