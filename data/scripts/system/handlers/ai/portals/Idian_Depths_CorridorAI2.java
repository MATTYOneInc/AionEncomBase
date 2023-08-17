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
package ai.portals;

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("idian_depths_corridor")
public class Idian_Depths_CorridorAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
		    case 731631: //Cygnea To Idian Depths.
			case 731641: //Levinshor To Idian Depths.
			    if (player.getLevel() >= 65) {
				    TeleportService2.teleportTo(player, 210090000, 691.99f, 811.69055f, 514.86566f, (byte) 29, TeleportAnimation.BEAM_ANIMATION);
                } else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Telepoter_Under_User);
				}
			break;
			case 731632: //Enshar To Idian Depths.
			case 731642: //Kaldor To Idian Depths.
				if (player.getLevel() >= 65) {
				    TeleportService2.teleportTo(player, 220100000, 691.99f, 811.69055f, 514.86566f, (byte) 29, TeleportAnimation.BEAM_ANIMATION);
                } else {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Telepoter_Under_User);
				}
		    break;
        }
	}
}