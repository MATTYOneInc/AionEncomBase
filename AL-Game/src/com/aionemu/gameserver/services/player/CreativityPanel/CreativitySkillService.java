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
package com.aionemu.gameserver.services.player.CreativityPanel;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.panel_cp.PanelCp;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CREATIVITY_POINTS_APPLY;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CreativitySkillService {

	public void enchantSkill(Player player, int id, int point) {
		PanelCp pcp = DataManager.PANEL_CP_DATA.getPanelCpId(id);
		if (point == 0) {
			player.getSkillList().addSkill(player, pcp.getSkillId(), 1);
			player.getCP().removePoint(player, id);
		} else {
			if (pcp.getSkillId() <= 0) {
				player.getSkillList().addSkill(player, pcp.getLearnSkill(), point + 1);
			} else {
				player.getSkillList().addSkill(player, pcp.getSkillId(), point + 1);
			}

			player.getCP().addPoint(player, id, point);
		}
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(0, 1, id, point));
	}

	public void learnSkill(Player player, int id, int point) { // TODO
		PanelCp pcp = DataManager.PANEL_CP_DATA.getPanelCpId(id);
		if (point >= 1) {
			player.getSkillList().addSkill(player, pcp.getLearnSkill(), point + 1);
			player.getCP().addPoint(player, id, point);
		} else if (point == 0) {
			SkillLearnService.removeSkill(player, pcp.getLearnSkill());
			player.getCP().removePoint(player, id);
		}
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(1, 1, id, point));
	}

	public void loginDaevaSkill(Player player, int id, int point) {
		PanelCp pcp = DataManager.PANEL_CP_DATA.getPanelCpId(id);
		if (point >= 1) {
			player.getSkillList().addSkill(player, pcp.getSkillId(), point + 1);
			player.getCP().addPoint(player, id, point);
		} else if (point == 0) {
			player.getSkillList().addSkill(player, pcp.getSkillId(), 1);
			player.getCP().removePoint(player, id);
		}
		PacketSendUtility.sendPacket(player, new SM_CREATIVITY_POINTS_APPLY(id, point));
	}

	public static CreativitySkillService getInstance() {
		return NewSingletonHolder.INSTANCE;
	}

	private static class NewSingletonHolder {

		private static final CreativitySkillService INSTANCE = new CreativitySkillService();
	}
}