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
package ai.instance.tallocsHollow;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/****/
/** Author (Encom)
/****/

@AIName("kinquid")
public class KinquidAI2 extends AggressiveNpcAI2
{
	private Future<?> skillTask;
	private AtomicBoolean isHome = new AtomicBoolean(true);
	
	@Override
	protected void handleCreatureAggro(Creature creature) {
		super.handleCreatureAggro(creature);
		if (isHome.compareAndSet(true, false)) {
			getPosition().getWorldMapInstance().getDoors().get(48).setOpen(false);
			check();
			cancelSkillTask();
			startSkillTask();
		}
	}
	
	@Override
	protected void handleBackHome() {
		cancelSkillTask();
		isHome.set(true);
		getPosition().getWorldMapInstance().getDoors().get(48).setOpen(true);
		super.handleBackHome();
		despawnDestroyer();
	}
	
	@Override
	protected void handleDespawned() {
		cancelSkillTask();
		super.handleDespawned();
	}
	
	@Override
	protected void handleDied() {
		super.handleDied();
		getPosition().getWorldMapInstance().getDoors().get(48).setOpen(true);
		cancelSkillTask();
	}
	
	private void cancelSkillTask() {
		if (skillTask != null && !skillTask.isDone()) {
			skillTask.cancel(true);
		}
	}
	
	private void startSkillTask() {
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelSkillTask();
				} else {
					SkillEngine.getInstance().getSkill(getOwner(), 19233, 60, getOwner()).useNoAnimationSkill();
					ThreadPoolManager.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							if (!isAlreadyDead() && getPosition().isSpawned()) {
								SkillEngine.getInstance().getSkill(getOwner(), 19234, 60, getOwner()).useNoAnimationSkill();
							}
						}
					}, 3500);
				}
			}
		}, 35000, 35000);
	}
	
	private void doSchedule() {
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				check();
			}
		}, 2500);
	}
	
	private void despawnDestroyer() {
		Npc cleaveArmor = getPosition().getWorldMapInstance().getNpc(282008);
		if (cleaveArmor != null) {
			cleaveArmor.getController().onDelete();
		}
		Npc accessoryDestruction = getPosition().getWorldMapInstance().getNpc(282009);
		if (accessoryDestruction != null) {
			accessoryDestruction.getController().onDelete();
		}
	}
	
	private void check() {
		despawnDestroyer();
		if (getPosition().isSpawned() && !isAlreadyDead() && !isHome.get()) {
			int spawnId = 0;
			switch (Rnd.get(1, 2)) {
				case 1:
					spawnId = 282008;
				break;
				case 2:
					spawnId = 282009;
				break;
			} switch (Rnd.get(1, 11)) {
                case 1:
                    spawn(spawnId, 266.706848f, 680.673279f, 1174.000000f, (byte) 0);
                break;
				case 2:
                    spawn(spawnId, 292.024658f, 719.713196f, 1174.000000f, (byte) 0);
                break;
				case 3:
                    spawn(spawnId, 263.433411f, 716.730042f, 1174.000000f, (byte) 0);
                break;
                case 4:
                    spawn(spawnId, 292.02466f, 719.7132f, 1169.3982f, (byte) 0);
                break;
                case 5:
                    spawn(spawnId, 263.4334f, 716.73004f, 1170.3693f, (byte) 0);
                break;
				case 6:
                    spawn(spawnId, 267.04f, 680.795f, 1167.27f, (byte) 119);
                break;
				case 7:
                    spawn(spawnId, 263.738f, 716.57f, 1170.34f, (byte) 33);
                break;
				case 8:
                    spawn(spawnId, 292.688f, 719.164f, 1169.33f, (byte) 22);
                break;
				case 9:
                    spawn(spawnId, 235.886f, 708.13f, 1170.82f, (byte) 48);
                break;
				case 10:
                    spawn(spawnId, 262.973f, 716.379f, 1170.29f, (byte) 7);
                break;
				case 11:
                    spawn(spawnId, 267.04f, 680.795f, 1167.27f, (byte) 119);
                break;
			}
		}
		doSchedule();
	}
}