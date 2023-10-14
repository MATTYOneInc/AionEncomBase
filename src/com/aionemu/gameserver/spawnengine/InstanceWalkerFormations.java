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
package com.aionemu.gameserver.spawnengine;

import static ch.lambdaj.Lambda.by;
import static ch.lambdaj.Lambda.group;
import static ch.lambdaj.Lambda.on;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.lambdaj.group.Group;

/**
 * @author Rolandas
 */
public class InstanceWalkerFormations {

	private static final Logger log = LoggerFactory.getLogger(InstanceWalkerFormations.class);

	private Map<String, List<ClusteredNpc>> groupedSpawnObjects;
	private Map<String, WalkerGroup> walkFormations;

	public InstanceWalkerFormations() {
		groupedSpawnObjects = new HashMap<String, List<ClusteredNpc>>();
		walkFormations = new HashMap<String, WalkerGroup>();
	}

	public WalkerGroup getSpawnWalkerGroup(String walkerId) {
		return walkFormations.get(walkerId);
	}

	protected synchronized boolean cacheWalkerCandidate(ClusteredNpc npcWalker) {
		String walkerId = npcWalker.getWalkTemplate().getRouteId();
		List<ClusteredNpc> candidateList = groupedSpawnObjects.get(walkerId);
		if (candidateList == null) {
			candidateList = new ArrayList<ClusteredNpc>();
			groupedSpawnObjects.put(walkerId, candidateList);
		}
		return candidateList.add(npcWalker);
	}

	/**
	 * Organizes spawns in all processed walker groups. Must be called only when
	 * spawning all npcs for the instance of world.
	 */
	protected void organizeAndSpawn() {
		for (List<ClusteredNpc> candidates : groupedSpawnObjects.values()) {
			Group<ClusteredNpc> bySize = group(candidates, by(on(ClusteredNpc.class).getPositionHash()));
			Set<String> keys = bySize.keySet();
			int maxSize = 0;
			List<ClusteredNpc> npcs = null;
			for (String key : keys) {
				if (bySize.find(key).size() > maxSize) {
					npcs = bySize.find(key);
					maxSize = npcs.size();
				}
			}
			if (maxSize == 1) {
				for (ClusteredNpc snpc : candidates) {
					snpc.spawn(snpc.getNpc().getSpawn().getZ());
				}
			} else {
				WalkerGroup wg = new WalkerGroup(npcs);
				if (candidates.get(0).getWalkTemplate().getPool() != candidates.size()) {
					log.warn("Incorrect pool for route: " + candidates.get(0).getWalkTemplate().getRouteId());
				}
				wg.form();
				wg.spawn();
				walkFormations.put(candidates.get(0).getWalkTemplate().getRouteId(), wg);
				// spawn the rest which didn't have the same coordinates
				for (ClusteredNpc snpc : candidates) {
					if (npcs.contains(snpc)) {
						continue;
					}
					snpc.spawn(snpc.getNpc().getZ());
				}
			}
		}
	}

	protected synchronized void onInstanceDestroy() {
		groupedSpawnObjects.clear();
		walkFormations.clear();
	}
}