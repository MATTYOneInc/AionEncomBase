/*

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
package com.aionemu.gameserver.services.towerofeternityservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.towerofeternity.TowerOfEternityLocation;
import com.aionemu.gameserver.model.towerofeternity.TowerOfEternityStateType;
import com.aionemu.gameserver.services.TowerOfEternityService;

/**
 * Created by Wnkrz on 22/08/2017.
 */

public abstract class TowerOfEternity<TE extends TowerOfEternityLocation> {
	private boolean started;
	private final TE towerOfEternityLocation;

	protected abstract void stopTowerOfEternity();

	protected abstract void startTowerOfEternity();

	private final AtomicBoolean closed = new AtomicBoolean();

	public TowerOfEternity(TE towerOfEternityLocation) {
		this.towerOfEternityLocation = towerOfEternityLocation;
	}

	public final void start() {
		boolean doubleStart = false;
		synchronized (this) {
			if (started) {
				doubleStart = true;
			} else {
				started = true;
			}
		}
		if (doubleStart) {
			return;
		}
		startTowerOfEternity();
	}

	public final void stop() {
		if (closed.compareAndSet(false, true)) {
			stopTowerOfEternity();
		}
	}

	protected void spawn(TowerOfEternityStateType type) {
		TowerOfEternityService.getInstance().spawn(getTowerOfEternityLocation(), type);
	}

	protected void despawn() {
		TowerOfEternityService.getInstance().despawn(getTowerOfEternityLocation());
	}

	public boolean isClosed() {
		return closed.get();
	}

	public TE getTowerOfEternityLocation() {
		return towerOfEternityLocation;
	}

	public int getTowerOfEternityLocationId() {
		return towerOfEternityLocation.getId();
	}
}