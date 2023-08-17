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
package com.aionemu.gameserver.model.gameobjects.player;

import com.aionemu.commons.utils.internal.chmv8.PlatformDependent;

import java.util.Iterator;
import java.util.Map;

public class BlockList implements Iterable<BlockedPlayer>
{
	public static final int MAX_BLOCKS = 10;
	
	private final Map<Integer, BlockedPlayer> blockedList;
	
	public BlockList() {
		this.blockedList = PlatformDependent.newConcurrentHashMap();
	}
	
	public BlockList(Map<Integer, BlockedPlayer> initialList) {
		this.blockedList = PlatformDependent.newConcurrentHashMap(initialList);
	}
	
	public void add(BlockedPlayer plr) {
		blockedList.put(plr.getObjId(), plr);
	}
	
	public void remove(int objIdOfPlayer) {
		blockedList.remove(objIdOfPlayer);
	}
	
	public BlockedPlayer getBlockedPlayer(String name) {
		Iterator<BlockedPlayer> iterator = blockedList.values().iterator();
		while (iterator.hasNext()) {
			BlockedPlayer entry = iterator.next();
			if (entry.getName().equalsIgnoreCase(name)) {
				return entry;
			}
		}
		return null;
	}
	
	public BlockedPlayer getBlockedPlayer(int playerObjId) {
		return blockedList.get(playerObjId);
	}
	
	public boolean contains(int playerObjectId) {
		return blockedList.containsKey(playerObjectId);
	}
	
	public int getSize() {
		return blockedList.size();
	}
	
	public boolean isFull() {
		return getSize() >= MAX_BLOCKS;
	}
	
	@Override
	public Iterator<BlockedPlayer> iterator() {
		return blockedList.values().iterator();
	}
}