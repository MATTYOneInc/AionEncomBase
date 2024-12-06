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
import com.aionemu.gameserver.services.AgentService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.apache.commons.lang.math.NumberUtils;

public class Agent extends AdminCommand
{
	private static final String COMMAND_START = "start";
	private static final String COMMAND_STOP = "stop";
	
	public Agent() {
		super("agent");
	}
	
	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		} if (COMMAND_STOP.equalsIgnoreCase(params[0]) || COMMAND_START.equalsIgnoreCase(params[0])) {
			handleStartStopFight(player, params);
		}
	}
	
	protected void handleStartStopFight(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}
		int agentId = NumberUtils.toInt(params[1]);
		if (!isValidAgentLocationId(player, agentId)) {
			showHelp(player);
			return;
		} if (COMMAND_START.equalsIgnoreCase(params[0])) {
			if (AgentService.getInstance().isFightInProgress(agentId)) {
				PacketSendUtility.sendMessage(player, "<Agent Fight> " + agentId + " is already start");
			} else {
				PacketSendUtility.sendMessage(player, "<Agent Fight> " + agentId + " started!");
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_GodElite);
					}
				});
				AgentService.getInstance().startAgentFight(agentId);
			}
		} else if (COMMAND_STOP.equalsIgnoreCase(params[0])) {
			if (!AgentService.getInstance().isFightInProgress(agentId)) {
				PacketSendUtility.sendMessage(player, "<Agent Fight> " + agentId + " is not start!");
			} else {
				PacketSendUtility.sendMessage(player, "<Agent Fight> " + agentId + " stopped!");
				AgentService.getInstance().stopAgentFight(agentId);
			}
		}
	}
	
	protected boolean isValidAgentLocationId(Player player, int agentId) {
		if (!AgentService.getInstance().getAgentLocations().keySet().contains(agentId)) {
			PacketSendUtility.sendMessage(player, "Id " + agentId + " is invalid");
			return false;
		}
		return true;
	}
	
	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //agent start|stop <Id>");
	}
}