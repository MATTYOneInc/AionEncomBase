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
package com.aionemu.gameserver.services.svsservice;

import java.util.Map;

import com.aionemu.gameserver.model.svs.SvsLocation;
import com.aionemu.gameserver.services.SvsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Rinzler (Encom)
 */

public class SvsStartRunnable implements Runnable {
	private final int id;

	public SvsStartRunnable(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		// Advance Corridor [Transidium Annex].
		SvsService.getInstance().transidiumAnnexMsg(id);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				// Advance Corridor [Transidium Annex].
				SvsService.getInstance().advanceCorridorSP(id);
			}
		}, 480000);
		Map<Integer, SvsLocation> locations = SvsService.getInstance().getSvsLocations();
		for (final SvsLocation loc : locations.values()) {
			if (loc.getId() == id) {
				SvsService.getInstance().startSvs(loc.getId());
			}
		}
	}
}