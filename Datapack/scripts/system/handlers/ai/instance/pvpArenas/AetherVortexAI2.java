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
package ai.instance.pvpArenas;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.skillengine.SkillEngine;

import java.util.concurrent.Future;

/****/
/** Author (Encom)
/****/

@AIName("aether_vortex")
public class AetherVortexAI2 extends AggressiveNpcAI2
{
	private Future<?> eventTask;
	
	@Override
	protected void handleSpawned() {
		super.handleSpawned();
		startEventTask();
	}
	
	@Override
	protected void handleDied() {
		cancelEventTask();
		super.handleDied();
	}
	
	@Override
	protected void handleDespawned() {
		cancelEventTask();
		super.handleDespawned();
	}
	
	private void cancelEventTask() {
		if (eventTask != null &&
		   !eventTask.isDone()) {
			eventTask.cancel(true);
		}
	}
	
	private void startEventTask() {
		eventTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (isAlreadyDead()) {
					cancelEventTask();
				} else {
					SkillEngine.getInstance().getSkill(getOwner(), 20059, 60, getOwner()).useNoAnimationSkill();
				}
			}
		}, 1000, 1000);
	}
}