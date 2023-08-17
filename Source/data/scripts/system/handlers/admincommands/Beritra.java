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
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.BeritraService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.apache.commons.lang.math.NumberUtils;

public class Beritra extends AdminCommand
{
	private static final String COMMAND_START = "start";
	private static final String COMMAND_STOP = "stop";
	
	public Beritra() {
		super("beritra");
	}
	
	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		} if (COMMAND_STOP.equalsIgnoreCase(params[0]) || COMMAND_START.equalsIgnoreCase(params[0])) {
			handleStartStopInvasion(player, params);
		}
	}
	
	protected void handleStartStopInvasion(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}
		int beritraId = NumberUtils.toInt(params[1]);
		if (!isValidBeritraLocationId(player, beritraId)) {
			showHelp(player);
			return;
		} if (COMMAND_START.equalsIgnoreCase(params[0])) {
			if (BeritraService.getInstance().isInvasionInProgress(beritraId)) {
				PacketSendUtility.sendMessage(player, "<Beritra Location> " + beritraId + " is already start");
			} else {
				PacketSendUtility.sendMessage(player, "<Beritra Location> " + beritraId + " started!");
				BeritraService.getInstance().startBeritraInvasion(beritraId);
			}
		} else if (COMMAND_STOP.equalsIgnoreCase(params[0])) {
			if (!BeritraService.getInstance().isInvasionInProgress(beritraId)) {
				PacketSendUtility.sendMessage(player, "<Beritra Location> " + beritraId + " is not start!");
				
			} else {
				PacketSendUtility.sendMessage(player, "<Beritra Location> " + beritraId + " stopped!");
				BeritraService.getInstance().stopBeritraInvasion(beritraId);
			}
		}
	}
	
	protected boolean isValidBeritraLocationId(Player player, int beritraId) {
		if (!BeritraService.getInstance().getBeritraLocations().keySet().contains(beritraId)) {
			PacketSendUtility.sendMessage(player, "Id " + beritraId + " is invalid");
			return false;
		}
		return true;
	}
	
	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //beritra start|stop <Id>");
	}
}