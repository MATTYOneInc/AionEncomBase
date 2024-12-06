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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aionemu.gameserver.model.templates.event.AtreianPassport;

/**
 * 
 * @author Ranastic
 *
 */
public class PlayerPassports {

	private final SortedMap<Integer, AtreianPassport> passports = new TreeMap<Integer, AtreianPassport>();

	public synchronized boolean addPassport(int id, AtreianPassport ap) {
		if (passports.containsKey(id)) {
			return false;
		}
		passports.put(id, ap);
		return true;
	}

	public synchronized boolean removePassport(int id) {
		if (passports.containsKey(id)) {
			passports.remove(id);
			return true;
		}
		return false;
	}

	public Collection<AtreianPassport> getAllPassports() {
		return passports.values();
	}
}