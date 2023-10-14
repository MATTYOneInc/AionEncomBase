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
package com.aionemu.gameserver.services.zorshivdredgionservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionLocation;
import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionStateType;
import com.aionemu.gameserver.services.ZorshivDredgionService;

/**
 * @author Rinzler (Encom)
 */

public abstract class ZorshivDredgion<ZL extends ZorshivDredgionLocation> {
	private boolean started;
	private final ZL zorshivDredgionLocation;

	protected abstract void stopZorshivDredgion();

	protected abstract void startZorshivDredgion();

	private final AtomicBoolean peace = new AtomicBoolean();

	public ZorshivDredgion(ZL zorshivDredgionLocation) {
		this.zorshivDredgionLocation = zorshivDredgionLocation;
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
		startZorshivDredgion();
	}

	public final void stop() {
		if (peace.compareAndSet(false, true)) {
			stopZorshivDredgion();
		}
	}

	protected void spawn(ZorshivDredgionStateType type) {
		ZorshivDredgionService.getInstance().spawn(getZorshivDredgionLocation(), type);
	}

	protected void despawn() {
		ZorshivDredgionService.getInstance().despawn(getZorshivDredgionLocation());
	}

	public boolean isPeace() {
		return peace.get();
	}

	public ZL getZorshivDredgionLocation() {
		return zorshivDredgionLocation;
	}

	public int getZorshivDredgionLocationId() {
		return zorshivDredgionLocation.getId();
	}
}