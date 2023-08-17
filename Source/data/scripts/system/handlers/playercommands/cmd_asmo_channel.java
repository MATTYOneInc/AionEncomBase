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
package playercommands;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Maestros
 */
public class cmd_asmo_channel extends PlayerCommand {

	public cmd_asmo_channel() {
		super("asmo");
	}

	@Override
	public void execute(Player player, String... params) {
		if (player.getRace() == Race.ASMODIANS && !player.isInPrison()) {
			int i = 1;
			boolean check = true;
			String adminTag = "";

			if (params.length < 1) {
				PacketSendUtility.sendMessage(player, "syntax : .asmo <message>");
				return;
			}

			StringBuilder sb = new StringBuilder(adminTag);
			if (AdminConfig.ADMIN_TAG_ENABLE) {
				if (player.getAccessLevel() == 1) {
					adminTag = AdminConfig.ADMIN_TAG_1.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 2) {
					adminTag = AdminConfig.ADMIN_TAG_2.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 3) {
					adminTag = AdminConfig.ADMIN_TAG_3.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 4) {
					adminTag = AdminConfig.ADMIN_TAG_4.replace("%s", sb.toString());
				}
				else if (player.getAccessLevel() == 5) {
					adminTag = AdminConfig.ADMIN_TAG_5.replace("%s", sb.toString());
				}
			}

			adminTag += player.getName() + " : ";

			StringBuilder sbMessage;
			if (player.isGM()) {
				sbMessage = new StringBuilder("[Asmodians]" + " " + adminTag);
			}
			else {
				sbMessage = new StringBuilder("[Asmodians]" + " " + player.getName() + " : ");
			}
			Race adminRace = Race.ASMODIANS;

			for (String s : params) {
				if (i++ != 0 && (check)) {
					sbMessage.append(s).append(" ");
				}
			}

			String message = sbMessage.toString().trim();
			int messageLenght = message.length();

			final String sMessage = message.substring(0, CustomConfig.MAX_CHAT_TEXT_LENGHT > messageLenght ? messageLenght : CustomConfig.MAX_CHAT_TEXT_LENGHT);
			final boolean toAll = params[0].equals("ALL");
			final Race race = adminRace;

			World.getInstance().doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					if (toAll || player.getRace() == race || (player.getAccessLevel() > 0)) {
						PacketSendUtility.sendMessage(player, sMessage);
					}
				}
			});
		}
		else {
			PacketSendUtility.sendMessage(player, "You are Elyos! You can't use this chat. Please use .ely <message> to use you're faction chat!");

		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax : .asmo <message>");
	}
}