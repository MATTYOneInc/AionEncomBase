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
package com.aionemu.gameserver.dataholders;

import com.aionemu.gameserver.skillengine.model.MotionTime;
import gnu.trove.map.hash.THashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kecimis
 *
 */
@XmlRootElement(name = "motion_times")
@XmlAccessorType(XmlAccessType.FIELD)
public class MotionData {

	@XmlElement(name = "motion_time")
	protected List<MotionTime> motionTimes;

	@XmlTransient
	private THashMap<String, MotionTime> motionTimesMap = new THashMap<String, MotionTime>();

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (MotionTime motion : motionTimes) {
			motionTimesMap.put(motion.getName(), motion);
		}
	}

	/**
	 * @return the motionTimeList
	 */
	public List<MotionTime> getMotionTimes() {
		if (motionTimes == null) {
			motionTimes = new ArrayList<MotionTime>();
		}
		return motionTimes;
	}

	public MotionTime getMotionTime(String name) {
		return motionTimesMap.get(name);
	}

	public int size() {
		if (motionTimes == null) {
			return 0;
		}
		return motionTimes.size();
	}
}