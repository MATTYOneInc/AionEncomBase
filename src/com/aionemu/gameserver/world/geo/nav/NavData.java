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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.geoEngine.models.GeoMap;
import com.aionemu.gameserver.geoEngine.scene.NavGeometry;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.utils.Util;

import gnu.trove.map.hash.TIntObjectHashMap;
import sun.misc.Cleaner;
import sun.nio.ch.DirectBuffer;

/**
 * Similar to {@link com.aionemu.gameserver.world.geo.GeoData GeoData}, this class stores
 * {@link GeoMap GeoMaps} that represent navigable space within a level. These maps are holding
 * Nav Meshes that can be used to pathfind.
 * 
 * @author Yon (Aion Reconstruction Project)
 */
public class NavData {
	
	private static final Logger LOG = LoggerFactory.getLogger(NavData.class);
	private final TIntObjectHashMap<GeoMap> navMaps = new TIntObjectHashMap<GeoMap>();
	
	private static final String NAV_DIR = "./data/nav/";
	
	private NavData() {};
	
	boolean isLoaded() {
		return !navMaps.isEmpty();
	}
	
	void loadNavMaps() {
		LOG.info("Loading Navigational Maps...");
		Util.printProgressBarHeader(DataManager.WORLD_MAPS_DATA.size());
		List<Integer> mapsWithErrors = new ArrayList<Integer>();
		
		for (WorldMapTemplate map : DataManager.WORLD_MAPS_DATA) {
			GeoMap geoMap = new GeoMap(Integer.toString(map.getMapId()), map.getWorldSize());
			try {
				if (loadNav(map.getMapId(), geoMap)) {
					navMaps.put(map.getMapId(), geoMap);
				}
			} catch (IOException e) {
				mapsWithErrors.add(map.getMapId());
			}
			
			Util.printCurrentProgress();
		}
		
		Util.printEndProgress();
		if (mapsWithErrors.size() > 0) {
			LOG.warn("Some maps did not have navigational data: ");
			LOG.warn(mapsWithErrors.toString());
		}
	}
	
	/**
	 * Returns the GeoMap representing the Nav Mesh for the given Map ID.
	 * <p>
	 * If no such map exists, this method will return null.
	 * 
	 * @param worldId -- The ID of the map to find the Nav Mesh for
	 */
	public GeoMap getNavMap(int worldId) {
		return navMaps.get(worldId);
	}
	
	private boolean loadNav(int worldId, GeoMap map) throws IOException {
		File navFile = new File(NAV_DIR, worldId + ".nav");
		RandomAccessFile raFile = null;
		FileChannel roChannel = null;
		MappedByteBuffer nav = null;
		try {
			raFile = new RandomAccessFile(navFile, "r");
			roChannel = raFile.getChannel();
			nav = roChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) roChannel.size()).load();
			nav.order(ByteOrder.LITTLE_ENDIAN);
			
//			int triCount = nav.getInt();
//			for (int i = 0; i < triCount; i++) {
//				float[] verts = new float[] {nav.getFloat(), nav.getFloat(), nav.getFloat()};
//			}
			
//			float[] verts = new float[nav.getInt()];
//			for (int i = 0; i < verts.length; i++) {
//				verts[i] = nav.getFloat();
//			}
			int floatCount = nav.getInt();
			nav.position((floatCount * 4) + 4);
			
			NavGeometry[] triangles = new NavGeometry[nav.getInt()];
			EdgeConnectionHolder[] triCon = new EdgeConnectionHolder[triangles.length];
			for (int i = 0; i < triangles.length; i++) {
				final int[] index = new int[3];
				index[0] = nav.getInt();
				index[1] = nav.getInt();
				index[2] = nav.getInt();
				
				triangles[i] = new NavGeometry(null, getVertices(nav, index));
				
				triCon[i] = new EdgeConnectionHolder();
				triCon[i].id1 = nav.getInt(); //Edge connection 1
				triCon[i].id2 = nav.getInt(); //Edge connection 2
				triCon[i].id3 = nav.getInt(); //Edge connection 3
			}
			
			destroyDirectByteBuffer(nav);
			
			for (int i = 0; i < triangles.length; i++) {
				if (triCon[i].id1 != -1) triangles[i].setEdge1(triangles[triCon[i].id1]);
				if (triCon[i].id2 != -1) triangles[i].setEdge2(triangles[triCon[i].id2]);
				if (triCon[i].id3 != -1) triangles[i].setEdge3(triangles[triCon[i].id3]);
				triangles[i].updateModelBound();
				map.attachChild(triangles[i]);
			}
			map.updateModelBound();
		} finally {
			try {
				if (roChannel != null) roChannel.close();
				if (raFile != null) raFile.close();
			} catch (IOException e) {
				LOG.error("Unable to close file resource: " + worldId + ".nav");
			}
		}
		return true;
	}
	
	private static float[] getVertices(MappedByteBuffer nav, int[] indices) {
		float[] ret = new float[indices.length * 3];
		for (int i = 0; i < indices.length; i++) {
			ret[i * 3] = nav.getFloat((indices[i] * 4 * 3) + 4);
			ret[(i * 3) + 1] = nav.getFloat((indices[i] * 4 * 3) + 8);
			ret[(i * 3) + 2] = nav.getFloat((indices[i] * 4 * 3) + 12);
		}
		return ret;
	}
	
//	private static float[] getVertices(float[] verts, int[] indices) {
//		float[] ret = new float[indices.length * 3];
//		for (int i = 0; i < indices.length; i++) {
//			ret[i * 3] = verts[indices[i] * 3];
//			ret[(i * 3) + 1] = verts[(indices[i] * 3) + 1];
//			ret[(i * 3) + 2] = verts[(indices[i] * 3) + 2];
//		}
//		return ret;
//	}
	
	private static void destroyDirectByteBuffer(Buffer toBeDestroyed) {
		Cleaner cleaner = ((DirectBuffer) toBeDestroyed).cleaner();
		if (cleaner != null) {
			cleaner.clean();
		}
	}
	
	private class EdgeConnectionHolder {
		int id1;
		int id2;
		int id3;
	}
	
	public static final NavData getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder {
		protected static final NavData INSTANCE = new NavData();
	}
	
}
