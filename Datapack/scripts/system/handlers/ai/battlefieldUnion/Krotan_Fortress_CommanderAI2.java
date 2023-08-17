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
package ai.battlefieldUnion;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/****/

@AIName("Krotan_Fortress_Commander")
public class Krotan_Fortress_CommanderAI2 extends AggressiveNpcAI2
{
	@Override
    protected void handleSpawned() {
		switch (getNpcId()) {
			//Krotan Fortress Commander [Balaur]
			case 884057: //Ereshkigal Icecrowned Krotan Commander.
				announceIcecrownedAppears();
			break;
			case 884058: //Ereshkigal Icecoated Krotan Commander.
				announceIcecoatedAppears();
			break;
			case 884059: //Ereshkigal Icebladed Krotan Commander.
				announceIcebladedAppears();
			break;
			case 884060: //Ereshkigal Icesteeped Krotan Commander.
				announceIcesteepedAppears();
			break;
			case 884061: //Ereshkigal Icedrenched Krotan Commander.
				announceIcedrenchedAppears();
			break;
			case 884062: //Ereshkigal Iceblooded Krotan Commander.
				announceIcebloodedAppears();
			break;
			//Krotan Fortress Commander [Elyos]
			case 884094: //Krotan Fortress Sunbathed Commander
				announceSunbathedAppears();
			break;
			case 884118: //Krotan Fortress Sunsoaked Commander
				announceSunsoakedAppears();
			break;
			case 884142: //Krotan Fortress Suntouched Commander.
				announceSuntouchedAppears();
			break;
			case 884166: //Krotan Fortress Sunsteeped Commander.
				announceSunsteepedAppears();
			break;
			case 884190: //Krotan Fortress Sundrenched Commander.
				announceSundrenchedAppears();
			break;
			case 884214: //Krotan Fortress Sunblessed Commander.
				announceSunblessedAppears();
			break;
			//Krotan Fortress Commander [Asmodians]
			case 884106: //Krotan Fortress Shadeprotected Commander.
				announceShadeprotectedAppears();
			break;
			case 884130: //Krotan Fortress Shadesoaked Commander.
				announceShadesoakedAppears();
			break;
			case 884154: //Krotan Fortress Shadetouched Commander.
				announceShadetouchedAppears();
			break;
			case 884178: //Krotan Fortress Shadesteeped Commander.
				announceShadesteepedAppears();
			break;
			case 884202: //Krotan Fortress Shadedrenched Commander.
				announceShadedrenchedAppears();
			break;
			case 884226: //Krotan Fortress Shadeblessed Commander.
				announceShadeblessedAppears();
			break;
		}
		super.handleSpawned();
    }
	
