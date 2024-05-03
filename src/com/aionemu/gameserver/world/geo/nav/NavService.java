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
package com.aionemu.gameserver.world.geo.nav;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.geoEngine.bounding.BoundingBox;
import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.math.Ray;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.geoEngine.models.GeoMap;
import com.aionemu.gameserver.geoEngine.scene.NavGeometry;
import com.aionemu.gameserver.geoEngine.scene.Spatial;
import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * Similar to {@link com.aionemu.gameserver.world.geo.GeoService GeoService}, this class is the entry point
 * for navigational queries (it's used for pathfinding).
 * 
 * @author Yon (Aion Reconstruction Project)
 */
public final class NavService {
	
	private static final Logger LOG = LoggerFactory.getLogger(NavService.class);
	private final NavData navData = NavData.getInstance();
	
	private NavService() {};
	
	public void initializeNav() {
		if (GeoDataConfig.GEO_NAV_ENABLE) {
			LOG.info("Navigational Data is Enabled.");
			if (!navData.isLoaded()) {
				navData.loadNavMaps();
			} else {
				LOG.warn("Attempted Double Loading of Navigational Data.");
			}
		} else {
			LOG.info("Navigational Data is Disabled.");
		}
	}
	
	/**
	 * This method checks if one entity (creature) can pull (forcibly move) another (target) by checking if the
	 * navmesh is continuous between the creature and the target. This method will immediately return true if the
	 * target is flying, will otherwise return true if the navmesh is continuous in a straight line between the two
	 * given entities, and will return false if no such continuous path exists. As a side-effect of how pathfinding is
	 * implemented, this method may also return false if the creature is flying too far above the navmesh; since this mimics
	 * retail behaviour, it will be left as is.
	 * <p>
	 * It's assumed that the entities can see eachother.
	 * <p>
	 * If {@link GeoDataConfig#GEO_NAV_ENABLE} is set to false, this method will immediately return true.
	 * 
	 * @param creature -- The entity attempting to pull {@code target}.
	 * @param target -- The target entity that {@code creature} is attempting to pull.
	 * @return true if the target can be pulled, false otherwise.
	 */
	public boolean canPullTarget(Creature creature, Creature target) {
		if (!GeoDataConfig.GEO_NAV_ENABLE) return true;
		if (target.isFlying()) return true;
		float x1 = creature.getX(), y1 = creature.getY(), z1 = creature.getZ();
		NavGeometry tile1 = getNavTile(creature.getWorldId(), x1, y1, z1);
		if (tile1 == null) {
			tile1 = getNavTileWithBox(creature.getWorldId(), x1, y1, z1);
			if (tile1 == null) return false;
		}
		float x2 = target.getX(), y2 = target.getY(), z2 = target.getZ();
		NavGeometry tile2 = getNavTile(target.getWorldId(), x2, y2, z2);
		if (tile2 == null) {
			tile2 = getNavTileWithBox(target.getWorldId(), x2, y2, z2);
			if (tile2 == null) return false;
		}
		//They're flipped around because the path needs to exist from the target (though it doesn't actually matter)
		float[][] path = attemptStraightLinePath(tile2, tile1, x2, y2, z2, x1, y1, z1);
		if (path != null && path.length == 1) return true;
		return false;
	}
	
	private float[][] attemptStraightLinePath(NavGeometry tile1, NavGeometry tile2, float x1, float y1, float z1, float x2, float y2, float z2) {
		//basic checks
		assert tile1 != null:"NavService#validateStraightLinePath() tile1 is null!";
		if (tile2 == null) return null;
		if (tile1 == tile2) return new float[][] {{x2, y2, z2}};
		//Attempt to funnel to the target without a path
		float[] targetDir = new float[] {x2 - x1, y2 - y1};
		NavGeometry last = tile1;
		NavGeometry current;
		if (last.isFunnelTowardsEdge((byte) 1, targetDir, x1, y1)) {
			current = last.getEdge1();
		} else if (last.isFunnelTowardsEdge((byte) 2, targetDir, x1, y1)) {
			current = last.getEdge2();
		} else if (last.isFunnelTowardsEdge((byte) 3, targetDir, x1, y1)) {
			current = last.getEdge3();
		} else {
			return null;
		}
		int triCount = 0;
		while (triCount < 50 && current != tile2 && current != null) {
			triCount++;
			switch (current.getEdgeMatching(last)) {
				case 0:
					return null;
				case 1:
					//check edge 2 and 3
					if (current.isFunnelTowardsEdge((byte) 2, targetDir, x1, y1)) {
						last = current;
						current = current.getEdge2();
						continue;
					}
					if (current.isFunnelTowardsEdge((byte) 3, targetDir, x1, y1)) {
						last = current;
						current = current.getEdge3();
						continue;
					}
					break;
				case 2:
					//check edge 1 and 3
					if (current.isFunnelTowardsEdge((byte) 1, targetDir, x1, y1)) {
						last = current;
						current = current.getEdge1();
						continue;
					}
					if (current.isFunnelTowardsEdge((byte) 3, targetDir, x1, y1)) {
						last = current;
						current = current.getEdge3();
						continue;
					}
					break;
				case 3:
					//check edge 1 and 2
					if (current.isFunnelTowardsEdge((byte) 1, targetDir, x1, y1)) {
						last = current;
						current = current.getEdge1();
						continue;
					}
					if (current.isFunnelTowardsEdge((byte) 2, targetDir, x1, y1)) {
						last = current;
						current = current.getEdge2();
						continue;
					}
					break;
			}
			return null;
		}
		if (current == tile2) {
			return funnelPathway(new NavPathway[] {new NavPathway(current, current.getEdgeMatching(last))}, true, x1, y1, z1, x2, y2, z2);
		}
		return null;
	}
	
	public float[][] navigateToTarget(Creature pathOwner, Creature target) {
		//basic checks
		if (pathOwner == null) return null;
		if (pathOwner.getLifeStats().isAlreadyDead()) return null;
		if (target == null) return null;
//		if (target.getLifeStats().isAlreadyDead()) return null;
		if (pathOwner.getWorldId() != target.getWorldId()) return null;
		
		int worldId = pathOwner.getWorldId();
		float x1 = pathOwner.getX(), y1 = pathOwner.getY(), z1 = pathOwner.getZ();
		float x2 = target.getX(), y2 = target.getY(), z2 = target.getZ();
		//TO-DO: Use Cached Tile for Creature
		return navigateFromLocationToLocation(worldId, null, null, x1, y1, z1, x2, y2, z2);
	}
	
	public float[][] navigateToLocation(Creature pathOwner, float x, float y, float z) {
		//basic checks
		if (pathOwner == null) return null;
		if (pathOwner.getLifeStats().isAlreadyDead()) return null;
		int worldId = pathOwner.getWorldId();
		float x1 = pathOwner.getX(), y1 = pathOwner.getY(), z1 = pathOwner.getZ();
		//TO-DO: Use Cached Tile for Creature
		return navigateFromLocationToLocation(worldId, null, null, x1, y1, z1, x, y, z);
	}
	
	private float[][] navigateFromLocationToLocation(int worldId, NavGeometry tile, NavGeometry tile2, float x1, float y1, float z1, float x2, float y2, float z2) {
		boolean boxed = false;
		if (tile == null) {
			tile = getNavTile(worldId, x1, y1, z1);
		}
		if (tile == null) {
			tile = getNavTileWithBox(worldId, x1, y1, z1);
			if (tile == null) return null;
			boxed = true;
		}
		if (tile2 == null) {
			tile2 = getNavTile(worldId, x2, y2, z2);
		}
		if (tile2 == null) {
			tile2 = getNavTileWithBox(worldId, x2, y2, z2);
		}
		if (tile == tile2) {
			return new float[][] {{x2, y2, z2}};
		}
		if (boxed) {
			float[] p = tile.getClosestPoint(x1, y1, z1);
			float[][] pathFromP = attemptStraightLinePath(tile, tile2, x1, y1, z1, x2, y2, z2);
			if (pathFromP == null) {
				NavHelper helper = new NavHelper(tile, tile2, p[0], p[1], p[2], x2, y2, z2);
				NavPathway[] pathway = helper.createPathway();
				helper.destroy();
				pathFromP = funnelPathway(pathway, tile2 != null, p[0], p[1], p[2], x2, y2, z2);
			}
			float[][] ret = new float[pathFromP.length + 1][];
			ret[0] = p;
			System.arraycopy(pathFromP, 0, ret, 1, pathFromP.length);
			return ret;
		}
		float[][] straightLinePath = attemptStraightLinePath(tile, tile2, x1, y1, z1, x2, y2, z2);
		if (straightLinePath != null) {
			return straightLinePath;
		}
		NavHelper helper = new NavHelper(tile, tile2, x1, y1, z1, x2, y2, z2);
		NavPathway[] pathway = helper.createPathway();
		helper.destroy();
		return funnelPathway(pathway, tile2 != null, x1, y1, z1, x2, y2, z2);
	}
	
	private static float[][] funnelPathway(NavPathway[] pathway, boolean includeTargetPoint, float x1, float y1, float z1, float x2, float y2, float z2) {
		if (pathway == null) return null; //Mob will ignore all obstacles
		if (pathway.length == 0) return new float[][] {{x1, y1, z1}}; //Mob will not move
		if (pathway.length == 1 && pathway[0].edge == 0) return new float[][] {{x2, y2, z2}}; //Mob will move directly to target
		ArrayList<float[]> ret = new ArrayList<float[]>();
		for (int i = pathway.length - 1; i >= 0;) {
			float[][] endpoints = pathway[i--].getEndpoints();
			float[] p;
			if (ret.size() == 0) {
				p = new float[] {x1, y1, z1};
			} else {
				p = ret.get(ret.size() - 1);
				while ((areEqualPoints(p, endpoints[0]) || areEqualPoints(p, endpoints[1])) && i >= 0) {
					endpoints = pathway[i--].getEndpoints();
				}
				if (i < 0 && (areEqualPoints(p, endpoints[0]) || areEqualPoints(p, endpoints[1]))) {
					if (includeTargetPoint) {
						ret.add(new float[]{x2, y2, z2});
					} else {
						ret.add(pathway[0].tile.getClosestPoint(x2, y2, z2));
					}
					break;
				}
			}
			float[] vec1 = new float[] {endpoints[0][0] - p[0], endpoints[0][1] - p[1], endpoints[0][2] - p[2]};
			float[] end1 = endpoints[0];
			int end1i = i;
			float[] pointer1 = endpoints[0];
			float[] vec2 = new float[] {endpoints[1][0] - p[0], endpoints[1][1] - p[1], endpoints[1][2] - p[2]};
			float[] end2 = endpoints[1];
			int end2i = i;
			float[] pointer2 = endpoints[1];
			boolean positive = crossZ(vec1, vec2) > 0; //Cannot == 0
			boolean done = false;
			while (!done && i >= 0) {
				endpoints = pathway[i].getEndpoints();
				boolean v1 = false;
				if (areEqualPoints(pointer1, endpoints[0]) || areEqualPoints(pointer1, endpoints[1])) {
					v1 = true;
				}
				if (v1) {
					//move vec2
					float[] vec2p; //vec2 placeholder
					if (areEqualPoints(pointer1, endpoints[0])) {
						vec2p = new float[] {endpoints[1][0] - p[0], endpoints[1][1] - p[1], endpoints[1][2] - p[2]};
						pointer2 = endpoints[1];
					} else {
						assert areEqualPoints(pointer1, endpoints[1]);
						vec2p = new float[] {endpoints[0][0] - p[0], endpoints[0][1] - p[1], endpoints[0][2] - p[2]};
						pointer2 = endpoints[0];
					}
					if (compareFunnelCross(crossZ(vec2p, vec2), positive, true)) {
						if (compareFunnelCross(crossZ(vec1, vec2p), positive, false)) {
							//move vec2, update end2, decrement i, continue
							vec2 = vec2p;
							end2 = pointer2;
							end2i = i;
							i--;
							continue;
						}
						//add end1 to ret, go back to end1i, vec2p wasn't between vec1 and vec2, and crossed over!
						ret.add(end1);
						i = end1i;
						done = true;
						break;
					}
					//vec2p made the funnel bigger! skip to next endpoints.
					i--;
				} else {
					//move vec1
					float[] vec1p; //vec1 placeholder
					if (areEqualPoints(pointer2, endpoints[0])) {
						vec1p = new float[] {endpoints[1][0] - p[0], endpoints[1][1] - p[1], endpoints[1][2] - p[2]};
						pointer1 = endpoints[1];
					} else {
						assert areEqualPoints(pointer2, endpoints[1]);
						vec1p = new float[] {endpoints[0][0] - p[0], endpoints[0][1] - p[1], endpoints[0][2] - p[2]};
						pointer1 = endpoints[0];
					}
					if (compareFunnelCross(crossZ(vec1, vec1p), positive, true)) {
						if (compareFunnelCross(crossZ(vec1p, vec2), positive, false)) {
							//move vec1, update end1, decrement i, continue
							vec1 = vec1p;
							end1 = pointer1;
							end1i = i;
							i--;
							continue;
						}
						//add end2 to ret, go back to end2i, vec1p wasn't between vec1 and vec2, and crossed over!
						ret.add(end2);
						i = end2i;
						done = true;
						break;
					}
					//vec1p made the funnel bigger! skip to next endpoints.
					i--;
				}
			}
			if (!done) {
				float[] vec1p = new float[] {x2 - p[0], y2 - p[1], z2 - p[2]};
				if (compareFunnelCross(crossZ(vec1, vec1p), positive, true)) {
					if (compareFunnelCross(crossZ(vec1p, vec2), positive, false)) {
						if (includeTargetPoint) {
							ret.add(new float[]{x2, y2, z2});
						} else {
							ret.add(pathway[0].tile.getClosestPoint(x2, y2, z2));
						}
					} else {
						//add end2 to ret, then target point
						ret.add(end2);
						if (includeTargetPoint) {
							ret.add(new float[]{x2, y2, z2});
						} else {
							ret.add(pathway[0].tile.getClosestPoint(x2, y2, z2));
						}
					}
				} else {
					//add end1 to ret, then target point
					ret.add(end1);
					if (includeTargetPoint) {
						ret.add(new float[]{x2, y2, z2});
					} else {
						ret.add(pathway[0].tile.getClosestPoint(x2, y2, z2));
					}
				}
				break;
			}
		}
		return ret.toArray(new float[0][]);
	}
	
	private static boolean areEqualPoints(float[] p1, float[] p2) {
		assert p1.length == 3 && p2.length == 3;
		return p1[0] == p2[0] && p1[1] == p2[1] && p1[2] == p2[2];
	}
	
	private static boolean compareFunnelCross(float crossZ, boolean positive, boolean zeroAllowed) {
		if (crossZ == 0) return zeroAllowed;
		if (positive) {
			return crossZ > 0;
		} else {
			return crossZ < 0;
		}
	}
	
	private static float crossZ(float[] vec1, float[] vec2/*, float x1, float y1, float x2, float y2*/) {
		return ((vec1[0] * vec2[1]) - (vec1[1] * vec2[0]));
//		return ((x1 * y2) - (y1 * x2));
	}
	
	private NavGeometry getNavTile(int worldId, float x, float y, float z) {
		GeoMap navMap = navData.getNavMap(worldId);
		if (navMap == null) return null;
		Vector3f pos = Vector3f.newInstance().set(x, y, z + 1F),
				 dir = Vector3f.newInstance().set(0, 0, -1F);
		Ray ray = new Ray(pos, dir);
		ray.setLimit(5F);
		CollisionResults results = new CollisionResults((byte) 1, false, 0); //Instance ID shouldn't be needed
		int collisionCount = navMap.collideWith(ray, results);
		Vector3f.recycle(pos);
		Vector3f.recycle(dir);
		if (collisionCount == 0) return null;
		Spatial ret = results.getClosestCollision().getGeometry();
		assert ret instanceof NavGeometry;
		try {
			return (NavGeometry) ret;
		} catch (ClassCastException e) {
			LOG.error(e.toString());
		}
		return null;
	}
	
	private NavGeometry getNavTileWithBox(int worldId, float x, float y, float z) {
		GeoMap navMap = navData.getNavMap(worldId);
		if (navMap == null) return null;
		Vector3f min = Vector3f.newInstance().set(x - 0.8F, y - 0.8F, z - 1),
				 max = Vector3f.newInstance().set(x + 0.8F, y + 0.8F, z + 4),
				center = Vector3f.newInstance().set(x, y, z + 0.2F);
		BoundingBox box = new BoundingBox(min,max);
		box.setCenter(center);
		CollisionResults results = new CollisionResults((byte) 1, false, 0); //Instance ID shouldn't be needed
		int collisionCount = navMap.collideWith(box, results);
		Vector3f.recycle(min);
		Vector3f.recycle(max);
		Vector3f.recycle(center);
		if (collisionCount == 0) return null;
		Spatial ret = results.getClosestCollision().getGeometry();
		assert ret instanceof NavGeometry;
		try {
			return (NavGeometry) ret;
		} catch (ClassCastException e) {
			LOG.error(e.toString());
		}
		return null;
	}
	
	static class NavPathway {
		NavGeometry tile;
		byte edge; //Values are 0, 1, 2, or 3
		
		NavPathway(NavGeometry tile, byte edge) {
			this.tile = tile;
			this.edge = edge;
		}
		
		float[][] getEndpoints() {
			float[][] ret;
			switch (edge) {
			case 0:
				//Means the target was inside the starting tile
				return null;
			case 1:
			case 2:
			case 3:
				ret = tile.getEndpoints(edge); 
				break;
			default:
				assert false:"Incorrect NavPathway Creation";
				return null;
			}
			return ret;
		}
	}
	
	public static final NavService getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder {
		protected static final NavService INSTANCE = new NavService();
	}
}
