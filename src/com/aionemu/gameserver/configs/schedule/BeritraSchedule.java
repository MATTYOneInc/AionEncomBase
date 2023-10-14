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

@XmlRootElement(name = "beritra_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class BeritraSchedule {
	@XmlElement(name = "beritra", required = true)
	private List<Beritra> beritrasList;

	public List<Beritra> getBeritrasList() {
		return beritrasList;
	}

	public void setInvasionsList(List<Beritra> beritraList) {
		this.beritrasList = beritraList;
	}

	public static BeritraSchedule load() {
		BeritraSchedule bs;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/beritra_schedule.xml"));
			bs = (BeritraSchedule) JAXBUtil.deserialize(xml, BeritraSchedule.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize beritra", e);
		}
		return bs;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "beritra")
	public static class Beritra {
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