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
package ai.worlds.reshanta.worldBoss.enragedGuardian;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/** Author (Encom)
/****/

@AIName("Ab1_BossNamed_60_03_Al")
public class Enraged_Eastern_GuardianAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
	}
	
	@Override
    protected void handleSpawned() {
        super.handleSpawned();
		startLifeTask();
		announceAb1NamedAppears();
    }
	
	private void startLifeTask() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			        @Override
			        public void visit(Player player) {
						AI2Actions.deleteOwner(Enraged_Eastern_GuardianAI2.this);
						//Enraged Eastern Guardian has disappeared.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Despawn_03);
			        }
				});
			}
		}, 3600000); //1Hr.
	}
	
	private void announceAb1NamedAppears() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//Siel's Eastern Guardian has appeared.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Named_Spawn_In_03, 0);
			}
		});
	}
	
	@Override
	protected void handleDied() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//Enraged Eastern Guardian has been defeated.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_Ab1_BossNamed_65_Al_Die_03);
			}
		});
		super.handleDied();
	}
}