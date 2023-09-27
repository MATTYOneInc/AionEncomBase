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
package com.aionemu.gameserver.services.iuservice;

import com.aionemu.gameserver.model.iu.IuLocation;
import com.aionemu.gameserver.model.iu.IuStateType;
import com.aionemu.gameserver.services.IuService;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rinzler (Encom)
 */

public abstract class Iu<IUL extends IuLocation>
{
	private boolean started;
	private final IUL iuLocation;
	protected abstract void stopConcert();
	protected abstract void startConcert();
	private final AtomicBoolean finished = new AtomicBoolean();
	
	public Iu(IUL iuLocation) {
		this.iuLocation = iuLocation;
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
		startConcert();
	}
	
	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			stopConcert();
		}
	}
	
	protected void spawn(IuStateType type) {
		IuService.getInstance().spawn(getIuLocation(), type);
	}
	
	protected void despawn() {
		IuService.getInstance().despawn(getIuLocation());
	}
	
	public boolean isFinished() {
		return finished.get();
	}
	
	public IUL getIuLocation() {
		return iuLocation;
	}
	
	public int getIuLocationId() {
		return iuLocation.getId();
	}
}