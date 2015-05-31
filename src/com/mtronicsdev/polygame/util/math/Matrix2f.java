package com.mtronicsdev.polygame.util.math;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Matrix2f {

    public float m00, m01,
            m10, m11;

    public Matrix2f() {
    }

    public Matrix2f(float... matrix) {
        if (matrix.length != 4) throw new IllegalArgumentException();
        m00 = matrix[0];
        m01 = matrix[1];
        m10 = matrix[2];
        m11 = matrix[3];
    }

    public Matrix2f(Matrix2f clone) {
        this.m00 = clone.m00;
        this.m01 = clone.m01;
        this.m10 = clone.m10;
        this.m11 = clone.m11;
    }

    public Vector2f multiply(Vector2f vector) {
        return new Vector2f(vector.x * m00 + vector.y * m01,
                vector.x * m10 + vector.y * m11);
    }

    @Override
    public String toString() {
        return "Matrix2f[" + m00 + ", " + m01 + ", "
                + m10 + ", " + m11 + "]";
    }
}