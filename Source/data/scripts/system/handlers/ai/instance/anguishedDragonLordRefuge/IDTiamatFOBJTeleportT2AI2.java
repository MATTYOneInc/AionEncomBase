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
package ai.instance.anguishedDragonLordRefuge;

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("blood_red_jewel")
public class IDTiamatFOBJTeleportT2AI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
		    case 730625: //Blood Red Jewel.
				switch (player.getWorldId()) {
					case 300520000: //Dragon Lord's Refuge 3.9
						PacketSendUtility.sendMessage(player, "you enter <Dragon Lord's Refuge 3.9>");
						TeleportService2.teleportTo(player, 300520000, 512.75183f, 515.7632f, 417.40436f, (byte) 0);
			        break;
					case 300630000: //[Anguished] Dragon Lord's Refuge 4.8
					    PacketSendUtility.sendMessage(player, "you enter <[Anguished] Dragon Lord's Refuge 4.8>");
						TeleportService2.teleportTo(player, 300630000, 512.75183f, 515.7632f, 417.40436f, (byte) 0);
			        break;
				}
		    break;
		}
	}
}