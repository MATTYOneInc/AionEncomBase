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

import java.io.File;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.aionemu.commons.utils.xml.JAXBUtil;

/**
 * @author Rinzler (Encom)
 */

@XmlRootElement(name = "instance_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class InstanceSchedule
{
	@XmlElement(name = "instance", required = true)
	private List<Instance> instancesList;
	
	public List<Instance> getInstancesList() {
		return instancesList;
	}
	
	public void setInstancesList(List<Instance> instanceList) {
		this.instancesList = instanceList;
	}
	
	public static InstanceSchedule load() {
		InstanceSchedule is;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/instance_schedule.xml"));
			is = (InstanceSchedule) JAXBUtil.deserialize(xml, InstanceSchedule.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize instance", e);
		}
		return is;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "instance")
	public static class Instance {
		@XmlAttribute(required = true)
		private int id;
		
		@XmlElement(name = "instanceTime", required = true)
		private List<String> instanceTimes;
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public List<String> getInstanceTimes() {
			return instanceTimes;
		}
		
		public void setInstanceTimes(List<String> instanceTimes) {
			this.instanceTimes = instanceTimes;
		}
	}
}