/*
 * Decompiled with CFR 0.150.
 */
package com.aionemu.gameserver.geoEngine.math;

import java.util.Random;

public final class FastMath {
	public static final double DBL_EPSILON = 2.220446049250313E-16;
	public static final float FLT_EPSILON = 1.1920929E-7f;
	public static final float ZERO_TOLERANCE = 1.0E-4f;
	public static final float ONE_THIRD = 0.33333334f;
	public static final float PI = (float) Math.PI;
	public static final float TWO_PI = (float) Math.PI * 2;
	public static final float HALF_PI = 1.5707964f;
	public static final float QUARTER_PI = 0.7853982f;
	public static final float INV_PI = 0.31830987f;
	public static final float INV_TWO_PI = 0.15915494f;
	public static final float DEG_TO_RAD = (float) Math.PI / 180;
	public static final float RAD_TO_DEG = 57.295776f;
	public static final Random rand = new Random(System.currentTimeMillis());

	private FastMath() {
	}

	public static boolean isPowerOfTwo(int number) {
		return number > 0 && (number & number - 1) == 0;
	}

	public static int nearestPowerOfTwo(int number) {
		return (int) Math.pow(2.0, Math.ceil(Math.log(number) / Math.log(2.0)));
	}

	public static float interpolateLinear(float scale, float startValue, float endValue) {
		if (startValue == endValue) {
			return startValue;
		}
		if (scale <= 0.0f) {
			return startValue;
		}
		if (scale >= 1.0f) {
			return endValue;
		}
		return (1.0f - scale) * startValue + scale * endValue;
	}

	public static Vector3f interpolateLinear(float scale, Vector3f startValue, Vector3f endValue) {
		Vector3f res = new Vector3f();
		res.x = FastMath.interpolateLinear(scale, startValue.x, endValue.x);
		res.y = FastMath.interpolateLinear(scale, startValue.y, endValue.y);
		res.z = FastMath.interpolateLinear(scale, startValue.z, endValue.z);
		return res;
	}

	public static float interpolateCatmullRom(float u, float T, float p0, float p1, float p2, float p3) {
		double c1 = p1;
		double c2 = -1.0 * (double) T * (double) p0 + (double) (T * p2);
		double c3 = 2.0f * T * p0 + (T - 3.0f) * p1 + (3.0f - 2.0f * T) * p2 + -T * p3;
		double c4 = -T * p0 + (2.0f - T) * p1 + (T - 2.0f) * p2 + T * p3;
		return (float) (((c4 * (double) u + c3) * (double) u + c2) * (double) u + c1);
	}

	public static Vector3f interpolateCatmullRom(float u, float T, Vector3f p0, Vector3f p1, Vector3f p2, Vector3f p3) {
		Vector3f res = new Vector3f();
		res.x = FastMath.interpolateCatmullRom(u, T, p0.x, p1.x, p2.x, p3.x);
		res.y = FastMath.interpolateCatmullRom(u, T, p0.y, p1.y, p2.y, p3.y);
		res.z = FastMath.interpolateCatmullRom(u, T, p0.z, p1.z, p2.z, p3.z);
		return res;
	}

	public static float acos(float fValue) {
		if (-1.0f < fValue) {
			if (fValue < 1.0f) {
				return (float) Math.acos(fValue);
			}
			return 0.0f;
		}
		return (float) Math.PI;
	}

	public static float asin(float fValue) {
		if (-1.0f < fValue) {
			if (fValue < 1.0f) {
				return (float) Math.asin(fValue);
			}
			return 1.5707964f;
		}
		return -1.5707964f;
	}

	public static float atan(float fValue) {
		return (float) Math.atan(fValue);
	}

	public static float atan2(float fY, float fX) {
		return (float) Math.atan2(fY, fX);
	}

	public static float ceil(float fValue) {
		return (float) Math.ceil(fValue);
	}

	public static float reduceSinAngle(float radians) {
		if (Math.abs(radians %= (float) Math.PI * 2) > (float) Math.PI) {
			radians -= (float) Math.PI * 2;
		}
		if (Math.abs(radians) > 1.5707964f) {
			radians = (float) Math.PI - radians;
		}
		return radians;
	}

	public static float sin2(float fValue) {
		if ((double) Math.abs(fValue = FastMath.reduceSinAngle(fValue)) <= 0.7853981633974483) {
			return (float) Math.sin(fValue);
		}
		return (float) Math.cos(1.5707963267948966 - (double) fValue);
	}

