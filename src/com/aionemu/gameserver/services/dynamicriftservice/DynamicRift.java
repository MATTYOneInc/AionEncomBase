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
package com.aionemu.gameserver.services.dynamicriftservice;

import com.aionemu.gameserver.model.dynamicrift.DynamicRiftLocation;
import com.aionemu.gameserver.model.dynamicrift.DynamicRiftStateType;
import com.aionemu.gameserver.services.DynamicRiftService;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rinzler (Encom)
 */

public abstract class DynamicRift<DL extends DynamicRiftLocation>
{
	private boolean started;
	private final DL dynamicRiftLocation;
	protected abstract void stopDynamicRift();
	protected abstract void startDynamicRift();
	private final AtomicBoolean closed = new AtomicBoolean();
	
	public DynamicRift(DL dynamicRiftLocation) {
		this.dynamicRiftLocation = dynamicRiftLocation;
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
		startDynamicRift();
	}
	
	public final void stop() {
		if (closed.compareAndSet(false, true)) {
			stopDynamicRift();
		}
	}
	
	protected void spawn(DynamicRiftStateType type) {
		DynamicRiftService.getInstance().spawn(getDynamicRiftLocation(), type);
	}
	
	protected void despawn() {
		DynamicRiftService.getInstance().despawn(getDynamicRiftLocation());
	}
	
	public boolean isClosed() {
		return closed.get();
	}
	
	public DL getDynamicRiftLocation() {
		return dynamicRiftLocation;
	}
	
	public int getDynamicRiftLocationId() {
		return dynamicRiftLocation.getId();
	}
}