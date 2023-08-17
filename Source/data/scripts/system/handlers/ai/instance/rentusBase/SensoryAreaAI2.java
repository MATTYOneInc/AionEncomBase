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
package ai.instance.rentusBase;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;

import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("sensory_area")
public class SensoryAreaAI2 extends AggressiveNpcAI2
{
	private AtomicBoolean startedEvent = new AtomicBoolean(false);
	
	@Override
	public void think() {
	}
	
	@Override
	protected void handleCreatureMoved(Creature creature) {
		if (creature instanceof Player) {
			final Player player = (Player) creature;
			if (MathUtil.getDistance(getOwner(), player) <= 10) {
				if (startedEvent.compareAndSet(false, true)) {
					switch (player.getWorldId()) {
		                case 300280000: //Rentus Base
						    //Xasta flies past overhead.
							PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDYun_Rasta_Spawn_01, 9000);
							//Use the anti-aircraft gun to attack Xasta flying overhead.
							PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDYun_Rasta_Spawn_02, 10000);
							//Xasta falls from the sky, wounded!
							PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDYun_Rasta_SUCCEED_01, 120000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(217309, 445.6442f, 439.13187f, 168.64172f, (byte) 40);
								}
							}, 10000);
							AI2Actions.deleteOwner(SensoryAreaAI2.this);
				        break;
					} switch (player.getWorldId()) {
		                case 300620000: //[Occupied] Rentus Base 4.8
						    //Xasta flies past overhead.
							PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDYun_Rasta_Spawn_01, 9000);
							//Use the anti-aircraft gun to attack Xasta flying overhead.
							PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDYun_Rasta_Spawn_02, 10000);
							//Xasta falls from the sky, wounded!
							PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_IDYun_Rasta_SUCCEED_01, 120000);
							ThreadPoolManager.getInstance().schedule(new Runnable() {
							    @Override
								public void run() {
								    spawn(236296, 445.6442f, 439.13187f, 168.64172f, (byte) 40);
								}
							}, 10000);
							AI2Actions.deleteOwner(SensoryAreaAI2.this);
				        break;
					}
				}
			}
		}
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
}