	public static float cos2(float fValue) {
		return FastMath.sin2(fValue + 1.5707964f);
	}

	public static float cos(float v) {
		return (float) Math.cos(v);
	}

	public static float sin(float v) {
		return (float) Math.sin(v);
	}

	public static float exp(float fValue) {
		return (float) Math.exp(fValue);
	}

	public static float abs(float fValue) {
		if (fValue < 0.0f) {
			return -fValue;
		}
		return fValue;
	}

	public static float floor(float fValue) {
		return (float) Math.floor(fValue);
	}

	public static float invSqrt(float fValue) {
		return (float) (1.0 / Math.sqrt(fValue));
	}

	public static float fastInvSqrt(float x) {
		float xhalf = 0.5f * x;
		int i = Float.floatToIntBits(x);
		i = 1597463174 - (i >> 1);
		x = Float.intBitsToFloat(i);
		x *= 1.5f - xhalf * x * x;
		return x;
	}

	public static float log(float fValue) {
		return (float) Math.log(fValue);
	}

	public static float log(float value, float base) {
		return (float) (Math.log(value) / Math.log(base));
	}

	public static float pow(float fBase, float fExponent) {
		return (float) Math.pow(fBase, fExponent);
	}

	public static float sqr(float fValue) {
		return fValue * fValue;
	}

	public static float sqrt(float fValue) {
		return (float) Math.sqrt(fValue);
	}

	public static float tan(float fValue) {
		return (float) Math.tan(fValue);
	}

	public static int sign(int iValue) {
		if (iValue > 0) {
			return 1;
		}
		if (iValue < 0) {
			return -1;
		}
		return 0;
	}

	public static float sign(float fValue) {
		return Math.signum(fValue);
	}

	public static int counterClockwise(Vector2f p0, Vector2f p1, Vector2f p2) {
		float dx1 = p1.x - p0.x;
		float dy2 = p2.y - p0.y;
		float dy1 = p1.y - p0.y;
		float dx2 = p2.x - p0.x;
		if (dx1 * dy2 > dy1 * dx2) {
			return 1;
		}
		if (dx1 * dy2 < dy1 * dx2) {
			return -1;
		}
		if (dx1 * dx2 < 0.0f || dy1 * dy2 < 0.0f) {
			return -1;
		}
		if (dx1 * dx1 + dy1 * dy1 < dx2 * dx2 + dy2 * dy2) {
			return 1;
		}
		return 0;
	}

	public static int pointInsideTriangle(Vector2f t0, Vector2f t1, Vector2f t2, Vector2f p) {
		int val1 = FastMath.counterClockwise(t0, t1, p);
		if (val1 == 0) {
			return 1;
		}
		int val2 = FastMath.counterClockwise(t1, t2, p);
		if (val2 == 0) {
			return 1;
		}
		if (val2 != val1) {
			return 0;
		}
		int val3 = FastMath.counterClockwise(t2, t0, p);
		if (val3 == 0) {
			return 1;
		}
		if (val3 != val1) {
			return 0;
		}
		return val3;
	}

	public static float determinant(double m00, double m01, double m02, double m03, double m10, double m11, double m12,
			double m13, double m20, double m21, double m22, double m23, double m30, double m31, double m32,
			double m33) {
		double det01 = m20 * m31 - m21 * m30;
		double det02 = m20 * m32 - m22 * m30;
		double det03 = m20 * m33 - m23 * m30;
		double det12 = m21 * m32 - m22 * m31;
		double det13 = m21 * m33 - m23 * m31;
		double det23 = m22 * m33 - m23 * m32;
		return (float) (m00 * (m11 * det23 - m12 * det13 + m13 * det12)
				- m01 * (m10 * det23 - m12 * det03 + m13 * det02) + m02 * (m10 * det13 - m11 * det03 + m13 * det01)
				- m03 * (m10 * det12 - m11 * det02 + m12 * det01));
	}

	public static float nextRandomFloat() {
		return rand.nextFloat();
	}

	public static int nextRandomInt(int min, int max) {
		return (int) (FastMath.nextRandomFloat() * (float) (max - min + 1)) + min;
	}

	public static int nextRandomInt() {
		return rand.nextInt();
	}

