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
package ai.event;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.NpcType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUSTOM_SETTINGS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("suspicious_box")
public class Suspicious_BoxAI2 extends NpcAI2
{
	private Future<?> suspiciousChestTask;
	private AtomicBoolean startedEvent = new AtomicBoolean(false);
	
	@Override
	protected void handleDied() {
		suspiciousChestTask.cancel(true);
		super.handleDied();
	}
	
	@Override
    protected void handleCreatureSee(Creature creature) {
        checkDistance(this, creature);
    }
	
    @Override
    protected void handleCreatureMoved(Creature creature) {
        checkDistance(this, creature);
    }
	
	private void checkDistance(NpcAI2 ai, Creature creature) {
        if (creature instanceof Player && !creature.getLifeStats().isAlreadyDead()) {
			final Player player = (Player) creature;
			if (player.isInGroup2() && MathUtil.isIn3dRange(getOwner(), creature, 10)) {
        		AI2Actions.deleteOwner(Suspicious_BoxAI2.this);
				AI2Actions.scheduleRespawn(Suspicious_BoxAI2.this);
				//One of the Ancient Treasure Boxes is missing.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_TREASUREBOX_DESPAWN_ONE, 0);
			} else if (MathUtil.isIn3dRange(getOwner(), creature, 10)) {
				if (startedEvent.compareAndSet(false, true)) {
					suspiciousChestTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
					    @Override
						public void run() {
						    eventChestStart();
						}
					}, 1000);
					suspiciousChestTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
					    @Override
						public void run() {
						    getOwner().setNpcType(NpcType.ATTACKABLE);
							PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.ATTACKABLE.getId(), 0));
						}
					}, 3000);
					suspiciousChestTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
					    @Override
					    public void run() {
							eventChestFail();
							AI2Actions.deleteOwner(Suspicious_BoxAI2.this);
							AI2Actions.scheduleRespawn(Suspicious_BoxAI2.this);
				        }
			        }, 180000);
				}
			}
        }
    }
	
	private void eventChestStart() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				PacketSendUtility.sendSys3Message(player, "\uE005", "You have <3 Minutes> for remove a curse on chest");
			}
		});
	}
	private void eventChestFail() {
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				//The treasure chest has disappeared because you failed to destroy the monsters within the time limit.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDABRECORE_OOPS_REWARD_IS_GONE);
			}
		});
	}
	
	@Override
	public boolean isMoveSupported() {
		return false;
	}
	
	@Override
	public int modifyOwnerDamage(int damage) {
		return 1;
	}
	
	@Override
	public int modifyDamage(int damage) {
		return 1;
	}
}