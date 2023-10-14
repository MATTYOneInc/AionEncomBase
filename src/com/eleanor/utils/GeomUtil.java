/*
 * Decompiled with CFR 0.150.
 */
package com.eleanor.utils;

import com.aionemu.gameserver.geoEngine.math.FastMath;
import com.aionemu.gameserver.geoEngine.math.Vector2f;
import com.aionemu.gameserver.geoEngine.math.Vector3f;

public class GeomUtil {
	public static Vector2f getNextPoint2D(Vector2f source, float angle, float distance) {
		double x = (double) source.x + (double) distance * Math.cos((double) angle * Math.PI / 180.0);
		double y = (double) source.y + (double) distance * Math.sin((double) angle * Math.PI / 180.0);
		return new Vector2f((float) x, (float) y);
	}

	public static Vector2f getNextPoint2D(float sX, float sY, float vecX, float vecY, float distance) {
		return new Vector2f(sX + vecX * distance, sY + vecY * distance);
	}

	public static Vector3f getDirection3D(Vector3f from, Vector3f to) {
		Vector3f direction = to.subtract(from);
		return direction.normalizeLocal();
	}

	public static Vector3f getNextPoint3D(Vector3f source, Vector3f direction, float distance) {
		return source.add(direction.mult(distance));
	}

	public static float getDistance3D(Vector3f source, float x2, float y2, float z2) {
		return GeomUtil.getDistance3D(source.x, source.y, source.z, x2, y2, z2);
	}

	public static float getDistance3D(float x1, float y1, float z1, float x2, float y2, float z2) {
		double dx = x1 - x2;
		double dy = y1 - y2;
		double dz = z1 - z2;
		return FastMath.sqrt((float) (dx * dx + dy * dy + dz * dz));
	}
}
