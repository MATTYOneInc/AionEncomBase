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
package ai.instance.illuminaryObelisk;

import ai.ActionItemNpcAI2;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/****/

@AIName("Defence_Charge")
public class Defence_ChargeAI2 extends NpcAI2
{
	@Override
    protected void handleSpawned() {
		switch (getNpcId()) {
			case 702218: //Eastern Defence Charge 01.
			    announceEasternCharging();
			break;
			case 702219: //Eastern Defence Charge 02.
			    announceEasternAlmostCharging();
			break;
			case 702220: //Eastern Defence Charge 03.
				announceEasternEndCharging();
			break;
			case 702221: //Western Defence Charge 01.
			    announceWesternCharging();
			break;
			case 702222: //Western Defence Charge 02.
			    announceWesternAlmostCharging();
			break;
			case 702223: //Western Defence Charge 03.
				announceWesternEndCharging();
			break;
			case 702224: //Southern Defence Charge 01.
			    announceSouthernCharging();
			break;
			case 702225: //Southern Defence Charge 02.
			    announceSouthernAlmostCharging();
			break;
			case 702226: //Southern Defence Charge 03.
				announceSouthernEndCharging();
			break;
			case 702227: //Northern Defence Charge 01.
			    announceNorthernCharging();
			break;
			case 702228: //Northern Defence Charge 02.
			    announceNorthernAlmostCharging();
			break;
			case 702229: //Northern Defence Charge 03.
				announceNorthernEndCharging();
			break;
		}
		super.handleSpawned();
    }
	
   /**
	* Eastern Shield.
	*/
	private void announceEasternCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The eastern shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_01);
			}
		});
	}
	private void announceEasternAlmostCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The eastern shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_01);
				//The eastern shield power generator will finish charging in 30 seconds.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_FINAL_CHARGE_01, 198000);
			}
		});
	}
	private void announceEasternEndCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The eastern shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_01);
				//This power generator is fully charged. It cannot be charged any further.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_END, 5000);
			}
		});
	}
	
   /**
	* Western Shield.
	*/
	private void announceWesternCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The western shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_02);
			}
		});
	}
	private void announceWesternAlmostCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The western shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_02);
				//The western shield power generator will finish charging in 30 seconds.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_FINAL_CHARGE_02, 198000);
			}
		});
	}
	private void announceWesternEndCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The western shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_02);
				//This power generator is fully charged. It cannot be charged any further.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_END, 5000);
			}
		});
	}
	
   /**
	* Southern Shield.
	*/
	private void announceSouthernCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The southern shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_03);
			}
		});
	}
	private void announceSouthernAlmostCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The southern shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_03);
				//The southern shield power generator will finish charging in 30 seconds.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_FINAL_CHARGE_03, 198000);
			}
		});
	}
	private void announceSouthernEndCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The southern shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_03);
				//This power generator is fully charged. It cannot be charged any further.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_END, 5000);
			}
		});
	}
	
   /**
	* Northern Shield.
	*/
	private void announceNorthernCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The northern shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_04);
			}
		});
	}
	private void announceNorthernAlmostCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The northern shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_04);
				//The northern shield power generator will finish charging in 30 seconds.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_FINAL_CHARGE_04, 198000);
			}
		});
	}
	private void announceNorthernEndCharging() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The northern shield power generator is charging.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_04);
				//This power generator is fully charged. It cannot be charged any further.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_OBJ_CHARGE_END, 5000);
			}
		});
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}