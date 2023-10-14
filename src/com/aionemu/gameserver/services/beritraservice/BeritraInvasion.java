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
package com.aionemu.gameserver.services.beritraservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.beritra.BeritraLocation;
import com.aionemu.gameserver.model.beritra.BeritraStateType;
import com.aionemu.gameserver.services.BeritraService;

/**
 * @author Rinzler (Encom)
 */

public abstract class BeritraInvasion<BL extends BeritraLocation>
{
	private boolean started;
	private final BL beritraLocation;
	protected abstract void stopBeritraInvasion();
	protected abstract void startBeritraInvasion();
	private final AtomicBoolean finished = new AtomicBoolean();
	
	public BeritraInvasion(BL beritraLocation) {
		this.beritraLocation = beritraLocation;
	}
	
	public final void start() {
		boolean doubleStart = false;
		synchronized (this) {
			if (started) {
				doubleStart = true;
			} else {
				started = true;
			}
		} if (doubleStart) {
			return;
		}
		startBeritraInvasion();
	}
	
	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			stopBeritraInvasion();
		}
	}
	
	protected void spawn(BeritraStateType type) {
		BeritraService.getInstance().spawn(getBeritraLocation(), type);
	}
	
	protected void despawn() {
		BeritraService.getInstance().despawn(getBeritraLocation());
	}
	
	public boolean isFinished() {
		return finished.get();
	}
	
	public BL getBeritraLocation() {
		return beritraLocation;
	}
	
	public int getBeritraLocationId() {
		return beritraLocation.getId();
	}
}