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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_CASTSPELL extends AionClientPacket {
	private int spellid;
	private int targetType;
	private float x, y, z;
	@SuppressWarnings("unused")
	private int targetObjectId;
	private int hitTime;
	private int level;
	private int unk;
	Logger log = LoggerFactory.getLogger(CM_CASTSPELL.class);

	public CM_CASTSPELL(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	@Override
	protected void readImpl() {
		spellid = readH();
		level = readC();
		targetType = readC();
		switch (targetType) {
		case 0:
		case 3:
		case 4:
		case 87:
			targetObjectId = readD();
			break;
		case 1:
			x = readF();
			y = readF();
			z = readF();
			break;
		case 2:
			x = readF();
			y = readF();
			z = readF();
			readF();
			readF();
			readF();
			readF();
			readF();
			readF();
			readF();
			readF();
			break;
		default:
			break;
		}
		hitTime = readH();
		unk = readD();
	}

	@Override
	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(spellid);

		/*
		 * KorLightNing if iscasting 'ESC' Key Send cancel casting
		 */
		if (spellid == 0 && player.isCasting()) {
			player.getController().cancelCurrentSkill();
		}

		if (template == null || template.isPassive()) {
			return;
		}
		if (player.isProtectionActive()) {
			player.getController().stopProtectionActiveTask();
		}
		long currentTime = System.currentTimeMillis();
		if (player.getNextSkillUse() > currentTime) {
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300021));
			return;
		}
		if (!player.getLifeStats().isAlreadyDead()) {
			player.getController().useSkill(template, targetType, x, y, z, hitTime, level);
		}
	}
}