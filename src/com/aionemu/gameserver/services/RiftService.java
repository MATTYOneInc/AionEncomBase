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
package com.aionemu.gameserver.services;

import java.util.*;
import javolution.util.FastMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.schedule.RiftSchedule;
import com.aionemu.gameserver.configs.schedule.RiftSchedule.Rift;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.*;
import com.aionemu.gameserver.model.gameobjects.*;
import com.aionemu.gameserver.model.rift.*;
import com.aionemu.gameserver.model.templates.spawns.riftspawns.*;
import com.aionemu.gameserver.services.rift.*;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.*;

/****/
/** Author Rinzler (Encom)
/****/

public class RiftService
{
	private RiftSchedule riftSchedule;
	private Map<Integer, RiftLocation> locations;
	private final Lock closing = new ReentrantLock();
	private static final int duration = CustomConfig.RIFT_DURATION;
	private FastMap<Integer, RiftLocation> activeRifts = new FastMap<Integer, RiftLocation>();
	
	public void initRiftLocations() {
		if (CustomConfig.RIFT_ENABLED) {
			locations = DataManager.RIFT_DATA.getRiftLocations();
		} else {
			locations = Collections.emptyMap();
		}
	}
	
	public void initRifts() {
		if (CustomConfig.RIFT_ENABLED) {
			riftSchedule = RiftSchedule.load();
			for (Rift rift: riftSchedule.getRiftsList()) {
				for (String openTimes: rift.getOpenTime()) {
					CronService.getInstance().schedule(new RiftOpenRunnable(rift.getWorldId()), openTimes);
				}
			}
		}
	}
	
	public boolean isValidId(int id) {
		if (isRift(id)) {
			return RiftService.getInstance().getRiftLocations().keySet().contains(id);
		} else {
			for (RiftLocation loc : RiftService.getInstance().getRiftLocations().values()) {
				if (loc.getWorldId() == id) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isRift(int id) {
		return id < 10000;
	}
	
	public boolean openRifts(int id) {
		if (isValidId(id)) {
			if (isRift(id)) {
				RiftLocation rift = getRiftLocation(id);
				if (rift.getSpawned().isEmpty()) {
					openRifts(rift);
					RiftInformer.sendRiftsInfo(rift.getWorldId());
					return true;
				}
			} else {
				boolean opened = false;
				for (RiftLocation rift : getRiftLocations().values()) {
					if (rift.getWorldId() == id && rift.getSpawned().isEmpty()) {
						openRifts(rift);
						opened = true;
					}
				}
				RiftInformer.sendRiftsInfo(id);
				return opened;
			}
		}
		return false;
	}
	
	public boolean closeRifts(int id) {
		if (isValidId(id)) {
			if (isRift(id)) {
				RiftLocation rift = getRiftLocation(id);
				if (!rift.getSpawned().isEmpty()) {
					closeRift(rift);
					return true;
				}
			} else {
				boolean opened = false;
				for (RiftLocation rift : getRiftLocations().values()) {
					if (rift.getWorldId() == id && !rift.getSpawned().isEmpty()) {
						closeRift(rift);
						opened = true;
					}
				}
				return opened;
			}
		}
		return false;
	}
	
	public void openRifts(RiftLocation location) {
		location.setOpened(true);
		RiftManager.getInstance().spawnRift(location);
		activeRifts.putEntry(location.getId(), location);
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				closeRifts();
			}
		}, duration * 3600 * 1000);
	}
	
	public void closeRift(RiftLocation location) {
		location.setOpened(false);
		for (VisibleObject npc: location.getSpawned()) {
			((Npc) npc).getController().cancelTask(TaskId.RESPAWN);
			npc.getController().onDelete();
		}
		location.getSpawned().clear();
	}
	
	public void closeRifts() {
		closing.lock();
		try {
			for (RiftLocation rift : activeRifts.values()) {
				closeRift(rift);
			}
			activeRifts.clear();
		}
		finally {
			closing.unlock();
		}
	}
	
	public int getDuration() {
		return duration;
	}
	
	public RiftLocation getRiftLocation(int id) {
		return locations.get(id);
	}
	
	public Map<Integer, RiftLocation> getRiftLocations() {
		return locations;
	}
	
	public static RiftService getInstance() {
		return RiftServiceHolder.INSTANCE;
	}
	
	private static class RiftServiceHolder {
		private static final RiftService INSTANCE = new RiftService();
	}
}