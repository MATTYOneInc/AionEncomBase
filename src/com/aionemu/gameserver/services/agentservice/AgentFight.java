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
package com.aionemu.gameserver.services.agentservice;

import com.aionemu.gameserver.model.agent.AgentLocation;
import com.aionemu.gameserver.model.agent.AgentStateType;
import com.aionemu.gameserver.services.AgentService;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Rinzler (Encom)
 */

public abstract class AgentFight<AL extends AgentLocation>
{
	private boolean started;
	private final AL agentLocation;
	protected abstract void stopAgentFight();
	protected abstract void startAgentFight();
	private final AtomicBoolean finished = new AtomicBoolean();
	
	public AgentFight(AL agentLocation) {
		this.agentLocation = agentLocation;
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
		startAgentFight();
	}
	
	public final void stop() {
		if (finished.compareAndSet(false, true)) {
			stopAgentFight();
		}
	}
	
	protected void spawn(AgentStateType type) {
		AgentService.getInstance().spawn(getAgentLocation(), type);
	}
	
	protected void despawn() {
		AgentService.getInstance().despawn(getAgentLocation());
	}
	
	public boolean isFinished() {
		return finished.get();
	}
	
	public AL getAgentLocation() {
		return agentLocation;
	}
	
	public int getAgentLocationId() {
		return agentLocation.getId();
	}
}