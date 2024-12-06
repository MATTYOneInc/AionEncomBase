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

@XmlRootElement(name = "rvr_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class RvrSchedule {
	@XmlElement(name = "rvr", required = true)
	private List<Rvr> rvrsList;

	public List<Rvr> getRvrsList() {
		return rvrsList;
	}

	public void setRvrsList(List<Rvr> rvrList) {
		this.rvrsList = rvrList;
	}

	public static RvrSchedule load() {
		RvrSchedule rs;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/rvr_schedule.xml"));
			rs = (RvrSchedule) JAXBUtil.deserialize(xml, RvrSchedule.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize rvr", e);
		}
		return rs;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "rvr")
	public static class Rvr {
		@XmlAttribute(required = true)
		private int id;

		@XmlElement(name = "rvrTime", required = true)
		private List<String> rvrTimes;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public List<String> getRvrTimes() {
			return rvrTimes;
		}

		public void setRvrTimes(List<String> rvrTimes) {
			this.rvrTimes = rvrTimes;
		}
	}
}