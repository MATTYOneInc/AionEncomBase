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
package com.aionemu.gameserver.services.instanceriftservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.instancerift.InstanceRiftLocation;
import com.aionemu.gameserver.model.instancerift.InstanceRiftStateType;
import com.aionemu.gameserver.services.InstanceRiftService;

/**
 * @author Rinzler (Encom)
 */

public abstract class RiftInstance<RL extends InstanceRiftLocation>
{
	private boolean started;
	private final RL instanceRiftLocation;
	protected abstract void stopInstanceRift();
	protected abstract void startInstanceRift();
	private final AtomicBoolean closed = new AtomicBoolean();
	
	public RiftInstance(RL instanceRiftLocation) {
		this.instanceRiftLocation = instanceRiftLocation;
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
		startInstanceRift();
	}
	
	public final void stop() {
		if (closed.compareAndSet(false, true)) {
			stopInstanceRift();
		}
	}
	
	protected void spawn(InstanceRiftStateType type) {
		InstanceRiftService.getInstance().spawn(getInstanceRiftLocation(), type);
	}
	
	protected void despawn() {
		InstanceRiftService.getInstance().despawn(getInstanceRiftLocation());
	}
	
	public boolean isClosed() {
		return closed.get();
	}
	
	public RL getInstanceRiftLocation() {
		return instanceRiftLocation;
	}
	
	public int getInstanceRiftLocationId() {
		return instanceRiftLocation.getId();
	}
}