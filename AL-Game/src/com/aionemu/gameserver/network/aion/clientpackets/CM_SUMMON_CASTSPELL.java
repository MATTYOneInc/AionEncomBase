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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

public class CM_SUMMON_CASTSPELL extends AionClientPacket {
	private static final Logger log = LoggerFactory.getLogger(CM_SUMMON_CASTSPELL.class);
	private int summonObjId;
	private int targetObjId;
	private int skillId;
	@SuppressWarnings("unused")
	private int skillLvl;
	@SuppressWarnings("unused")
	private float unk;

	public CM_SUMMON_CASTSPELL(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		summonObjId = readD();
		skillId = readH();
		skillLvl = readC();
		targetObjId = readD();
		unk = readF();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		long currentTime = System.currentTimeMillis();
		if (player.getNextSummonSkillUse() > currentTime) {
			return;
		}
		Summon summon = player.getSummon();
		if (summon == null) {
			return;
		}
		if (summon.getObjectId() != summonObjId) {
			return;
		}
		Creature target = null;
		if (targetObjId != summon.getObjectId()) {
			VisibleObject obj = summon.getKnownList().getObject(targetObjId);
			if (obj instanceof Creature) {
				target = (Creature) obj;
			}
		} else {
			target = summon;
		}
		if (target != null) {
			player.setNextSummonSkillUse(currentTime + 1100);
			summon.getController().useSkill(skillId, target);
		}
	}
}