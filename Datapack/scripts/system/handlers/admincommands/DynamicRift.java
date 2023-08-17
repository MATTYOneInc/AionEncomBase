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
import com.aionemu.gameserver.services.DynamicRiftService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import org.apache.commons.lang.math.NumberUtils;

public class DynamicRift extends AdminCommand
{
	private static final String COMMAND_START = "start";
	private static final String COMMAND_STOP = "stop";
	
	public DynamicRift() {
		super("dynamicrift");
	}
	
	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		} if (COMMAND_STOP.equalsIgnoreCase(params[0]) || COMMAND_START.equalsIgnoreCase(params[0])) {
			handleStartStopDynamic(player, params);
		}
	}
	
	protected void handleStartStopDynamic(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}
		int dynamicRiftId = NumberUtils.toInt(params[1]);
		if (!isValidDynamicRiftLocationId(player, dynamicRiftId)) {
			showHelp(player);
			return;
		} if (COMMAND_START.equalsIgnoreCase(params[0])) {
			if (DynamicRiftService.getInstance().isDynamicRiftInProgress(dynamicRiftId)) {
				PacketSendUtility.sendMessage(player, "<Dynamic Rift> " + dynamicRiftId + " is already start");
			} else {
				PacketSendUtility.sendMessage(player, "<Dynamic Rift> " + dynamicRiftId + " started!");
				DynamicRiftService.getInstance().startDynamicRift(dynamicRiftId);
			}
		} else if (COMMAND_STOP.equalsIgnoreCase(params[0])) {
			if (!DynamicRiftService.getInstance().isDynamicRiftInProgress(dynamicRiftId)) {
				PacketSendUtility.sendMessage(player, "<Dynamic Rift> " + dynamicRiftId + " is not start!");
			} else {
				PacketSendUtility.sendMessage(player, "<Dynamic Rift> " + dynamicRiftId + " stopped!");
				DynamicRiftService.getInstance().stopDynamicRift(dynamicRiftId);
			}
		}
	}
	
	protected boolean isValidDynamicRiftLocationId(Player player, int dynamicRiftId) {
		if (!DynamicRiftService.getInstance().getDynamicRiftLocations().keySet().contains(dynamicRiftId)) {
			PacketSendUtility.sendMessage(player, "Id " + dynamicRiftId + " is invalid");
			return false;
		}
		return true;
	}
	
	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //dynamicrift start|stop <Id>");
	}
}