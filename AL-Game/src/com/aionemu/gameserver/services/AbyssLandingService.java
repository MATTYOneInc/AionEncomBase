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

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.AbyssLandingConfig;
import com.aionemu.gameserver.dao.AbyssLandingDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.landing.LandingLocation;
import com.aionemu.gameserver.model.landing.LandingPointsEnum;
import com.aionemu.gameserver.model.landing.LandingStateType;
import com.aionemu.gameserver.model.landing_special.LandingSpecialLocation;
import com.aionemu.gameserver.model.landing_special.LandingSpecialStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.landingspawns.LandingSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_LANDING;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_LANDING_LEVEL;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abysslandingservice.AbyssLanding;
import com.aionemu.gameserver.services.abysslandingservice.Landing;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

public class AbyssLandingService {
	private static Logger log = LoggerFactory.getLogger(AbyssLandingService.class);
	private static Map<Integer, LandingLocation> abyssLanding;
	private final Map<Integer, Landing<?>> activeLanding = new FastMap<Integer, Landing<?>>().shared();
	private final int questRate = AbyssLandingConfig.ABYSS_LANDING_QUEST_RATE;

	public void initLandingLocations() {
		abyssLanding = DataManager.LANDING_LOCATION_DATA.getLandingLocations();
		DAOManager.getDAO(AbyssLandingDAO.class).loadLandingLocations(abyssLanding);
		for (LandingLocation loc : getLandingLocations().values()) {
			startLanding(loc.getId());
		}
		log.info("[AbyssLandingService] Loaded " + abyssLanding.size() + " Locations");
	}

	public void startLanding(final int id) {
		final Landing<?> land;
		synchronized (this) {
			if (activeLanding.containsKey(id)) {
				return;
			}
			land = new AbyssLanding(abyssLanding.get(id));
			activeLanding.put(id, land);
		}
		land.start(getLandingLocation(id).getLevel());
	}

	public void stopLanding(int id) {
		if (!activeLanding.containsKey(id)) {
			return;
		}
		Landing<?> landing;
		synchronized (this) {
			landing = activeLanding.remove(id);
		}
		if (landing == null) {
			return;
		}
		landing.stop();
	}

