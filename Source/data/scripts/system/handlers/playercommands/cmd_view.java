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
package playercommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemRemodelService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

public class cmd_view extends PlayerCommand
{
    private static final int REMODEL_PREVIEW_DURATION = 60;
	
	public cmd_view() {
        super("view");
    }
	
    public void executeCommand(Player admin, String[] params) {
        if (params.length < 1 || params[0] == "") {
            PacketSendUtility.sendMessage(admin, "Syntax: .view <itemid>");
            return;
        }
        int itemId = 0;
        try {
            itemId = Integer.parseInt(params[0]);
        } catch (@SuppressWarnings("unused") Exception e) {
            PacketSendUtility.sendMessage(admin, "Error! Item id's are numbers like 187000090 or [item:187000090]!");
            return;
        }
        ItemRemodelService.commandViewRemodelItem(admin, itemId, REMODEL_PREVIEW_DURATION);
    }
	
    @Override
    public void execute(Player player, String... params) {
        executeCommand(player, params);
    }
}