/**
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
package com.aionemu.gameserver.network.aion.gmhandler;

import com.aionemu.gameserver.configs.administration.PanelConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.world.World;

/**
 * @author Alcapwnd
 */
public class CmdAddSkill extends AbstractGMHandler {

	public CmdAddSkill(Player admin, String params) {
		super(admin, params);
		run();
	}

	private void run() {
		Player t = admin;
		if (admin.getClientConnection().getAccount().getAccessLevel() <= PanelConfig.SKILL_PANEL_LEVEL) {
			PacketSendUtility.sendMessage(admin, "You haven't access this panel commands");
			return;
		}

		if (admin.getTarget() != null && admin.getTarget() instanceof Player) {
			t = World.getInstance().findPlayer(Util.convertName(admin.getTarget().getName()));
		}
		if (params == null) {
			return;
		}
		for (SkillTemplate template : DataManager.SKILL_DATA.getSkillData().valueCollection()) {
			if (template.getNamedesc() != null && template.getNamedesc().equalsIgnoreCase(params)) {
				PacketSendUtility.sendMessage(admin, "You added Skill " + template.getName() + "to " + t.getName());
				PacketSendUtility.sendMessage(t, "Admin has add Skill " + template.getName() + "to you.");
				t.getSkillList().addSkill(t, template.getSkillId(), 1);
			}
		}
	}
}