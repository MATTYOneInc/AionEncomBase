/*
 * Decompiled with CFR 0.150.
 */
package com.aionemu.gameserver.geoEngine.math;

import java.nio.FloatBuffer;
import java.util.logging.Logger;

import com.aionemu.gameserver.geoEngine.utils.BufferUtils;

public final class Matrix4f implements Cloneable {
	private static final Logger logger = Logger.getLogger(Matrix4f.class.getName());
	public float m00;
	public float m01;
	public float m02;
	public float m03;
	public float m10;
	public float m11;
	public float m12;
	public float m13;
	public float m20;
	public float m21;
	public float m22;
	public float m23;
	public float m30;
	public float m31;
	public float m32;
	public float m33;
	public static final Matrix4f IDENTITY = new Matrix4f();

	public Matrix4f() {
		this.loadIdentity();
	}

	public Matrix4f(float m00, float m01, float m02, float m03, float m10, float m11, float m12, float m13, float m20,
			float m21, float m22, float m23, float m30, float m31, float m32, float m33) {
		this.m00 = m00;
		this.m01 = m01;
		this.m02 = m02;
		this.m03 = m03;
		this.m10 = m10;
		this.m11 = m11;
		this.m12 = m12;
		this.m13 = m13;
		this.m20 = m20;
		this.m21 = m21;
		this.m22 = m22;
		this.m23 = m23;
		this.m30 = m30;
		this.m31 = m31;
		this.m32 = m32;
		this.m33 = m33;
	}

	public Matrix4f(float[] array) {
		this.set(array, false);
	}

	public Matrix4f(Matrix4f mat) {
		this.copy(mat);
	}

	public void copy(Matrix4f matrix) {
		if (null == matrix) {
			this.loadIdentity();
		} else {
			this.m00 = matrix.m00;
			this.m01 = matrix.m01;
			this.m02 = matrix.m02;
			this.m03 = matrix.m03;
			this.m10 = matrix.m10;
			this.m11 = matrix.m11;
			this.m12 = matrix.m12;
			this.m13 = matrix.m13;
			this.m20 = matrix.m20;
			this.m21 = matrix.m21;
			this.m22 = matrix.m22;
			this.m23 = matrix.m23;
			this.m30 = matrix.m30;
			this.m31 = matrix.m31;
			this.m32 = matrix.m32;
			this.m33 = matrix.m33;
		}
	}

	public void get(float[] matrix) {
		this.get(matrix, true);
	}

	public void get(float[] matrix, boolean rowMajor) {
		if (matrix.length != 16) {
			throw new IllegalArgumentException("Array must be of size 16.");
		}
		if (rowMajor) {
			matrix[0] = this.m00;
			matrix[1] = this.m01;
			matrix[2] = this.m02;
			matrix[3] = this.m03;
			matrix[4] = this.m10;
			matrix[5] = this.m11;
			matrix[6] = this.m12;
			matrix[7] = this.m13;
			matrix[8] = this.m20;
			matrix[9] = this.m21;
			matrix[10] = this.m22;
			matrix[11] = this.m23;
			matrix[12] = this.m30;
			matrix[13] = this.m31;
			matrix[14] = this.m32;
			matrix[15] = this.m33;
		} else {
			matrix[0] = this.m00;
			matrix[4] = this.m01;
			matrix[8] = this.m02;
			matrix[12] = this.m03;
			matrix[1] = this.m10;
			matrix[5] = this.m11;
			matrix[9] = this.m12;
			matrix[13] = this.m13;
			matrix[2] = this.m20;
			matrix[6] = this.m21;
			matrix[10] = this.m22;
			matrix[14] = this.m23;
			matrix[3] = this.m30;
			matrix[7] = this.m31;
			matrix[11] = this.m32;
			matrix[15] = this.m33;
		}
	}

	public float get(int i, int j) {
		switch (i) {
		case 0: {
			switch (j) {
			case 0: {
				return this.m00;
			}
			case 1: {
				return this.m01;
			}
			case 2: {
				return this.m02;
			}
			case 3: {
				return this.m03;
			}
			}
		}
		case 1: {
			switch (j) {
			case 0: {
				return this.m10;
			}
			case 1: {
				return this.m11;
			}
			case 2: {
				return this.m12;
			}
			case 3: {
				return this.m13;
			}
			}
		}
		case 2: {
			switch (j) {
			case 0: {
				return this.m20;
			}
			case 1: {
				return this.m21;
			}
			case 2: {
				return this.m22;
			}
			case 3: {
				return this.m23;
			}
			}
		}
		case 3: {
			switch (j) {
			case 0: {
				return this.m30;
			}
			case 1: {
				return this.m31;
			}
			case 2: {
				return this.m32;
			}
			case 3: {
				return this.m33;
			}
			}
		}
		}
		logger.warning("Invalid matrix index.");
		throw new IllegalArgumentException("Invalid indices into matrix.");
	}

	public float[] getColumn(int i) {
		return this.getColumn(i, null);
	}

	public float[] getColumn(int i, float[] store) {
		if (store == null) {
			store = new float[4];
		}
		switch (i) {
		case 0: {
			store[0] = this.m00;
			store[1] = this.m10;
			store[2] = this.m20;
			store[3] = this.m30;
			break;
		}
		case 1: {
			store[0] = this.m01;
			store[1] = this.m11;
			store[2] = this.m21;
			store[3] = this.m31;
			break;
		}
		case 2: {
			store[0] = this.m02;
			store[1] = this.m12;
			store[2] = this.m22;
			store[3] = this.m32;
			break;
		}
		case 3: {
			store[0] = this.m03;
			store[1] = this.m13;
			store[2] = this.m23;
			store[3] = this.m33;
			break;
		}
		default: {
			logger.warning("Invalid column index.");
			throw new IllegalArgumentException("Invalid column index. " + i);
		}
		}
		return store;
	}

	public void setColumn(int i, float[] column) {
		if (column == null) {
			logger.warning("Column is null. Ignoring.");
			return;
		}
		switch (i) {
		case 0: {
			this.m00 = column[0];
			this.m10 = column[1];
			this.m20 = column[2];
			this.m30 = column[3];
			break;
		}
		case 1: {
			this.m01 = column[0];
			this.m11 = column[1];
			this.m21 = column[2];
			this.m31 = column[3];
			break;
		}
		case 2: {
			this.m02 = column[0];
			this.m12 = column[1];
			this.m22 = column[2];
			this.m32 = column[3];
			break;
		}
		case 3: {
			this.m03 = column[0];
			this.m13 = column[1];
			this.m23 = column[2];
			this.m33 = column[3];
			break;
		}
		default: {
			logger.warning("Invalid column index.");
			throw new IllegalArgumentException("Invalid column index. " + i);
		}
		}
	}

