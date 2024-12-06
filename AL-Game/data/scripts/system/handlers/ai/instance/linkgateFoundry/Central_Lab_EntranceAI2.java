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
package ai.instance.linkgateFoundry;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("central_lab_entrance")
public class Central_Lab_EntranceAI2 extends NpcAI2
{
	@Override
    protected void handleDialogStart(Player player) {
        if (player.getInventory().getFirstItemByItemId(185000196) != null) {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
        } else {
            PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
        }
    }
	
   /**
	* http://na.aiononline.com/media/uploads/images/front-page-banners/AION_Patch_Notes_021716.pdf
	* - The number of keys required to move to the central lab has been reduced from 1/5/7 to 1/3/5
	*/
	@Override
    public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		int instanceId = getPosition().getInstanceId();
		switch (getNpcId()) {
		    case 702339: //Central Lab Entrance.
		        switch (player.getWorldId()) {
		            case 301270000: //Linkgate Foundry 4.7
				        if (dialogId == 20007 && player.getInventory().decreaseByItemId(185000196, 1)) {
			                startWoundedBelsagos();
							//The Central Laboratory has been opened. You can now enter Wounded Belsagos' Realm.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDLDF4Re_01_DoorOpen_01, 2000);
							TeleportService2.teleportTo(player, 301270000, instanceId, 212.77286f, 259.47467f, 313.61807f, (byte) 1);
					    } else if (dialogId == 20008 && player.getInventory().decreaseByItemId(185000196, 3)) {
			                startVolatileBelsagos();
							//The Central Laboratory has been opened. You may enter the Volatile Belsagos Realm.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDLDF4Re_01_DoorOpen_02, 2000);
							TeleportService2.teleportTo(player, 301270000, instanceId, 212.77286f, 259.47467f, 313.61807f, (byte) 1);
					    } else if (dialogId == 20009 && player.getInventory().decreaseByItemId(185000196, 5)) {
			                startFuriousBelsagos();
							//The Central Laboratory has been opened. You may enter the Furious Belsagos Realm.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDLDF4Re_01_DoorOpen_03, 2000);
							TeleportService2.teleportTo(player, 301270000, instanceId, 212.77286f, 259.47467f, 313.61807f, (byte) 1);
					    }
				    break;
			    }
			break;
		}
		return true;
	}
	
	private void startWoundedBelsagos() {
		spawn(234990, 244.57622f, 259.80493f, 312.3084f, (byte) 75); //Wounded Belsagos.
	}
	
	private void startVolatileBelsagos() {
		spawn(233898, 244.57622f, 259.80493f, 312.3084f, (byte) 75); //Volatile Belsagos.
	}
	
	private void startFuriousBelsagos() {
		spawn(234991, 244.57622f, 259.80493f, 312.3084f, (byte) 75); //Furious Belsagos.
	}
}