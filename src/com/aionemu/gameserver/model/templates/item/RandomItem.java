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
package com.aionemu.gameserver.model.templates.item;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;

/**
 * @author vlog
 */
@XmlType(name = "RandomItem")
public class RandomItem {

	@XmlAttribute(name = "type")
	protected RandomType type;
	@XmlAttribute(name = "count")
	protected int count;

	@XmlAttribute(name = "rnd_min")
	public int rndMin;

	@XmlAttribute(name = "rnd_max")
	public int rndMax;

	public int getCount() {
		return count;
	}

	public RandomType getType() {
		return type;
	}

	public int getRndMin() {
		return rndMin;
	}

	public int getRndMax() {
		return rndMax;
	}

	public final int getResultCount() {
		if ((count == 0) && (rndMin == 0) && (rndMax == 0)) {
			return 1;
		}
		if ((rndMin > 0) || (rndMax > 0)) {
			if (rndMax < rndMin) {
				LoggerFactory.getLogger(RandomItem.class).warn("Wrong rnd result item definition {} {}", rndMin, rndMax);
				return 1;
			}
			return Rnd.get(rndMin, rndMax);
		}
		return count;
	}
}