	public static void spawn(LandingLocation loc, LandingStateType estate) {
		if (estate.equals(estate)) {
		}
		List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getLandingSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns) {
			for (SpawnTemplate st : group.getSpawnTemplates()) {
				LandingSpawnTemplate landingtTemplate = (LandingSpawnTemplate) st;
				if (landingtTemplate.getEStateType().equals(estate)) {
					loc.getSpawned().add(SpawnEngine.spawnObject(landingtTemplate, 1));
				}
			}
		}
	}

	public static void despawn(LandingLocation loc) {
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

	public void updateRedemptionLanding(int points, LandingPointsEnum type, boolean win) {
		LandingLocation loc = redemptionLanding();
		if (win) {
			switch (type) {
			case BASE:
				loc.setBasePoints(loc.getBasePoints() + points);
				break;
			case SIEGE:
				loc.setSiegePoints(loc.getSiegePoints() + points);
				break;
			case COMMANDER:
				loc.setCommanderPoints(loc.getCommanderPoints() + points);
				break;
			case ARTIFACT:
				loc.setArtifactPoints(loc.getArtifactPoints() + points);
				break;
			case QUEST:
				loc.setQuestPoints(loc.getQuestPoints() + (points * questRate));
				break;
			case MONUMENT:
				loc.setMonumentsPoints(loc.getMonumentsPoints() + points);
				break;
			case FACILITY:
				loc.setFacilityPoints(loc.getFacilityPoints() + points);
				break;
			}
		} else {
			switch (type) {
			case BASE:
				if (loc.getBasePoints() < points) {
					return;
				} else {
					loc.setBasePoints(loc.getBasePoints() - points);
				}
				break;
			case SIEGE:
				if (loc.getSiegePoints() < points) {
					return;
				} else {
					loc.setSiegePoints(loc.getSiegePoints() - points);
				}
				break;
			case COMMANDER:
				if (loc.getCommanderPoints() < points) {
					return;
				} else {
					loc.setCommanderPoints(loc.getCommanderPoints() - points);
				}
				break;
			case ARTIFACT:
				if (loc.getArtifactPoints() < points) {
					return;
				} else {
					loc.setArtifactPoints(loc.getArtifactPoints() - points);
				}
				break;
			case QUEST:
				if (loc.getQuestPoints() < (points * questRate)) {
					return;
				} else {
					loc.setQuestPoints(loc.getQuestPoints() - (points * questRate));
				}
				break;
			case MONUMENT:
				if (loc.getMonumentsPoints() < points) {
					return;
				} else {
					loc.setMonumentsPoints(loc.getMonumentsPoints() - points);
				}
				break;
			case FACILITY:
				if (loc.getFacilityPoints() < points) {
					return;
				} else {
					loc.setFacilityPoints(loc.getFacilityPoints() - points);
				}
				break;
			}
		}
		int totalScore = loc.getArtifactPoints() + loc.getCommanderPoints() + loc.getFacilityPoints()
				+ loc.getBasePoints() + loc.getMonumentsPoints() + loc.getQuestPoints() + loc.getSiegePoints();
		loc.setPoints(totalScore);
		if (win) {
			checkRedemptionLanding(totalScore, true);
		} else {
			checkRedemptionLanding(totalScore, false);
		}
		onUpdate();
	}

	public void updateHarbingerLanding(int points, LandingPointsEnum type, boolean win) {
		LandingLocation loc = harbingerLanding();
		if (win) {
			switch (type) {
			case BASE:
				loc.setBasePoints(loc.getBasePoints() + points);
				break;
			case SIEGE:
				loc.setSiegePoints(loc.getSiegePoints() + points);
				break;
			case COMMANDER:
				loc.setCommanderPoints(loc.getCommanderPoints() + points);
				break;
			case ARTIFACT:
				loc.setArtifactPoints(loc.getArtifactPoints() + points);
				break;
			case QUEST:
				loc.setQuestPoints(loc.getQuestPoints() + (points * questRate));
				break;
			case MONUMENT:
				loc.setMonumentsPoints(loc.getMonumentsPoints() + points);
				break;
			case FACILITY:
				loc.setFacilityPoints(loc.getFacilityPoints() + points);
				break;
			}
		} else {
			switch (type) {
			case BASE:
				if (loc.getBasePoints() < points) {
					return;
				} else {
					loc.setBasePoints(loc.getBasePoints() - points);
				}
				break;
			case SIEGE:
				if (loc.getSiegePoints() < points) {
					return;
				} else {
					loc.setSiegePoints(loc.getSiegePoints() - points);
				}
				break;
			case COMMANDER:
				if (loc.getCommanderPoints() < points) {
					return;
				} else {
					loc.setCommanderPoints(loc.getCommanderPoints() - points);
				}
				break;
			case ARTIFACT:
				if (loc.getArtifactPoints() < points) {
					return;
				} else {
					loc.setArtifactPoints(loc.getArtifactPoints() - points);
				}
				break;
			case QUEST:
				if (loc.getQuestPoints() < (points * questRate)) {
					return;
				} else {
					loc.setQuestPoints(loc.getQuestPoints() - (points * questRate));
				}
				break;
			case MONUMENT:
				if (loc.getMonumentsPoints() < points) {
					return;
				} else {
					loc.setMonumentsPoints(loc.getMonumentsPoints() - points);
				}
				break;
			case FACILITY:
				if (loc.getFacilityPoints() < points) {
					return;
				} else {
					loc.setFacilityPoints(loc.getFacilityPoints() - points);
				}
				break;
			}
		}
		int totalScore = loc.getArtifactPoints() + loc.getCommanderPoints() + loc.getFacilityPoints()
				+ loc.getBasePoints() + loc.getMonumentsPoints() + loc.getQuestPoints() + loc.getSiegePoints();
		loc.setPoints(totalScore);
		if (win) {
			checkHarbingerLanding(totalScore, true);
		} else {
			checkHarbingerLanding(totalScore, false);
		}
		onUpdate();
	}

	public void AnnounceToPoints(final Player pl, final DescriptionId race, final DescriptionId name, final int points,
			final LandingPointsEnum type) {
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				switch (type) {
				case SIEGE:
					// %0 has occupied %0 and the Landing is now enhanced.
					PacketSendUtility.sendPacket(player,
							SM_SYSTEM_MESSAGE.STR_MSG_BUILDUP_NOTICE_CONTRIBUTE_USER_OCCUPY(race, name));
					break;
				case BASE:
					// %0 has occupied %1 Base and the Landing is now enhanced.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE
							.STR_MSG_BUILDUP_NOTICE_CONTRIBUTE_USER_OCCUPY_BASECAMP(race, name.toString()));
					break;
				case QUEST:
					// Completed quest has contributed %0 points to the Landing.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_BUILDUP_POINT_QUEST_GAIN(points));
					// %0's completed quest has enhanced the Landing.
					PacketSendUtility.sendPacket(player,
							SM_SYSTEM_MESSAGE.STR_MSG_BUILDUP_NOTICE_CONTRIBUTE_USER_QUEST(pl.getName()));
					break;
				}
			}
		});
	}

	public void checkRedemptionLanding(int points, boolean gain) {
		int level = 0;
		if (points >= 0 && points <= 199999) {
			level = 1;
		} else if (points >= 200000 && points <= 299999) {
			level = 2;
		} else if (points >= 300000 && points <= 399999) {
			level = 3;
		} else if (points >= 400000 && points <= 499999) {
			level = 4;
		} else if (points >= 500000 && points <= 599999) {
			level = 5;
		} else if (points >= 600000 && points <= 699999) {
			level = 6;
		} else if (points >= 700000 && points <= 799999) {
			level = 7;
		} else if (points >= 800000) {
			level = 8;
		}
		if (gain && level != redemptionLanding().getLevel()) {
			levelUpRedemptionLanding(level);
		}
		if (!gain && level != redemptionLanding().getLevel()) {
			onRedemptionLandingLevelDown(level);
		}
	}

	public void checkHarbingerLanding(int points, boolean gain) {
		int level = 0;
		if (points >= 0 && points <= 199999) {
			level = 1;
		} else if (points >= 200000 && points <= 299999) {
			level = 2;
		} else if (points >= 300000 && points <= 399999) {
			level = 3;
		} else if (points >= 400000 && points <= 499999) {
			level = 4;
		} else if (points >= 500000 && points <= 599999) {
			level = 5;
		} else if (points >= 600000 && points <= 699999) {
			level = 6;
		} else if (points >= 700000 && points <= 799999) {
			level = 7;
		} else if (points >= 800000) {
			level = 8;
		}
		if (gain && level != harbingerLanding().getLevel()) {
			levelUpHarbingerLanding(level);
		}
		if (!gain && level != harbingerLanding().getLevel()) {
			onHarbingerLandingLevelDown(level);
		}
	}

	public void levelUpRedemptionLanding(int level) {
		redemptionLanding().setLevel(level);
		stopLanding(redemptionLanding().getId());
		startLanding(redemptionLanding().getId());
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				// Landing Level Up.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ABYSS_OP_LEVEL_UP_LIGHT);
				PacketSendUtility.sendPacket(player,
						new SM_ABYSS_LANDING_LEVEL(0, redemptionLanding().getLevel(), redemptionLanding().getLevel()));
			}
		});
	}

	public void levelUpHarbingerLanding(int level) {
		harbingerLanding().setLevel(level);
		stopLanding(harbingerLanding().getId());
		startLanding(harbingerLanding().getId());
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				// Landing Level Up.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ABYSS_OP_LEVEL_UP_DARK);
				PacketSendUtility.sendPacket(player,
						new SM_ABYSS_LANDING_LEVEL(1, harbingerLanding().getLevel(), harbingerLanding().getLevel()));
			}
		});
	}

	public void onHarbingerLandingLevelDown(int level) {
		harbingerLanding().setLevel(level);
		stopLanding(harbingerLanding().getId());
		startLanding(harbingerLanding().getId());
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				// Landing Weakened.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ABYSS_OP_LEVEL_DOWN);
				PacketSendUtility.sendPacket(player,
						new SM_ABYSS_LANDING_LEVEL(1, harbingerLanding().getLevel(), harbingerLanding().getLevel()));
			}
		});
	}

	public void onRedemptionLandingLevelDown(int level) {
		redemptionLanding().setLevel(level);
		stopLanding(redemptionLanding().getId());
		startLanding(redemptionLanding().getId());
		World.getInstance().doOnAllPlayers(new Visitor<Player>() {
			@Override
			public void visit(Player player) {
				// Landing Weakened.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ABYSS_OP_LEVEL_DOWN);
				PacketSendUtility.sendPacket(player,
						new SM_ABYSS_LANDING_LEVEL(0, redemptionLanding().getLevel(), redemptionLanding().getLevel()));
			}
		});
	}

	/**
	 * MONUMENT
	 */
	public void onRewardMonuments(Race race, int id, int points) {
		LandingSpecialLocation lsl = AbyssLandingSpecialService.getInstance().getLandingSpecialLocation(id);
		if (race == Race.ASMODIANS) {
			updateHarbingerLanding(points, LandingPointsEnum.MONUMENT, true);
			AbyssLandingSpecialService.getInstance().startLanding(id);
		} else {
			updateRedemptionLanding(points, LandingPointsEnum.MONUMENT, true);
			AbyssLandingSpecialService.getInstance().startLanding(id);
		}
		lsl.setType(LandingSpecialStateType.ACTIVE);
		AbyssLandingSpecialService.onSave(lsl);
	}

	public void onDieMonuments(Race race, int id, int points) {
		LandingSpecialLocation lsl = AbyssLandingSpecialService.getInstance().getLandingSpecialLocation(id);
		if (race == Race.ELYOS) {
			updateRedemptionLanding(points, LandingPointsEnum.MONUMENT, true);
			updateHarbingerLanding(points, LandingPointsEnum.MONUMENT, false);
			stopLanding(id);
		} else {
			updateRedemptionLanding(points, LandingPointsEnum.MONUMENT, false);
			updateHarbingerLanding(points, LandingPointsEnum.MONUMENT, true);
			stopLanding(id);
		}
		lsl.setType(LandingSpecialStateType.NO_ACTIVE);
		AbyssLandingSpecialService.onSave(lsl);
	}

	/**
	 * COMMANDER
	 */
	public void onRewardCommander(Race race, int id, int points) {
		if (race == Race.ASMODIANS) {
			updateHarbingerLanding(points, LandingPointsEnum.COMMANDER, true);
			AbyssLandingSpecialService.getInstance().startLanding(id);
		} else {
			updateRedemptionLanding(points, LandingPointsEnum.COMMANDER, true);
			AbyssLandingSpecialService.getInstance().startLanding(id);
		}
	}

	public void onDieCommander(Race race, int id, int points) {
		if (race == Race.ELYOS) {
			updateRedemptionLanding(points, LandingPointsEnum.COMMANDER, true);
			updateHarbingerLanding(points, LandingPointsEnum.COMMANDER, false);
			AbyssLandingSpecialService.getInstance().stopLanding(id);
		} else {
			updateRedemptionLanding(points, LandingPointsEnum.COMMANDER, false);
			updateHarbingerLanding(points, LandingPointsEnum.COMMANDER, true);
			AbyssLandingSpecialService.getInstance().stopLanding(id);
		}
	}

	/**
	 * FACILITY
	 */
	public void onRewardFacility(Race race, int points) {
		if (race == Race.ASMODIANS) {
			updateHarbingerLanding(points, LandingPointsEnum.FACILITY, true);
			updateRedemptionLanding(points, LandingPointsEnum.FACILITY, false);
		} else {
			updateRedemptionLanding(points, LandingPointsEnum.FACILITY, true);
			updateHarbingerLanding(points, LandingPointsEnum.FACILITY, false);
		}
	}

	public void onEnterWorld(Player player) {
		PacketSendUtility.sendPacket(player, new SM_ABYSS_LANDING());
		PacketSendUtility.sendPacket(player,
				new SM_ABYSS_LANDING_LEVEL(0, redemptionLanding().getLevel(), redemptionLanding().getLevel()));
		PacketSendUtility.sendPacket(player,
				new SM_ABYSS_LANDING_LEVEL(1, harbingerLanding().getLevel(), harbingerLanding().getLevel()));
	}

	public void onUpdate() {
		getDAO().updateLocation(getLandingLocation(redemptionLanding().getId()));
		getDAO().updateLocation(getLandingLocation(harbingerLanding().getId()));
	}

	private AbyssLandingDAO getDAO() {
		return DAOManager.getDAO(AbyssLandingDAO.class);
	}

	public void sendPacketToPlayer(Player player) {
		PacketSendUtility.sendPacket(player, new SM_ABYSS_LANDING());
	}

	public static AbyssLandingService getInstance() {
		return AbyssLandingService.SingletonHolder.instance;
	}

	private static class SingletonHolder {
		protected static final AbyssLandingService instance = new AbyssLandingService();
	}

	public LandingLocation getLandingLocation(int id) {
		return abyssLanding.get(id);
	}

	public LandingLocation redemptionLanding() {
		return abyssLanding.get(1);
	}

	public LandingLocation harbingerLanding() {
		return abyssLanding.get(2);
	}

	public static Map<Integer, LandingLocation> getLandingLocations() {
		return abyssLanding;
	}
}