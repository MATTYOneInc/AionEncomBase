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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

/**
 * @author FrozenKiller
 */
public class CM_SKILL_ANIMATION extends AionClientPacket {

	private int SkillId;
	private int SkillSkinId;

	public CM_SKILL_ANIMATION(int opcode, State state, State... restStates) {
		super(opcode, state, restStates);
	}

	protected void readImpl() {
		SkillId = readH();
		SkillSkinId = readH();
	}

	protected void runImpl() {
		Player player = getConnection().getActivePlayer();
		if (SkillSkinId > 0) {
			player.getSkillSkinList().setActive(SkillSkinId);
		} else {
			player.getSkillSkinList().setDeactive(SkillId);
		}
	}
}