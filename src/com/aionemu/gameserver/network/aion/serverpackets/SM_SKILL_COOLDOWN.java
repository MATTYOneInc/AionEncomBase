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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.PacketLoggerService;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;

public class SM_SKILL_COOLDOWN extends AionServerPacket {
	private Map<Integer, Long> cooldowns;

	public SM_SKILL_COOLDOWN(Map<Integer, Long> cooldowns) {
		this.cooldowns = cooldowns;
	}

	@Override
	protected void writeImpl(AionConnection con) {
		PacketLoggerService.getInstance().logPacketSM(this.getPacketName());
		long currentTime = System.currentTimeMillis();

		writeH(calculateSize(con.getActivePlayer()));
		writeC(1);

		for (Map.Entry<Integer, Long> entry : this.cooldowns.entrySet()) {
			int left = (int)(entry.getValue() - currentTime);
			ArrayList<Integer> skillsWithCooldown = DataManager.SKILL_DATA.getSkillsForCooldownId(entry.getKey(), con.getActivePlayer());
			for (Integer index : skillsWithCooldown) {
				int skillId = index;
				SkillTemplate skillTemplate = DataManager.SKILL_DATA.getSkillTemplate(skillId);
				int cooldown = skillTemplate.getCooldown();

				writeH(skillId);
				writeD(Math.max(left, 0));
				writeD(cooldown * 100);
			}
		}
	}

	private int calculateSize(Player player) {
		int size = 0;
		for (Map.Entry<Integer, Long> entry : this.cooldowns.entrySet())
			size += DataManager.SKILL_DATA.getSkillsForCooldownId(entry.getKey(), player).size();
		return size;
	}
}