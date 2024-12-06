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
package ai.instance.transidiumAnnex;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("hangar_controller")
public class Hangar_ControllerAI2 extends NpcAI2
{
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			switch (getNpcId()) {
				case 297310: //Chariot Hangar I Controller.
				    announceGAB1SubTankA();
				break;
				case 297311: //Chariot Hangar II Controller.
				    announceGAB1SubTankB();
				break;
				case 297312: //Ignus Engine Hangar I Controller.
					announceGAB1SubTankC();
				break;
				case 297313: //Ignus Engine Hangar II Controller.
					announceGAB1SubTankD();
				break;
			}
		}
	}
	
	private void announceGAB1SubTankA() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//Chariot Hangar I Controller is under attack.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_TANK_A_ATTACKED);
				}
			}
		});
	}
	private void announceGAB1SubTankB() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//Chariot Hangar II Controller is under attack.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_TANK_B_ATTACKED);
				}
			}
		});
	}
	private void announceGAB1SubTankC() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//Ignus Engine Hangar I Controller is under attack.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_TANK_C_ATTACKED);
				}
			}
		});
	}
	private void announceGAB1SubTankD() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//Ignus Engine Hangar II Controller is under attack.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_TANK_D_ATTACKED);
				}
			}
		});
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}