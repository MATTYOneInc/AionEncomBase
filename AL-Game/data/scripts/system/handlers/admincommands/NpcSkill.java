/*
 * This file is part of Encom.
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
package admincommands;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npcskill.NpcSkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

import java.util.List;

/**
 * @author Wakizashi
 */
public class NpcSkill extends AdminCommand {

	public NpcSkill() {
		super("npcskill");
	}

	@Override
	public void execute(Player admin, String... params) {
		Npc target = null;
		VisibleObject creature = admin.getTarget();
		if (admin.getTarget() instanceof Npc) {
			target = (Npc) creature;
		}

		if (target == null) {
			PacketSendUtility.sendMessage(admin, "You should select a valid target first!");
			return;
		}

		StringBuilder strbld = new StringBuilder("-list of skills:\n");

		List<NpcSkillTemplate> list = DataManager.NPC_SKILL_DATA.getNpcSkillList(target.getNpcId()).getNpcSkills();

		for (NpcSkillTemplate skill : list)
			strbld.append("    level " + skill.getSkillLevel() + " of " + skill.getSkillid() + ".\n");
		showAllLines(admin, strbld.toString());
	}

	private void showAllLines(Player admin, String str) {
		int index = 0;
		String[] strarray = str.split("\n");

		while (index < strarray.length - 20) {
			StringBuilder strbld = new StringBuilder();
			for (int i = 0; i < 20; i++, index++) {
				strbld.append(strarray[index]);
				if (i < 20 - 1)
					strbld.append("\n");
			}
			PacketSendUtility.sendMessage(admin, strbld.toString());
		}
		int odd = strarray.length - index;
		StringBuilder strbld = new StringBuilder();
		for (int i = 0; i < odd; i++, index++)
			strbld.append(strarray[index] + "\n");
		PacketSendUtility.sendMessage(admin, strbld.toString());
	}

	@Override
	public void onFail(Player player, String message) {
		// TODO Auto-generated method stub
	}
}