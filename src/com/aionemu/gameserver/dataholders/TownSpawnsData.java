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

import com.aionemu.gameserver.model.templates.spawns.Spawn;
import com.aionemu.gameserver.model.templates.towns.TownLevel;
import com.aionemu.gameserver.model.templates.towns.TownSpawn;
import com.aionemu.gameserver.model.templates.towns.TownSpawnMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "town_spawns_data")
public class TownSpawnsData
{
	@XmlElement(name = "spawn_map")
	private List<TownSpawnMap> spawnMap;
	
	private TIntObjectHashMap<TownSpawnMap> spawnMapsData = new TIntObjectHashMap<TownSpawnMap>();
	
	void afterUnmarshal(Unmarshaller u, Object parent) {
		spawnMapsData.clear();
		for (TownSpawnMap map : spawnMap) {
			spawnMapsData.put(map.getMapId(), map);
		}
		spawnMap.clear();
		spawnMap = null;
	}
	
	public int getSpawnsCount() {
		int counter = 0;
		for (TownSpawnMap spawnMap : spawnMapsData.valueCollection()) {
			for (TownSpawn townSpawn : spawnMap.getTownSpawns()) {
				for (TownLevel townLevel : townSpawn.getTownLevels()) {
					counter+= townLevel.getSpawns().size();
				}
			}
		}
		return counter;
	}
	
	public List<Spawn> getSpawns(int townId, int townLevel) {
		for(TownSpawnMap spawnMap : spawnMapsData.valueCollection()) {
			if(spawnMap.getTownSpawn(townId) != null) {
				TownSpawn townSpawn = spawnMap.getTownSpawn(townId);
				return townSpawn.getSpawnsForLevel(townLevel).getSpawns();
			}
		}
		return null;
	}

	public int getWorldIdForTown(int townId) {
		for(TownSpawnMap spawnMap : spawnMapsData.valueCollection())
			if(spawnMap.getTownSpawn(townId) != null) {
				return spawnMap.getMapId();
			}
		return 0;
	}
}