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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Maestross
 */
public class cmd_world_channel extends PlayerCommand {

	public cmd_world_channel() {
		super("world");
	}

	@Override
	public void execute(Player player, String... params) {
		int i = 1;
		int ap = CustomConfig.WORLD_CHANNEL_AP_COSTS;
		boolean check = true;
		String adminTag = "";

		if (params.length < 1) {
			PacketSendUtility.sendMessage(player, "syntax : .world <message>");
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
			sbMessage = new StringBuilder("[World-Chat]" + " " + adminTag);
		}
		else {
			sbMessage = new StringBuilder("[World-Chat]" + " " + player.getName() + " : ");
		}

		for (String s : params) {
			if (i++ != 0 && (check)) {
				sbMessage.append(s).append(" ");
			}
		}

		String message = sbMessage.toString().trim();
		int messageLenght = message.length();

		final String sMessage = message.substring(0, CustomConfig.MAX_CHAT_TEXT_LENGHT > messageLenght ? messageLenght : CustomConfig.MAX_CHAT_TEXT_LENGHT);
		if (player.isGM()) {

			World.getInstance().doOnAllPlayers(new Visitor<Player>() {

				@Override
				public void visit(Player player) {
					PacketSendUtility.sendMessage(player, sMessage);
				}
			});
		}
		else if (!player.isGM() && !player.isInPrison()) {
			if (player.getAbyssRank().getAp() < ap) {
				PacketSendUtility.sendMessage(player, "You dont have enough ap, you only have:" + player.getAbyssRank().getAp());
			}
			else {
				AbyssPointsService.addAp(player, -ap);
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {

					@Override
					public void visit(Player player) {
						PacketSendUtility.sendMessage(player, sMessage);
					}
				});
			}
		}
		else {
			PacketSendUtility.sendMessage(player, "You dont have enough ap, you only have:" + player.getAbyssRank().getAp());
		}
	}

	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax : .world <message>");
	}
}