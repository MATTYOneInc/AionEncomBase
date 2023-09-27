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

@XmlRootElement(name = "circus_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class CircusSchedule
{
	@XmlElement(name = "circus", required = true)
	private List<Circus> circussList;
	
	public List<Circus> getCircussList() {
		return circussList;
	}
	
	public void setCircussList(List<Circus> circusList) {
		this.circussList = circusList;
	}
	
	public static CircusSchedule load() {
		CircusSchedule cs;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/circus_schedule.xml"));
			cs = (CircusSchedule) JAXBUtil.deserialize(xml, CircusSchedule.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize circus", e);
		}
		return cs;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "circus")
	public static class Circus {
		@XmlAttribute(required = true)
		private int id;
		
		@XmlElement(name = "circusTime", required = true)
		private List<String> circusTimes;
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public List<String> getCircusTimes() {
			return circusTimes;
		}
		
		public void setCircusTimes(List<String> circusTimes) {
			this.circusTimes = circusTimes;
		}
	}
}