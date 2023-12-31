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

import java.util.ArrayList;
import java.util.Map;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_SKILL_COOLDOWN extends AionServerPacket {
	private Map<Integer, Long> cooldowns;

	public SM_SKILL_COOLDOWN(Map<Integer, Long> cooldowns) {
		this.cooldowns = cooldowns;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		writeH(calculateSize());
		writeC(1);
		long currentTime = System.currentTimeMillis();
		for (Map.Entry<Integer, Long> entry : cooldowns.entrySet()) {
			int left = (int) ((entry.getValue() - currentTime) / 1000);
			ArrayList<Integer> skillsWithCooldown = DataManager.SKILL_DATA.getSkillsForDelayId(entry.getKey());
			for (int index = 0; index < skillsWithCooldown.size(); index++) {
				int skillId = skillsWithCooldown.get(index);
				writeH(skillId);
				writeD(left > 0 ? left : 0);
				writeD(DataManager.SKILL_DATA.getSkillTemplate(skillId).getCooldown());
			}
		}
	}

	private int calculateSize() {
		int size = 0;
		for (Map.Entry<Integer, Long> entry : cooldowns.entrySet()) {
			size += DataManager.SKILL_DATA.getSkillsForDelayId(entry.getKey()).size();
		}
		return size;
	}
}