   /**
	* Elyos
	*/
	private void announceSunbathedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sunbathed Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_01_Spawn_Li, 0);
			}
		});
	}
	private void announceSunsoakedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sunsoaked Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_02_Spawn_Li, 10000);
			}
		});
	}
	private void announceSuntouchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Suntouched Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_03_Spawn_Li, 20000);
			}
		});
	}
	private void announceSunsteepedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sunsteeped Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_04_Spawn_Li, 30000);
			}
		});
	}
	private void announceSundrenchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sundrenched Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_05_Spawn_Li, 40000);
			}
		});
	}
	private void announceSunblessedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sunblessed Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_06_Spawn_Li, 50000);
			}
		});
	}
	
   /**
	* Asmodians
	*/
	private void announceShadeprotectedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadeprotected Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_01_Spawn_Da, 0);
			}
		});
	}
	private void announceShadesoakedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadesoaked Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_02_Spawn_Da, 10000);
			}
		});
	}
	private void announceShadetouchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadetouched Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_03_Spawn_Da, 20000);
			}
		});
	}
	private void announceShadesteepedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadesteeped Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_04_Spawn_Da, 30000);
			}
		});
	}
	private void announceShadedrenchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadedrenched Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_05_Spawn_Da, 40000);
			}
		});
	}
	private void announceShadeblessedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadeblessed Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_06_Spawn_Da, 50000);
			}
		});
	}
	
   /**
	* Balaur
	*/
	private void announceIcecrownedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icecrowned Krotan Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_01_Spawn_Dr, 0);
			}
		});
	}
	private void announceIcecoatedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icecoated Krotan Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_02_Spawn_Dr, 10000);
			}
		});
	}
	private void announceIcebladedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icebladed Krotan Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_03_Spawn_Dr, 20000);
			}
		});
	}
	private void announceIcesteepedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icesteeped Krotan Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_04_Spawn_Dr, 30000);
			}
		});
	}
	private void announceIcedrenchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icedrenched Krotan Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_05_Spawn_Dr, 40000);
			}
		});
	}
	private void announceIcebloodedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Iceblooded Krotan Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_06_Spawn_Dr, 50000);
			}
		});
	}
	
	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			//Krotan Fortress Commander [Balaur]
			case 884057: //Ereshkigal Icecrowned Krotan Commander.
				announceIcecrownedDied();
			break;
			case 884058: //Ereshkigal Icecoated Krotan Commander.
				announceIcecoatedDied();
			break;
			case 884059: //Ereshkigal Icebladed Krotan Commander.
				announceIcebladedDied();
			break;
			case 884060: //Ereshkigal Icesteeped Krotan Commander.
				announceIcesteepedDied();
			break;
			case 884061: //Ereshkigal Icedrenched Krotan Commander.
				announceIcedrenchedDied();
			break;
			case 884062: //Ereshkigal Iceblooded Krotan Commander.
				announceIcebloodedDied();
			break;
			//Krotan Fortress Commander [Elyos]
			case 884094: //Krotan Fortress Sunbathed Commander
				announceSunbathedDied();
			break;
			case 884118: //Krotan Fortress Sunsoaked Commander
				announceSunsoakedDied();
			break;
			case 884142: //Krotan Fortress Suntouched Commander.
				announceSuntouchedDied();
			break;
			case 884166: //Krotan Fortress Sunsteeped Commander.
				announceSunsteepedDied();
			break;
			case 884190: //Krotan Fortress Sundrenched Commander.
				announceSundrenchedDied();
			break;
			case 884214: //Krotan Fortress Sunblessed Commander.
				announceSunblessedDied();
			break;
			//Krotan Fortress Commander [Asmodians]
			case 884106: //Krotan Fortress Shadeprotected Commander.
				announceShadeprotectedDied();
			break;
			case 884130: //Krotan Fortress Shadesoaked Commander.
				announceShadesoakedDied();
			break;
			case 884154: //Krotan Fortress Shadetouched Commander.
				announceShadetouchedDied();
			break;
			case 884178: //Krotan Fortress Shadesteeped Commander.
				announceShadesteepedDied();
			break;
			case 884202: //Krotan Fortress Shadedrenched Commander.
				announceShadedrenchedDied();
			break;
			case 884226: //Krotan Fortress Shadeblessed Commander.
				announceShadeblessedDied();
			break;
		}
		super.handleDied();
	}
	
   /**
	* Elyos
	*/
	private void announceSunbathedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sunbathed Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_01_Die_Li, 0);
			}
		});
	}
	private void announceSunsoakedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sunsoaked Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_02_Die_Li, 0);
			}
		});
	}
	private void announceSuntouchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Suntouched Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_03_Die_Li, 0);
			}
		});
	}
	private void announceSunsteepedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sunsteeped Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_04_Die_Li, 0);
			}
		});
	}
	private void announceSundrenchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sundrenched Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_05_Die_Li, 0);
			}
		});
	}
	private void announceSunblessedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Sunblessed Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_06_Die_Li, 0);
			}
		});
	}
	
   /**
	* Asmodians
	*/
	private void announceShadeprotectedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadeprotected Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_01_Die_Da, 0);
			}
		});
	}
	private void announceShadesoakedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadesoaked Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_02_Die_Da, 0);
			}
		});
	}
	private void announceShadetouchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadetouched Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_03_Die_Da, 0);
			}
		});
	}
	private void announceShadesteepedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadesteeped Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_04_Die_Da, 0);
			}
		});
	}
	private void announceShadedrenchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadedrenched Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_05_Die_Da, 0);
			}
		});
	}
	private void announceShadeblessedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Krotan Fortress Shadeblessed Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_06_Die_Da, 0);
			}
		});
	}
	
   /**
	* Balaur
	*/
	private void announceIcecrownedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icecrowned Krotan Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_01_Die_Dr, 0);
			}
		});
	}
	private void announceIcecoatedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icecoated Krotan Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_02_Die_Dr, 0);
			}
		});
	}
	private void announceIcebladedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icebladed Krotan Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_03_Die_Dr, 0);
			}
		});
	}
	private void announceIcesteepedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icesteeped Krotan Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_04_Die_Dr, 0);
			}
		});
	}
	private void announceIcedrenchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icedrenched Krotan Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_05_Die_Dr, 0);
			}
		});
	}
	private void announceIcebloodedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Iceblooded Krotan Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1221_commander_06_Die_Dr, 0);
			}
		});
	}
}