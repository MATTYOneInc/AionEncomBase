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
package com.aionemu.gameserver.services.conquestservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.callbacks.EnhancedObject;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.conquest.ConquestLocation;
import com.aionemu.gameserver.model.conquest.ConquestStateType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.services.ConquestService;

/**
 * @author Rinzler (Encom)
 */

public abstract class ConquestOffering<CL extends ConquestLocation> {
	private boolean started;
	private Npc conquestBoss;
	private final CL conquestLocation;
	private boolean conquestBossDestroyed;

	protected abstract void stopConquest();

	protected abstract void startConquest();

	private final AtomicBoolean finished = new AtomicBoolean();
	private final ConquestBossDestroyListener conquestBossDestroyListener = new ConquestBossDestroyListener(this);

	public ConquestOffering(CL conquestLocation) {
		this.conquestLocation = conquestLocation;
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
		startConquest();
	}

	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			stopConquest();
		}
	}

	protected void initConquestBoss() {
		Npc cb = null;
		for (VisibleObject obj : getConquestLocation().getSpawned()) {
			int npcId = ((Npc) obj).getNpcId();
			// Conquest/Offering Inggison.
			if ((npcId < 236530) || npcId > 236553) {
				cb = (Npc) obj;
			}
			// Conquest/Offering Gelkmaros.
			else if ((npcId < 236586) || npcId > 236609) {
				cb = (Npc) obj;
			}
		}
		if (cb == null) {
			throw new NullPointerException("No <Conquest/Offering Boss> was found in loc:" + getConquestLocationId());
		}
		setConquestBoss(cb);
		addConquestBossListeners();
	}

	protected void spawn(ConquestStateType type) {
		ConquestService.getInstance().spawn(getConquestLocation(), type);
	}

	protected void despawn() {
		ConquestService.getInstance().despawn(getConquestLocation());
	}

	protected void addConquestBossListeners() {
		AbstractAI ai = (AbstractAI) getConquestBoss().getAi2();
		EnhancedObject eo = (EnhancedObject) ai;
		eo.addCallback(getConquestBossDestroyListener());
	}

	protected void rmvConquestBossListener() {
		AbstractAI ai = (AbstractAI) getConquestBoss().getAi2();
		EnhancedObject eo = (EnhancedObject) ai;
		eo.removeCallback(getConquestBossDestroyListener());
	}

	public boolean isConquestBossDestroyed() {
		return conquestBossDestroyed;
	}

	public void setConquestBossDestroyed(boolean state) {
		this.conquestBossDestroyed = state;
	}

	public Npc getConquestBoss() {
		return conquestBoss;
	}

	public void setConquestBoss(Npc conquestBoss) {
		this.conquestBoss = conquestBoss;
	}

	public ConquestBossDestroyListener getConquestBossDestroyListener() {
		return conquestBossDestroyListener;
	}

	public boolean isFinished() {
		return finished.get();
	}

	public CL getConquestLocation() {
		return conquestLocation;
	}

	public int getConquestLocationId() {
		return conquestLocation.getId();
	}
}