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
package com.aionemu.gameserver.services.agentservice;

import com.aionemu.gameserver.model.agent.AgentLocation;
import com.aionemu.gameserver.model.agent.AgentStateType;

/**
 * @author Rinzler (Encom)
 */

public class Fight extends AgentFight<AgentLocation>
{
	public Fight(AgentLocation agent) {
		super(agent);
	}
	
	@Override
	public void startAgentFight() {
		getAgentLocation().setActiveAgent(this);
		despawn();
		spawn(AgentStateType.FIGHT);
	}
	
	@Override
	public void stopAgentFight() {
		getAgentLocation().setActiveAgent(null);
		despawn();
		spawn(AgentStateType.PEACE);
	}
}