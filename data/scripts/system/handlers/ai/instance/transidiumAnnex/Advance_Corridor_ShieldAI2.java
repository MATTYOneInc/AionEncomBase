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

@AIName("advance_corridor_shield")
public class Advance_Corridor_ShieldAI2 extends NpcAI2
{
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
			switch (getNpcId()) {
				case 297306: //Belus Advance Corridor Shield.
				    announceBelusAdvance();
				break;
				case 297307: //Aspida Advance Corridor Shield.
				    announceAspidaAdvance();
				break;
				case 297308: //Atanatos Advance Corridor Shield.
					announceAtanatosAdvance();
				break;
				case 297309: //Disillon Advance Corridor Shield.
					announceDisillonAdvance();
				break;
			}
		}
	}
	
	private void announceBelusAdvance() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The Belus Advance Corridor Shield is under attack.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_PORTAL_DEST_69_ATTACKED);
				}
			}
		});
	}
	private void announceAspidaAdvance() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The Aspida Advance Corridor Shield is under attack.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_PORTAL_DEST_70_ATTACKED);
				}
			}
		});
	}
	private void announceAtanatosAdvance() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The Atanatos Advance Corridor Shield is under attack.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_PORTAL_DEST_71_ATTACKED);
				}
			}
		});
	}
	private void announceDisillonAdvance() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				if (player.isOnline()) {
					//The Disillon Advance Corridor Shield is under attack.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_PORTAL_DEST_72_ATTACKED);
				}
			}
		});
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}