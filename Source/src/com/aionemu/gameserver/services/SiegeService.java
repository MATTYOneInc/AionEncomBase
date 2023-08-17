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
package com.aionemu.gameserver.services;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.schedule.SiegeSchedule;
import com.aionemu.gameserver.configs.schedule.SiegeSchedule.Fortress;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.siege.*;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;
import com.aionemu.gameserver.model.templates.world.WeatherEntry;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.*;
import com.aionemu.gameserver.services.siegeservice.*;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldType;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javolution.util.FastMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;

public class SiegeService
{
	private static final Logger log = LoggerFactory.getLogger("SIEGE_LOG");

	private static final String SIEGE_LOCATION_STATUS_BROADCAST_SCHEDULE = "0 0 * ? * *";
	private static final SiegeService instance = new SiegeService();
	private final Map<Integer, Siege<?>> activeSieges = new FastMap<Integer, Siege<?>>().shared();
	private SiegeSchedule siegeSchedule;
	private Map<Integer, ArtifactLocation> artifacts;
	private Map<Integer, FortressLocation> fortresses;
	private Map<Integer, SiegeLocation> locations;
	
	public static SiegeService getInstance() {
		return instance;
	}
	
	public void initSiegeLocations() {
		if (SiegeConfig.SIEGE_ENABLED) {
			if (siegeSchedule != null) {
				log.error("[SiegeService] should not be initialized two times!");
				return;
			}
			artifacts = DataManager.SIEGE_LOCATION_DATA.getArtifacts();
			fortresses = DataManager.SIEGE_LOCATION_DATA.getFortress();
			locations = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations();
			DAOManager.getDAO(SiegeDAO.class).loadSiegeLocations(locations);
			log.info("[SiegeService] Loaded " + locations.size() + " siege locations.");
		} else {
			artifacts = Collections.emptyMap();
			fortresses = Collections.emptyMap();
			locations = Collections.emptyMap();
			log.info("[SiegeService] Sieges are disabled in config.");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void initSieges() {
		if (!SiegeConfig.SIEGE_ENABLED) {
			return;
		}
		GameServer.log.info("[SiegeService] is initialized...");

		for (Integer i : getSiegeLocations().keySet()) {
			deSpawnNpcs(i);
		} for (FortressLocation f : getFortresses().values()) {
			spawnNpcs(f.getLocationId(), f.getRace(), SiegeModType.PEACE);
		} for (ArtifactLocation a : getStandaloneArtifacts().values()) {
			spawnNpcs(a.getLocationId(), a.getRace(), SiegeModType.PEACE);
		}
		siegeSchedule = SiegeSchedule.load();
		for (Fortress f : siegeSchedule.getFortressesList()) {
			for (String siegeTime : f.getSiegeTimes()) {
				CronService.getInstance().schedule(new SiegeStartRunnable(f.getId()), siegeTime);
			}
		} for (ArtifactLocation artifact : artifacts.values()) {
			if (artifact.isStandAlone()) {
				log.debug("Starting siege of artifact #" + artifact.getLocationId());
				startSiege(artifact.getLocationId());
			} else {
				log.debug("Artifact #" + artifact.getLocationId() + " siege was not started, it belongs to fortress");
			}
		}
		updateFortressNextState();
		CronService.getInstance().schedule(new Runnable() {
		    @Override
			public void run() {
				updateFortressNextState();
				World.getInstance().doOnAllPlayers(new Visitor<Player>() {
					public void visit(Player player) {
						for (FortressLocation fortress : getFortresses().values()) {
							PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(fortress.getLocationId(), false));
						}
						PacketSendUtility.sendPacket(player, new SM_FORTRESS_STATUS());
						for (FortressLocation fortress : getFortresses().values()) {
							PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(fortress.getLocationId(), true));
						}
					}
				});
			}
		}, SIEGE_LOCATION_STATUS_BROADCAST_SCHEDULE);
	}
	
	public void checkSiegeStart(int locationId) {
		if (SiegeConfig.SIEGE_AUTO_RACE && SiegeAutoRace.isAutoSiege(locationId)) {
			SiegeAutoRace.AutoSiegeRace(locationId);
		} else {
			startSiege(locationId);
		}
	}
	
