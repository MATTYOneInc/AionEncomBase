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
package ai.rvr;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("rift_generator")
public class Rift_GeneratorAI2 extends NpcAI2
{
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			announceRiftGeneratorUnderAttack();
		}
	}
	
	@Override
	protected void handleDied() {
        announceRiftGeneratorDie();
		super.handleDied();
	}
	
	private void announceRiftGeneratorUnderAttack() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Rift Generator is under attack! Once it is destroyed, the Dimensional Vortex will close.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHAT_INVADEPORTL_KEEPER_SYSTEM_MSG01);
			}
		});
	}
	private void announceRiftGeneratorDie() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The Rift Generator has been destroyed.
				//The Dimensional Vortex will close shortly, the infiltration alliance will be disbanded, and its members will be returned home.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CHAT_INVADEPORTL_KEEPER_SYSTEM_MSG03);
			}
		});
	}
}