	public void set(int i, int j, float value) {
		switch (i) {
		case 0: {
			switch (j) {
			case 0: {
				this.m00 = value;
				return;
			}
			case 1: {
				this.m01 = value;
				return;
			}
			case 2: {
				this.m02 = value;
				return;
			}
			case 3: {
				this.m03 = value;
				return;
			}
			}
		}
		case 1: {
			switch (j) {
			case 0: {
				this.m10 = value;
				return;
			}
			case 1: {
				this.m11 = value;
				return;
			}
			case 2: {
				this.m12 = value;
				return;
			}
			case 3: {
				this.m13 = value;
				return;
			}
			}
		}
		case 2: {
			switch (j) {
			case 0: {
				this.m20 = value;
				return;
			}
			case 1: {
				this.m21 = value;
				return;
			}
			case 2: {
				this.m22 = value;
				return;
			}
			case 3: {
				this.m23 = value;
				return;
			}
			}
		}
		case 3: {
			switch (j) {
			case 0: {
				this.m30 = value;
				return;
			}
			case 1: {
				this.m31 = value;
				return;
			}
			case 2: {
				this.m32 = value;
				return;
			}
			case 3: {
				this.m33 = value;
				return;
			}
			}
		}
		}
		logger.warning("Invalid matrix index.");
		throw new IllegalArgumentException("Invalid indices into matrix.");
	}

	public void set(float[][] matrix) {
		if (matrix.length != 4 || matrix[0].length != 4) {
			throw new IllegalArgumentException("Array must be of size 16.");
		}
		this.m00 = matrix[0][0];
		this.m01 = matrix[0][1];
		this.m02 = matrix[0][2];
		this.m03 = matrix[0][3];
		this.m10 = matrix[1][0];
		this.m11 = matrix[1][1];
		this.m12 = matrix[1][2];
		this.m13 = matrix[1][3];
		this.m20 = matrix[2][0];
		this.m21 = matrix[2][1];
		this.m22 = matrix[2][2];
		this.m23 = matrix[2][3];
		this.m30 = matrix[3][0];
		this.m31 = matrix[3][1];
		this.m32 = matrix[3][2];
		this.m33 = matrix[3][3];
	}

	public Matrix4f set(Matrix4f matrix) {
		this.m00 = matrix.m00;
		this.m01 = matrix.m01;
		this.m02 = matrix.m02;
		this.m03 = matrix.m03;
		this.m10 = matrix.m10;
		this.m11 = matrix.m11;
		this.m12 = matrix.m12;
		this.m13 = matrix.m13;
		this.m20 = matrix.m20;
		this.m21 = matrix.m21;
		this.m22 = matrix.m22;
		this.m23 = matrix.m23;
		this.m30 = matrix.m30;
		this.m31 = matrix.m31;
		this.m32 = matrix.m32;
		this.m33 = matrix.m33;
		return this;
	}

	public void set(float[] matrix) {
		this.set(matrix, true);
	}

	public void set(float[] matrix, boolean rowMajor) {
		if (matrix.length != 16) {
			throw new IllegalArgumentException("Array must be of size 16.");
		}
		if (rowMajor) {
			this.m00 = matrix[0];
			this.m01 = matrix[1];
			this.m02 = matrix[2];
			this.m03 = matrix[3];
			this.m10 = matrix[4];
			this.m11 = matrix[5];
			this.m12 = matrix[6];
			this.m13 = matrix[7];
			this.m20 = matrix[8];
			this.m21 = matrix[9];
			this.m22 = matrix[10];
			this.m23 = matrix[11];
			this.m30 = matrix[12];
			this.m31 = matrix[13];
			this.m32 = matrix[14];
			this.m33 = matrix[15];
		} else {
			this.m00 = matrix[0];
			this.m01 = matrix[4];
			this.m02 = matrix[8];
			this.m03 = matrix[12];
			this.m10 = matrix[1];
			this.m11 = matrix[5];
			this.m12 = matrix[9];
			this.m13 = matrix[13];
			this.m20 = matrix[2];
			this.m21 = matrix[6];
			this.m22 = matrix[10];
			this.m23 = matrix[14];
			this.m30 = matrix[3];
			this.m31 = matrix[7];
			this.m32 = matrix[11];
			this.m33 = matrix[15];
		}
	}

	public Matrix4f transpose() {
		float[] tmp = new float[16];
		this.get(tmp, true);
		Matrix4f mat = new Matrix4f(tmp);
		return mat;
	}

	public Matrix4f transposeLocal() {
		float tmp = this.m01;
		this.m01 = this.m10;
		this.m10 = tmp;
		tmp = this.m02;
		this.m02 = this.m20;
		this.m20 = tmp;
		tmp = this.m03;
		this.m03 = this.m30;
		this.m30 = tmp;
		tmp = this.m12;
		this.m12 = this.m21;
		this.m21 = tmp;
		tmp = this.m13;
		this.m13 = this.m31;
		this.m31 = tmp;
		tmp = this.m23;
		this.m23 = this.m32;
		this.m32 = tmp;
		return this;
	}

	public FloatBuffer toFloatBuffer() {
		return this.toFloatBuffer(false);
	}