	public void startSiege(final int siegeLocationId) {
		Siege<?> siege;
		synchronized (this) {
			if (activeSieges.containsKey(siegeLocationId)) {
				return;
			}
			siege = newSiege(siegeLocationId);
			activeSieges.put(siegeLocationId, siege);
		}
		siege.startSiege();
		if (siege.isEndless()) {
			return;
		}
		ThreadPoolManager.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				stopSiege(siegeLocationId);
			}
		}, siege.getSiegeLocation().getSiegeDuration() * 1000);
	}
	
	public void stopSiege(int siegeLocationId) {
		log.debug("Stopping siege of siege location: " + siegeLocationId);
		if (!isSiegeInProgress(siegeLocationId)) {
			log.debug("Siege of siege location " + siegeLocationId + " is not in progress, it was captured earlier?");
			return;
		}
		Siege<?> siege;
		synchronized (this) {
			siege = activeSieges.remove(siegeLocationId);
		} if (siege == null || siege.isFinished()) {
			return;
		}
		siege.stopSiege();
	}
	
	protected void updateFortressNextState() {
		Calendar currentHourPlus1 = Calendar.getInstance();
		currentHourPlus1.set(Calendar.MINUTE, 0);
		currentHourPlus1.set(Calendar.SECOND, 0);
		currentHourPlus1.set(Calendar.MILLISECOND, 0);
		currentHourPlus1.add(Calendar.HOUR, 1);
		Map<Runnable, JobDetail> siegeStartRunables = CronService.getInstance().getRunnables();
		siegeStartRunables = Maps.filterKeys(siegeStartRunables, new Predicate<Runnable>() {
			@Override
			public boolean apply(@Nullable Runnable runnable) {
				return (runnable instanceof SiegeStartRunnable);
			}
		});
		Map<Integer, List<Trigger>> siegeIdToStartTriggers = Maps.newHashMap();
		for (Map.Entry<Runnable, JobDetail> entry : siegeStartRunables.entrySet()) {
			SiegeStartRunnable fssr = (SiegeStartRunnable) entry.getKey();
			List<Trigger> storage = siegeIdToStartTriggers.get(fssr.getLocationId());
			if (storage == null) {
				storage = Lists.newArrayList();
				siegeIdToStartTriggers.put(fssr.getLocationId(), storage);
			}
			storage.addAll(CronService.getInstance().getJobTriggers(entry.getValue()));
		}
		for (Map.Entry<Integer, List<Trigger>> entry : siegeIdToStartTriggers.entrySet()) {
			List<Date> nextFireDates = Lists.newArrayListWithCapacity(entry.getValue().size());
			for (Trigger trigger : entry.getValue()) {
				nextFireDates.add(trigger.getNextFireTime());
			}
			Collections.sort(nextFireDates);
			Date nextSiegeDate = nextFireDates.get(0);
			Calendar siegeStartHour = Calendar.getInstance();
			siegeStartHour.setTime(nextSiegeDate);
			siegeStartHour.set(Calendar.MINUTE, 0);
			siegeStartHour.set(Calendar.SECOND, 0);
			siegeStartHour.set(Calendar.MILLISECOND, 0);
			SiegeLocation fortress = getSiegeLocation(entry.getKey());
			Calendar siegeCalendar = Calendar.getInstance();
			siegeCalendar.set(Calendar.MINUTE, 0);
			siegeCalendar.set(Calendar.SECOND, 0);
			siegeCalendar.set(Calendar.MILLISECOND, 0);
			siegeCalendar.add(Calendar.HOUR, 0);
			siegeCalendar.add(Calendar.SECOND, getRemainingSiegeTimeInSeconds(fortress.getLocationId()));
			if (SiegeConfig.SIEGE_AUTO_RACE && SiegeAutoRace.isAutoSiege(fortress.getLocationId())) {
				siegeStartHour.add(Calendar.HOUR, 1);
			} if (currentHourPlus1.getTimeInMillis() == siegeStartHour.getTimeInMillis() || siegeCalendar.getTimeInMillis() > currentHourPlus1.getTimeInMillis()) {
				fortress.setNextState(1);
			} else {
				fortress.setNextState(0);
			}
		}
	}
	
	public int getSecondsBeforeHourEnd() {
		Calendar c = Calendar.getInstance();
		int minutesAsSeconds = c.get(Calendar.MINUTE) * 60;
		int seconds = c.get(Calendar.SECOND);
		return 3600 - (minutesAsSeconds + seconds);
	}
	
	public int getRemainingSiegeTimeInSeconds(int siegeLocationId) {
		Siege<?> siege = getSiege(siegeLocationId);
		if (siege == null || siege.isFinished()) {
			return 0;
		} if (!siege.isStarted()) {
			return siege.getSiegeLocation().getSiegeDuration();
		} if (siege.getSiegeLocation().getSiegeDuration() == -1) {
			return -1;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, siege.getSiegeLocation().getSiegeDuration());
		int result = (int) ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000);
		return result > 0 ? result : 0;
	}
	
	public Siege<?> getSiege(SiegeLocation loc) {
		return activeSieges.get(loc.getLocationId());
	}
	
	public Siege<?> getSiege(Integer siegeLocationId) {
		return activeSieges.get(siegeLocationId);
	}
	
	public boolean isSiegeInProgress(int fortressId) {
		return activeSieges.containsKey(fortressId);
	}
	
	public Map<Integer, FortressLocation> getFortresses() {
		return fortresses;
	}
	
	public FortressLocation getFortress(int fortressId) {
		return fortresses.get(fortressId);
	}
	
	public Map<Integer, ArtifactLocation> getArtifacts() {
		return artifacts;
	}
	
	public ArtifactLocation getArtifact(int id) {
		return getArtifacts().get(id);
	}
	
	public Map<Integer, ArtifactLocation> getStandaloneArtifacts() {
		return Maps.filterValues(artifacts, new Predicate<ArtifactLocation>() {
			@Override
			public boolean apply(@Nullable ArtifactLocation input) {
				return input != null && input.isStandAlone();
			}
		});
	}
	
	public Map<Integer, ArtifactLocation> getFortressArtifacts() {
		return Maps.filterValues(artifacts, new Predicate<ArtifactLocation>() {
			@Override
			public boolean apply(@Nullable ArtifactLocation input) {
				return input != null && input.getOwningFortress() != null;
			}
		});
	}
	
	public Map<Integer, SiegeLocation> getSiegeLocations() {
		return locations;
	}
	
	public SiegeLocation getSiegeLocation(int locationId) {
		return locations.get(locationId);
	}
	
	public Map<Integer, SiegeLocation> getSiegeLocations(int worldId) {
		Map<Integer, SiegeLocation> mapLocations = new FastMap<Integer, SiegeLocation>();
		for (SiegeLocation location : getSiegeLocations().values()) {
			if (location.getWorldId() == worldId) {
				mapLocations.put(location.getLocationId(), location);
			}
		}
		return mapLocations;
	}
	
	protected Siege<?> newSiege(int siegeLocationId) {
		if (fortresses.containsKey(siegeLocationId)) {
			return new FortressSiege(fortresses.get(siegeLocationId));
		} if (artifacts.containsKey(siegeLocationId)) {
			return new ArtifactSiege(artifacts.get(siegeLocationId));
		}
		throw new SiegeException("Unknown siege handler for siege location: " + siegeLocationId);
	}
	
	public void cleanLegionId(int legionId) {
		for (SiegeLocation loc : this.getSiegeLocations().values()) {
			if (loc.getLegionId() == legionId) {
				loc.setLegionId(0);
				break;
			}
		}
	}
	
	public void spawnNpcs(int siegeLocationId, SiegeRace race, SiegeModType type) {
		List<SpawnGroup2> siegeSpawns = DataManager.SPAWNS_DATA2.getSiegeSpawnsByLocId(siegeLocationId);
		for (SpawnGroup2 group : siegeSpawns) {
			for (SpawnTemplate template : group.getSpawnTemplates()) {
				SiegeSpawnTemplate siegetemplate = (SiegeSpawnTemplate) template;
				if (siegetemplate.getSiegeRace().equals(race) && siegetemplate.getSiegeModType().equals(type)) {
					SpawnEngine.spawnObject(siegetemplate, 1);
				}
			}
		}
	}
	
	public void deSpawnNpcs(int siegeLocationId) {
		Collection<SiegeNpc> siegeNpcs = World.getInstance().getLocalSiegeNpcs(siegeLocationId);
		for (SiegeNpc npc : siegeNpcs) {
			npc.getController().onDelete();
		}
	}
	
	public boolean isSiegeNpcInActiveSiege(Npc npc) {
		if ((npc instanceof SiegeNpc)) {
			FortressLocation fort = getFortress(((SiegeNpc) npc).getSiegeId());
			if (fort != null) {
				if (fort.isVulnerable()) {
					return true;
				} if (fort.getNextState() == 1) {
					return npc.getSpawn().getRespawnTime() >= getSecondsBeforeHourEnd();
				}
			}
		}
		return false;
	}
	
	public void broadcastUpdate() {
		broadcast(new SM_SIEGE_LOCATION_INFO(), null);
	}
	
	public void broadcastUpdate(SiegeLocation loc) {
		Influence.getInstance().recalculateInfluence();
		broadcast(new SM_SIEGE_LOCATION_INFO(loc), new SM_INFLUENCE_RATIO());
	}
	
	public void broadcast(final AionServerPacket pkt1, final AionServerPacket pkt2) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			public void visit(Player player) {
				if (pkt1 != null) {
					PacketSendUtility.sendPacket(player, pkt1);
				} if (pkt2 != null) {
					PacketSendUtility.sendPacket(player, pkt2);
				}
			}
		});
	}
	
	public void broadcastUpdate(SiegeLocation loc, DescriptionId nameId) {
		SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO(loc);
		SM_SYSTEM_MESSAGE info = loc.getLegionId() == 0 ? new SM_SYSTEM_MESSAGE(1404542, loc.getRace().getDescriptionId(), nameId) : new SM_SYSTEM_MESSAGE(1301038, LegionService.getInstance().getLegion(loc.getLegionId()).getLegionName(), nameId);
		broadcast(pkt, info, loc.getRace());
	}
	
	private void broadcast(final AionServerPacket pkt, final AionServerPacket info, final SiegeRace race) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			public void visit(Player player) {
				if (player.getRace().getRaceId() == race.getRaceId()) {
					PacketSendUtility.sendPacket(player, info);
				}
				PacketSendUtility.sendPacket(player, pkt);
			}
		});
	}
	
	private void broadcast(final SM_RIFT_ANNOUNCE rift, final SM_SYSTEM_MESSAGE info) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			public void visit(Player player) {
				PacketSendUtility.sendPacket(player, rift);
				if (info != null && player.getWorldType().equals(WorldType.BALAUREA) ||
				    info != null && player.getWorldType().equals(WorldType.PANESTERRA)) {
					PacketSendUtility.sendPacket(player, info);
				}
			}
		});
	}
	
	public void onPlayerLogin(Player player) {
		if (SiegeConfig.SIEGE_ENABLED) {
			PacketSendUtility.sendPacket(player, new SM_INFLUENCE_RATIO());
			PacketSendUtility.sendPacket(player, new SM_SIEGE_LOCATION_INFO());
		}
	}
	
	public void onEnterSiegeWorld(Player player) {
		FastMap<Integer, SiegeLocation> worldLocations = new FastMap<Integer, SiegeLocation>();
		FastMap<Integer, ArtifactLocation> worldArtifacts = new FastMap<Integer, ArtifactLocation>();
		for (SiegeLocation location : getSiegeLocations().values()) {
			if (location.getWorldId() == player.getWorldId()) {
				worldLocations.put(location.getLocationId(), location);
			}
		} for (ArtifactLocation artifact : getArtifacts().values()) {
			if (artifact.getWorldId() == player.getWorldId()) {
				worldArtifacts.put(artifact.getLocationId(), artifact);
			}
		}
		PacketSendUtility.sendPacket(player, new SM_SHIELD_EFFECT(worldLocations.values()));
		PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO3(worldArtifacts.values()));
	}
	
	public void onWeatherChanged(WeatherEntry entry) {
    }
	
	public int getFortressId(int locId) {
		switch (locId) {
			case 49:
            case 61:
				return 1011;
			case 36:
			case 54:
				return 1131;
			case 37:
			case 55:
				return 1132;
			case 39:
			case 56:
				return 1141;
			case 45:
			case 57:
			case 72:
			case 75:
				return 1221;
			case 46:
			case 58:
			case 73:
			case 76:
				return 1231;
			case 47:
			case 59:
			case 74:
			case 77:
				return 1241;
			//4.7
			case 102:
				return 7011;
			case 103:
				return 10111;
			case 104:
				return 10211;	
			case 105:
				return 10311;
			case 106:
				return 10411;
		}
		return 0;
	}
}