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
package ai.instance.cradleOfEternity;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("Eternity_Rift")
public class Eternity_RiftAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
		if (player.isArchDaeva()) {
		    PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
	}
	
	@Override
    public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		if (dialogId == 104) {
			switch (getNpcId()) {
			    case 806053: //Eternity Rift.
				    if (player.getCommonData().getRace() == Race.ASMODIANS) {
					    TeleportService2.teleportTo(player, 220120000, 448.4795f, 499.93314f, 299.85013f, (byte) 0);
				    } else if (player.getCommonData().getRace() == Race.ELYOS) {
					    TeleportService2.teleportTo(player, 210110000, 448.4795f, 499.93314f, 299.85013f, (byte) 0);
				    }
				break;
			}
		}
		return true;
	}
}