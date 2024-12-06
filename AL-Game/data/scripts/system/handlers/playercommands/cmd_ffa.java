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
package com.aionemu.gameserver.services.events;

import com.aionemu.gameserver.configs.main.*;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.events.*;
import com.aionemu.gameserver.utils.*;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created by (Encom)
 */

public class cmd_ffa extends PlayerCommand
{
    public cmd_ffa() {
        super("ffa");
    }

    @Override
    public void execute(Player player, String... params) {
        if (!FFAConfig.FFA_ENABLED) {
            PacketSendUtility.sendSys3Message(player, "\uE00B", "<FFA> is disabled!!!");
            return;
        } if (player.getLevel() < 10) {
            PacketSendUtility.sendSys3Message(player, "\uE00B", "<FFA> You must reached lvl 10!");
            return;
        } if (player.isInInstance() && !FFAService.getInstance().isInArena(player) && !player.isFFA()) {
            PacketSendUtility.sendSys3Message(player, "\uE00B", "<FFA> You can't use <FFA> mod in instance!!!");
            return;
        } if (player.getBattleground() != null || LadderService.getInstance().isInQueue(player) || player.isSpectating()||player.getLifeStats().isAlreadyDead()) {
            PacketSendUtility.sendSys3Message(player, "\uE00B", "<FFA> You cannot enter <FFA> while in a battleground, in the queue, while spectating or being dead !!!");
            return;
        } if (FFAService.getInstance().isInArena(player)) {
            PacketSendUtility.sendSys3Message(player, "\uE00B", "<FFA> You will be leaving <FFA> in 10 seconds!");
            FFAService.getInstance().leaveArena(player);
        } else {
            if (player.getController().isInCombat()) {
                PacketSendUtility.sendSys3Message(player, "\uE00B", "<FFA> You cannot enter <FFA> while in combat.");
                return;
            }
            PacketSendUtility.sendSys3Message(player, "\uE00B", "<FFA> You will be entering <FFA> in 10 seconds. To leave <FFA> write .ffa!!!");
            FFAService.getInstance().enterArena(player, false);
        }
    }
}