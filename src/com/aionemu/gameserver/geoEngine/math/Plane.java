/*
 * Decompiled with CFR 0.150.
 */
package com.aionemu.gameserver.geoEngine.math;

import java.util.logging.Logger;

public class Plane implements Cloneable {
	private static final Logger logger = Logger.getLogger(Plane.class.getName());
	protected Vector3f normal;
	protected float constant;

	public Plane() {
		this.normal = new Vector3f();
	}

	public Plane(Vector3f normal, float constant) {
		if (normal == null) {
			logger.warning("Normal was null, created default normal.");
			normal = new Vector3f();
		}
		this.normal = normal;
		this.constant = constant;
	}

	public void setNormal(Vector3f normal) {
		if (normal == null) {
			logger.warning("Normal was null, created default normal.");
			normal = new Vector3f();
		}
		this.normal.set(normal);
	}

	public void setNormal(float x, float y, float z) {
		if (this.normal == null) {
			logger.warning("Normal was null, created default normal.");
			this.normal = new Vector3f();
		}
		this.normal.set(x, y, z);
	}

	public Vector3f getNormal() {
		return this.normal;
	}

	public void setConstant(float constant) {
		this.constant = constant;
	}

	public float getConstant() {
		return this.constant;
	}

	public Vector3f getClosestPoint(Vector3f point, Vector3f store) {
		float t = (this.constant - this.normal.dot(point)) / this.normal.dot(this.normal);
		return store.set(this.normal).multLocal(t).addLocal(point);
	}

	public Vector3f getClosestPoint(Vector3f point) {
		return this.getClosestPoint(point, new Vector3f());
	}

	public Vector3f reflect(Vector3f point, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		float d = this.pseudoDistance(point);
		store.set(this.normal).negateLocal().multLocal(d * 2.0f);
		store.addLocal(point);
		return store;
	}

	public float pseudoDistance(Vector3f point) {
		return this.normal.dot(point) - this.constant;
	}

	public Side whichSide(Vector3f point) {
		float dis = this.pseudoDistance(point);
		if (dis < 0.0f) {
			return Side.Negative;
		}
		if (dis > 0.0f) {
			return Side.Positive;
		}
		return Side.None;
	}

	public boolean isOnPlane(Vector3f point) {
		float dist = this.pseudoDistance(point);
		return dist < 1.1920929E-7f && dist > -1.1920929E-7f;
	}

	public void setPlanePoints(AbstractTriangle t) {
		this.setPlanePoints(t.get1(), t.get2(), t.get3());
	}

	public void setOriginNormal(Vector3f origin, Vector3f normal) {
		this.normal.set(normal);
		this.constant = normal.x * origin.x + normal.y * origin.y + normal.z * origin.z;
	}

	public void setPlanePoints(Vector3f v1, Vector3f v2, Vector3f v3) {
		this.normal.set(v2).subtractLocal(v1);
		this.normal.crossLocal(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z).normalizeLocal();
		this.constant = this.normal.dot(v1);
	}

	public String toString() {
		return this.getClass().getSimpleName() + " [Normal: " + this.normal + " - Constant: " + this.constant + "]";
	}

	public Class<? extends Plane> getClassTag() {
		return this.getClass();
	}

	public Plane clone() {
		try {
			Plane p = (Plane) super.clone();
			p.normal = this.normal.clone();
			return p;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	public static enum Side {
		None, Positive, Negative;

	}
}
