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
package com.aionemu.gameserver.services.moltenusservice;

import com.aionemu.gameserver.model.moltenus.MoltenusLocation;
import com.aionemu.gameserver.model.moltenus.MoltenusStateType;
import com.aionemu.gameserver.services.MoltenusService;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rinzler (Encom)
 */

public abstract class MoltenusFight<ML extends MoltenusLocation>
{
	private boolean started;
	private final ML moltenusLocation;
	protected abstract void stopMoltenus();
	protected abstract void startMoltenus();
	private final AtomicBoolean finished = new AtomicBoolean();
	
	public MoltenusFight(ML moltenusLocation) {
		this.moltenusLocation = moltenusLocation;
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
		startMoltenus();
	}
	
	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			stopMoltenus();
		}
	}
	
	protected void spawn(MoltenusStateType type) {
		MoltenusService.getInstance().spawn(getMoltenusLocation(), type);
	}
	
	protected void despawn() {
		MoltenusService.getInstance().despawn(getMoltenusLocation());
	}
	
	public boolean isFinished() {
		return finished.get();
	}
	
	public ML getMoltenusLocation() {
		return moltenusLocation;
	}
	
	public int getMoltenusLocationId() {
		return moltenusLocation.getId();
	}
}