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
package com.aionemu.gameserver.world.container;

import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.world.exceptions.DuplicateAionObjectException;
import javolution.util.FastList;
import javolution.util.FastMap;

import java.util.Iterator;
import java.util.Map;

public class LegionContainer implements Iterable<Legion>
{
	private final Map<Integer, Legion> legionsById = new FastMap<Integer, Legion>().shared();
	private final Map<String, Legion> legionsByName = new FastMap<String, Legion>().shared();
	
	public void add(Legion legion) {
		if (legion == null || legion.getLegionName() == null) {
			return;
		} if (legionsById.put(legion.getLegionId(), legion) != null) {
			throw new DuplicateAionObjectException();
		} if (legionsByName.put(legion.getLegionName().toLowerCase(), legion) != null) {
			throw new DuplicateAionObjectException();
		}
	}
	
	public void remove(Legion legion) {
		legionsById.remove(legion.getLegionId());
		legionsByName.remove(legion.getLegionName().toLowerCase());
	}
	
	public Legion get(int legionId) {
		return legionsById.get(legionId);
	}
	
	public Legion get(String name) {
		return legionsByName.get(name.toLowerCase());
	}
	
	public FastList<Legion> getAllLegions() {
    	FastList<Legion> list = new FastList<Legion>();
    	list.addAll(legionsByName.values());
    	return list;
    }
	
	public boolean contains(int legionId) {
		return legionsById.containsKey(legionId);
	}
	
	public boolean contains(String name) {
		return legionsByName.containsKey(name.toLowerCase());
	}
	
	@Override
	public Iterator<Legion> iterator() {
		return legionsById.values().iterator();
	}
	
	public void clear() {
		legionsById.clear();
		legionsByName.clear();
	}
}