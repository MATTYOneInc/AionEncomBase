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

import com.aionemu.gameserver.configs.administration.CommandsConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TITLE_INFO;
import com.aionemu.gameserver.network.loginserver.LoginServer;
import com.aionemu.gameserver.network.loginserver.serverpackets.SM_ACCOUNT_TOLL_INFO;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.LunaShopService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

import java.util.Arrays;

public class Set extends AdminCommand
{
	public Set() {
		super("set");
	}
	
	@Override
	public void execute(Player admin, String... params) {
		Player target = null;
		VisibleObject creature = admin.getTarget();
		if (admin.getTarget() instanceof Player) {
			target = (Player) creature;
		} if (target == null) {
			PacketSendUtility.sendMessage(admin, "You should select a target first!");
			return;
		} if (params.length < 2) {
			PacketSendUtility.sendMessage(admin, "You should enter second params!");
			return;
		}
		String paramValue = params[1];
		if (params[0].equals("class")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			byte newClass;
			try {
				newClass = Byte.parseByte(paramValue);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			PlayerClass oldClass = target.getPlayerClass();
			setClass(target, oldClass, newClass);
		} else if (params[0].equals("exp")) { //120086901
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			long exp;
			try {
				exp = Long.parseLong(paramValue);
			}
			catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			target.getCommonData().setExp(exp, false);
			PacketSendUtility.sendMessage(admin, "Set exp of target to " + paramValue);
		}
		//<Abyss Points>
		else if (params[0].equals("ap")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			int ap;
			try {
				ap = Integer.parseInt(paramValue);
			} catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			AbyssPointsService.addAp(target, ap);
			if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Add your <Abyss Points> + " + ap + ".");
			} else {
				PacketSendUtility.sendMessage(admin, "Add " + target.getName() + " <Abyss Points> + " + ap + ".");
				PacketSendUtility.sendMessage(target, "Admin add Abyss Points + " + ap + ".");
			}
		}
		//<Glory Points>
		else if (params[0].equals("gp")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			int gp;
			try {
				gp = Integer.parseInt(paramValue);
			} catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			AbyssPointsService.addGp(target, gp);
			if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Add your <Glory Points> + " + gp + ".");
			} else {
				PacketSendUtility.sendMessage(admin, "Add " + target.getName() + " <Glory Points> + " + gp + ".");
				PacketSendUtility.sendMessage(target, "Admin add <Glory Points> +  " + gp + ".");
			}
		} else if (params[0].equals("level")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			int level;
			try {
				level = Integer.parseInt(paramValue);
			} catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			Player player = target;
			if (level <= GSConfig.PLAYER_MAX_LEVEL) {
				player.getCommonData().setLevel(level);
			}
			PacketSendUtility.sendMessage(admin, "Set " + player.getCommonData().getName() + " level to " + level);
		} else if (params[0].equals("title")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			int titleId;
			try {
				titleId = Integer.parseInt(paramValue);
			} catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			Player player = target;
			if (titleId <= 378) { //Aion 5.8 Title
				setTitle(player, titleId);
			}
			PacketSendUtility.sendMessage(admin, "Set " + player.getCommonData().getName() + " title to " + titleId);
		} else if (params[0].equals("toll")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			int toll;
			try {
				toll = Integer.parseInt(paramValue);
			} catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			} if (LoginServer.getInstance().sendPacket(new SM_ACCOUNT_TOLL_INFO(toll, target.getClientConnection().getAccount().getLuna(), target.getAcountName()))) {
				target.getClientConnection().getAccount().setToll(toll);
				PacketSendUtility.sendMessage(admin, "Tolls setted to " + toll + ".");
			} else {
				PacketSendUtility.sendMessage(admin, "ls communication error.");
			} if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Set your <Toll> to " + toll + ".");
			} else {
				PacketSendUtility.sendMessage(admin, "Set " + target.getName() + " <Toll> to " + toll + ".");
				PacketSendUtility.sendMessage(target, "Admin set your <Toll> to " + toll + ".");
			}
		} else if (params[0].equals("luna")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			int luna;
			try {
				luna = Integer.parseInt(paramValue);
			} catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			LunaShopService.getInstance().lunaPointController(target, (int) (target.getLunaAccount() + luna));
			if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Add your <Luna Points> + " + luna + ".");
			} else {
				PacketSendUtility.sendMessage(admin, "Add " + target.getName() + " <Luna Points> + " + luna + ".");
				PacketSendUtility.sendMessage(target, "Admin Add <Luna Points> + " + luna + ".");
			}
		} else if (params[0].equals("key")) {
			if (admin.getAccessLevel() < CommandsConfig.SET) {
				PacketSendUtility.sendMessage(admin, "You dont have enough rights to execute this command");
				return;
			}
			int key;
			try {
				key = Integer.parseInt(paramValue);
			} catch (NumberFormatException e) {
				PacketSendUtility.sendMessage(admin, "You should enter valid second params!");
				return;
			}
			LunaShopService.getInstance().muniKeysController(target, target.getMuniKeys() + key);
			if (target == admin) {
				PacketSendUtility.sendMessage(admin, "Add your <Munirunerk's Keys> + " + key + ".");
			} else {
				PacketSendUtility.sendMessage(admin, "Add " + target.getName() + " <Munirunerk's Keys> + " + key + ".");
				PacketSendUtility.sendMessage(target, "Admin Add <Munirunerk's Keys> + " + key + ".");
			}
		}
	}
	
	private void setTitle(Player player, int value) {
		PacketSendUtility.sendPacket(player, new SM_TITLE_INFO(value));
		PacketSendUtility.broadcastPacket(player, (new SM_TITLE_INFO(player, value)));
		player.getCommonData().setTitleId(value);
	}
	
	private void setClass(Player player, PlayerClass oldClass, byte value) {
		PlayerClass playerClass = PlayerClass.getPlayerClassById(value);
		int level = player.getLevel();
		if (level < 9) {
			PacketSendUtility.sendMessage(player, "You can only switch class after reach level 9");
			return;
		} if (Arrays.asList(1, 2, 4, 5, 7, 8, 10, 11, 13, 14, 16).contains(oldClass.ordinal())) {
			PacketSendUtility.sendMessage(player, "You already switched class");
			return;
		}
		int newClassId = playerClass.ordinal();
		switch (oldClass.ordinal()) {
			case 0: //Warrior.
				if (newClassId == 1 || newClassId == 2)
			break;
			case 3: //Scout.
				if (newClassId == 4 || newClassId == 5)
			break;
			case 6: //Mage.
				if (newClassId == 7 || newClassId == 8)
			break;
			case 9: //Priest.
				if (newClassId == 10 || newClassId == 11)
			break;
			case 12: //Technist.
				if (newClassId == 13 || newClassId == 14)
			break;
			case 15: //Muse.
				if (newClassId == 16)
			break;
			default:
				PacketSendUtility.sendMessage(player, "Invalid class switch chosen");
			return;
		}
		player.getCommonData().setPlayerClass(playerClass);
		player.getController().upgradePlayer();
		PacketSendUtility.sendMessage(player, "You have successfuly switched class");
	}
	
	@Override
	public void onFail(Player player, String message) {
		PacketSendUtility.sendMessage(player, "syntax //set <class|exp|ap|gp|level|title|luna>");
	}
}