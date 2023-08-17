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
package ai.instance.theHexway;

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("shiningmagicward")
public class ShiningMagicWardAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
		    case 700455: //Shining Magic Ward.
				switch (player.getWorldId()) {
                    case 300080000: //Left Wing Chamber.
					    if (player.getCommonData().getRace() == Race.ASMODIANS) {
						   PacketSendUtility.sendMessage(player, "you enter <Primum Landing>");
						   TeleportService2.teleportTo(player, 400010000, 1071.7615f, 2851.7764f, 1636.0677f, (byte) 38);
			            } else if (player.getCommonData().getRace() == Race.ELYOS) {
						   PacketSendUtility.sendMessage(player, "you enter <Terminon Landing>");
						   TeleportService2.teleportTo(player, 400010000, 2872.6626f, 1029.0958f, 1527.9968f, (byte) 103);
					    }
			        break;
				} switch (player.getWorldId()) {
                    case 300700000: //The Hexway 4.3.
					    PacketSendUtility.sendMessage(player, "you enter in <Silentera Canyon [Master Server]>");
						TeleportService2.teleportTo(player, 600110000, 528.7647f, 766.7518f, 299.61633f, (byte) 1);
			        break;
				}
		    break;
		}
	}
}