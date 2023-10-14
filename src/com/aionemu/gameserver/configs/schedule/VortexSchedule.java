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

@XmlRootElement(name = "vortex_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class VortexSchedule
{
	@XmlElement(name = "vortex", required = true)
	private List<Vortex> vortexsList;
	
	public List<Vortex> getVortexsList() {
		return vortexsList;
	}
	
	public void setInvasionsList(List<Vortex> vortexList) {
		this.vortexsList = vortexList;
	}
	
	public static VortexSchedule load() {
		VortexSchedule vs;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/vortex_schedule.xml"));
			vs = (VortexSchedule) JAXBUtil.deserialize(xml, VortexSchedule.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize vortex", e);
		}
		return vs;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "vortex")
	public static class Vortex {
		@XmlAttribute(required = true)
		private int id;
		
		@XmlElement(name = "invasionTime", required = true)
		private List<String> invasionTimes;
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public List<String> getInvasionTimes() {
			return invasionTimes;
		}
		
		public void setInvasionTimes(List<String> invasionTimes) {
			this.invasionTimes = invasionTimes;
		}
	}
}