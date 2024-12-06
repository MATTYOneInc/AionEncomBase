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
import com.aionemu.gameserver.services.MoltenusService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;
import org.apache.commons.lang.math.NumberUtils;

public class Moltenus extends AdminCommand
{
	private static final String COMMAND_START = "start";
	private static final String COMMAND_STOP = "stop";
	
	public Moltenus() {
		super("moltenus");
	}
	
	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		} if (COMMAND_STOP.equalsIgnoreCase(params[0]) || COMMAND_START.equalsIgnoreCase(params[0])) {
			handleStartStopMoltenus(player, params);
		}
	}
	
	protected void handleStartStopMoltenus(Player player, String... params) {
		if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
			showHelp(player);
			return;
		}
		int moltenusId = NumberUtils.toInt(params[1]);
		if (!isValidMoltenusLocationId(player, moltenusId)) {
			showHelp(player);
			return;
		} if (COMMAND_START.equalsIgnoreCase(params[0])) {
			if (MoltenusService.getInstance().isMoltenusInProgress(moltenusId)) {
				PacketSendUtility.sendMessage(player, "<Resurrected Moltenus> " + moltenusId + " is already start");
			} else {
				PacketSendUtility.sendMessage(player, "<Resurrected Moltenus> " + moltenusId + " started!");
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					@Override
					public void visit(Player player) {
						PacketSendUtility.sendSys3Message(player, "\uE005", "<Resurrected Moltenus> appear in the abyss !!!");
					}
				});
				MoltenusService.getInstance().startMoltenus(moltenusId);
			}
		} else if (COMMAND_STOP.equalsIgnoreCase(params[0])) {
			if (!MoltenusService.getInstance().isMoltenusInProgress(moltenusId)) {
				PacketSendUtility.sendMessage(player, "<Resurrected Moltenus> " + moltenusId + " is not start!");
			} else {
				PacketSendUtility.sendMessage(player, "<Resurrected Moltenus> " + moltenusId + " stopped!");
				MoltenusService.getInstance().stopMoltenus(moltenusId);
			}
		}
	}
	
	protected boolean isValidMoltenusLocationId(Player player, int moltenusId) {
		if (!MoltenusService.getInstance().getMoltenusLocations().keySet().contains(moltenusId)) {
			PacketSendUtility.sendMessage(player, "Id " + moltenusId + " is invalid");
			return false;
		}
		return true;
	}
	
	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //moltenus start|stop <Id>");
	}
}