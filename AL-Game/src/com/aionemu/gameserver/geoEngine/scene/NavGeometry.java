/**
 * This file is part of the Aion Reconstruction Project Server.
 *
 * The Aion Reconstruction Project Server is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * The Aion Reconstruction Project Server is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with the Aion Reconstruction Project Server. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 * @AionReconstructionProjectTeam
 */
package com.aionemu.gameserver.geoEngine.scene;

import com.aionemu.gameserver.geoEngine.bounding.BoundingBox;
import com.aionemu.gameserver.geoEngine.bounding.BoundingVolume;
import com.aionemu.gameserver.geoEngine.bounding.Intersection;
import com.aionemu.gameserver.geoEngine.collision.Collidable;
import com.aionemu.gameserver.geoEngine.collision.CollisionResult;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.collision.UnsupportedCollisionException;
import com.aionemu.gameserver.geoEngine.math.Matrix3f;
import com.aionemu.gameserver.geoEngine.math.Ray;
import com.aionemu.gameserver.geoEngine.math.Triangle;
import com.aionemu.gameserver.geoEngine.math.Vector3f;

/**
 * A simple extension of {@link Spatial}. This class represents a single node
 * of a Nav Mesh. Only Triangular nodes are supported. It's assumed that none of
 * the nodes given to this class have a plane that is parallel to the (vertical) Z-axis.
 * <p>
 * A reference to neighboring nodes is maintained for use while pathfinding later,
 * along with an {@link #data incenter} value to estimate how far away this node is from other points,
 * and an {@link #data inRad} value to act as the path length off of this node.
 * 
 * @author Yon (Aion Reconstruction Project)
 */
public class NavGeometry extends Spatial {
	
	private NavGeometry edge1;
	private NavGeometry edge2;
	private NavGeometry edge3;
	/**
	 * Contains 3 vertices in the first 9 indices (0 ~> 8), the incenter of this triangle in the next three indices (9 ~> 11),
	 * and the the radius of the incircle in the last index (12).
	 */
	final private float[] data;
//	final public float inRad;
//	final public float[] incenter;
	
	public NavGeometry(String name, float[] verts) {
		assert verts.length == 9:"NavGeometry does not support non-triangle nodes!";
		this.data = new float[13];
		System.arraycopy(verts, 0, this.data, 0, verts.length);
		
		float[] p1 = getVertex(0);
		float[] p2 = getVertex(1);
		float[] p3 = getVertex(2);
		float[] edge1 = {p2[0] - p1[0], p2[1] - p1[1], p2[2] - p1[2]};
		float[] edge2 = {p3[0] - p2[0], p3[1] - p2[1], p3[2] - p2[2]};
		float[] edge3 = {p1[0] - p3[0], p1[1] - p3[1], p1[2] - p3[2]};
		float edge1Len = (float) Math.sqrt(sumOfSquaredComps(edge1));
		float edge2Len = (float) Math.sqrt(sumOfSquaredComps(edge2));
		float edge3Len = (float) Math.sqrt(sumOfSquaredComps(edge3));
		float lenSum = edge1Len + edge2Len + edge3Len;
		float[] incenter = new float[] {((edge2Len*p1[0]) + (edge3Len*p2[0]) + (edge1Len*p3[0]))/lenSum,
		                        ((edge2Len*p1[1]) + (edge3Len*p2[1]) + (edge1Len*p3[1]))/lenSum,
		                        ((edge2Len*p1[2]) + (edge3Len*p2[2]) + (edge1Len*p3[2]))/lenSum};
		System.arraycopy(incenter, 0, this.data, 9, incenter.length);
		float inRad = ((float) Math.sqrt(lenSum*(lenSum - edge1Len)*(lenSum - edge2Len)*(lenSum - edge3Len)))/lenSum;
		this.data[12] = inRad;
	}
	
