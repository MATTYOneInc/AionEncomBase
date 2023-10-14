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

public class Triangle
extends AbstractTriangle
implements Reusable {
    private static final ObjectFactory FACTORY = new ObjectFactory(){

        public Object create() {
            return new Triangle();
        }
    };
    private Vector3f pointa = new Vector3f();
    private Vector3f pointb = new Vector3f();
    private Vector3f pointc = new Vector3f();
    private transient Vector3f center;
    private transient Vector3f normal;
    private float projection;
    private int index;

    public Triangle() {
    }

    public Triangle(Vector3f p1, Vector3f p2, Vector3f p3) {
        this.pointa.set(p1);
        this.pointb.set(p2);
        this.pointc.set(p3);
    }

    public Vector3f get(int i) {
        switch (i) {
            case 0: {
                return this.pointa;
            }
            case 1: {
                return this.pointb;
            }
            case 2: {
                return this.pointc;
            }
        }
        return null;
    }

    @Override
    public Vector3f get1() {
        return this.pointa;
    }

    @Override
    public Vector3f get2() {
        return this.pointb;
    }

    @Override
    public Vector3f get3() {
        return this.pointc;
    }

    public void set(int i, Vector3f point) {
        switch (i) {
            case 0: {
                this.pointa.set(point);
                break;
            }
            case 1: {
                this.pointb.set(point);
                break;
            }
            case 2: {
                this.pointc.set(point);
            }
        }
    }

    public void set(int i, float x, float y, float z) {
        switch (i) {
            case 0: {
                this.pointa.set(x, y, z);
                break;
            }
            case 1: {
                this.pointb.set(x, y, z);
                break;
            }
            case 2: {
                this.pointc.set(x, y, z);
            }
        }
    }

    public void set1(Vector3f v) {
        this.pointa.set(v);
    }

    public void set2(Vector3f v) {
        this.pointb.set(v);
    }

    public void set3(Vector3f v) {
        this.pointc.set(v);
    }

    @Override
    public void set(Vector3f v1, Vector3f v2, Vector3f v3) {
        this.pointa.set(v1);
        this.pointb.set(v2);
        this.pointc.set(v3);
    }

    public void calculateCenter() {
        if (this.center == null) {
            this.center = new Vector3f(this.pointa);
        } else {
            this.center.set(this.pointa);
        }
        this.center.addLocal(this.pointb).addLocal(this.pointc).multLocal(0.33333334f);
    }

    public void calculateNormal() {
        if (this.normal == null) {
            this.normal = new Vector3f(this.pointb);
        } else {
            this.normal.set(this.pointb);
        }
        this.normal.subtractLocal(this.pointa).crossLocal(this.pointc.x - this.pointa.x, this.pointc.y - this.pointa.y, this.pointc.z - this.pointa.z);
        this.normal.normalizeLocal();
    }

    public Vector3f getCenter() {
        if (this.center == null) {
            this.calculateCenter();
        }
        return this.center;
    }

    public void setCenter(Vector3f center) {
        this.center = center;
    }

    public Vector3f getNormal() {
        if (this.normal == null) {
            this.calculateNormal();
        }
        return this.normal;
    }

    public void setNormal(Vector3f normal) {
        this.normal = normal;
    }

    public float getProjection() {
        return this.projection;
    }

    public void setProjection(float projection) {
        this.projection = projection;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static Vector3f computeTriangleNormal(Vector3f v1, Vector3f v2, Vector3f v3, Vector3f store) {
        if (store == null) {
            store = new Vector3f(v2);
        } else {
            store.set(v2);
        }
        store.subtractLocal(v1).crossLocal(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
        return store.normalizeLocal();
    }

    public Class<? extends Triangle> getClassTag() {
        return this.getClass();
    }

    public Triangle clone() {
        try {
            Triangle t = (Triangle)super.clone();
            t.pointa = this.pointa.clone();
            t.pointb = this.pointb.clone();
            t.pointc = this.pointc.clone();
            return t;
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void reset() {
        this.pointa.reset();
        this.pointb.reset();
        this.pointc.reset();
        this.center = null;
        this.normal = null;
        this.projection = 0.0f;
        this.index = 0;
    }

    public static Triangle newInstance() {
        if (GeoDataConfig.GEO_OBJECT_FACTORY_ENABLE) {
            return (Triangle)FACTORY.object();
        }
        return new Triangle();
    }

    public static void recycle(Triangle instance) {
        if (GeoDataConfig.GEO_OBJECT_FACTORY_ENABLE) {
            FACTORY.recycle((Object)instance);
        } else {
            instance = null;
        }
    }
}

