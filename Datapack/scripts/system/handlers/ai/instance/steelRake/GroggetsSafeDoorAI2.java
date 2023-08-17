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
package ai.instance.steelRake;

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("groggetssafedoor")
public class GroggetsSafeDoorAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
		    case 730199: //Groggets Safe Door.
				switch (player.getWorldId()) {
                    case 300100000: //Steel Rake.
						if (player.getInventory().decreaseByItemId(185000046, 1)) { //Grogget's Safe Key.
						    PacketSendUtility.sendMessage(player, "you enter <Inside Steel Rake>");
							TeleportService2.teleportTo(player, 300100000, 702.11993f, 500.80948f, 939.60675f, (byte) 0);
						} else {
							PacketSendUtility.sendMessage(player, "you must have <Grogget's Safe Key> for use this teleporter");
						}
			        break;
				} switch (player.getWorldId()) {
                    case 300460000: //Steel Rake Cabin 3.0
					    PacketSendUtility.sendMessage(player, "you enter <Inside Steel Rake Cabin>");
						TeleportService2.teleportTo(player, 300460000, 702.11993f, 500.80948f, 939.60675f, (byte) 0);
			        break;
				}
		    break;
		}
	}
}