	private float sumOfSquaredComps(float[] vec) {
		return (vec[0]*vec[0]) + (vec[1]*vec[1]) + (vec[2]*vec[2]);
	}
	
	public void setEdge1(NavGeometry connection) {
		edge1 = connection;
	}
	
	public void setEdge2(NavGeometry connection) {
		edge2 = connection;
	}
	
	public void setEdge3(NavGeometry connection) {
		edge3 = connection;
	}
	
	public NavGeometry getEdge1() {
		return edge1;
	}
	
	public NavGeometry getEdge2() {
		return edge2;
	}
	
	public NavGeometry getEdge3() {
		return edge3;
	}
	
	public byte getEdgeMatching(NavGeometry tri) {
		if (edge1 == tri) return 1;
		if (edge2 == tri) return 2;
		if (edge3 == tri) return 3;
		return 0;
	}
	
	/**
	 * Finds the closest point on this {@link NavGeometry} to the given point.
	 * <p>
	 * Note: This is not currently implemented, and will instead return the closest
	 * vertex to the given point, or the incenter if it's closer than any vertex.
	 * 
	 * @param x -- the x-component of the given point.
	 * @param y -- the y-component of the given point.
	 * @param z -- the z-component of the given point.
	 * @return A float[] containing the x, y, z components, in that order, of
	 * the closest point on this {@link NavGeometry} to the given point.
	 */
	public float[] getClosestPoint(float x, float y, float z) {
		//FIXME: Implement proper algorithm; consider com.aionemu.gameserver.geoEngine.math.Plane#getClosestPoint(Vector3f)
		float[] p2 = {x, y, z};
		float[] incenter = new float[] {data[9], data[10], data[11]};
		float v1 = euclideanDistance(getVertex(0), p2);
		float v2 = euclideanDistance(getVertex(1), p2);
		float v3 = euclideanDistance(getVertex(2), p2);
		float in = euclideanDistance(incenter, p2);
		float min = Math.min(v1, Math.min(v2, Math.min(v3, in)));
		if (min == v1) return getVertex(0);
		if (min == v2) return getVertex(1);
		if (min == v3) return getVertex(2);
		return incenter;
	}
	
	public float[] getVertex(int i) {
		return new float[] {data[i*3], data[i*3 + 1], data[i*3 + 2]};
	}
	
	public float[][] getEndpoints(byte edge) {
		float[][] ret = new float[2][];
		switch (edge) {
		case 1:
			ret[0] = new float[] {data[0], data[1], data[2]};
			ret[1] = new float[] {data[3], data[4], data[5]};
			break;
		case 2:
			ret[0] = new float[] {data[3], data[4], data[5]};
			ret[1] = new float[] {data[6], data[7], data[8]};
			break;
		case 3:
			ret[0] = new float[] {data[6], data[7], data[8]};
			ret[1] = new float[] {data[0], data[1], data[2]};
			break;
		default:
			assert false:"NavGeometry: Unknown edge: " + edge;
			return null;
		}
		return ret;
	}
	
	public float getInRad() {
		return data[12];
	}
	
	public float getPriority(float x, float y, float z) {
		float[] incenter = new float[] {data[9], data[10], data[11]};
		float dx = Math.abs(incenter[0] - x);
		float dy = Math.abs(incenter[1] - y);
		float dz = Math.abs(incenter[2] - z);
		return dx + dy + dz;
	}
	
	private float euclideanDistance(float[] p1, float[] p2) {
		float dx = Math.abs(p1[0] - p2[0]);
		float dy = Math.abs(p1[1] - p2[1]);
		float dz = Math.abs(p1[2] - p2[2]);
		return dx + dy + dz; 
	}
	
