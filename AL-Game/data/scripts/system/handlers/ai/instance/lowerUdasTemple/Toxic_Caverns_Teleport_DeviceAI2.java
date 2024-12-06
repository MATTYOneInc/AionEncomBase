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
package ai.instance.lowerUdasTemple;

import ai.ActionItemNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("toxic_caverns_teleport_device")
public class Toxic_Caverns_Teleport_DeviceAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player) {
		switch (getNpcId()) {
		    case 700966: //Toxic Caverns Teleport Device.
				if (player.getInventory().decreaseByItemId(186000110, 1)) { //Surkana Crystal.
				    TeleportService2.teleportTo(player, 300160000, 1338.5336f, 806.8095f, 113.52537f, (byte) 0, TeleportAnimation.JUMP_ANIMATION);
                } else {
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
				}
		    break;
		}
	}
}