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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.schedule.BeritraSchedule;
import com.aionemu.gameserver.configs.schedule.BeritraSchedule.Beritra;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.beritra.BeritraLocation;
import com.aionemu.gameserver.model.beritra.BeritraStateType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.beritraspawns.BeritraSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.beritraservice.BeritraInvasion;
import com.aionemu.gameserver.services.beritraservice.BeritraStartRunnable;
import com.aionemu.gameserver.services.beritraservice.Invade;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

public class BeritraService {
	private BeritraSchedule beritraSchedule;
	private Map<Integer, BeritraLocation> beritra;
	private static Logger log = LoggerFactory.getLogger(BeritraService.class);
	private static final int duration = CustomConfig.BERITRA_DURATION;
	// Beritra Invasion 4.7
	private FastMap<Integer, VisibleObject> adventPortal = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventEffect = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventControl = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventDirecting = new FastMap<Integer, VisibleObject>();
	// Ereshkigal Invasion 4.9
	private FastMap<Integer, VisibleObject> adventEreshPortal = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventEreshEffect = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventEreshControl = new FastMap<Integer, VisibleObject>();
	private FastMap<Integer, VisibleObject> adventEreshDirecting = new FastMap<Integer, VisibleObject>();

	private final Map<Integer, BeritraInvasion<?>> activeInvasions = new FastMap<Integer, BeritraInvasion<?>>()
			.shared();

	public void initBeritraLocations() {
		if (CustomConfig.BERITRA_ENABLED) {
			beritra = DataManager.BERITRA_DATA.getBeritraLocations();
			for (BeritraLocation loc : getBeritraLocations().values()) {
				spawn(loc, BeritraStateType.PEACE);
			}
			log.info("[BeritraService] Loaded " + beritra.size() + " beritra locations.");
		} else {
			beritra = Collections.emptyMap();
		}
	}

	public void initBeritra() {
		if (CustomConfig.BERITRA_ENABLED) {
			log.info("[BeritraService] is initialized...");
			beritraSchedule = BeritraSchedule.load();
			for (Beritra beritra : beritraSchedule.getBeritrasList()) {
				for (String invasionTime : beritra.getInvasionTimes()) {
					CronService.getInstance().schedule(new BeritraStartRunnable(beritra.getId()), invasionTime);
				}
			}
		}
	}

