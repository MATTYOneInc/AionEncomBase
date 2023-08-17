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
package com.aionemu.gameserver.model.templates.spawns;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "spawns" })
@XmlRootElement(name = "house")
public class HouseSpawns implements Comparable<HouseSpawns> {

	@XmlElement(name = "spawn", required = true)
	protected List<HouseSpawn> spawns;

	@XmlAttribute(name = "address", required = true)
	protected int address;

	public List<HouseSpawn> getSpawns() {
		if (spawns == null) {
			spawns = new ArrayList<HouseSpawn>();
		}
		return spawns;
	}

	public int getAddress() {
		return address;
	}

	public void setAddress(int value) {
		address = value;
	}

	@Override
	public int compareTo(HouseSpawns o) {
		return o.address - address;
	}
}