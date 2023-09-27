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

@XmlRootElement(name = "anoha_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class AnohaSchedule
{
	@XmlElement(name = "anoha", required = true)
	private List<Anoha> anohasList;
	
	public List<Anoha> getAnohasList() {
		return anohasList;
	}
	
	public void setBerserksList(List<Anoha> anohaList) {
		this.anohasList = anohaList;
	}
	
	public static AnohaSchedule load() {
		AnohaSchedule as;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/anoha_schedule.xml"));
			as = (AnohaSchedule) JAXBUtil.deserialize(xml, AnohaSchedule.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize anoha", e);
		}
		return as;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "anoha")
	public static class Anoha {
		@XmlAttribute(required = true)
		private int id;
		
		@XmlElement(name = "berserkTime", required = true)
		private List<String> berserkTimes;
		
		public int getId() {
			return id;
		}
		
		public void setId(int id) {
			this.id = id;
		}
		
		public List<String> getBerserkTimes() {
			return berserkTimes;
		}
		
		public void setBerserkTimes(List<String> berserkTimes) {
			this.berserkTimes = berserkTimes;
		}
	}
}