/*
 * Decompiled with CFR 0.150.
 */
package com.aionemu.gameserver.geoEngine.math;

import com.aionemu.gameserver.geoEngine.math.FastMath;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.logging.Logger;

public final class Vector2f
implements Cloneable {
    private static final Logger logger = Logger.getLogger(Vector2f.class.getName());
    public static final Vector2f ZERO = new Vector2f(0.0f, 0.0f);
    public static final Vector2f UNIT_XY = new Vector2f(1.0f, 1.0f);
    public float x;
    public float y;

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f() {
        this.y = 0.0f;
        this.x = 0.0f;
    }

    public Vector2f(Vector2f vector2f) {
        this.x = vector2f.x;
        this.y = vector2f.y;
    }

    public Vector2f set(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Vector2f set(Vector2f vec) {
        this.x = vec.x;
        this.y = vec.y;
        return this;
    }

    public Vector2f add(Vector2f vec) {
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        return new Vector2f(this.x + vec.x, this.y + vec.y);
    }

    public Vector2f addLocal(Vector2f vec) {
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        this.x += vec.x;
        this.y += vec.y;
        return this;
    }

    public Vector2f addLocal(float addX, float addY) {
        this.x += addX;
        this.y += addY;
        return this;
    }

    public Vector2f add(Vector2f vec, Vector2f result) {
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        if (result == null) {
            result = new Vector2f();
        }
        result.x = this.x + vec.x;
        result.y = this.y + vec.y;
        return result;
    }

    public float dot(Vector2f vec) {
        if (null == vec) {
            logger.warning("Provided vector is null, 0 returned.");
            return 0.0f;
        }
        return this.x * vec.x + this.y * vec.y;
    }

    public Vector3f cross(Vector2f v) {
        return new Vector3f(0.0f, 0.0f, this.determinant(v));
    }

    public float determinant(Vector2f v) {
        return this.x * v.y - this.y * v.x;
    }

    public Vector2f interpolate(Vector2f finalVec, float changeAmnt) {
        this.x = (1.0f - changeAmnt) * this.x + changeAmnt * finalVec.x;
        this.y = (1.0f - changeAmnt) * this.y + changeAmnt * finalVec.y;
        return this;
    }

    public Vector2f interpolate(Vector2f beginVec, Vector2f finalVec, float changeAmnt) {
        this.x = (1.0f - changeAmnt) * beginVec.x + changeAmnt * finalVec.x;
        this.y = (1.0f - changeAmnt) * beginVec.y + changeAmnt * finalVec.y;
        return this;
    }

    public static boolean isValidVector(Vector2f vector) {
        if (vector == null) {
            return false;
        }
        if (Float.isNaN(vector.x) || Float.isNaN(vector.y)) {
            return false;
        }
        return !Float.isInfinite(vector.x) && !Float.isInfinite(vector.y);
    }

    public float length() {
        return FastMath.sqrt(this.lengthSquared());
    }

    public float lengthSquared() {
        return this.x * this.x + this.y * this.y;
    }

    public float distanceSquared(Vector2f v) {
        double dx = this.x - v.x;
        double dy = this.y - v.y;
        return (float)(dx * dx + dy * dy);
    }

    public float distanceSquared(float otherX, float otherY) {
        double dx = this.x - otherX;
        double dy = this.y - otherY;
        return (float)(dx * dx + dy * dy);
    }

    public float distance(Vector2f v) {
        return FastMath.sqrt(this.distanceSquared(v));
    }

    public Vector2f mult(float scalar) {
        return new Vector2f(this.x * scalar, this.y * scalar);
    }

    public Vector2f multLocal(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }

    public Vector2f multLocal(Vector2f vec) {
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        this.x *= vec.x;
        this.y *= vec.y;
        return this;
    }

    public Vector2f mult(float scalar, Vector2f product) {
        if (null == product) {
            product = new Vector2f();
        }
        product.x = this.x * scalar;
        product.y = this.y * scalar;
        return product;
    }

    public Vector2f divide(float scalar) {
        return new Vector2f(this.x / scalar, this.y / scalar);
    }

    public Vector2f divideLocal(float scalar) {
        this.x /= scalar;
        this.y /= scalar;
        return this;
    }

    public Vector2f negate() {
        return new Vector2f(-this.x, -this.y);
    }

    public Vector2f negateLocal() {
        this.x = -this.x;
        this.y = -this.y;
        return this;
    }

    public Vector2f subtract(Vector2f vec) {
        return this.subtract(vec, null);
    }

    public Vector2f subtract(Vector2f vec, Vector2f store) {
        if (store == null) {
            store = new Vector2f();
        }
        store.x = this.x - vec.x;
        store.y = this.y - vec.y;
        return store;
    }

    public Vector2f subtract(float valX, float valY) {
        return new Vector2f(this.x - valX, this.y - valY);
    }

    public Vector2f subtractLocal(Vector2f vec) {
        if (null == vec) {
            logger.warning("Provided vector is null, null returned.");
            return null;
        }
        this.x -= vec.x;
        this.y -= vec.y;
        return this;
    }

    public Vector2f subtractLocal(float valX, float valY) {
        this.x -= valX;
        this.y -= valY;
        return this;
    }

    public Vector2f normalize() {
        float length = this.length();
        if (length != 0.0f) {
            return this.divide(length);
        }
        return this.divide(1.0f);
    }

    public Vector2f normalizeLocal() {
        float length = this.length();
        if (length != 0.0f) {
            return this.divideLocal(length);
        }
        return this.divideLocal(1.0f);
    }

    public float smallestAngleBetween(Vector2f otherVector) {
        float dotProduct = this.dot(otherVector);
        float angle = FastMath.acos(dotProduct);
        return angle;
    }

    public float angleBetween(Vector2f otherVector) {
        float angle = FastMath.atan2(otherVector.y, otherVector.x) - FastMath.atan2(this.y, this.x);
        return angle;
    }

    public float getX() {
        return this.x;
    }

    public Vector2f setX(float x) {
        this.x = x;
        return this;
    }

    public float getY() {
        return this.y;
    }

    public Vector2f setY(float y) {
        this.y = y;
        return this;
    }

    public float getAngle() {
        return -FastMath.atan2(this.y, this.x);
    }

    public Vector2f zero() {
        this.y = 0.0f;
        this.x = 0.0f;
        return this;
    }

    public int hashCode() {
        int hash = 37;
        hash += 37 * hash + Float.floatToIntBits(this.x);
        hash += 37 * hash + Float.floatToIntBits(this.y);
        return hash;
    }

    public Vector2f clone() {
        try {
            return (Vector2f)super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public float[] toArray(float[] floats) {
        if (floats == null) {
            floats = new float[]{this.x, this.y};
        }
        return floats;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Vector2f)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        Vector2f comp = (Vector2f)o;
        if (Float.compare(this.x, comp.x) != 0) {
            return false;
        }
        return Float.compare(this.y, comp.y) == 0;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.x = in.readFloat();
        this.y = in.readFloat();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
    }

    public Class<? extends Vector2f> getClassTag() {
        return this.getClass();
    }

    public void rotateAroundOrigin(float angle, boolean cw) {
        if (cw) {
            angle = -angle;
        }
        float newX = FastMath.cos(angle) * this.x - FastMath.sin(angle) * this.y;
        float newY = FastMath.sin(angle) * this.x + FastMath.cos(angle) * this.y;
        this.x = newX;
        this.y = newY;
    }
}

