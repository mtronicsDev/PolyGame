package com.mtronicsdev.polygame.util.math;

import java.nio.FloatBuffer;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Matrix4f {

    public float m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33;

    public Matrix4f() {

    }

    public Matrix4f(float... matrix) {
        if (matrix.length != 9) throw new IllegalArgumentException();
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
    }

    public Matrix4f(Matrix4f clone) {
        this.m00 = clone.m00;
        this.m01 = clone.m01;
        this.m02 = clone.m02;
        this.m03 = clone.m03;
        this.m10 = clone.m10;
        this.m11 = clone.m11;
        this.m12 = clone.m12;
        this.m13 = clone.m13;
        this.m20 = clone.m20;
        this.m21 = clone.m21;
        this.m22 = clone.m22;
        this.m23 = clone.m23;
        this.m30 = clone.m30;
        this.m31 = clone.m31;
        this.m32 = clone.m32;
        this.m33 = clone.m33;
    }

    public Matrix4f setIdentity() {
        m00 = 1;
        m01 = 0;
        m02 = 0;
        m03 = 0;
        m10 = 0;
        m11 = 1;
        m12 = 0;
        m13 = 0;
        m20 = 0;
        m21 = 0;
        m22 = 1;
        m23 = 0;
        m30 = 0;
        m31 = 0;
        m32 = 0;
        m33 = 1;

        return this;
    }

    public Matrix4f scale(Vector3f scalar) {
        m00 *= scalar.x;
        m01 *= scalar.x;
        m02 *= scalar.x;
        m03 *= scalar.x;
        m10 *= scalar.y;
        m11 *= scalar.y;
        m12 *= scalar.y;
        m13 *= scalar.y;
        m20 *= scalar.z;
        m21 *= scalar.z;
        m22 *= scalar.z;
        m23 *= scalar.z;

        return this;
    }

    public Matrix4f scale(Vector2f scalar) {
        m00 *= scalar.x;
        m01 *= scalar.x;
        m02 *= scalar.x;
        m03 *= scalar.x;
        m10 *= scalar.y;
        m11 *= scalar.y;
        m12 *= scalar.y;
        m13 *= scalar.y;

        return this;
    }

    public FloatBuffer store(FloatBuffer buffer) {
        buffer.put(m00);
        buffer.put(m01);
        buffer.put(m02);
        buffer.put(m03);
        buffer.put(m10);
        buffer.put(m11);
        buffer.put(m12);
        buffer.put(m13);
        buffer.put(m20);
        buffer.put(m21);
        buffer.put(m22);
        buffer.put(m23);
        buffer.put(m30);
        buffer.put(m31);
        buffer.put(m32);
        buffer.put(m33);

        return buffer;
    }

    /**
     * Adapted version of the LWJGL 2 {@code Matrix4f.rotate(float, Vector3f, Matrix4f, Matrix4f)} method.
     * <p>
     * <p>It can be found at:
     * http://github.com/LWJGL/lwjgl/blob/master/src/java/org/lwjgl/util/vector/Matrix4f.java</p>
     *
     * @param angle The rotation angle in radians
     * @param axis  The rotation axis
     * @return This matrix, after the rotation has been applied
     */
    public Matrix4f rotate(float angle, Vector3f axis) {
        float c = (float) cos(angle);
        float s = (float) sin(angle);
        float oneMinusC = 1.0f - c;
        float xy = axis.x * axis.y;
        float yz = axis.y * axis.z;
        float xz = axis.x * axis.z;
        float xs = axis.x * s;
        float ys = axis.y * s;
        float zs = axis.z * s;

        float f00 = axis.x * axis.x * oneMinusC + c;
        float f01 = xy * oneMinusC + zs;
        float f02 = xz * oneMinusC - ys;
        // n[3] not used
        float f10 = xy * oneMinusC - zs;
        float f11 = axis.y * axis.y * oneMinusC + c;
        float f12 = yz * oneMinusC + xs;
        // n[7] not used
        float f20 = xz * oneMinusC + ys;
        float f21 = yz * oneMinusC - xs;
        float f22 = axis.z * axis.z * oneMinusC + c;

        float t00 = m00 * f00 + m10 * f01 + m20 * f02;
        float t01 = m01 * f00 + m11 * f01 + m21 * f02;
        float t02 = m02 * f00 + m12 * f01 + m22 * f02;
        float t03 = m03 * f00 + m13 * f01 + m23 * f02;
        float t10 = m00 * f10 + m10 * f11 + m20 * f12;
        float t11 = m01 * f10 + m11 * f11 + m21 * f12;
        float t12 = m02 * f10 + m12 * f11 + m22 * f12;
        float t13 = m03 * f10 + m13 * f11 + m23 * f12;

        m20 = m00 * f20 + m10 * f21 + m20 * f22;
        m21 = m01 * f20 + m11 * f21 + m21 * f22;
        m22 = m02 * f20 + m12 * f21 + m22 * f22;
        m23 = m03 * f20 + m13 * f21 + m23 * f22;

        m00 = t00;
        m01 = t01;
        m02 = t02;
        m03 = t03;
        m10 = t10;
        m11 = t11;
        m12 = t12;
        m13 = t13;

        return this;
    }

    public Matrix4f translate(Vector3f vector) {
        m30 += m00 * vector.x + m10 * vector.y + m20 * vector.z;
        m31 += m01 * vector.x + m11 * vector.y + m21 * vector.z;
        m32 += m02 * vector.x + m12 * vector.y + m22 * vector.z;
        m33 += m03 * vector.x + m13 * vector.y + m23 * vector.z;

        return this;
    }

    public Matrix4f translate(Vector2f vector) {
        m30 += m00 * vector.x + m10 * vector.y;
        m31 += m01 * vector.x + m11 * vector.y;
        m32 += m02 * vector.x + m12 * vector.y;
        m33 += m03 * vector.x + m13 * vector.y;

        return this;
    }

    @Override
    public String toString() {
        return "Matrix3f[" + m00 + ", " + m01 + ", " + m02 + ", " + m03 + ", "
                + m10 + ", " + m11 + ", " + m12 + ", " + m13 + ", "
                + m20 + ", " + m21 + ", " + m22 + ", " + m23 + ", "
                + m30 + ", " + m31 + ", " + m32 + ", " + m33 + "]";
    }
}