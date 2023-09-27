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
package com.aionemu.gameserver.configs.schedule;

import com.aionemu.commons.utils.xml.JAXBUtil;
import org.apache.commons.io.FileUtils;

import javax.xml.bind.annotation.*;
import java.io.File;
import java.util.List;

/**
 * @author Rinzler (Encom)
 */

@XmlRootElement(name = "agent_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class AgentSchedule
{
	@XmlElement(name = "agent", required = true)
	private List<Agent> agentsList;
	
	public List<Agent> getAgentsList() {
		return agentsList;
	}
	
	public void setFightsList(List<Agent> agentList) {
		this.agentsList = agentList;
	}
	
	public static AgentSchedule load() {
		AgentSchedule as;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/agent_schedule.xml"));
			as = (AgentSchedule) JAXBUtil.deserialize(xml, AgentSchedule.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize agent", e);
		}
		return as;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "agent")
	public static class Agent {
		@XmlAttribute(required = true)
		private int id;
		
		@XmlElement(name = "fightTime", required = true)
		private List<String> fightTimes;
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public List<String> getFightTimes() {
			return fightTimes;
		}
		
		public void setFightTimes(List<String> fightTimes) {
			this.fightTimes = fightTimes;
		}
	}
}