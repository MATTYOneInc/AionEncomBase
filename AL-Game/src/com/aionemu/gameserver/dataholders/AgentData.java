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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.agent.AgentLocation;
import com.aionemu.gameserver.model.templates.agent.AgentTemplate;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "agent_fight")
public class AgentData {
	@XmlElement(name = "agent_location")
	private List<AgentTemplate> agentTemplates;

	@XmlTransient
	private FastMap<Integer, AgentLocation> agent = new FastMap<Integer, AgentLocation>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (AgentTemplate template : agentTemplates) {
			agent.put(template.getId(), new AgentLocation(template));
		}
	}

	public int size() {
		return agent.size();
	}

	public FastMap<Integer, AgentLocation> getAgentLocations() {
		return agent;
	}
}