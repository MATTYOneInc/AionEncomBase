/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  gnu.trove.map.hash.TIntObjectHashMap
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.aionemu.gameserver.world.geo;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.geoEngine.GeoWorldLoader;
import com.aionemu.gameserver.geoEngine.models.GeoMap;
import com.aionemu.gameserver.geoEngine.scene.Spatial;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.world.geo.DummyGeoData;
import com.aionemu.gameserver.world.geo.GeoData;
import gnu.trove.map.hash.TIntObjectHashMap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RealGeoData
implements GeoData {
    private static final Logger log = LoggerFactory.getLogger(RealGeoData.class);
    private TIntObjectHashMap<GeoMap> geoMaps = new TIntObjectHashMap();

    @Override
    public void loadGeoMaps() {
        Map<String, Spatial> models = this.loadMeshes();
        this.loadWorldMaps(models);
        models.clear();
        models = null;
        log.info("\u5730\u7406\u6570\u636e: " + this.geoMaps.size() + " \u8f7d\u5165GEO\u5730\u56fe!");
    }

    protected void loadWorldMaps(Map<String, Spatial> models) {
        log.info("\u8f7d\u5165GEO\u5730\u56fe..");
        Util.printProgressBarHeader(DataManager.WORLD_MAPS_DATA.size());
        ArrayList<Integer> mapsWithErrors = new ArrayList<Integer>();
        for (WorldMapTemplate map : DataManager.WORLD_MAPS_DATA) {
			GeoMap geoMap = new GeoMap(Integer.toString(map.getMapId()), map.getWorldSize());
			try {
				if (GeoWorldLoader.loadWorld(map.getMapId(), models, geoMap)) {
					geoMaps.put(map.getMapId(), geoMap);
				}
			}
			catch (Throwable t) {
				mapsWithErrors.add(map.getMapId());
				geoMaps.put(map.getMapId(), DummyGeoData.DUMMY_MAP);
			}
			Util.printCurrentProgress();
		}
        Util.printEndProgress();
        if (mapsWithErrors.size() > 0) {
            log.warn("\u4e00\u4e9b\u5730\u56fe\u672a\u6b63\u786e\u52a0\u8f7d\u5e76\u6062\u590d\u4e3a\u865a\u62df\u5b9e\u73b0: ");
            log.warn(((Object)mapsWithErrors).toString());
        }
    }

    protected Map<String, Spatial> loadMeshes() {
        log.info("\u8f7d\u5165\u7f51\u683c..");
        Map<String, Spatial> models = null;
        try {
            models = GeoWorldLoader.loadMeshs("data/geo/meshs.geo");
        }
        catch (IOException e) {
            throw new IllegalStateException("Problem loading meshes", e);
        }
        return models;
    }

    @Override
    public GeoMap getMap(int worldId) {
        GeoMap geoMap = (GeoMap)this.geoMaps.get(worldId);
        return geoMap != null ? geoMap : DummyGeoData.DUMMY_MAP;
    }
}