	public FloatBuffer toFloatBuffer(boolean columnMajor) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		this.fillFloatBuffer(fb, columnMajor);
		fb.rewind();
		return fb;
	}

	public FloatBuffer fillFloatBuffer(FloatBuffer fb) {
		return this.fillFloatBuffer(fb, false);
	}

	public FloatBuffer fillFloatBuffer(FloatBuffer fb, boolean columnMajor) {
		if (columnMajor) {
			fb.put(this.m00).put(this.m10).put(this.m20).put(this.m30);
			fb.put(this.m01).put(this.m11).put(this.m21).put(this.m31);
			fb.put(this.m02).put(this.m12).put(this.m22).put(this.m32);
			fb.put(this.m03).put(this.m13).put(this.m23).put(this.m33);
		} else {
			fb.put(this.m00).put(this.m01).put(this.m02).put(this.m03);
			fb.put(this.m10).put(this.m11).put(this.m12).put(this.m13);
			fb.put(this.m20).put(this.m21).put(this.m22).put(this.m23);
			fb.put(this.m30).put(this.m31).put(this.m32).put(this.m33);
		}
		return fb;
	}

	public void fillFloatArray(float[] f, boolean columnMajor) {
		if (columnMajor) {
			f[0] = this.m00;
			f[1] = this.m10;
			f[2] = this.m20;
			f[3] = this.m30;
			f[4] = this.m01;
			f[5] = this.m11;
			f[6] = this.m21;
			f[7] = this.m31;
			f[8] = this.m02;
			f[9] = this.m12;
			f[10] = this.m22;
			f[11] = this.m32;
			f[12] = this.m03;
			f[13] = this.m13;
			f[14] = this.m23;
			f[15] = this.m33;
		} else {
			f[0] = this.m00;
			f[1] = this.m01;
			f[2] = this.m02;
			f[3] = this.m03;
			f[4] = this.m10;
			f[5] = this.m11;
			f[6] = this.m12;
			f[7] = this.m13;
			f[8] = this.m20;
			f[9] = this.m21;
			f[10] = this.m22;
			f[11] = this.m23;
			f[12] = this.m30;
			f[13] = this.m31;
			f[14] = this.m32;
			f[15] = this.m33;
		}
	}

	public Matrix4f readFloatBuffer(FloatBuffer fb) {
		return this.readFloatBuffer(fb, false);
	}

	public Matrix4f readFloatBuffer(FloatBuffer fb, boolean columnMajor) {
		if (columnMajor) {
			this.m00 = fb.get();
			this.m10 = fb.get();
			this.m20 = fb.get();
			this.m30 = fb.get();
			this.m01 = fb.get();
			this.m11 = fb.get();
			this.m21 = fb.get();
			this.m31 = fb.get();
			this.m02 = fb.get();
			this.m12 = fb.get();
			this.m22 = fb.get();
			this.m32 = fb.get();
			this.m03 = fb.get();
			this.m13 = fb.get();
			this.m23 = fb.get();
			this.m33 = fb.get();
		} else {
			this.m00 = fb.get();
			this.m01 = fb.get();
			this.m02 = fb.get();
			this.m03 = fb.get();
			this.m10 = fb.get();
			this.m11 = fb.get();
			this.m12 = fb.get();
			this.m13 = fb.get();
			this.m20 = fb.get();
			this.m21 = fb.get();
			this.m22 = fb.get();
			this.m23 = fb.get();
			this.m30 = fb.get();
			this.m31 = fb.get();
			this.m32 = fb.get();
			this.m33 = fb.get();
		}
		return this;
	}

	public void loadIdentity() {
		this.m03 = 0.0f;
		this.m02 = 0.0f;
		this.m01 = 0.0f;
		this.m13 = 0.0f;
		this.m12 = 0.0f;
		this.m10 = 0.0f;
		this.m23 = 0.0f;
		this.m21 = 0.0f;
		this.m20 = 0.0f;
		this.m32 = 0.0f;
		this.m31 = 0.0f;
		this.m30 = 0.0f;
		this.m33 = 1.0f;
		this.m22 = 1.0f;
		this.m11 = 1.0f;
		this.m00 = 1.0f;
	}

	public void fromFrustum(float near, float far, float left, float right, float top, float bottom, boolean parallel) {
		this.loadIdentity();
		if (parallel) {
			this.m00 = 2.0f / (right - left);
			this.m11 = 2.0f / (top - bottom);
			this.m22 = -2.0f / (far - near);
			this.m33 = 1.0f;
			this.m03 = -(right + left) / (right - left);
			this.m13 = -(top + bottom) / (top - bottom);
			this.m23 = -(far + near) / (far - near);
		} else {
			this.m00 = 2.0f * near / (right - left);
			this.m11 = 2.0f * near / (top - bottom);
			this.m32 = -1.0f;
			this.m33 = -0.0f;
			this.m02 = (right + left) / (right - left);
			this.m12 = (top + bottom) / (top - bottom);
			this.m22 = -(far + near) / (far - near);
			this.m23 = -(2.0f * far * near) / (far - near);
		}
	}

	public void fromAngleAxis(float angle, Vector3f axis) {
		Vector3f normAxis = axis.normalize();
		this.fromAngleNormalAxis(angle, normAxis);
	}

	public void fromAngleNormalAxis(float angle, Vector3f axis) {
		this.zero();
		this.m33 = 1.0f;
		float fCos = FastMath.cos(angle);
		float fSin = FastMath.sin(angle);
		float fOneMinusCos = 1.0f - fCos;
		float fX2 = axis.x * axis.x;
		float fY2 = axis.y * axis.y;
		float fZ2 = axis.z * axis.z;
		float fXYM = axis.x * axis.y * fOneMinusCos;
		float fXZM = axis.x * axis.z * fOneMinusCos;
		float fYZM = axis.y * axis.z * fOneMinusCos;
		float fXSin = axis.x * fSin;
		float fYSin = axis.y * fSin;
		float fZSin = axis.z * fSin;
		this.m00 = fX2 * fOneMinusCos + fCos;
		this.m01 = fXYM - fZSin;
		this.m02 = fXZM + fYSin;
		this.m10 = fXYM + fZSin;
		this.m11 = fY2 * fOneMinusCos + fCos;
		this.m12 = fYZM - fXSin;
		this.m20 = fXZM - fYSin;
		this.m21 = fYZM + fXSin;
		this.m22 = fZ2 * fOneMinusCos + fCos;
	}

	public void multLocal(float scalar) {
		this.m00 *= scalar;
		this.m01 *= scalar;
		this.m02 *= scalar;
		this.m03 *= scalar;
		this.m10 *= scalar;
		this.m11 *= scalar;
		this.m12 *= scalar;
		this.m13 *= scalar;
		this.m20 *= scalar;
		this.m21 *= scalar;
		this.m22 *= scalar;
		this.m23 *= scalar;
		this.m30 *= scalar;
		this.m31 *= scalar;
		this.m32 *= scalar;
		this.m33 *= scalar;
	}

	public Matrix4f mult(float scalar) {
		Matrix4f out = new Matrix4f();
		out.set(this);
		out.multLocal(scalar);
		return out;
	}

	public Matrix4f mult(float scalar, Matrix4f store) {
		store.set(this);
		store.multLocal(scalar);
		return store;
	}

	public Matrix4f mult(Matrix4f in2) {
		return this.mult(in2, null);
	}

	public Matrix4f mult(Matrix4f in2, Matrix4f store) {
		if (store == null) {
			store = new Matrix4f();
		}
		float temp00 = this.m00 * in2.m00 + this.m01 * in2.m10 + this.m02 * in2.m20 + this.m03 * in2.m30;
		float temp01 = this.m00 * in2.m01 + this.m01 * in2.m11 + this.m02 * in2.m21 + this.m03 * in2.m31;
		float temp02 = this.m00 * in2.m02 + this.m01 * in2.m12 + this.m02 * in2.m22 + this.m03 * in2.m32;
		float temp03 = this.m00 * in2.m03 + this.m01 * in2.m13 + this.m02 * in2.m23 + this.m03 * in2.m33;
		float temp10 = this.m10 * in2.m00 + this.m11 * in2.m10 + this.m12 * in2.m20 + this.m13 * in2.m30;
		float temp11 = this.m10 * in2.m01 + this.m11 * in2.m11 + this.m12 * in2.m21 + this.m13 * in2.m31;
		float temp12 = this.m10 * in2.m02 + this.m11 * in2.m12 + this.m12 * in2.m22 + this.m13 * in2.m32;
		float temp13 = this.m10 * in2.m03 + this.m11 * in2.m13 + this.m12 * in2.m23 + this.m13 * in2.m33;
		float temp20 = this.m20 * in2.m00 + this.m21 * in2.m10 + this.m22 * in2.m20 + this.m23 * in2.m30;
		float temp21 = this.m20 * in2.m01 + this.m21 * in2.m11 + this.m22 * in2.m21 + this.m23 * in2.m31;
		float temp22 = this.m20 * in2.m02 + this.m21 * in2.m12 + this.m22 * in2.m22 + this.m23 * in2.m32;
		float temp23 = this.m20 * in2.m03 + this.m21 * in2.m13 + this.m22 * in2.m23 + this.m23 * in2.m33;
		float temp30 = this.m30 * in2.m00 + this.m31 * in2.m10 + this.m32 * in2.m20 + this.m33 * in2.m30;
		float temp31 = this.m30 * in2.m01 + this.m31 * in2.m11 + this.m32 * in2.m21 + this.m33 * in2.m31;
		float temp32 = this.m30 * in2.m02 + this.m31 * in2.m12 + this.m32 * in2.m22 + this.m33 * in2.m32;
		float temp33 = this.m30 * in2.m03 + this.m31 * in2.m13 + this.m32 * in2.m23 + this.m33 * in2.m33;
		store.m00 = temp00;
		store.m01 = temp01;
		store.m02 = temp02;
		store.m03 = temp03;
		store.m10 = temp10;
		store.m11 = temp11;
		store.m12 = temp12;
		store.m13 = temp13;
		store.m20 = temp20;
		store.m21 = temp21;
		store.m22 = temp22;
		store.m23 = temp23;
		store.m30 = temp30;
		store.m31 = temp31;
		store.m32 = temp32;
		store.m33 = temp33;
		return store;
	}

	public Matrix4f multLocal(Matrix4f in2) {
		return this.mult(in2, this);
	}

	public Vector3f mult(Vector3f vec) {
		return this.mult(vec, null);
	}

	public Vector3f mult(Vector3f vec, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		float vx = vec.x;
		float vy = vec.y;
		float vz = vec.z;
		store.x = this.m00 * vx + this.m01 * vy + this.m02 * vz + this.m03;
		store.y = this.m10 * vx + this.m11 * vy + this.m12 * vz + this.m13;
		store.z = this.m20 * vx + this.m21 * vy + this.m22 * vz + this.m23;
		return store;
	}

	public Vector3f multNormal(Vector3f vec, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		float vx = vec.x;
		float vy = vec.y;
		float vz = vec.z;
		store.x = this.m00 * vx + this.m01 * vy + this.m02 * vz;
		store.y = this.m10 * vx + this.m11 * vy + this.m12 * vz;
		store.z = this.m20 * vx + this.m21 * vy + this.m22 * vz;
		return store;
	}

	public Vector3f multNormalAcross(Vector3f vec, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		float vx = vec.x;
		float vy = vec.y;
		float vz = vec.z;
		store.x = this.m00 * vx + this.m10 * vy + this.m20 * vz;
		store.y = this.m01 * vx + this.m11 * vy + this.m21 * vz;
		store.z = this.m02 * vx + this.m12 * vy + this.m22 * vz;
		return store;
	}

	public float multProj(Vector3f vec, Vector3f store) {
		float vx = vec.x;
		float vy = vec.y;
		float vz = vec.z;
		store.x = this.m00 * vx + this.m01 * vy + this.m02 * vz + this.m03;
		store.y = this.m10 * vx + this.m11 * vy + this.m12 * vz + this.m13;
		store.z = this.m20 * vx + this.m21 * vy + this.m22 * vz + this.m23;
		return this.m30 * vx + this.m31 * vy + this.m32 * vz + this.m33;
	}

	public Vector3f multAcross(Vector3f vec, Vector3f store) {
		if (null == vec) {
			logger.info("Source vector is null, null result returned.");
			return null;
		}
		if (store == null) {
			store = new Vector3f();
		}
		float vx = vec.x;
		float vy = vec.y;
		float vz = vec.z;
		store.x = this.m00 * vx + this.m10 * vy + this.m20 * vz + this.m30 * 1.0f;
		store.y = this.m01 * vx + this.m11 * vy + this.m21 * vz + this.m31 * 1.0f;
		store.z = this.m02 * vx + this.m12 * vy + this.m22 * vz + this.m32 * 1.0f;
		return store;
	}

	public float[] mult(float[] vec4f) {
		if (null == vec4f || vec4f.length != 4) {
			logger.warning("invalid array given, must be nonnull and length 4");
			return null;
		}
		float x = vec4f[0];
		float y = vec4f[1];
		float z = vec4f[2];
		float w = vec4f[3];
		vec4f[0] = this.m00 * x + this.m01 * y + this.m02 * z + this.m03 * w;
		vec4f[1] = this.m10 * x + this.m11 * y + this.m12 * z + this.m13 * w;
		vec4f[2] = this.m20 * x + this.m21 * y + this.m22 * z + this.m23 * w;
		vec4f[3] = this.m30 * x + this.m31 * y + this.m32 * z + this.m33 * w;
		return vec4f;
	}

	public float[] multAcross(float[] vec4f) {
		if (null == vec4f || vec4f.length != 4) {
			logger.warning("invalid array given, must be nonnull and length 4");
			return null;
		}
		float x = vec4f[0];
		float y = vec4f[1];
		float z = vec4f[2];
		float w = vec4f[3];
		vec4f[0] = this.m00 * x + this.m10 * y + this.m20 * z + this.m30 * w;
		vec4f[1] = this.m01 * x + this.m11 * y + this.m21 * z + this.m31 * w;
		vec4f[2] = this.m02 * x + this.m12 * y + this.m22 * z + this.m32 * w;
		vec4f[3] = this.m03 * x + this.m13 * y + this.m23 * z + this.m33 * w;
		return vec4f;
	}

	public Matrix4f invert() {
		return this.invert(null);
	}

	public Matrix4f invert(Matrix4f store) {
		float fB0;
		float fA5;
		float fB1;
		float fA4;
		float fB2;
		float fA3;
		float fB3;
		float fA2;
		float fB4;
		float fA1;
		float fB5;
		float fA0;
		float fDet;
		if (store == null) {
			store = new Matrix4f();
		}
		if (FastMath.abs(fDet = (fA0 = this.m00 * this.m11 - this.m01 * this.m10)
				* (fB5 = this.m22 * this.m33 - this.m23 * this.m32)
				- (fA1 = this.m00 * this.m12 - this.m02 * this.m10) * (fB4 = this.m21 * this.m33 - this.m23 * this.m31)
				+ (fA2 = this.m00 * this.m13 - this.m03 * this.m10) * (fB3 = this.m21 * this.m32 - this.m22 * this.m31)
				+ (fA3 = this.m01 * this.m12 - this.m02 * this.m11) * (fB2 = this.m20 * this.m33 - this.m23 * this.m30)
				- (fA4 = this.m01 * this.m13 - this.m03 * this.m11) * (fB1 = this.m20 * this.m32 - this.m22 * this.m30)
				+ (fA5 = this.m02 * this.m13 - this.m03 * this.m12)
						* (fB0 = this.m20 * this.m31 - this.m21 * this.m30)) <= 0.0f) {
			throw new ArithmeticException("This matrix cannot be inverted");
		}
		store.m00 = this.m11 * fB5 - this.m12 * fB4 + this.m13 * fB3;
		store.m10 = -this.m10 * fB5 + this.m12 * fB2 - this.m13 * fB1;
		store.m20 = this.m10 * fB4 - this.m11 * fB2 + this.m13 * fB0;
		store.m30 = -this.m10 * fB3 + this.m11 * fB1 - this.m12 * fB0;
		store.m01 = -this.m01 * fB5 + this.m02 * fB4 - this.m03 * fB3;
		store.m11 = this.m00 * fB5 - this.m02 * fB2 + this.m03 * fB1;
		store.m21 = -this.m00 * fB4 + this.m01 * fB2 - this.m03 * fB0;
		store.m31 = this.m00 * fB3 - this.m01 * fB1 + this.m02 * fB0;
		store.m02 = this.m31 * fA5 - this.m32 * fA4 + this.m33 * fA3;
		store.m12 = -this.m30 * fA5 + this.m32 * fA2 - this.m33 * fA1;
		store.m22 = this.m30 * fA4 - this.m31 * fA2 + this.m33 * fA0;
		store.m32 = -this.m30 * fA3 + this.m31 * fA1 - this.m32 * fA0;
		store.m03 = -this.m21 * fA5 + this.m22 * fA4 - this.m23 * fA3;
		store.m13 = this.m20 * fA5 - this.m22 * fA2 + this.m23 * fA1;
		store.m23 = -this.m20 * fA4 + this.m21 * fA2 - this.m23 * fA0;
		store.m33 = this.m20 * fA3 - this.m21 * fA1 + this.m22 * fA0;
		float fInvDet = 1.0f / fDet;
		store.multLocal(fInvDet);
		return store;
	}

	public Matrix4f invertLocal() {
		float fA0 = this.m00 * this.m11 - this.m01 * this.m10;
		float fB5 = this.m22 * this.m33 - this.m23 * this.m32;
		float fA1 = this.m00 * this.m12 - this.m02 * this.m10;
		float fB4 = this.m21 * this.m33 - this.m23 * this.m31;
		float fA2 = this.m00 * this.m13 - this.m03 * this.m10;
		float fB3 = this.m21 * this.m32 - this.m22 * this.m31;
		float fA3 = this.m01 * this.m12 - this.m02 * this.m11;
		float fB2 = this.m20 * this.m33 - this.m23 * this.m30;
		float fA4 = this.m01 * this.m13 - this.m03 * this.m11;
		float fB1 = this.m20 * this.m32 - this.m22 * this.m30;
		float fA5 = this.m02 * this.m13 - this.m03 * this.m12;
		float fB0 = this.m20 * this.m31 - this.m21 * this.m30;
		float fDet = fA0 * fB5 - fA1 * fB4 + fA2 * fB3 + fA3 * fB2 - fA4 * fB1 + fA5 * fB0;
		if (FastMath.abs(fDet) <= 0.0f) {
			return this.zero();
		}
		float f00 = this.m11 * fB5 - this.m12 * fB4 + this.m13 * fB3;
		float f10 = -this.m10 * fB5 + this.m12 * fB2 - this.m13 * fB1;
		float f20 = this.m10 * fB4 - this.m11 * fB2 + this.m13 * fB0;
		float f30 = -this.m10 * fB3 + this.m11 * fB1 - this.m12 * fB0;
		float f01 = -this.m01 * fB5 + this.m02 * fB4 - this.m03 * fB3;
		float f11 = this.m00 * fB5 - this.m02 * fB2 + this.m03 * fB1;
		float f21 = -this.m00 * fB4 + this.m01 * fB2 - this.m03 * fB0;
		float f31 = this.m00 * fB3 - this.m01 * fB1 + this.m02 * fB0;
		float f02 = this.m31 * fA5 - this.m32 * fA4 + this.m33 * fA3;
		float f12 = -this.m30 * fA5 + this.m32 * fA2 - this.m33 * fA1;
		float f22 = this.m30 * fA4 - this.m31 * fA2 + this.m33 * fA0;
		float f32 = -this.m30 * fA3 + this.m31 * fA1 - this.m32 * fA0;
		float f03 = -this.m21 * fA5 + this.m22 * fA4 - this.m23 * fA3;
		float f13 = this.m20 * fA5 - this.m22 * fA2 + this.m23 * fA1;
		float f23 = -this.m20 * fA4 + this.m21 * fA2 - this.m23 * fA0;
		float f33 = this.m20 * fA3 - this.m21 * fA1 + this.m22 * fA0;
		this.m00 = f00;
		this.m01 = f01;
		this.m02 = f02;
		this.m03 = f03;
		this.m10 = f10;
		this.m11 = f11;
		this.m12 = f12;
		this.m13 = f13;
		this.m20 = f20;
		this.m21 = f21;
		this.m22 = f22;
		this.m23 = f23;
		this.m30 = f30;
		this.m31 = f31;
		this.m32 = f32;
		this.m33 = f33;
		float fInvDet = 1.0f / fDet;
		this.multLocal(fInvDet);
		return this;
	}

	public Matrix4f adjoint() {
		return this.adjoint(null);
	}

	public Matrix4f adjoint(Matrix4f store) {
		if (store == null) {
			store = new Matrix4f();
		}
		float fA0 = this.m00 * this.m11 - this.m01 * this.m10;
		float fA1 = this.m00 * this.m12 - this.m02 * this.m10;
		float fA2 = this.m00 * this.m13 - this.m03 * this.m10;
		float fA3 = this.m01 * this.m12 - this.m02 * this.m11;
		float fA4 = this.m01 * this.m13 - this.m03 * this.m11;
		float fA5 = this.m02 * this.m13 - this.m03 * this.m12;
		float fB0 = this.m20 * this.m31 - this.m21 * this.m30;
		float fB1 = this.m20 * this.m32 - this.m22 * this.m30;
		float fB2 = this.m20 * this.m33 - this.m23 * this.m30;
		float fB3 = this.m21 * this.m32 - this.m22 * this.m31;
		float fB4 = this.m21 * this.m33 - this.m23 * this.m31;
		float fB5 = this.m22 * this.m33 - this.m23 * this.m32;
		store.m00 = this.m11 * fB5 - this.m12 * fB4 + this.m13 * fB3;
		store.m10 = -this.m10 * fB5 + this.m12 * fB2 - this.m13 * fB1;
		store.m20 = this.m10 * fB4 - this.m11 * fB2 + this.m13 * fB0;
		store.m30 = -this.m10 * fB3 + this.m11 * fB1 - this.m12 * fB0;
		store.m01 = -this.m01 * fB5 + this.m02 * fB4 - this.m03 * fB3;
		store.m11 = this.m00 * fB5 - this.m02 * fB2 + this.m03 * fB1;
		store.m21 = -this.m00 * fB4 + this.m01 * fB2 - this.m03 * fB0;
		store.m31 = this.m00 * fB3 - this.m01 * fB1 + this.m02 * fB0;
		store.m02 = this.m31 * fA5 - this.m32 * fA4 + this.m33 * fA3;
		store.m12 = -this.m30 * fA5 + this.m32 * fA2 - this.m33 * fA1;
		store.m22 = this.m30 * fA4 - this.m31 * fA2 + this.m33 * fA0;
		store.m32 = -this.m30 * fA3 + this.m31 * fA1 - this.m32 * fA0;
		store.m03 = -this.m21 * fA5 + this.m22 * fA4 - this.m23 * fA3;
		store.m13 = this.m20 * fA5 - this.m22 * fA2 + this.m23 * fA1;
		store.m23 = -this.m20 * fA4 + this.m21 * fA2 - this.m23 * fA0;
		store.m33 = this.m20 * fA3 - this.m21 * fA1 + this.m22 * fA0;
		return store;
	}

	public float determinant() {
		float fA0 = this.m00 * this.m11 - this.m01 * this.m10;
		float fA1 = this.m00 * this.m12 - this.m02 * this.m10;
		float fA2 = this.m00 * this.m13 - this.m03 * this.m10;
		float fA3 = this.m01 * this.m12 - this.m02 * this.m11;
		float fA4 = this.m01 * this.m13 - this.m03 * this.m11;
		float fA5 = this.m02 * this.m13 - this.m03 * this.m12;
		float fB0 = this.m20 * this.m31 - this.m21 * this.m30;
		float fB1 = this.m20 * this.m32 - this.m22 * this.m30;
		float fB2 = this.m20 * this.m33 - this.m23 * this.m30;
		float fB3 = this.m21 * this.m32 - this.m22 * this.m31;
		float fB4 = this.m21 * this.m33 - this.m23 * this.m31;
		float fB5 = this.m22 * this.m33 - this.m23 * this.m32;
		float fDet = fA0 * fB5 - fA1 * fB4 + fA2 * fB3 + fA3 * fB2 - fA4 * fB1 + fA5 * fB0;
		return fDet;
	}

	public Matrix4f zero() {
		this.m03 = 0.0f;
		this.m02 = 0.0f;
		this.m01 = 0.0f;
		this.m00 = 0.0f;
		this.m13 = 0.0f;
		this.m12 = 0.0f;
		this.m11 = 0.0f;
		this.m10 = 0.0f;
		this.m23 = 0.0f;
		this.m22 = 0.0f;
		this.m21 = 0.0f;
		this.m20 = 0.0f;
		this.m33 = 0.0f;
		this.m32 = 0.0f;
		this.m31 = 0.0f;
		this.m30 = 0.0f;
		return this;
	}

	public Matrix4f add(Matrix4f mat) {
		Matrix4f result = new Matrix4f();
		result.m00 = this.m00 + mat.m00;
		result.m01 = this.m01 + mat.m01;
		result.m02 = this.m02 + mat.m02;
		result.m03 = this.m03 + mat.m03;
		result.m10 = this.m10 + mat.m10;
		result.m11 = this.m11 + mat.m11;
		result.m12 = this.m12 + mat.m12;
		result.m13 = this.m13 + mat.m13;
		result.m20 = this.m20 + mat.m20;
		result.m21 = this.m21 + mat.m21;
		result.m22 = this.m22 + mat.m22;
		result.m23 = this.m23 + mat.m23;
		result.m30 = this.m30 + mat.m30;
		result.m31 = this.m31 + mat.m31;
		result.m32 = this.m32 + mat.m32;
		result.m33 = this.m33 + mat.m33;
		return result;
	}

	public void addLocal(Matrix4f mat) {
		this.m00 += mat.m00;
		this.m01 += mat.m01;
		this.m02 += mat.m02;
		this.m03 += mat.m03;
		this.m10 += mat.m10;
		this.m11 += mat.m11;
		this.m12 += mat.m12;
		this.m13 += mat.m13;
		this.m20 += mat.m20;
		this.m21 += mat.m21;
		this.m22 += mat.m22;
		this.m23 += mat.m23;
		this.m30 += mat.m30;
		this.m31 += mat.m31;
		this.m32 += mat.m32;
		this.m33 += mat.m33;
	}

	public Vector3f toTranslationVector() {
		return new Vector3f(this.m03, this.m13, this.m23);
	}

	public void toTranslationVector(Vector3f vector) {
		vector.set(this.m03, this.m13, this.m23);
	}

	public Matrix3f toRotationMatrix() {
		return new Matrix3f(this.m00, this.m01, this.m02, this.m10, this.m11, this.m12, this.m20, this.m21, this.m22);
	}

	public void toRotationMatrix(Matrix3f mat) {
		mat.m00 = this.m00;
		mat.m01 = this.m01;
		mat.m02 = this.m02;
		mat.m10 = this.m10;
		mat.m11 = this.m11;
		mat.m12 = this.m12;
		mat.m20 = this.m20;
		mat.m21 = this.m21;
		mat.m22 = this.m22;
	}

	public void setRotationMatrix(Matrix3f mat) {
		this.m00 = mat.m00;
		this.m01 = mat.m01;
		this.m02 = mat.m02;
		this.m10 = mat.m10;
		this.m11 = mat.m11;
		this.m12 = mat.m12;
		this.m20 = mat.m20;
		this.m21 = mat.m21;
		this.m22 = mat.m22;
	}

	public void setScale(float x, float y, float z) {
		this.m00 *= x;
		this.m11 *= y;
		this.m22 *= z;
	}

	public void setScale(Vector3f scale) {
		this.m00 *= scale.x;
		this.m11 *= scale.y;
		this.m22 *= scale.z;
	}

	public void setTranslation(float[] translation) {
		if (translation.length != 3) {
			throw new IllegalArgumentException("Translation size must be 3.");
		}
		this.m03 = translation[0];
		this.m13 = translation[1];
		this.m23 = translation[2];
	}

	public void setTranslation(float x, float y, float z) {
		this.m03 = x;
		this.m13 = y;
		this.m23 = z;
	}

	public void setTranslation(Vector3f translation) {
		this.m03 = translation.x;
		this.m13 = translation.y;
		this.m23 = translation.z;
	}

	public void setInverseTranslation(float[] translation) {
		if (translation.length != 3) {
			throw new IllegalArgumentException("Translation size must be 3.");
		}
		this.m03 = -translation[0];
		this.m13 = -translation[1];
		this.m23 = -translation[2];
	}

	public void angleRotation(Vector3f angles) {
		float angle = angles.z * ((float) Math.PI / 180);
		float sy = FastMath.sin(angle);
		float cy = FastMath.cos(angle);
		angle = angles.y * ((float) Math.PI / 180);
		float sp = FastMath.sin(angle);
		float cp = FastMath.cos(angle);
		angle = angles.x * ((float) Math.PI / 180);
		float sr = FastMath.sin(angle);
		float cr = FastMath.cos(angle);
		this.m00 = cp * cy;
		this.m10 = cp * sy;
		this.m20 = -sp;
		this.m01 = sr * sp * cy + cr * -sy;
		this.m11 = sr * sp * sy + cr * cy;
		this.m21 = sr * cp;
		this.m02 = cr * sp * cy + -sr * -sy;
		this.m12 = cr * sp * sy + -sr * cy;
		this.m22 = cr * cp;
		this.m03 = 0.0f;
		this.m13 = 0.0f;
		this.m23 = 0.0f;
	}

	public void setInverseRotationRadians(float[] angles) {
		if (angles.length != 3) {
			throw new IllegalArgumentException("Angles must be of size 3.");
		}
		double cr = FastMath.cos(angles[0]);
		double sr = FastMath.sin(angles[0]);
		double cp = FastMath.cos(angles[1]);
		double sp = FastMath.sin(angles[1]);
		double cy = FastMath.cos(angles[2]);
		double sy = FastMath.sin(angles[2]);
		this.m00 = (float) (cp * cy);
		this.m10 = (float) (cp * sy);
		this.m20 = (float) (-sp);
		double srsp = sr * sp;
		double crsp = cr * sp;
		this.m01 = (float) (srsp * cy - cr * sy);
		this.m11 = (float) (srsp * sy + cr * cy);
		this.m21 = (float) (sr * cp);
		this.m02 = (float) (crsp * cy + sr * sy);
		this.m12 = (float) (crsp * sy - sr * cy);
		this.m22 = (float) (cr * cp);
	}

	public void setInverseRotationDegrees(float[] angles) {
		if (angles.length != 3) {
			throw new IllegalArgumentException("Angles must be of size 3.");
		}
		float[] vec = new float[] { angles[0] * 57.295776f, angles[1] * 57.295776f, angles[2] * 57.295776f };
		this.setInverseRotationRadians(vec);
	}

	public void inverseTranslateVect(float[] vec) {
		if (vec.length != 3) {
			throw new IllegalArgumentException("vec must be of size 3.");
		}
		vec[0] = vec[0] - this.m03;
		vec[1] = vec[1] - this.m13;
		vec[2] = vec[2] - this.m23;
	}

	public void inverseTranslateVect(Vector3f data) {
		data.x -= this.m03;
		data.y -= this.m13;
		data.z -= this.m23;
	}

	public void translateVect(Vector3f data) {
		data.x += this.m03;
		data.y += this.m13;
		data.z += this.m23;
	}

	public void inverseRotateVect(Vector3f vec) {
		float vx = vec.x;
		float vy = vec.y;
		float vz = vec.z;
		vec.x = vx * this.m00 + vy * this.m10 + vz * this.m20;
		vec.y = vx * this.m01 + vy * this.m11 + vz * this.m21;
		vec.z = vx * this.m02 + vy * this.m12 + vz * this.m22;
	}

	public void rotateVect(Vector3f vec) {
		float vx = vec.x;
		float vy = vec.y;
		float vz = vec.z;
		vec.x = vx * this.m00 + vy * this.m01 + vz * this.m02;
		vec.y = vx * this.m10 + vy * this.m11 + vz * this.m12;
		vec.z = vx * this.m20 + vy * this.m21 + vz * this.m22;
	}

	public String toString() {
		StringBuilder result = new StringBuilder("Matrix4f\n[\n");
		result.append(" ");
		result.append(this.m00);
		result.append("  ");
		result.append(this.m01);
		result.append("  ");
		result.append(this.m02);
		result.append("  ");
		result.append(this.m03);
		result.append(" \n");
		result.append(" ");
		result.append(this.m10);
		result.append("  ");
		result.append(this.m11);
		result.append("  ");
		result.append(this.m12);
		result.append("  ");
		result.append(this.m13);
		result.append(" \n");
		result.append(" ");
		result.append(this.m20);
		result.append("  ");
		result.append(this.m21);
		result.append("  ");
		result.append(this.m22);
		result.append("  ");
		result.append(this.m23);
		result.append(" \n");
		result.append(" ");
		result.append(this.m30);
		result.append("  ");
		result.append(this.m31);
		result.append("  ");
		result.append(this.m32);
		result.append("  ");
		result.append(this.m33);
		result.append(" \n]");
		return result.toString();
	}

	public int hashCode() {
		int hash = 37;
		hash = 37 * hash + Float.floatToIntBits(this.m00);
		hash = 37 * hash + Float.floatToIntBits(this.m01);
		hash = 37 * hash + Float.floatToIntBits(this.m02);
		hash = 37 * hash + Float.floatToIntBits(this.m03);
		hash = 37 * hash + Float.floatToIntBits(this.m10);
		hash = 37 * hash + Float.floatToIntBits(this.m11);
		hash = 37 * hash + Float.floatToIntBits(this.m12);
		hash = 37 * hash + Float.floatToIntBits(this.m13);
		hash = 37 * hash + Float.floatToIntBits(this.m20);
		hash = 37 * hash + Float.floatToIntBits(this.m21);
		hash = 37 * hash + Float.floatToIntBits(this.m22);
		hash = 37 * hash + Float.floatToIntBits(this.m23);
		hash = 37 * hash + Float.floatToIntBits(this.m30);
		hash = 37 * hash + Float.floatToIntBits(this.m31);
		hash = 37 * hash + Float.floatToIntBits(this.m32);
		hash = 37 * hash + Float.floatToIntBits(this.m33);
		return hash;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Matrix4f) || o == null) {
			return false;
		}
		if (this == o) {
			return true;
		}
		Matrix4f comp = (Matrix4f) o;
		if (Float.compare(this.m00, comp.m00) != 0) {
			return false;
		}
		if (Float.compare(this.m01, comp.m01) != 0) {
			return false;
		}
		if (Float.compare(this.m02, comp.m02) != 0) {
			return false;
		}
		if (Float.compare(this.m03, comp.m03) != 0) {
			return false;
		}
		if (Float.compare(this.m10, comp.m10) != 0) {
			return false;
		}
		if (Float.compare(this.m11, comp.m11) != 0) {
			return false;
		}
		if (Float.compare(this.m12, comp.m12) != 0) {
			return false;
		}
		if (Float.compare(this.m13, comp.m13) != 0) {
			return false;
		}
		if (Float.compare(this.m20, comp.m20) != 0) {
			return false;
		}
		if (Float.compare(this.m21, comp.m21) != 0) {
			return false;
		}
		if (Float.compare(this.m22, comp.m22) != 0) {
			return false;
		}
		if (Float.compare(this.m23, comp.m23) != 0) {
			return false;
		}
		if (Float.compare(this.m30, comp.m30) != 0) {
			return false;
		}
		if (Float.compare(this.m31, comp.m31) != 0) {
			return false;
		}
		if (Float.compare(this.m32, comp.m32) != 0) {
			return false;
		}
		return Float.compare(this.m33, comp.m33) == 0;
	}

	public Class<? extends Matrix4f> getClassTag() {
		return this.getClass();
	}

	public boolean isIdentity() {
		return this.m00 == 1.0f && this.m01 == 0.0f && this.m02 == 0.0f && this.m03 == 0.0f && this.m10 == 0.0f
				&& this.m11 == 1.0f && this.m12 == 0.0f && this.m13 == 0.0f && this.m20 == 0.0f && this.m21 == 0.0f
				&& this.m22 == 1.0f && this.m23 == 0.0f && this.m30 == 0.0f && this.m31 == 0.0f && this.m32 == 0.0f
				&& this.m33 == 1.0f;
	}

	public void scale(Vector3f scale) {
		this.m00 *= scale.getX();
		this.m10 *= scale.getX();
		this.m20 *= scale.getX();
		this.m30 *= scale.getX();
		this.m01 *= scale.getY();
		this.m11 *= scale.getY();
		this.m21 *= scale.getY();
		this.m31 *= scale.getY();
		this.m02 *= scale.getZ();
		this.m12 *= scale.getZ();
		this.m22 *= scale.getZ();
		this.m32 *= scale.getZ();
	}

	public void scale(float scale) {
		this.m00 *= scale;
		this.m10 *= scale;
		this.m20 *= scale;
		this.m30 *= scale;
		this.m01 *= scale;
		this.m11 *= scale;
		this.m21 *= scale;
		this.m31 *= scale;
		this.m02 *= scale;
		this.m12 *= scale;
		this.m22 *= scale;
		this.m32 *= scale;
	}

	static boolean equalIdentity(Matrix4f mat) {
		if ((double) Math.abs(mat.m00 - 1.0f) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m11 - 1.0f) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m22 - 1.0f) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m33 - 1.0f) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m01) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m02) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m03) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m10) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m12) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m13) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m20) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m21) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m23) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m30) > 1.0E-4) {
			return false;
		}
		if ((double) Math.abs(mat.m31) > 1.0E-4) {
			return false;
		}
		return !((double) Math.abs(mat.m32) > 1.0E-4);
	}

	public Matrix4f clone() {
		try {
			return (Matrix4f) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
