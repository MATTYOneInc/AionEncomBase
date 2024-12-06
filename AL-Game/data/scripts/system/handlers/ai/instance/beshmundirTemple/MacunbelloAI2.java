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
package ai.instance.beshmundirTemple;

import ai.AggressiveNpcAI2;

import com.aionemu.commons.network.util.ThreadPoolManager;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.manager.EmoteManager;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("macunbello")
public class MacunbelloAI2 extends AggressiveNpcAI2
{
	private Future<?> rightHandTask;
	private Future<?> activeEventTask;
	private AtomicBoolean isHome = new AtomicBoolean(true);
	private Phase phase = Phase.ACTIVE;
	private boolean canThink = true;
	
	private enum Phase {
		ACTIVE,
		RIGHT_HAND,
	}
	
	@Override
	public boolean canThink() {
		return canThink;
	}
	
	@Override
	public void handleAttack(Creature creature) {
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false)) {
			getPosition().getWorldMapInstance().getDoors().get(467).setOpen(false);
			//Whoever ye may be, thou shalt not escape this curse.
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1500063, getObjectId(), 0, 0);
			startMacumbelloRightHandEvent();
		}
	}
	
	private void cancelActiveEventTask() {
		if (activeEventTask != null && !activeEventTask.isDone()) {
			activeEventTask.cancel(true);
		}
	}
	
	private void cancelRightHandEventTask() {
		if (rightHandTask != null && !rightHandTask.isDone()) {
			rightHandTask.cancel(true);
		}
	}
	
	private void startMacumbelloRightHandEvent() {
		rightHandTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.ACTIVE)) {
					phase = Phase.RIGHT_HAND;
					cancelActiveEventTask();
					//Come forth, my faithful servants!
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1500060, getObjectId(), 0, 0);
					//Hurry and devour these foolish Daevas!
					NpcShoutsService.getInstance().sendMsg(getOwner(), 1500061, getObjectId(), 0, 3000);
					canThink = false;
					EmoteManager.emoteStopAttacking(getOwner());
					setStateIfNot(AIState.WALKING);
					spawnMacumbelloRightHandEvent();
					rightHandTask = ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							if (!isAlreadyDead() && !isHome.get() && phase.equals(Phase.RIGHT_HAND)) {
								phase = Phase.ACTIVE;
								canThink = true;
								Creature creature = getAggroList().getMostHated();
								if (creature == null || creature.getLifeStats().isAlreadyDead() || !getOwner().canSee(creature)) {
									setStateIfNot(AIState.FIGHT);
									think();
								} else {
									getMoveController().abortMove();
									getOwner().setTarget(creature);
									getOwner().getGameStats().renewLastAttackTime();
									getOwner().getGameStats().renewLastAttackedTime();
									getOwner().getGameStats().renewLastChangeTargetTime();
									getOwner().getGameStats().renewLastSkillTime();
									setStateIfNot(AIState.FIGHT);
									handleMoveValidate();
								}
							}
						}
					}, 11000);
				}
			}
		}, 14000);
	}
	
	private void deleteNpcs(List<Npc> npcs) {
		for (Npc npc : npcs) {
			if (npc != null) {
				npc.getController().onDelete();
			}
		}
	}
	
	private void spawnMacumbelloRightHandEvent() {
		moveToBoss((Npc)spawn(281698, 1007.2226f, 109.83921f, 242.70653f, (byte) 30));
		moveToBoss((Npc)spawn(281698, 952.07434f, 109.54505f, 242.71020f, (byte) 29));
		moveToBoss((Npc)spawn(281698, 1004.0906f, 159.85290f, 241.77126f, (byte) 90));
		moveToBoss((Npc)spawn(281698, 955.58966f, 160.59547f, 241.77678f, (byte) 90));
	}
	
	private void moveToBoss(final Npc npc) {
		if (!isAlreadyDead() && !isHome.get()) {
			npc.setTarget(getOwner());
			npc.getMoveController().moveToTargetObject();
		}
	}
	
	@Override
	protected void handleDespawned() {
		super.handleDespawned();
		cancelActiveEventTask();
		cancelRightHandEventTask();
	}
	
	@Override
	protected void handleBackHome() {
		isHome.set(true);
		phase = Phase.ACTIVE;
		canThink = true;
		cancelActiveEventTask();
		super.handleBackHome();
	}
	
	@Override
	protected void handleDied() {
		cancelActiveEventTask();
		cancelRightHandEventTask();
		final WorldPosition p = getPosition();
		if (p != null) {
			deleteNpcs(p.getWorldMapInstance().getNpcs(281698));
		}
		super.handleDied();
	}
}