	public void startBeritraInvasion(final int id) {
		final BeritraInvasion<?> invade;
		synchronized (this) {
			if (activeInvasions.containsKey(id)) {
				return;
			}
			invade = new Invade(beritra.get(id));
			activeInvasions.put(id, invade);
		}
		invade.start();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				stopBeritraInvasion(id);
			}
		}, duration * 3600 * 1000);
	}

	public void stopBeritraInvasion(int id) {
		if (!isInvasionInProgress(id)) {
			return;
		}
		BeritraInvasion<?> invade;
		synchronized (this) {
			invade = activeInvasions.remove(id);
		}
		if (invade == null || invade.isFinished()) {
			return;
		}
		invade.stop();
		devilUnitReturnMsg(id);
		beritraLegionReturnMsg(id);
	}

	public void spawn(BeritraLocation loc, BeritraStateType bstate) {
		if (bstate.equals(BeritraStateType.INVASION)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getBeritraSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				BeritraSpawnTemplate beritratemplate = (BeritraSpawnTemplate) st;
				if (beritratemplate.getBStateType().equals(bstate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(beritratemplate, 1));
				}
			}
		}
	}

	/**
	 * Beritra Invasion Msg.
	 */
	public boolean beritraInvasionMsg(int id) {
		switch (id) {
		case 1:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_INVADE_VRITRA_SPECIAL);
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean invasionCorridorMsg(int id) {
		switch (id) {
		case 1:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					// The Beritra Legion's Invasion Corridor has appeared.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_MESSAGE_01);
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean devilUnitThroughMsg(int id) {
		switch (id) {
		case 1:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					// The Devil Unit has infiltrated through the Invasion Corridor.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_MESSAGE_02);
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean devilUnitReturnMsg(int id) {
		switch (id) {
		case 1:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					// The Devil Unit is preparing for its return.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_MESSAGE_03);
				}
			});
			return true;
		default:
			return false;
		}
	}

	/**
	 * Ereshkigal Invasion Msg.
	 */
	public boolean ereshkigalInvasionMsg(int id) {
		switch (id) {
		case 35:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_INVADE_VRITRA_SPECIAL);
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean ereshkigalCorridorMsg(int id) {
		switch (id) {
		case 35:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					// The Ereshkigal Legion's Invasion Corridor has been created.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_Ere_MESSAGE_01);
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean ereshkigalLegionThroughMsg(int id) {
		switch (id) {
		case 35:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					// The Ereshkigal Legion's Magic weapon has infiltrated through the Invasion
					// Corridor.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_Ere_MESSAGE_02);
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean beritraLegionReturnMsg(int id) {
		switch (id) {
		case 35:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					// The Beritra Legion Devil Unit is preparing for its return.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_MESSAGE_03);
				}
			});
			return true;
		default:
			return false;
		}
	}

	/**
	 * Dredgion Defense Msg.
	 */
	public boolean dredgionDefenseMsg(int id) {
		switch (id) {
		case 57:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Dreadgion_Start_L);
				}
			});
			return true;
		default:
			return false;
		}
	}

	/**
	 * Beritra Invasion Effect.
	 */
	public boolean adventControlSP(int id) {
		switch (id) {
		case 1:
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 702529, 858.5479f, 1151.3783f, 278.46576f, (byte) 71),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 702529, 1519.0f, 1911.0f, 289.5f, (byte) 10), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 702529, 260.20285f, 2134.1099f, 207.375f, (byte) 9),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 702529, 1586.9154f, 2078.2305f, 155.875f, (byte) 66),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 702529, 1702.6613f, 1662.9213f, 102.19242f, (byte) 64),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 702529, 2485.3333f, 824.3736f, 100.625f, (byte) 56),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 702529, 382.0f, 2929.0f, 100.25f, (byte) 42), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 702529, 1470.0549f, 1890.8654f, 106.22974f, (byte) 7),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 702529, 540.073f, 2107.656f, 103.375f, (byte) 107),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 702529, 2762.5203f, 830.4615f, 383.87866f, (byte) 58),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 702529, 1929.1307f, 1953.1182f, 289.32068f, (byte) 64),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 702529, 2729.864f, 1890.5359f, 189.625f, (byte) 39),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 702529, 2429.1567f, 2619.0974f, 40.25f, (byte) 40),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 702529, 555.4294f, 2231.6064f, 44.089336f, (byte) 71),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 702529, 1771.0f, 1356.0f, 18.125f, (byte) 34), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 702529, 593.9777f, 481.57568f, 416.42203f, (byte) 60),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 702529, 2943.966f, 2272.9531f, 231.43457f, (byte) 32),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 702529, 1429.0f, 1949.0f, 138.5625f, (byte) 27), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220020000, 702529, 400.68732f, 1715.6395f, 441.6271f, (byte) 20),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220020000, 702529, 580.48895f, 355.1831f, 485.2271f, (byte) 61),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 702529, 2895.708f, 1516.8243f, 250.65457f, (byte) 70),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 702529, 1761.3639f, 506.55444f, 247.3006f, (byte) 53),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 702529, 1870.0f, 1675.0f, 247.375f, (byte) 53), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 702529, 780.0f, 1240.0f, 224.0f, (byte) 76), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 702529, 2565.876f, 286.7999f, 287.49225f, (byte) 18),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 702529, 465.72607f, 1786.9429f, 206.01352f, (byte) 3),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 702529, 2473.5154f, 1896.5199f, 23.560577f, (byte) 19),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 702529, 335.0f, 370.0f, 5.25f, (byte) 83), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 702529, 417.68674f, 2836.982f, 245.81363f, (byte) 49),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 702529, 591.8911f, 1341.8286f, 276.875f, (byte) 119),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 702529, 1488.8247f, 1256.1757f, 298.05154f, (byte) 46),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 702529, 2186.1123f, 922.95953f, 186.69003f, (byte) 32),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220080000, 702529, 2651.8286f, 2716.1633f, 202.89534f, (byte) 80),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220080000, 702529,
					192.61913f, 527.22363f, 196.70428f, (byte) 103), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220080000, 702529, 1583.1567f, 1106.1504f, 132.79529f, (byte) 87),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702529, 2020.6847f, 2832.924f, 2830.972f, (byte) 51),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702529, 820.0f, 865.0f, 1671.1095f, (byte) 83), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702529, 2280.3706f, 870.72766f, 2831.548f, (byte) 98),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702529, 3332.5474f, 1371.9772f, 2666.258f, (byte) 89),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702529, 1123.5983f, 2096.5151f, 2886.9402f, (byte) 96),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 702529, 656.06f, 808.15f, 165.125f, (byte) 78), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 702529, 289.80325f, 506.02426f, 158.125f, (byte) 114),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 702529, 1182.1178f, 348.7145f, 128.5f, (byte) 76), 1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702529, 1155.9978f, 1075.4766f, 303.375f, (byte) 104),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702529, 675.446f, 1001.7693f, 274.59036f, (byte) 66),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702529, 386.59848f, 1810.1382f, 226.42104f, (byte) 89),
					1));
			adventControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702529, 1836.0f, 142.0f, 242.625f, (byte) 86), 1));
			return true;
		default:
			return false;
		}
	}

	public boolean adventEffectSP(int id) {
		switch (id) {
		case 1:
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 702549, 858.5479f, 1151.3783f, 278.46576f, (byte) 71),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 702549, 1519.0f, 1911.0f, 289.5f, (byte) 10), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 702549, 260.20285f, 2134.1099f, 207.375f, (byte) 9),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 702549, 1586.9154f, 2078.2305f, 155.875f, (byte) 66),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 702549, 1702.6613f, 1662.9213f, 102.19242f, (byte) 64),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 702549, 2485.3333f, 824.3736f, 100.625f, (byte) 56),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 702549, 382.0f, 2929.0f, 100.25f, (byte) 42), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 702549, 1470.0549f, 1890.8654f, 106.22974f, (byte) 7),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 702549, 540.073f, 2107.656f, 103.375f, (byte) 107),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 702549, 2762.5203f, 830.4615f, 383.87866f, (byte) 58),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 702549, 1929.1307f, 1953.1182f, 289.32068f, (byte) 64),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 702549, 2729.864f, 1890.5359f, 189.625f, (byte) 39),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 702549, 2429.1567f, 2619.0974f, 40.25f, (byte) 40),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 702549, 555.4294f, 2231.6064f, 44.089336f, (byte) 71),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 702549, 1771.0f, 1356.0f, 18.125f, (byte) 34), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 702549, 593.9777f, 481.57568f, 416.42203f, (byte) 60),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 702549, 2943.966f, 2272.9531f, 231.43457f, (byte) 32),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 702549, 1429.0f, 1949.0f, 138.5625f, (byte) 27), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220020000, 702549, 400.68732f, 1715.6395f, 441.6271f, (byte) 20),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220020000, 702549, 580.48895f, 355.1831f, 485.2271f, (byte) 61),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 702549, 2895.708f, 1516.8243f, 250.65457f, (byte) 70),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 702549, 1761.3639f, 506.55444f, 247.3006f, (byte) 53),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 702549, 1870.0f, 1675.0f, 247.375f, (byte) 53), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 702549, 780.0f, 1240.0f, 224.0f, (byte) 76), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 702549, 2565.876f, 286.7999f, 287.49225f, (byte) 18),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 702549, 465.72607f, 1786.9429f, 206.01352f, (byte) 3),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 702549, 2473.5154f, 1896.5199f, 23.560577f, (byte) 19),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 702549, 335.0f, 370.0f, 5.25f, (byte) 83), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 702549, 417.68674f, 2836.982f, 245.81363f, (byte) 49),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 702549, 591.8911f, 1341.8286f, 276.875f, (byte) 119),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 702549, 1488.8247f, 1256.1757f, 298.05154f, (byte) 46),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 702549, 2186.1123f, 922.95953f, 186.69003f, (byte) 32),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220080000, 702549, 2651.8286f, 2716.1633f, 202.89534f, (byte) 80),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220080000, 702549,
					192.61913f, 527.22363f, 196.70428f, (byte) 103), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220080000, 702549, 1583.1567f, 1106.1504f, 132.79529f, (byte) 87),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702549, 2020.6847f, 2832.924f, 2830.972f, (byte) 51),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702549, 820.0f, 865.0f, 1671.1095f, (byte) 83), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702549, 2280.3706f, 870.72766f, 2831.548f, (byte) 98),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702549, 3332.5474f, 1371.9772f, 2666.258f, (byte) 89),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702549, 1123.5983f, 2096.5151f, 2886.9402f, (byte) 96),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 702549, 656.06f, 808.15f, 165.125f, (byte) 78), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 702549, 289.80325f, 506.02426f, 158.125f, (byte) 114),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 702549, 1182.1178f, 348.7145f, 128.5f, (byte) 76), 1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702549, 1155.9978f, 1075.4766f, 303.375f, (byte) 104),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702549, 675.446f, 1001.7693f, 274.59036f, (byte) 66),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702549, 386.59848f, 1810.1382f, 226.42104f, (byte) 89),
					1));
			adventEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702549, 1836.0f, 142.0f, 242.625f, (byte) 86), 1));
			return true;
		default:
			return false;
		}
	}

	public boolean adventPortalSP(int id) {
		switch (id) {
		case 1:
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 702550, 858.5479f, 1151.3783f, 278.46576f, (byte) 71),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 702550, 1519.0f, 1911.0f, 289.5f, (byte) 10), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 702550, 260.20285f, 2134.1099f, 207.375f, (byte) 9),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 702550, 1586.9154f, 2078.2305f, 155.875f, (byte) 66),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 702550, 1702.6613f, 1662.9213f, 102.19242f, (byte) 64),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 702550, 2485.3333f, 824.3736f, 100.625f, (byte) 56),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 702550, 382.0f, 2929.0f, 100.25f, (byte) 42), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 702550, 1470.0549f, 1890.8654f, 106.22974f, (byte) 7),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 702550, 540.073f, 2107.656f, 103.375f, (byte) 107),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 702550, 2762.5203f, 830.4615f, 383.87866f, (byte) 58),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 702550, 1929.1307f, 1953.1182f, 289.32068f, (byte) 64),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 702550, 2729.864f, 1890.5359f, 189.625f, (byte) 39),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 702550, 2429.1567f, 2619.0974f, 40.25f, (byte) 40),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 702550, 555.4294f, 2231.6064f, 44.089336f, (byte) 71),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 702550, 1771.0f, 1356.0f, 18.125f, (byte) 34), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 702550, 593.9777f, 481.57568f, 416.42203f, (byte) 60),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 702550, 2943.966f, 2272.9531f, 231.43457f, (byte) 32),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 702550, 1429.0f, 1949.0f, 138.5625f, (byte) 27), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220020000, 702550, 400.68732f, 1715.6395f, 441.6271f, (byte) 20),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220020000, 702550, 580.48895f, 355.1831f, 485.2271f, (byte) 61),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 702550, 2895.708f, 1516.8243f, 250.65457f, (byte) 70),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 702550, 1761.3639f, 506.55444f, 247.3006f, (byte) 53),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 702550, 1870.0f, 1675.0f, 247.375f, (byte) 53), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 702550, 780.0f, 1240.0f, 224.0f, (byte) 76), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 702550, 2565.876f, 286.7999f, 287.49225f, (byte) 18),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 702550, 465.72607f, 1786.9429f, 206.01352f, (byte) 3),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 702550, 2473.5154f, 1896.5199f, 23.560577f, (byte) 19),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 702550, 335.0f, 370.0f, 5.25f, (byte) 83), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 702550, 417.68674f, 2836.982f, 245.81363f, (byte) 49),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 702550, 591.8911f, 1341.8286f, 276.875f, (byte) 119),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 702550, 1488.8247f, 1256.1757f, 298.05154f, (byte) 46),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 702550, 2186.1123f, 922.95953f, 186.69003f, (byte) 32),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220080000, 702550, 2651.8286f, 2716.1633f, 202.89534f, (byte) 80),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220080000, 702550,
					192.61913f, 527.22363f, 196.70428f, (byte) 103), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220080000, 702550, 1583.1567f, 1106.1504f, 132.79529f, (byte) 87),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702550, 2020.6847f, 2832.924f, 2830.972f, (byte) 51),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702550, 820.0f, 865.0f, 1671.1095f, (byte) 83), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702550, 2280.3706f, 870.72766f, 2831.548f, (byte) 98),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702550, 3332.5474f, 1371.9772f, 2666.258f, (byte) 89),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702550, 1123.5983f, 2096.5151f, 2886.9402f, (byte) 96),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 702550, 656.06f, 808.15f, 165.125f, (byte) 78), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 702550, 289.80325f, 506.02426f, 158.125f, (byte) 114),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 702550, 1182.1178f, 348.7145f, 128.5f, (byte) 76), 1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702550, 1155.9978f, 1075.4766f, 303.375f, (byte) 104),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702550, 675.446f, 1001.7693f, 274.59036f, (byte) 66),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702550, 386.59848f, 1810.1382f, 226.42104f, (byte) 89),
					1));
			adventPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 702550, 1836.0f, 142.0f, 242.625f, (byte) 86), 1));
			return true;
		default:
			return false;
		}
	}

	public boolean adventDirectingSP(int id) {
		switch (id) {
		case 1:
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 855231, 858.5479f, 1151.3783f, 278.46576f, (byte) 71),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 855231, 1519.0f, 1911.0f, 289.5f, (byte) 10), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210020000, 855231, 260.20285f, 2134.1099f, 207.375f, (byte) 9),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 855231, 1586.9154f, 2078.2305f, 155.875f, (byte) 66),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 855231, 1702.6613f, 1662.9213f, 102.19242f, (byte) 64),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210030000, 855231, 2485.3333f, 824.3736f, 100.625f, (byte) 56),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 855231, 382.0f, 2929.0f, 100.25f, (byte) 42), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 855231, 1470.0549f, 1890.8654f, 106.22974f, (byte) 7),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210040000, 855231, 540.073f, 2107.656f, 103.375f, (byte) 107),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 855231, 2762.5203f, 830.4615f, 383.87866f, (byte) 58),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 855231, 1929.1307f, 1953.1182f, 289.32068f, (byte) 64),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210130000, 855231, 2729.864f, 1890.5359f, 189.625f, (byte) 39),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 855231, 2429.1567f, 2619.0974f, 40.25f, (byte) 40),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 855231, 555.4294f, 2231.6064f, 44.089336f, (byte) 71),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210060000, 855231, 1771.0f, 1356.0f, 18.125f, (byte) 34), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 855231, 593.9777f, 481.57568f, 416.42203f, (byte) 60),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 855231, 2943.966f, 2272.9531f, 231.43457f, (byte) 32),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(210070000, 855231, 1429.0f, 1949.0f, 138.5625f, (byte) 27), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220020000, 855231, 400.68732f, 1715.6395f, 441.6271f, (byte) 20),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220020000, 855231, 580.48895f, 355.1831f, 485.2271f, (byte) 61),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 855231, 2895.708f, 1516.8243f, 250.65457f, (byte) 70),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 855231, 1761.3639f, 506.55444f, 247.3006f, (byte) 53),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220030000, 855231, 1870.0f, 1675.0f, 247.375f, (byte) 53), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 855231, 780.0f, 1240.0f, 224.0f, (byte) 76), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 855231, 2565.876f, 286.7999f, 287.49225f, (byte) 18),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220040000, 855231, 465.72607f, 1786.9429f, 206.01352f, (byte) 3),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 855231, 2473.5154f, 1896.5199f, 23.560577f, (byte) 19),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 855231, 335.0f, 370.0f, 5.25f, (byte) 83), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220050000, 855231, 417.68674f, 2836.982f, 245.81363f, (byte) 49),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 855231, 591.8911f, 1341.8286f, 276.875f, (byte) 119),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 855231, 1488.8247f, 1256.1757f, 298.05154f, (byte) 46),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220140000, 855231, 2186.1123f, 922.95953f, 186.69003f, (byte) 32),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220080000, 855231, 2651.8286f, 2716.1633f, 202.89534f, (byte) 80),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(220080000, 855231,
					192.61913f, 527.22363f, 196.70428f, (byte) 103), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(220080000, 855231, 1583.1567f, 1106.1504f, 132.79529f, (byte) 87),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 855231, 2020.6847f, 2832.924f, 2830.972f, (byte) 51),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 855231, 820.0f, 865.0f, 1671.1095f, (byte) 83), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 855231, 2280.3706f, 870.72766f, 2831.548f, (byte) 98),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 855231, 3332.5474f, 1371.9772f, 2666.258f, (byte) 89),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 855231, 1123.5983f, 2096.5151f, 2886.9402f, (byte) 96),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 855231, 656.06f, 808.15f, 165.125f, (byte) 78), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 855231, 289.80325f, 506.02426f, 158.125f, (byte) 114),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600090000, 855231, 1182.1178f, 348.7145f, 128.5f, (byte) 76), 1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 855231, 1155.9978f, 1075.4766f, 303.375f, (byte) 104),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 855231, 675.446f, 1001.7693f, 274.59036f, (byte) 66),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 855231, 386.59848f, 1810.1382f, 226.42104f, (byte) 89),
					1));
			adventDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(600100000, 855231, 1836.0f, 142.0f, 242.625f, (byte) 86), 1));
			return true;
		default:
			return false;
		}
	}

	/**
	 * Ereshkigal Invasion Effect.
	 */
	public boolean adventControlEreshSP(int id) {
		switch (id) {
		case 35:
			adventEreshControl.put(702529, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 702529,
					2065.3005f, 2473.1807f, 2900.1775f, (byte) 115), 1));
			adventEreshControl.put(702529, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 702529,
					1722.8392f, 1903.1249f, 2892.1248f, (byte) 107), 1));
			adventEreshControl.put(702529, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 702529,
					670.00000f, 2700.0000f, 2897.5470f, (byte) 107), 1));
			adventEreshControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702529, 1813.9344f, 1827.3582f, 2885.6187f, (byte) 33),
					1));
			adventEreshControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702529, 2606.2485f, 1892.8187f, 2908.7598f, (byte) 47),
					1));
			adventEreshControl.put(702529, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702529, 2773.0369f, 1152.2582f, 2801.5713f, (byte) 37),
					1));
			return true;
		default:
			return false;
		}
	}

	public boolean adventEffectEreshSP(int id) {
		switch (id) {
		case 35:
			adventEreshEffect.put(702549, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 702549,
					2065.3005f, 2473.1807f, 2900.1775f, (byte) 115), 1));
			adventEreshEffect.put(702549, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 702549,
					1722.8392f, 1903.1249f, 2892.1248f, (byte) 107), 1));
			adventEreshEffect.put(702549, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 702549,
					670.00000f, 2700.0000f, 2897.5470f, (byte) 107), 1));
			adventEreshEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702549, 1813.9344f, 1827.3582f, 2885.6187f, (byte) 33),
					1));
			adventEreshEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702549, 2606.2485f, 1892.8187f, 2908.7598f, (byte) 47),
					1));
			adventEreshEffect.put(702549, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702549, 2773.0369f, 1152.2582f, 2801.5713f, (byte) 37),
					1));
			return true;
		default:
			return false;
		}
	}

	public boolean adventPortalEreshSP(int id) {
		switch (id) {
		case 35:
			adventEreshPortal.put(702550, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 702550,
					2065.3005f, 2473.1807f, 2900.1775f, (byte) 115), 1));
			adventEreshPortal.put(702550, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 702550,
					1722.8392f, 1903.1249f, 2892.1248f, (byte) 107), 1));
			adventEreshPortal.put(702550, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000, 702550,
					670.00000f, 2700.0000f, 2897.5470f, (byte) 107), 1));
			adventEreshPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702550, 1813.9344f, 1827.3582f, 2885.6187f, (byte) 33),
					1));
			adventEreshPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702550, 2606.2485f, 1892.8187f, 2908.7598f, (byte) 47),
					1));
			adventEreshPortal.put(702550, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 702550, 2773.0369f, 1152.2582f, 2801.5713f, (byte) 37),
					1));
			return true;
		default:
			return false;
		}
	}

	public boolean adventDirectingEreshSP(int id) {
		switch (id) {
		case 35:
			adventEreshDirecting.put(855231, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000,
					855231, 2065.3005f, 2473.1807f, 2900.1775f, (byte) 115), 1));
			adventEreshDirecting.put(855231, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000,
					855231, 1722.8392f, 1903.1249f, 2892.1248f, (byte) 107), 1));
			adventEreshDirecting.put(855231, SpawnEngine.spawnObject(SpawnEngine.addNewSingleTimeSpawn(400010000,
					855231, 670.00000f, 2700.0000f, 2897.5470f, (byte) 107), 1));
			adventEreshDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 855231, 1813.9344f, 1827.3582f, 2885.6187f, (byte) 33),
					1));
			adventEreshDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 855231, 2606.2485f, 1892.8187f, 2908.7598f, (byte) 47),
					1));
			adventEreshDirecting.put(855231, SpawnEngine.spawnObject(
					SpawnEngine.addNewSingleTimeSpawn(400010000, 855231, 2773.0369f, 1152.2582f, 2801.5713f, (byte) 37),
					1));
			return true;
		default:
			return false;
		}
	}

	public void despawn(BeritraLocation loc) {
		if (loc.getSpawned() == null) {
			return;
		}
		for (VisibleObject obj : loc.getSpawned()) {
			Npc spawned = (Npc) obj;
			spawned.setDespawnDelayed(true);
			if (spawned.getAggroList().getList().isEmpty()) {
				spawned.getController().cancelTask(TaskId.RESPAWN);
				obj.getController().onDelete();
			}
		}
		loc.getSpawned().clear();
	}

	public boolean isInvasionInProgress(int id) {
		return activeInvasions.containsKey(id);
	}

	public Map<Integer, BeritraInvasion<?>> getActiveInvasions() {
		return activeInvasions;
	}

	public int getDuration() {
		return duration;
	}

	public BeritraLocation getBeritraLocation(int id) {
		return beritra.get(id);
	}

	public Map<Integer, BeritraLocation> getBeritraLocations() {
		return beritra;
	}

	public static BeritraService getInstance() {
		return BeritraServiceHolder.INSTANCE;
	}

	private static class BeritraServiceHolder {
		private static final BeritraService INSTANCE = new BeritraService();
	}
}