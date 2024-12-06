/*
 * Decompiled with CFR 0.150.
 */
package com.eleanor.processors.movement;

import com.aionemu.gameserver.geoEngine.math.Vector2f;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.geo.GeoService;
import com.eleanor.utils.GeomUtil;

public class PathfindHelper {
	private static final int VISIBLE_ANGLE = 180;
	private static final int PATHFIND_ANGLE_STEP = 20;

	public static Vector3f selectStep(Creature source, Vector3f target) {
		int mapId = source.getPosition().getMapId();
		int instanceId = source.getPosition().getInstanceId();
		float zOffset = source.getObjectTemplate().getBoundRadius().getUpper() / 2.0f;
		if ((double) zOffset > 2.2) {
			zOffset = 2.2f;
		}
		float newZOffset = Math.max(0.6f, source.getObjectTemplate().getBoundRadius().getUpper() * 0.7f);
		if (source.getTarget() instanceof Player) {
			newZOffset = 1.5f;
		}
		Vector3f sourcePoint = new Vector3f(source.getX(), source.getY(), source.getZ());
		Vector3f targetPoint = target.clone();
		double futureDistance = GeomUtil.getDistance3D(sourcePoint.x, sourcePoint.y, sourcePoint.z, targetPoint.x,
				targetPoint.y, targetPoint.z);
		int offset = VISIBLE_ANGLE / 2;
		int rounds = VISIBLE_ANGLE / PATHFIND_ANGLE_STEP + 1;

		Vector3f closetsPoint = null;
		double minimalDistance = Short.MAX_VALUE;

		for (int i = 0; i < rounds; ++i) {
			Vector3f rotated = PathfindHelper.Rotate(source, sourcePoint.x, sourcePoint.y, targetPoint.x, targetPoint.y,
					futureDistance, i * PATHFIND_ANGLE_STEP - offset, targetPoint.z);
			if (targetPoint.z - rotated.z > source.getObjectTemplate().getBoundRadius().getUpper() || rotated.z == 0.0f)
				continue;

			double newRotatedDistance = MathUtil.getDistance(sourcePoint.x, sourcePoint.y, sourcePoint.z, rotated.x,
					rotated.y, rotated.z);
			boolean canPassTemp = GeoService.getInstance().canPass(mapId, sourcePoint.x, sourcePoint.y,
					sourcePoint.z + zOffset, rotated.x, rotated.y, rotated.z + newZOffset, (float) newRotatedDistance,
					instanceId);
			if (!canPassTemp)
				continue;

			double canPassDistance = MathUtil.getDistance(targetPoint.x, targetPoint.y, targetPoint.z, rotated.x,
					rotated.y, rotated.z);
			if (!(minimalDistance > canPassDistance))
				break;
			minimalDistance = canPassDistance;
			closetsPoint = rotated;
		}
		return closetsPoint;
	}

	public static Vector3f selectFollowStep(Creature source, VisibleObject target) {
		int mapId = source.getPosition().getMapId();
		int instanceId = source.getPosition().getInstanceId();
		if (target.getPosition().getMapId() != mapId || target.getPosition().getInstanceId() != instanceId) {
			return null;
		}
		Vector3f point = new Vector3f(target.getX(), target.getY(), target.getZ());
		assert (point.x != 0.0f && point.y != 0.0f);
		return PathfindHelper.selectStep(source, point);
	}

	private static Vector3f Rotate(Creature owner, float cx, float cy, float x1, float y1, double radius, float degrees,
			float defaultZ) {
		double beginDeg = Math.toDegrees(Math.atan2(y1 - cy, x1 - cx));
		degrees = (float) ((double) degrees + beginDeg);
		double x = (double) cx + radius * Math.cos((double) degrees * Math.PI / 180.0);
		double y = (double) cy + radius * Math.sin((double) degrees * Math.PI / 180.0);
		double z = GeoService.getInstance().getZ(owner.getWorldId(), (float) x, (float) y, defaultZ, 100.0f,
				owner.getInstanceId());
		return new Vector3f((float) x, (float) y, (float) z);
	}

	public static Vector3f getRandomPoint(Creature source, float minRange, float maxRange) {
		Vector3f origin = new Vector3f(source.getX(), source.getY(), source.getZ());
		assert (minRange > 0.0f && maxRange > minRange);
		int SearchAngle = 360;
		int AngleStep = 60;
		int randDist = (int) (Math.random() * (double) maxRange + (double) minRange);
		int randAngle = (int) (Math.random() * 360.0);
		for (int i = 0; i < SearchAngle; i += AngleStep) {
			Vector2f rotated2D = GeomUtil.getNextPoint2D(new Vector2f(origin.x, origin.y), randAngle + i, randDist);
		}
		return null;
	}
}