	public boolean isTowardsEdge(byte edge, float[] vec) {
		float[] p0 = new float[] {data[0], data[1]};
		float[] p1 = new float[] {data[3], data[4]};
		float[] p2 = new float[] {data[6], data[7]};
		float[] vec1;
		float[] vec2;
		float[] vec3;
		switch (edge) {
		case 1:
			//Edge 1 is point 0 and 1
			vec1 = new float[] {p0[0] - p2[0], p0[1] - p2[1]};
			vec2 = new float[] {p1[0] - p2[0], p1[1] - p2[1]};
			vec3 = new float[] {vec[0] - p2[0], vec[1] - p2[1]};
			break;
		case 2:
			//Edge 2 is point 1 and 2
			vec1 = new float[] {p1[0] - p0[0], p1[1] - p0[1]};
			vec2 = new float[] {p2[0] - p0[0], p2[1] - p0[1]};
			vec3 = new float[] {vec[0] - p0[0], vec[1] - p0[1]};
			break;
		case 3:
			//Edge 3 is point 2 and 0
			vec1 = new float[] {p2[0] - p1[0], p2[1] - p1[1]};
			vec2 = new float[] {p0[0] - p1[0], p0[1] - p1[1]};
			vec3 = new float[] {vec[0] - p1[0], vec[1] - p1[1]};
			break;
		default:
			return false;
		}
		boolean positive = crossZ(vec1, vec2) > 0;
		if (compareCross(crossZ(vec1, vec3), positive) && compareCross(crossZ(vec3, vec2), positive)) {
			return true; //vec3 is between vec1 and vec2
		}
		return false;
	}
	
	/**
	 * Creates a funnel to the specified edge's endpoints from the specified starting point and
	 * compares the given direction vector to the walls of the funnel. If the direction vector
	 * is between or on the walls of the funnel, this method will return true, false otherwise.
	 * <p>
	 * If an invalid edge number is passed in, edge 3 will be considered.
	 * 
	 * @param edge - The number corresponding to the edge in question (valid range is 1 - 3)
	 * @param dir - A vector from the starting point of the funnel to the destination
	 * @param x - The starting point of the funnel's x component
	 * @param y - The starting point of the funnel's y component
	 * @return True if the direction vector passes through the specified edge, false otherwise.
	 */
	public boolean isFunnelTowardsEdge(byte edge, float[] dir, float x, float y) {
		float[][] endpoints;
		if (edge == 1) {
			endpoints = getEndpoints((byte) 1);
		} else if (edge == 2) {
			endpoints = getEndpoints((byte) 2);
		} else {
			endpoints = getEndpoints((byte) 3);
		}
		
		float[] vec1 = new float[] {endpoints[0][0] - x, endpoints[0][1] - y};
		float[] vec2 = new float[] {endpoints[1][0] - x, endpoints[1][1] - y};
		boolean positive = crossZ(vec1, vec2) > 0;
		if (compareCross(crossZ(vec1, dir), positive) && compareCross(crossZ(dir, vec2), positive)) {
			return true; //dir is between vec1 and vec2
		}
		return false;
	}
	
	private static float crossZ(float[] vec1, float[] vec2/*, float x1, float y1, float x2, float y2*/) {
		return ((vec1[0] * vec2[1]) - (vec1[1] * vec2[0]));
//		return ((x1 * y2) - (y1 * x2));
	}
	
	private static boolean compareCross(float crossZ, boolean positive) {
		if (positive) {
			return crossZ >= 0;
		} else {
			return crossZ <= 0;
		}
	}
	
