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
import com.aionemu.gameserver.configs.schedule.ConquestSchedule;
import com.aionemu.gameserver.configs.schedule.ConquestSchedule.Conquest;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.conquest.ConquestLocation;
import com.aionemu.gameserver.model.conquest.ConquestStateType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.conquestspawns.ConquestSpawnTemplate;
import com.aionemu.gameserver.services.conquestservice.ConquestOffering;
import com.aionemu.gameserver.services.conquestservice.ConquestStartRunnable;
import com.aionemu.gameserver.services.conquestservice.Offering;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */
public class ConquestService {
	private ConquestSchedule conquestSchedule;
	private Map<Integer, ConquestLocation> conquest;
	private static final int duration = CustomConfig.CONQUEST_DURATION;
	private final Map<Integer, ConquestOffering<?>> activeConquest = new FastMap<Integer, ConquestOffering<?>>()
			.shared();
	private static final Logger log = LoggerFactory.getLogger(ZorshivDredgionService.class);

	public void initConquestLocations() {
		if (CustomConfig.CONQUEST_ENABLED) {
			conquest = DataManager.CONQUEST_DATA.getConquestLocations();
			for (ConquestLocation loc : getConquestLocations().values()) {
				spawn(loc, ConquestStateType.PEACE);
			}
			log.info("[ConquestService] Loaded " + conquest.size() + " locations.");
		} else {
			log.info("[ConquestService] Conquest is disabled in config...");
			conquest = Collections.emptyMap();
		}
	}

	public void initOffering() {
		if (CustomConfig.CONQUEST_ENABLED) {
			log.info("[ConquestService] is initialized...");
			conquestSchedule = ConquestSchedule.load();
			for (Conquest conquest : conquestSchedule.getConquestsList()) {
				for (String offeringTime : conquest.getOfferingTimes()) {
					CronService.getInstance().schedule(new ConquestStartRunnable(conquest.getId()), offeringTime);
				}
			}
		}
	}

	public void startConquest(final int id) {
		final ConquestOffering<?> offering;
		synchronized (this) {
			if (activeConquest.containsKey(id)) {
				return;
			}
			offering = new Offering(conquest.get(id));
			activeConquest.put(id, offering);
		}
		offering.start();
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				stopConquest(id);
			}
		}, duration * 3600 * 1000);
	}

	public void stopConquest(int id) {
		if (!isConquestInProgress(id)) {
			return;
		}
		ConquestOffering<?> offering;
		synchronized (this) {
			offering = activeConquest.remove(id);
		}
		if (offering == null || offering.isFinished()) {
			return;
		}
		offering.stop();
	}

	public void spawn(ConquestLocation loc, ConquestStateType ostate) {
		if (ostate.equals(ConquestStateType.CONQUEST)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getConquestSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				ConquestSpawnTemplate conquesttemplate = (ConquestSpawnTemplate) st;
				if (conquesttemplate.getOStateType().equals(ostate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(conquesttemplate, 1));
				}
			}
		}
	}

	public boolean conquestOfferingMsg(int id) {
		switch (id) {
		case 1:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendSys3Message(player, "\uE00D",
							"The <Conquest/Offering> a rare monster appeared !!!");
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean emperorVaultMsg(int id) {
		switch (id) {
		case 3:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendSys3Message(player, "\uE0BD", "Shugo Emperor's Vault is now open !!!");
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean trillirunerkSafeMsg(int id) {
		switch (id) {
		case 4:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendSys3Message(player, "\uE11C", "Emperor Trillirunerk's Safe is now open !!!");
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean smolderingFireTempleMsg(int id) {
		switch (id) {
		case 5:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendSys3Message(player, "\uE114", "Smoldering Fire Temple is now open !!!");
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean kumukiCaveMsg(int id) {
		switch (id) {
		case 6:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendSys3Message(player, "\uE054", "Kumuki Cave is now open !!!");
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean IDEventDefMsg(int id) {
		switch (id) {
		case 11:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendSys3Message(player, "\uE079", "IDEvent Def UnderPath is now open !!!");
				}
			});
			return true;
		default:
			return false;
		}
	}

	public boolean tiamarantaMsg(int id) {
		switch (id) {
		case 13:
			World.getInstance().doOnAllPlayers(new Visitor<Player>() {
				@Override
				public void visit(Player player) {
					PacketSendUtility.sendSys3Message(player, "\uE04C", "Tiamaranta's Eye is now open !!!");
				}
			});
			return true;
		default:
			return false;
		}
	}

	public void despawn(ConquestLocation loc) {
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

	public boolean isConquestInProgress(int id) {
		return activeConquest.containsKey(id);
	}

	public Map<Integer, ConquestOffering<?>> getActiveConquest() {
		return activeConquest;
	}

	public int getDuration() {
		return duration;
	}

	public ConquestLocation getConquestLocation(int id) {
		return conquest.get(id);
	}

	public Map<Integer, ConquestLocation> getConquestLocations() {
		return conquest;
	}

	public static ConquestService getInstance() {
		return ConquestServiceHolder.INSTANCE;
	}

	private static class ConquestServiceHolder {
		private static final ConquestService INSTANCE = new ConquestService();
	}
}