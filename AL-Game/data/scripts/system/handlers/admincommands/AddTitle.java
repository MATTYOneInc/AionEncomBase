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

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

public class AddTitle extends AdminCommand
{
	public AddTitle() {
		super("addtitle");
	}
	
	@Override
	public void execute(Player player, String... params) {
		if ((params.length < 1) || (params.length > 2)) {
			onFail(player, null);
			return;
		}
		int titleId = Integer.parseInt(params[0]);
		if ((titleId > 377) || (titleId < 1)) {
			PacketSendUtility.sendMessage(player, "title id " + titleId + " is invalid (must be between 1 and 327)");
			return;
		}
		Player target = null;
		if (params.length == 2) {
			target = World.getInstance().findPlayer(Util.convertName(params[1]));
			if (target == null) {
				PacketSendUtility.sendMessage(player, "player " + params[1] + " was not found");
				return;
			}
		} else {
			VisibleObject creature = player.getTarget();
			if (player.getTarget() instanceof Player) {
				target = (Player) creature;
			} if (target == null) {
				target = player;
			}
		} if (titleId < 378) {
			titleId = target.getRace().getRaceId() * 377 + titleId;
		} if (!target.getTitleList().addTitle(titleId, false, 0)) {
			PacketSendUtility.sendMessage(player, "you can't add title #" + titleId + " to " + (target.equals(player) ? "yourself" : target.getName()));
		} else {
			if (target.equals(player)) {
				PacketSendUtility.sendMessage(player, "you added to yourself title #" + titleId);
			} else {
				PacketSendUtility.sendMessage(player, "you added to " + target.getName() + " title #" + titleId);
				PacketSendUtility.sendMessage(target, player.getName() + " gave you title #" + titleId);
			}
		}
	}
	
	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //addtitle title_id [playerName]");
	}
}