/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.services.anohaservice;

import com.aionemu.gameserver.model.anoha.AnohaLocation;
import com.aionemu.gameserver.model.anoha.AnohaStateType;
import com.aionemu.gameserver.services.AnohaService;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rinzler (Encom)
 */

public abstract class BerserkAnoha<AL extends AnohaLocation>
{
	private boolean started;
	private final AL anohaLocation;
	protected abstract void stopAnoha();
	protected abstract void startAnoha();
	private final AtomicBoolean finished = new AtomicBoolean();
	
	public BerserkAnoha(AL anohaLocation) {
		this.anohaLocation = anohaLocation;
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
		startAnoha();
	}
	
	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			stopAnoha();
		}
	}
	
	protected void spawn(AnohaStateType type) {
		AnohaService.getInstance().spawn(getAnohaLocation(), type);
	}
	
	protected void despawn() {
		AnohaService.getInstance().despawn(getAnohaLocation());
	}
	
	public boolean isFinished() {
		return finished.get();
	}
	
	public AL getAnohaLocation() {
		return anohaLocation;
	}
	
	public int getAnohaLocationId() {
		return anohaLocation.getId();
	}
}