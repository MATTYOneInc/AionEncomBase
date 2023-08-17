/*
 * This file is part of Encom. **ENCOM FUCK OTHER SVN**
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
package com.aionemu.gameserver.model.templates.event;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

import com.aionemu.gameserver.utils.gametime.DateTimeUtil;

/**
 * @author Ghostfur (Aion-Unique)
 */
@XmlRootElement(name = "atreian_passport")
@XmlAccessorType(value = XmlAccessType.NONE)
public class EventsWindow {

	@XmlAttribute(name = "id", required = true)
	private int id;

	@XmlAttribute(name = "item", required = true)
	private int item;

	@XmlAttribute(name = "count", required = true)
	private long count;

	@XmlAttribute(name = "period_start", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar pStart;

	@XmlAttribute(name = "period_end", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar pEnd;

	@XmlAttribute(name = "remaining_time", required = true)
	private int remaining_time;

	@XmlAttribute(name = "min_level", required = true)
	private int min_level;

	@XmlAttribute(name = "max_level", required = true)
	private int max_level;

	@XmlAttribute(name = "dailyMaxCount", required = true)
	private int dailyMaxCount;

	private Timestamp lastStamp;

	public int getId() {
		return id;
	}

	public int getItemId() {
		return item;
	}

	public long getCount() {
		return count;
	}

	public int getMaxCountOfDay() {
		return dailyMaxCount;
	}

	public DateTime getPeriodStart() {
		return DateTimeUtil.getDateTime(pStart.toGregorianCalendar());
	}

	public DateTime getPeriodEnd() {
		return DateTimeUtil.getDateTime(pEnd.toGregorianCalendar());
	}

	public int getRemainingTime() {
		return remaining_time;
	}

	public int getMinLevel() {
		return min_level;
	}

	public int getMaxLevel() {
		return max_level;
	}

	public Timestamp getLastStamp() {
		return lastStamp;
	}

	public void setLastStamp(Timestamp timestamp) {
		lastStamp = timestamp;
	}
}