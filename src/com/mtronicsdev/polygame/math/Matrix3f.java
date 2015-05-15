package com.mtronicsdev.polygame.math;

public class Matrix3f {

    public float m00, m01, m02,
            m10, m11, m12,
            m20, m21, m22;

    public Matrix3f() {

    }

    public Matrix3f(float... matrix) {
        if (matrix.length != 9) throw new IllegalArgumentException();
        this.m00 = matrix[0];
        this.m01 = matrix[1];
        this.m02 = matrix[2];
        this.m10 = matrix[3];
        this.m11 = matrix[4];
        this.m12 = matrix[5];
        this.m20 = matrix[6];
        this.m21 = matrix[7];
        this.m22 = matrix[8];
    }

    public Matrix3f(Matrix3f clone) {
        this.m00 = clone.m00;
        this.m01 = clone.m01;
        this.m02 = clone.m02;
        this.m10 = clone.m10;
        this.m11 = clone.m11;
        this.m12 = clone.m12;
        this.m20 = clone.m20;
        this.m21 = clone.m21;
        this.m22 = clone.m22;
    }

    public Vector3f multiply(Vector3f vector) {
        return new Vector3f(vector.x * m00 + vector.y * m01 + vector.z * m02,
                vector.x * m10 + vector.y * m11 + vector.z * m12,
                vector.x * m20 + vector.y * m21 + vector.z * m22);
    }

    @Override
    public String toString() {
        return "Matrix3f[" + m00 + ", " + m01 + ", " + m02 + ", "
                + m10 + ", " + m11 + ", " + m12 + ", "
                + m20 + ", " + m21 + ", " + m22 + "]";
    }
}