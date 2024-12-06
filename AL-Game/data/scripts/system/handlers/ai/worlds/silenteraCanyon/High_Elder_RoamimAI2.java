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
package ai.worlds.silenteraCanyon;

import ai.AggressiveNpcAI2;
import com.aionemu.gameserver.ai2.AIName;
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

@AIName("roamim")
public class High_Elder_RoamimAI2 extends AggressiveNpcAI2
{
	private int elderPhase = 0;
	private AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true)) {
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage) {
		if (hpPercentage == 95 && elderPhase < 1) {
			elderPhase = 1;
			announceHighElderRoamimFurious();
		} if (hpPercentage == 50 && elderPhase < 2) {
			elderPhase = 2;
			announceHighElderRoamimSummoned();
		} if (hpPercentage == 10 && elderPhase < 3) {
			elderPhase = 3;
			announceHighElderRoamimFurious();
		}
	}
	
	private void announceHighElderRoamimFurious() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//High Elder Roamim is furious!
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Underpass_Nephilim_Raid_Rage);
			}
		});
	}
	private void announceHighElderRoamimSummoned() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//High Elder Roamim has summoned players.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Underpass_Nephilim_Raid_Recall);
			}
		});
	}
	private void announceHighElderRoamimReset() {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//High Elder Roamim's threat level has reset!
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Underpass_Nephilim_Raid_ResetAggro);
			}
		});
	}
	
	@Override
	protected void handleBackHome() {
		isAggred.set(false);
		announceHighElderRoamimReset();
		super.handleBackHome();
	}
}