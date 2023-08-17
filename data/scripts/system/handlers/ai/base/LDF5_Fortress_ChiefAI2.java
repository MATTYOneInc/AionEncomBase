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
package ai.base;

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

@AIName("ldf5_fortress_chief")
public class LDF5_Fortress_ChiefAI2 extends AggressiveNpcAI2
{
	@Override
    protected void handleSpawned() {
        switch (getNpcId()) {
		    /**
			 * Elyos
			 */
			case 251880: //Elyos Hero's Fall Defense Chief.
			    announceLDF5FortressLiCenter();
		    break;
			case 251881: //Elyos Ashen Glade Defense Chief.
			    announceLDF5FortressLiUp();
		    break;
			case 251882: //Elyos Smoldering Crag Defense Chief.
			    announceLDF5FortressLiDown();
		    break;
			/**
			 * Asmodians
			 */
			case 251960: //Asmodians Hero's Fall Defense Chief.
			    announceLDF5FortressDaCenter();
		    break;
			case 251961: //Asmodians Ashen Glade Defense Chief.
			    announceLDF5FortressDaUp();
		    break;
			case 251962: //Asmodians Smoldering Crag Defense Chief.
			    announceLDF5FortressDaDown();
		    break;
			/**
			 * Balaur
			 */
			case 252040: //Balaur Hero's Fall Defense Chief.
			    announceLDF5FortressDrCenter();
		    break;
			case 252041: //Balaur Ashen Glade Defense Chief.
			    announceLDF5FortressDrUp();
		    break;
			case 252042: //Balaur Smoldering Crag Defense Chief.
			    announceLDF5FortressDrDown();
		    break;
		}
		super.handleSpawned();
    }
	
	/**
	 * Msg Elyos
	 */
	private void announceLDF5FortressLiCenter() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Elyos have captured the Hero's Fall Defense Point.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Li_Center);
			}
		});
	}
	private void announceLDF5FortressLiUp() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Elyos have captured the Ashen Glade Defense Point.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Li_Up);
			}
		});
	}
	private void announceLDF5FortressLiDown() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Elyos have captured the Smoldering Crag Defense Point.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Li_Down);
			}
		});
	}
	
	/**
	 * Msg Asmodians
	 */
	private void announceLDF5FortressDaCenter() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Asmodians have captured the Hero's Fall Defense Point.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Da_Center);
			}
		});
	}
	private void announceLDF5FortressDaUp() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Asmodians have captured the Ashen Glade Defense Point.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Da_Up);
			}
		});
	}
	private void announceLDF5FortressDaDown() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Asmodians have captured the Smoldering Crag Defense Point.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Da_Down);
			}
		});
	}
	
	/**
	 * Msg Balaur
	 */
	private void announceLDF5FortressDrCenter() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Balaur have captured the Hero's Fall Defense Point.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Dr_Center);
			}
		});
	}
	private void announceLDF5FortressDrUp() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Balaur have captured the Ashen Glade Defense Point.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Dr_Up);
			}
		});
	}
	private void announceLDF5FortressDrDown() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Balaur have captured the Smoldering Crag Defense Point.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Dr_Down);
			}
		});
	}
}