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
/** Author(Encom)
/****/

@AIName("Miren_Fortress_Commander")
public class Miren_Fortress_CommanderAI2 extends AggressiveNpcAI2
{
	@Override
    protected void handleSpawned() {
		switch (getNpcId()) {
			//Miren Fortress Commander [Balaur]
			case 884063: //Ereshkigal Icecrowned Miren Commander.
				announceIcecrownedAppears();
			break;
			case 884064: //Ereshkigal Icecoated Miren Commander.
				announceIcecoatedAppears();
			break;
			case 884065: //Ereshkigal Icebladed Miren Commander.
				announceIcebladedAppears();
			break;
			case 884066: //Ereshkigal Icesteeped Miren Commander.
				announceIcesteepedAppears();
			break;
			case 884067: //Ereshkigal Icedrenched Miren Commander.
				announceIcedrenchedAppears();
			break;
			case 884068: //Ereshkigal Iceblooded Miren Commander.
				announceIcebloodedAppears();
			break;
			//Miren Fortress Commander [Elyos]
			case 884100: //Miren Fortress Sunbathed Commander.
				announceSunbathedAppears();
			break;
			case 884124: //Miren Fortress Sunsoaked Commander.
				announceSunsoakedAppears();
			break;
			case 884148: //Miren Fortress Suntouched Commander.
				announceSuntouchedAppears();
			break;
			case 884172: //Miren Fortress Sunsteeped Commander.
				announceSunsteepedAppears();
			break;
			case 884196: //Miren Fortress Sundrenched Commander.
				announceSundrenchedAppears();
			break;
			case 884220: //Miren Fortress Sunblessed Commander.
				announceSunblessedAppears();
			break;
			//Miren Fortress Commander [Asmodians]
			case 884112: //Miren Fortress Shadeprotected Commander.
				announceShadeprotectedAppears();
			break;
			case 884136: //Miren Fortress Shadesoaked Commander.
				announceShadesoakedAppears();
			break;
			case 884160: //Miren Fortress Shadetouched Commander.
				announceShadetouchedAppears();
			break;
			case 884184: //Miren Fortress Shadesteeped Commander.
				announceShadesteepedAppears();
			break;
			case 884208: //Miren Fortress Shadedrenched Commander.
				announceShadedrenchedAppears();
			break;
			case 884232: //Miren Fortress Shadeblessed Commander.
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
				//The Miren Fortress Sunbathed Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_01_Spawn_Li, 0);
			}
		});
	}
	private void announceSunsoakedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Sunsoaked Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_02_Spawn_Li, 10000);
			}
		});
	}
	private void announceSuntouchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Suntouched Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_03_Spawn_Li, 20000);
			}
		});
	}
	private void announceSunsteepedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Sunsteeped Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_04_Spawn_Li, 30000);
			}
		});
	}
	private void announceSundrenchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Sundrenched Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_05_Spawn_Li, 40000);
			}
		});
	}
	private void announceSunblessedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Sunblessed Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_06_Spawn_Li, 50000);
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
				//The Miren Fortress Shadeprotected Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_01_Spawn_Da, 0);
			}
		});
	}
	private void announceShadesoakedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadesoaked Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_02_Spawn_Da, 10000);
			}
		});
	}
	private void announceShadetouchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadetouched Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_03_Spawn_Da, 20000);
			}
		});
	}
	private void announceShadesteepedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadesteeped Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_04_Spawn_Da, 30000);
			}
		});
	}
	private void announceShadedrenchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadedrenched Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_05_Spawn_Da, 40000);
			}
		});
	}
	private void announceShadeblessedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadeblessed Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_06_Spawn_Da, 50000);
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
				//The Ereshkigal Icecrowned Miren Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_01_Spawn_Dr, 0);
			}
		});
	}
	private void announceIcecoatedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icecoated Miren Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_02_Spawn_Dr, 10000);
			}
		});
	}
	private void announceIcebladedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icebladed Miren Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_03_Spawn_Dr, 20000);
			}
		});
	}
	private void announceIcesteepedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icesteeped Miren Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_04_Spawn_Dr, 30000);
			}
		});
	}
	private void announceIcedrenchedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icedrenched Miren Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_05_Spawn_Dr, 40000);
			}
		});
	}
	private void announceIcebloodedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Iceblooded Miren Commander has appeared!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_06_Spawn_Dr, 50000);
			}
		});
	}
	
	@Override
	protected void handleDied() {
		switch (getNpcId()) {
			//Miren Fortress Commander [Balaur]
			case 884063: //Ereshkigal Icecrowned Miren Commander.
				announceIcecrownedDied();
			break;
			case 884064: //Ereshkigal Icecoated Miren Commander.
				announceIcecoatedDied();
			break;
			case 884065: //Ereshkigal Icebladed Miren Commander.
				announceIcebladedDied();
			break;
			case 884066: //Ereshkigal Icesteeped Miren Commander.
				announceIcesteepedDied();
			break;
			case 884067: //Ereshkigal Icedrenched Miren Commander.
				announceIcedrenchedDied();
			break;
			case 884068: //Ereshkigal Iceblooded Miren Commander.
				announceIcebloodedDied();
			break;
			//Miren Fortress Commander [Elyos]
			case 884100: //Miren Fortress Sunbathed Commander.
				announceSunbathedDied();
			break;
			case 884124: //Miren Fortress Sunsoaked Commander.
				announceSunsoakedDied();
			break;
			case 884148: //Miren Fortress Suntouched Commander.
				announceSuntouchedDied();
			break;
			case 884172: //Miren Fortress Sunsteeped Commander.
				announceSunsteepedDied();
			break;
			case 884196: //Miren Fortress Sundrenched Commander.
				announceSundrenchedDied();
			break;
			case 884220: //Miren Fortress Sunblessed Commander.
				announceSunblessedDied();
			break;
			//Miren Fortress Commander [Asmodians]
			case 884112: //Miren Fortress Shadeprotected Commander.
				announceShadeprotectedDied();
			break;
			case 884136: //Miren Fortress Shadesoaked Commander.
				announceShadesoakedDied();
			break;
			case 884160: //Miren Fortress Shadetouched Commander.
				announceShadetouchedDied();
			break;
			case 884184: //Miren Fortress Shadesteeped Commander.
				announceShadesteepedDied();
			break;
			case 884208: //Miren Fortress Shadedrenched Commander.
				announceShadedrenchedDied();
			break;
			case 884232: //Miren Fortress Shadeblessed Commander.
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
				//The Miren Fortress Sunbathed Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_01_Die_Li, 0);
			}
		});
	}
	private void announceSunsoakedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Sunsoaked Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_02_Die_Li, 0);
			}
		});
	}
	private void announceSuntouchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Suntouched Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_03_Die_Li, 0);
			}
		});
	}
	private void announceSunsteepedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Sunsteeped Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_04_Die_Li, 0);
			}
		});
	}
	private void announceSundrenchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Sundrenched Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_05_Die_Li, 0);
			}
		});
	}
	private void announceSunblessedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Sunblessed Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_06_Die_Li, 0);
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
				//The Miren Fortress Shadeprotected Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_01_Die_Da, 0);
			}
		});
	}
	private void announceShadesoakedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadesoaked Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_02_Die_Da, 0);
			}
		});
	}
	private void announceShadetouchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadetouched Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_03_Die_Da, 0);
			}
		});
	}
	private void announceShadesteepedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadesteeped Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_04_Die_Da, 0);
			}
		});
	}
	private void announceShadedrenchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadedrenched Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_05_Die_Da, 0);
			}
		});
	}
	private void announceShadeblessedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Miren Fortress Shadeblessed Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_06_Die_Da, 0);
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
				//The Ereshkigal Icecrowned Miren Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_01_Die_Dr, 0);
			}
		});
	}
	private void announceIcecoatedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icecoated Miren Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_02_Die_Dr, 0);
			}
		});
	}
	private void announceIcebladedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icebladed Miren Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_03_Die_Dr, 0);
			}
		});
	}
	private void announceIcesteepedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icesteeped Miren Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_04_Die_Dr, 0);
			}
		});
	}
	private void announceIcedrenchedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Icedrenched Miren Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_05_Die_Dr, 0);
			}
		});
	}
	private void announceIcebloodedDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Ereshkigal Iceblooded Miren Commander has been slain!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_1241_commander_06_Die_Dr, 0);
			}
		});
	}
}