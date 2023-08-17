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
package com.aionemu.gameserver.spawnengine;

import com.aionemu.commons.utils.internal.chmv8.PlatformDependent;

import java.util.Map;

/**
 * @author Rolandas
 */
class WalkerFormationsCache {

	private static Map<Integer, WorldWalkerFormations> formations = PlatformDependent.newConcurrentHashMap();

	private WalkerFormationsCache() {
	}

	protected static InstanceWalkerFormations getInstanceFormations(int worldId, int instanceId) {
		WorldWalkerFormations wwf = formations.get(worldId);
		if (wwf == null) {
			wwf = new WorldWalkerFormations();
			formations.put(worldId, wwf);
		}
		return wwf.getInstanceFormations(instanceId);
	}

	protected static void onInstanceDestroy(int worldId, int instanceId) {
		getInstanceFormations(worldId, instanceId).onInstanceDestroy();
	}
}