	@Override
	public int collideWith(Collidable other, CollisionResults results) throws UnsupportedCollisionException {
//		if ((results.getIntentions() & (getCollisionFlags() >> 8)) == 0) return 0; //This is assumed
		
		if (other instanceof Ray) {
			if (!worldBound.intersects(((Ray) other))) {
				return 0;
			}
			Vector3f intersection = Vector3f.newInstance();
			Vector3f p1 = Vector3f.newInstance().set(data[0], data[1], data[2]);
			Vector3f p2 = Vector3f.newInstance().set(data[3], data[4], data[5]);
			Vector3f p3 = Vector3f.newInstance().set(data[6], data[7], data[8]);
			Triangle tri = Triangle.newInstance();
			tri.set(p1, p2, p3);
			if (((Ray) other).intersectWhere(tri, intersection)) {
				Vector3f displacement = intersection.subtract(((Ray) other).getOrigin());
				float distance = displacement.length();
				if (distance > ((Ray) other).limit) {
					Triangle.recycle(tri);
					Vector3f.recycle(p1);
					Vector3f.recycle(p2);
					Vector3f.recycle(p3);
					Vector3f.recycle(displacement);
					return 0;
				}
				
				CollisionResult res = new CollisionResult();
				res.setContactPoint(intersection);
				res.setGeometry(this);
				res.setDistance(distance);
				results.addCollision(res);
				
				Triangle.recycle(tri);
				Vector3f.recycle(p1);
				Vector3f.recycle(p2);
				Vector3f.recycle(p3);
				Vector3f.recycle(displacement);
				return 1;
			}
			Triangle.recycle(tri);
			Vector3f.recycle(p1);
			Vector3f.recycle(p2);
			Vector3f.recycle(p3);
			Vector3f.recycle(intersection);
			return 0;
		} else if (other instanceof BoundingBox) {
			if (worldBound.intersects((BoundingBox) other)) {
				Vector3f p1 = Vector3f.newInstance().set(data[0], data[1], data[2]);
				Vector3f p2 = Vector3f.newInstance().set(data[3], data[4], data[5]);
				Vector3f p3 = Vector3f.newInstance().set(data[6], data[7], data[8]);
				if (Intersection.intersect(((BoundingBox) other), p1, p2, p3)) {
					CollisionResult res = new CollisionResult();
					res.setGeometry(this);
					res.setDistance(worldBound.getCenter().distance(((BoundingBox) other).getCenter()));
					results.addCollision(res);
					Vector3f.recycle(p1);
					Vector3f.recycle(p2);
					Vector3f.recycle(p3);
					return 1;
				}
				Vector3f.recycle(p1);
				Vector3f.recycle(p2);
				Vector3f.recycle(p3);
			}
			return 0;
		} else {
			throw new UnsupportedCollisionException();
		}
	}
	
	@Override
	public void updateModelBound() {
		Vector3f min = new Vector3f();
		Vector3f max = new Vector3f();
		float[] vert1 = getVertex(0);
		float[] vert2 = getVertex(1);
		float[] vert3 = getVertex(2);
		min.setX(Math.min(vert1[0], Math.min(vert2[0], vert3[0])));
		min.setY(Math.min(vert1[1], Math.min(vert2[1], vert3[1])));
		min.setZ(Math.min(vert1[2], Math.min(vert2[2], vert3[2])));
		
		max.setX(Math.max(vert1[0], Math.max(vert2[0], vert3[0])));
		max.setY(Math.max(vert1[1], Math.max(vert2[1], vert3[1])));
		max.setZ(Math.max(vert1[2], Math.max(vert2[2], vert3[2])));
		
		if (worldBound instanceof BoundingBox) {
			((BoundingBox) worldBound).setMinMax(min, max);
		} else {
			worldBound = new BoundingBox(min, max);
		}
	}
	
	@Override
	public void setModelBound(BoundingVolume modelBound) {
		this.worldBound = modelBound;
	}
	
	@Override
	public int getVertexCount() {
		return 3;
	}
	
	@Override
	public int getTriangleCount() {
		return 1;
	}
	
	@Override
	public short getCollisionFlags() {
		return 0x100;
	}
	
	@Override
	public void setCollisionFlags(short flags) {
		return;
	}
	
	@Override
	public void setTransform(Matrix3f rotation, Vector3f loc, float scale) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String toString() {
		float[] incenter = new float[] {data[9], data[10], data[11]};
		return "(" + incenter[0] + ", " + incenter[1] + ", " + incenter[2] + ") " + super.toString();
	}
	
}
