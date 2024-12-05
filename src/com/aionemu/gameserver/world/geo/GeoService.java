/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javolution.util.FastList
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.aionemu.gameserver.world.geo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;

import javolution.util.FastList;

public class GeoService {
	private static final Logger log = LoggerFactory.getLogger(GeoService.class);
	private static final FastList<Integer> npcsExclude = new FastList<>();
	private GeoData geoData;

	public static FastList<Integer> getNpcsExclude() {
		return npcsExclude;
	}

	public static final GeoService getInstance() {
		return SingletonHolder.instance;
	}

	public void initializeGeo() {
		switch (this.getConfiguredGeoType()) {
		case GEO_MESHES: {
			this.geoData = new RealGeoData();
			break;
		}
		case NO_GEO: {
			this.geoData = new DummyGeoData();
		}
		}
		log.info("Configured Geo type: ");

		if (GeoDataConfig.GEO_MONONO2_IN_USE) {
			log.info("MONONO2 GEO: active in geodata.properties, you must use only MONONO2 GEODATA here |data|geo");
		} else {
			log.info(
					"MONONO2 GEO: deactivated in geodata.properties, you must use only STANDART GEODATA here |data|geo");
		}

		this.geoData.loadGeoMaps();
	}

	public void setDoorState(int worldId, int instanceId, String name, boolean isOpened) {
		if (GeoDataConfig.GEO_ENABLE) {
			this.geoData.getMap(worldId).setDoorState(instanceId, name, isOpened);
		}
	}

	public float getZAfterMoveBehind(int worldId, float x, float y, float z, int instanceId) {
		if (GeoDataConfig.GEO_ENABLE) {
			return this.getZ(worldId, x, y, z, 0.0f, instanceId);
		}
		return this.getZ(worldId, x, y, z, 0.5f, instanceId);
	}

	public float getZ(VisibleObject object) {
		float newZ = this.geoData.getMap(object.getWorldId()).getZ(object.getX(), object.getY(), object.getZ(),
				object.getInstanceId());
		if (GeoDataConfig.GEO_ENABLE) {
			newZ += 0.001f;
		}
		return newZ;
	}

	public float getZ(int worldId, float x, float y, float z, float defaultUp, int instanceId) {
		float newZ = this.geoData.getMap(worldId).getZ(x, y, z, instanceId);
		if (GeoDataConfig.GEO_ENABLE && defaultUp != 100.0f) {
			newZ += 0.001f;
		}
		return newZ;
	}

	public float getZW(int worldId, float x, float y, float z, float defaultUp, int instanceId) {
		float newZ = this.geoData.getMap(worldId).getZW(x, y, z, instanceId);
		if (GeoDataConfig.GEO_ENABLE && defaultUp != 100.0f) {
			newZ += 0.001f;
		}
		return newZ;
	}

	public float getZ(int worldId, float x, float y) {
		float newZ = this.geoData.getMap(worldId).getZ(x, y);
		if (GeoDataConfig.GEO_ENABLE) {
			newZ += 0.001f;
		}
		return newZ;
	}

	public float getZW(int worldId, float x, float y) {
		float newZ = this.geoData.getMap(worldId).getZW(x, y);
		if (GeoDataConfig.GEO_ENABLE) {
			newZ += 0.001f;
		}
		return newZ;
	}

	public String getDoorName(int worldId, String meshFile, float x, float y, float z) {
		return this.geoData.getMap(worldId).getDoorName(worldId, meshFile, x, y, z);
	}

	public CollisionResults getCollisions(VisibleObject object, float x, float y, float z, boolean changeDirection,
			byte intentions) {
		return this.geoData.getMap(object.getWorldId()).getCollisions(object.getX(), object.getY(),
				object.getZ() - 0.6f, x, y, z, changeDirection, true, object.getInstanceId(), intentions);
	}

	public boolean canSee(VisibleObject object, VisibleObject target) {
		if (!GeoDataConfig.CANSEE_ENABLE) {
			return true;
		}
		if (object.getWorldId() == 301110000 || object.getWorldId() == 301360000) {
			return true;
		}
		float limit = (float) (MathUtil.getDistance(object, target)
				- (double) target.getObjectTemplate().getBoundRadius().getCollision());
		if (limit <= 0.0f) {
			return true;
		}
		float upperTarget = target.getObjectTemplate().getBoundRadius().getUpper() / 2.0f;
		if ((double) upperTarget > 2.2) {
			upperTarget = 2.2f;
		}
		float objectUp = object.getObjectTemplate().getBoundRadius().getUpper() / 2.0f;
		if (object instanceof Player) {
			objectUp = 1.5f;
		} else if (target instanceof Player) {
			upperTarget = 1.5f;
		}
		return this.geoData.getMap(object.getWorldId()).canSee(object.getX(), object.getY(), object.getZ() + objectUp,
				target.getX(), target.getY(), target.getZ() + upperTarget, limit, object.getInstanceId());
	}

	public boolean canPass(VisibleObject object, VisibleObject target) {
		float limit = (float) (MathUtil.getDistance(object, target)
				- (double) target.getObjectTemplate().getBoundRadius().getCollision());
		if (limit <= 0.0f) {
			return true;
		}
		float upperTarget = target.getObjectTemplate().getBoundRadius().getUpper() / 2.0f;
		if ((double) upperTarget > 2.2) {
			upperTarget = 2.2f;
		}
		float objectUp = object.getObjectTemplate().getBoundRadius().getUpper() / 2.0f;
		if (object instanceof Player) {
			objectUp = 1.5f;
		} else if (target instanceof Player) {
			upperTarget = 1.5f;
		}
		return this.geoData.getMap(object.getWorldId()).canPass(object.getX(), object.getY(), object.getZ() + objectUp,
				target.getX(), target.getY(), target.getZ() + upperTarget, limit, object.getInstanceId());
	}

	public boolean canSee(int worldId, float x, float y, float z, float x1, float y1, float z1, float limit,
			int instanceId) {
		if (worldId == 301110000 || worldId == 301360000) {
			return true;
		}
		return this.geoData.getMap(worldId).canSee(x, y, z, x1, y1, z1, limit, instanceId);
	}

	public boolean canPass(int worldId, float x, float y, float z, float x1, float y1, float z1, float limit,
			int instanceId) {
		return this.geoData.getMap(worldId).canPass(x, y, z, x1, y1, z1, limit, instanceId);
	}

	public boolean canPassWalker(int worldId, float x, float y, float z, float x1, float y1, float z1, float limit,
			int instanceId) {
		return this.geoData.getMap(worldId).canPassWalker(x, y, z, x1, y1, z1, limit, instanceId);
	}

	public boolean isGeoOn() {
		return GeoDataConfig.GEO_ENABLE;
	}

	public Vector3f getClosestCollision(Creature object, float x, float y, float z, boolean changeDirection,
			byte intentions) {
		return this.geoData.getMap(object.getWorldId()).getClosestCollision(object.getX(), object.getY(),
				object.getZ() - 0.6f, x, y, z, changeDirection, object.isInFlyingState(), object.getInstanceId(),
				intentions);
	}

	public GeoType getConfiguredGeoType() {
		if (GeoDataConfig.GEO_ENABLE) {
			return GeoType.GEO_MESHES;
		}
		return GeoType.NO_GEO;
	}

	private static final class SingletonHolder {
		protected static final GeoService instance = new GeoService();

		private SingletonHolder() {
		}
	}
}
