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
package com.aionemu.gameserver.services.idiandepthsservice;

import com.aionemu.gameserver.model.idiandepths.IdianDepthsLocation;
import com.aionemu.gameserver.model.idiandepths.IdianDepthsStateType;
import com.aionemu.gameserver.services.IdianDepthsService;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rinzler (Encom)
 */

public abstract class IdianDepths<IL extends IdianDepthsLocation>
{
	private boolean started;
	private final IL idianDepthsLocation;
	protected abstract void stopIdianDepths();
	protected abstract void startIdianDepths();
	private final AtomicBoolean closed = new AtomicBoolean();
	
	public IdianDepths(IL idianDepthsLocation) {
		this.idianDepthsLocation = idianDepthsLocation;
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
		startIdianDepths();
	}
	
	public final void stop() {
		if (closed.compareAndSet(false, true)) {
			stopIdianDepths();
		}
	}
	
	protected void spawn(IdianDepthsStateType type) {
		IdianDepthsService.getInstance().spawn(getIdianDepthsLocation(), type);
	}
	
	protected void despawn() {
		IdianDepthsService.getInstance().despawn(getIdianDepthsLocation());
	}
	
	public boolean isClosed() {
		return closed.get();
	}
	
	public IL getIdianDepthsLocation() {
		return idianDepthsLocation;
	}
	
	public int getIdianDepthsLocationId() {
		return idianDepthsLocation.getId();
	}
}