	public static Vector3f sphericalToCartesian(Vector3f sphereCoords, Vector3f store) {
		store.y = sphereCoords.x * FastMath.sin(sphereCoords.z);
		float a = sphereCoords.x * FastMath.cos(sphereCoords.z);
		store.x = a * FastMath.cos(sphereCoords.y);
		store.z = a * FastMath.sin(sphereCoords.y);
		return store;
	}

	public static Vector3f cartesianToSpherical(Vector3f cartCoords, Vector3f store) {
		if (cartCoords.x == 0.0f) {
			cartCoords.x = 1.1920929E-7f;
		}
		store.x = FastMath
				.sqrt(cartCoords.x * cartCoords.x + cartCoords.y * cartCoords.y + cartCoords.z * cartCoords.z);
		store.y = FastMath.atan(cartCoords.z / cartCoords.x);
		if (cartCoords.x < 0.0f) {
			store.y += (float) Math.PI;
		}
		store.z = FastMath.asin(cartCoords.y / store.x);
		return store;
	}

	public static Vector3f sphericalToCartesianZ(Vector3f sphereCoords, Vector3f store) {
		store.z = sphereCoords.x * FastMath.sin(sphereCoords.z);
		float a = sphereCoords.x * FastMath.cos(sphereCoords.z);
		store.x = a * FastMath.cos(sphereCoords.y);
		store.y = a * FastMath.sin(sphereCoords.y);
		return store;
	}

	public static Vector3f cartesianZToSpherical(Vector3f cartCoords, Vector3f store) {
		if (cartCoords.x == 0.0f) {
			cartCoords.x = 1.1920929E-7f;
		}
		store.x = FastMath
				.sqrt(cartCoords.x * cartCoords.x + cartCoords.y * cartCoords.y + cartCoords.z * cartCoords.z);
		store.z = FastMath.atan(cartCoords.z / cartCoords.x);
		if (cartCoords.x < 0.0f) {
			store.z += (float) Math.PI;
		}
		store.y = FastMath.asin(cartCoords.y / store.x);
		return store;
	}

	public static float normalize(float val, float min, float max) {
		if (Float.isInfinite(val) || Float.isNaN(val)) {
			return 0.0f;
		}
		float range = max - min;
		while (val > max) {
			val -= range;
		}
		while (val < min) {
			val += range;
		}
		return val;
	}

	public static float copysign(float x, float y) {
		if (y >= 0.0f && x <= 0.0f) {
			return -x;
		}
		if (y < 0.0f && x >= 0.0f) {
			return -x;
		}
		return x;
	}

	public static float clamp(float input, float min, float max) {
		return input < min ? min : (input > max ? max : input);
	}

	public static float saturate(float input) {
		return FastMath.clamp(input, 0.0f, 1.0f);
	}

	public static float convertHalfToFloat(int half) {
		switch (half) {
		case 0x0000:
			return 0f;
		case 0x8000:
			return -0f;
		case 0x7c00:
			return Float.POSITIVE_INFINITY;
		case 0xfc00:
			return Float.NEGATIVE_INFINITY;
		// TODO: Support for NaN?
		default:
			return Float.intBitsToFloat(
					((half & 0x8000) << 16) | (((half & 0x7c00) + 0x1C000) << 13) | ((half & 0x03FF) << 13));
		}
	}

	public static short convertFloatToHalf(float flt) {
		if (Float.isNaN(flt)) {
			throw new UnsupportedOperationException("NaN to half conversion not supported!");
		} else if (flt == Float.POSITIVE_INFINITY) {
			return (short) 0x7c00;
		} else if (flt == Float.NEGATIVE_INFINITY) {
			return (short) 0xfc00;
		} else if (flt == 0f) {
			return (short) 0x0000;
		} else if (flt == -0f) {
			return (short) 0x8000;
		} else if (flt > 65504f) {
			// max value supported by half float
			return 0x7bff;
		} else if (flt < -65504f) {
			return (short) (0x7bff | 0x8000);
		} else if (flt > 0f && flt < 5.96046E-8f) {
			return 0x0001;
		} else if (flt < 0f && flt > -5.96046E-8f) {
			return (short) 0x8001;
		}
		int f = Float.floatToIntBits(flt);
		return (short) (((f >> 16) & 0x8000) | ((((f & 0x7f800000) - 0x38000000) >> 13) & 0x7c00)
				| ((f >> 13) & 0x03ff));
	}
}
