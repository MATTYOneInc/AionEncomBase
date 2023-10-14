/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javolution.context.ObjectFactory
 *  javolution.lang.Reusable
 */
package com.aionemu.gameserver.geoEngine.math;

import com.aionemu.gameserver.configs.main.GeoDataConfig;

import javolution.context.ObjectFactory;
import javolution.lang.Reusable;

public class Array3f
implements Reusable {
    private static final ObjectFactory FACTORY = new ObjectFactory(){

        public Object create() {
            return new Array3f();
        }
    };
    public float a = 0.0f;
    public float b = 0.0f;
    public float c = 0.0f;

    public void reset() {
        this.a = 0.0f;
        this.b = 0.0f;
        this.c = 0.0f;
    }

    public static Array3f newInstance() {
        if (GeoDataConfig.GEO_OBJECT_FACTORY_ENABLE) {
            return (Array3f)FACTORY.object();
        }
        return new Array3f();
    }

    public static void recycle(Array3f instance) {
        if (GeoDataConfig.GEO_OBJECT_FACTORY_ENABLE) {
            FACTORY.recycle((Object)instance);
        } else {
            instance = null;
        }
    }
}

