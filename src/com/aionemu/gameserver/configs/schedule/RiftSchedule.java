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

@XmlRootElement(name = "rift_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class RiftSchedule
{
	@XmlElement(name = "rift", required = true)
	private List<Rift> riftsList;
	
	public List<Rift> getRiftsList() {
		return riftsList;
	}
	
	public void setRiftsList(List<Rift> riftList) {
		this.riftsList = riftList;
	}
	
	public static RiftSchedule load() {
		RiftSchedule rs;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/rift_schedule.xml"));
			rs = (RiftSchedule) JAXBUtil.deserialize(xml, RiftSchedule.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize rifts", e);
		}
		return rs;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "rift")
	public static class Rift {
		@XmlAttribute(required = true)
		private int id;
		
		@XmlElement(name = "openTime", required = true)
		private List<String> openTimes;
		
		public int getWorldId() {
			return id;
		}
		
		public List<String> getOpenTime() {
			return openTimes;
		}
		
		public void setOpenTimes(List<String> openTimes) {
			this.openTimes = openTimes;
		}
	}
}