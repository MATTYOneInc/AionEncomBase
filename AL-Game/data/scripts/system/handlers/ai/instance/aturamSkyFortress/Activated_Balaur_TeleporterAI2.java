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
package ai.instance.aturamSkyFortress;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/** Author (Encom)
/****/

@AIName("Activated_Balaur_Teleporter")
public class Activated_Balaur_TeleporterAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player) {
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
	}
	
	@Override
    public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex) {
		int instanceId = getPosition().getInstanceId();
		switch (getNpcId()) {
		    case 702660: //Activated Balaur Teleporter.
		        switch (player.getWorldId()) {
		            case 300240000: //Aturam Sky Fortress.
				        if (dialogId == 10000) {
							//You've heard the contents of a Flagon may help recover HP.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_Doping_02_AD, 0);
							//Power Generator Protection System is in operation.
							//Shutting down the protection system requires destroying the Power Generators in the correct sequence.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_A_FOBJ_SanctuaryATK, 5000);
							//Destroy the Power Generators in the correct sequence to attack Popuchin.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_A_Zone_Explain_01, 10000);
							TeleportService2.teleportTo(player, 300240000, instanceId, 613.54504f, 448.762f, 655.32684f, (byte) 59); //Dredgion Fabrication Zone.
					    } else if (dialogId == 10001) {
							//You see a large obelisk pulsing with energy. Go on. Take some.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_Doping_01_AD, 0);
							//Destroying all Spy Crystals opens the door to the Talon Laboratory.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_B_Zone_Explain_01, 5000);
							TeleportService2.teleportTo(player, 300240000, instanceId, 636.91830f, 360.335f, 638.15230f, (byte) 80); //Warehouse Zone.
					    }
				    break;
			    } switch (player.getWorldId()) {
		            case 300241000: //[Event] Aturam Sky Fortress.
				        if (dialogId == 10000) {
							//You've heard the contents of a Flagon may help recover HP.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_Doping_02_AD, 0);
							//Power Generator Protection System is in operation.
							//Shutting down the protection system requires destroying the Power Generators in the correct sequence.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_A_FOBJ_SanctuaryATK, 5000);
							//Destroy the Power Generators in the correct sequence to attack Popuchin.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_A_Zone_Explain_01, 10000);
							TeleportService2.teleportTo(player, 300241000, instanceId, 613.54504f, 448.762f, 655.32684f, (byte) 59); //Dredgion Fabrication Zone.
					    } else if (dialogId == 10001) {
							//You see a large obelisk pulsing with energy. Go on. Take some.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_Doping_01_AD, 0);
							//Destroying all Spy Crystals opens the door to the Talon Laboratory.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_B_Zone_Explain_01, 5000);
							TeleportService2.teleportTo(player, 300241000, instanceId, 636.91830f, 360.335f, 638.15230f, (byte) 80); //Warehouse Zone.
					    }
				    break;
			    }
			break;
			case 702664: //Activated Balaur Teleporter.
		        switch (player.getWorldId()) {
		            case 300240000: //Aturam Sky Fortress.
				        if (dialogId == 10000) {
							TeleportService2.teleportTo(player, 300240000, instanceId, 636.24340f, 448.515f, 655.45500f, (byte) 59); //Atrium.
					    } else if (dialogId == 10001) {
							//You see a large obelisk pulsing with energy. Go on. Take some.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_Doping_01_AD, 0);
							//Destroying all Spy Crystals opens the door to the Talon Laboratory.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_B_Zone_Explain_01, 5000);
							TeleportService2.teleportTo(player, 300240000, instanceId, 636.91830f, 360.335f, 638.15230f, (byte) 80); //Warehouse Zone.
					    }
				    break;
			    } switch (player.getWorldId()) {
		            case 300241000: //[Event] Aturam Sky Fortress.
				        if (dialogId == 10000) {
							TeleportService2.teleportTo(player, 300241000, instanceId, 636.24340f, 448.515f, 655.45500f, (byte) 59); //Atrium.
					    } else if (dialogId == 10001) {
							//You see a large obelisk pulsing with energy. Go on. Take some.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_Doping_01_AD, 0);
							//Destroying all Spy Crystals opens the door to the Talon Laboratory.
							PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_IDStation_B_Zone_Explain_01, 5000);
							TeleportService2.teleportTo(player, 300241000, instanceId, 636.91830f, 360.335f, 638.15230f, (byte) 80); //Warehouse Zone.
					    }
				    break;
			    }
			break;
			case 730392: //Activated Balaur Teleporter.
		        switch (player.getWorldId()) {
		            case 300240000: //Aturam Sky Fortress.
				        if (dialogId == 10000) {
							TeleportService2.teleportTo(player, 300240000, instanceId, 636.24340f, 448.515f, 655.45500f, (byte) 59); //Atrium.
					    } else if (dialogId == 10001) {
							if (player.getCommonData().getRace() == Race.ASMODIANS) {
						        TeleportService2.teleportTo(player, 220080000, 702.56445f, 2852.6885f, 215.72034f, (byte) 48); //Enshar.
							} else if (player.getCommonData().getRace() == Race.ELYOS) {
								TeleportService2.teleportTo(player, 210070000, 2418.4604f, 630.34576f, 548.80962f, (byte) 8); //Cygnea.
							}
						}
				    break;
			    } switch (player.getWorldId()) {
		            case 300241000: //[Event] Aturam Sky Fortress.
				        if (dialogId == 10000) {
							TeleportService2.teleportTo(player, 300241000, instanceId, 636.24340f, 448.515f, 655.45500f, (byte) 59); //Atrium.
					    } else if (dialogId == 10001) {
							if (player.getCommonData().getRace() == Race.ASMODIANS) {
						        TeleportService2.teleportTo(player, 220080000, 702.56445f, 2852.6885f, 215.72034f, (byte) 48); //Enshar.
							} else if (player.getCommonData().getRace() == Race.ELYOS) {
								TeleportService2.teleportTo(player, 210070000, 2418.4604f, 630.34576f, 548.80962f, (byte) 8); //Cygnea.
							}
						}
				    break;
			    }
			break;
		}
		return true;
	}
}