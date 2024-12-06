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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.IdianDepthsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.apache.commons.lang.math.NumberUtils;

public class IdianDepths extends AdminCommand
{
	private static final String COMMAND_START = "start";
	private static final String COMMAND_STOP = "stop";
	
	public IdianDepths() {
		super("idian");
	}
	
	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		} if (COMMAND_STOP.equalsIgnoreCase(params[0]) || COMMAND_START.equalsIgnoreCase(params[0])) {
			handleStartStopIdian(player, params);
		}
	}
	
	protected void handleStartStopIdian(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}
		int idianDepthsId = NumberUtils.toInt(params[1]);
		if (!isValidIdianDepthsLocationId(player, idianDepthsId)) {
			showHelp(player);
			return;
		} if (COMMAND_START.equalsIgnoreCase(params[0])) {
			if (IdianDepthsService.getInstance().isIdianDepthsInProgress(idianDepthsId)) {
				PacketSendUtility.sendMessage(player, "<Idian Depths> " + idianDepthsId + " is already start");
			} else {
				PacketSendUtility.sendMessage(player, "<Idian Depths> " + idianDepthsId + " started!");
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys3Message(player, "\uE0AA", "<Idian Depths> is now open !!!");
					}
				});
				IdianDepthsService.getInstance().startIdianDepths(idianDepthsId);
			}
		} else if (COMMAND_STOP.equalsIgnoreCase(params[0])) {
			if (!IdianDepthsService.getInstance().isIdianDepthsInProgress(idianDepthsId)) {
				PacketSendUtility.sendMessage(player, "<Idian Depths> " + idianDepthsId + " is not start!");
			} else {
				PacketSendUtility.sendMessage(player, "<Idian Depths> " + idianDepthsId + " stopped!");
				IdianDepthsService.getInstance().stopIdianDepths(idianDepthsId);
			}
		}
	}
	
	protected boolean isValidIdianDepthsLocationId(Player player, int idianDepthsId) {
		if (!IdianDepthsService.getInstance().getIdianDepthsLocations().keySet().contains(idianDepthsId)) {
			PacketSendUtility.sendMessage(player, "Id " + idianDepthsId + " is invalid");
			return false;
		}
		return true;
	}
	
	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //idian start|stop <Id>");
	}
}