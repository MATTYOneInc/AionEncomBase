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
package ai.instance.elementisForest;

import ai.AggressiveNpcAI2;
import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import java.util.concurrent.Future;

/**
 * @author Luzien
 */
@AIName("canyonfragment")
public class CanyonFragmentAI2 extends AggressiveNpcAI2 {

	private Future<?> task;

	@Override
	public boolean canThink() {
		return false;
	}

	@Override
	public void handleSpawned() {
		super.handleSpawned();
		schedule();
	}

	@Override
	public void handleDied() {
		super.handleDied();
		if (!task.isDone()) {
			task.cancel(false);
		}
	}

	private void schedule() {
		task = ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!isAlreadyDead()) {
					spawn(282430, getPosition().getX(), getPosition().getY(), getPosition().getZ(), (byte) 0);
					AI2Actions.deleteOwner(CanyonFragmentAI2.this);
				}
			}
		}, 25000);
	}
}