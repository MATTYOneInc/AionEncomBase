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
import com.aionemu.gameserver.services.NightmareCircusService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.apache.commons.lang.math.NumberUtils;

public class NightmareCircus extends AdminCommand
{
	private static final String COMMAND_START = "start";
	private static final String COMMAND_STOP = "stop";
	
	public NightmareCircus() {
		super("circus");
	}
	
	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		} if (COMMAND_STOP.equalsIgnoreCase(params[0]) || COMMAND_START.equalsIgnoreCase(params[0])) {
			handleStartStopInstance(player, params);
		}
	}
	
	protected void handleStartStopInstance(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}
		int nightmareId = NumberUtils.toInt(params[1]);
		if (!isValidNightmareCircusLocationId(player, nightmareId)) {
			showHelp(player);
			return;
		} if (COMMAND_START.equalsIgnoreCase(params[0])) {
			if (NightmareCircusService.getInstance().isNightmareCircusInProgress(nightmareId)) {
				PacketSendUtility.sendMessage(player, "<Nightmare Circus> " + nightmareId + " is already start");
			} else {
				PacketSendUtility.sendMessage(player, "<Nightmare Circus> " + nightmareId + " started!");
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys3Message(player, "\uE04C", "<Nightmare Circus 4.3> is now open !!!");
					}
				});
				NightmareCircusService.getInstance().startNightmareCircus(nightmareId);
			}
		} else if (COMMAND_STOP.equalsIgnoreCase(params[0])) {
			if (!NightmareCircusService.getInstance().isNightmareCircusInProgress(nightmareId)) {
				PacketSendUtility.sendMessage(player, "<Nightmare Circus> " + nightmareId + " is not start!");
			} else {
				PacketSendUtility.sendMessage(player, "<Nightmare Circus> " + nightmareId + " stopped!");
				NightmareCircusService.getInstance().stopNightmareCircus(nightmareId);
			}
		}
	}
	
	protected boolean isValidNightmareCircusLocationId(Player player, int nightmareId) {
		if (!NightmareCircusService.getInstance().getNightmareCircusLocations().keySet().contains(nightmareId)) {
			PacketSendUtility.sendMessage(player, "Id " + nightmareId + " is invalid");
			return false;
		}
		return true;
	}
	
	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //circus start|stop <Id>");
	}
}