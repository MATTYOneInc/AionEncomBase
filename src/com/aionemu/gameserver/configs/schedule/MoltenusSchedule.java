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

@XmlRootElement(name = "moltenus_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class MoltenusSchedule
{
	@XmlElement(name = "moltenus", required = true)
	private List<Moltenus> moltenussList;
	
	public List<Moltenus> getMoltenussList() {
		return moltenussList;
	}
	
	public void setFightsList(List<Moltenus> moltenusList) {
		this.moltenussList = moltenusList;
	}
	
	public static MoltenusSchedule load() {
		MoltenusSchedule ms;
		try {
			String xml = FileUtils.readFileToString(new File("./config/schedule/moltenus_schedule.xml"));
			ms = (MoltenusSchedule) JAXBUtil.deserialize(xml, MoltenusSchedule.class);
		} catch (Exception e) {
			throw new RuntimeException("Failed to initialize moltenus", e);
		}
		return ms;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "moltenus")
	public static class Moltenus {
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