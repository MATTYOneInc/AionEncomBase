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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.base.BaseLocation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.services.base.Base;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import org.apache.commons.lang.math.NumberUtils;

/**
 * @author Rinzler
 */

@SuppressWarnings("rawtypes")
public class BaseCommand extends AdminCommand
{
	private static final String COMMAND_LIST = "list";
	private static final String COMMAND_CAPTURE = "capture";
	
	public BaseCommand() {
		super("base");
	}
	
	@Override
	public void execute(Player player, String... params) {
		if (params.length == 0) {
			showHelp(player);
			return;
		} if (COMMAND_LIST.equalsIgnoreCase(params[0])) {
			handleList(player, params);
		} else if (COMMAND_CAPTURE.equals(params[0])) {
			capture(player, params);
		}
	}
	
	protected boolean isValidBaseLocationId(Player player, int baseId) {
		if (!BaseService.getInstance().getBaseLocations().keySet().contains(baseId)) {
			PacketSendUtility.sendMessage(player, "Id " + baseId + " is invalid");
			return false;
		}
		return true;
	}
	
	protected void handleList(Player player, String[] params) {
		if (params.length != 1) {
			showHelp(player);
			return;
		} for (BaseLocation base : BaseService.getInstance().getBaseLocations().values()) {
			PacketSendUtility.sendMessage(player, "Base:" + base.getId() + " belongs to " + base.getRace());
		}
	}
	
	protected void capture(Player player, String[] params) {
		if (params.length < 3 || !NumberUtils.isNumber(params[1])) {
			showHelp(player);
			return;
		}
		int baseId = NumberUtils.toInt(params[1]);
		if (!isValidBaseLocationId(player, baseId)) {
			return;
		}
		Race race = null;
		try {
			race = Race.valueOf(params[2].toUpperCase());
		} catch (IllegalArgumentException e) {
		} if (race == null) {
			PacketSendUtility.sendMessage(player, params[2] + " is not valid race");
			showHelp(player);
			return;
		}
		Base base = BaseService.getInstance().getActiveBase(baseId);
		if (base != null) {
			BaseService.getInstance().capture(baseId, race);
		}
	}
	
	protected void showHelp(Player player) {
		PacketSendUtility.sendMessage(player, "AdminCommand //base Help\n" + "//base list\n" + "//base capture <Id> <Race (ELYOS, ASMODIANS, NPC)>");
	}
}