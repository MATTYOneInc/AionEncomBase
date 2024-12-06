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
package ai.instance.seizedDanuarSanctuary;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("catacombs")
public class TheCatacombsEntranceAI2 extends NpcAI2
{
	@Override
    protected void handleDialogStart(Player player) {
        if (player.getInventory().getFirstItemByItemId(185000181) != null) { //The Catacombs Key.
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
        } else {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
        }
    }
	
	@Override
    public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		int instanceId = getPosition().getInstanceId();
		switch (getNpcId()) {
		    case 701873: //The Catacombs Entrance.
		        switch (player.getWorldId()) {
		            case 301140000: //Seized Danuar Sanctuary 4.8
				        if (dialogId == 10000 && player.getInventory().decreaseByItemId(185000181, 1)) { //The Catacombs Key.
			                TeleportService2.teleportTo(player, 301140000, instanceId, 1032.0134f, 369.56287f, 297.8753f, (byte) 32);
					    }
				    break;
			    } switch (player.getWorldId()) {
				    case 301380000: //Danuar Sanctuary 4.8
					    if (dialogId == 10000 && player.getInventory().decreaseByItemId(185000181, 1)) { //The Catacombs Key.
					        TeleportService2.teleportTo(player, 301380000, instanceId, 1032.0134f, 369.56287f, 297.8753f, (byte) 32);
						}
					break;
				}
			break;
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
}