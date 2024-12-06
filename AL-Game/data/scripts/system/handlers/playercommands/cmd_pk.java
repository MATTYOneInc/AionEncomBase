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
import com.aionemu.gameserver.services.events.BanditService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created by wanke on 13/02/2017.
 */

public class cmd_pk extends PlayerCommand
{
    public cmd_pk() {
        super("pk");
    }
	
    @Override
    public void execute(Player player, String... params) {
        if (!player.isBandit()) {
            BanditService.getInstance().startBandit(player);
            PacketSendUtility.sendSys3Message(player, "\uE005", "<[PK] Bandit> started !!!");
        } else {
            BanditService.getInstance().stopBandit(player);
            PacketSendUtility.sendSys3Message(player, "\uE005", "<[PK] Bandit> stop !!!");
        }
    }
}