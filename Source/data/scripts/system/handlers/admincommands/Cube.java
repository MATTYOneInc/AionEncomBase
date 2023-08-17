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
import com.aionemu.gameserver.services.CubeExpandService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Kamui
 *
 */
public class Cube extends AdminCommand {

	public Cube() {
		super("cube");
	}

	@Override
	public void execute(Player player, String... params) {
		if (player.getNpcExpands() >= 9) {
			PacketSendUtility.sendMessage(player, "Aucune extension n'est disponible pour votre inventaire.");
			return;
		}
        while (player.getNpcExpands() < 9) {
            CubeExpandService.expand(player, true);
        }
		PacketSendUtility.sendMessage(player, "Vous venez de recevoir toutes les extensions de votre inventaire.");
	}
	
	@Override
	public void onFail(Player admin, String message) {
		PacketSendUtility.sendMessage(admin, "Syntaxe : .cube");
	}
}