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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerABDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.atreian_bestiary.AtreianBestiaryTemplate;
import com.aionemu.gameserver.services.player.AtreianBestiaryService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Ranastic
 */
public class AtreianBestiary extends AdminCommand {

	public AtreianBestiary() {
		super("bestiary");
	}

	@Override
	public void execute(Player admin, String... params) {
		if (params.length < 2) {
			PacketSendUtility.sendMessage(admin, "syntax //bestiary <npc_id>");
			return;
		}
		int templateId = Integer.parseInt(params[0]);
		AtreianBestiaryTemplate template = DataManager.ATREIAN_BESTIARY.getAtreianBestiaryTemplateByNpcId(templateId);
		if (template != null) {
			AtreianBestiaryService.getInstance().onKill(admin, templateId);
			PacketSendUtility.sendMessage(admin, "Added kill count to Atreian Bestiary for npc "+ templateId);
		}
		else {
			PacketSendUtility.sendMessage(admin, "Npc "+ templateId + " isn't exist in monster_books.xml");
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //bestiary <npc_id>");
	}
}