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

import java.util.Map;

import javolution.util.FastMap;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.events.*;
import com.aionemu.gameserver.utils.*;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

public class cmd_solo extends PlayerCommand
{
    private static Map<Integer, Long> nextUse = new FastMap<Integer, Long>();
    private static final int REGISTRATION_DELAY = 8 * 60 * 1000;
    
	public cmd_solo() {
        super("vs");
    }
	
    @Override
    public void execute(Player player, String... params) {
        if (!LadderService.getInstance().isInQueue(player)) {
            if (LadderService.getInstance().registerForSolo(player)) {
                PacketSendUtility.sendSys3Message(player, "\uE00E", "You are now registered in queue <1Vs1>");
            } else {
                PacketSendUtility.sendSys3Message(player, "\uE00E", "Failed to save in queue <1Vs1>");
            }
        } else {
            LadderService.getInstance().unregisterFromQueue(player);
            PacketSendUtility.sendSys3Message(player, "\uE00E", "You are now unsubscribed from queue <1Vs1>");
        }
    }
}