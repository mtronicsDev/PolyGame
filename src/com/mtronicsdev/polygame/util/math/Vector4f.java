package com.mtronicsdev.polygame.util.math;

import static java.lang.Math.sqrt;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Vector4f {

    public static final Vector4f ZERO = new Vector4f();

    public float x, y, z, w;

    public Vector4f() {
        x = y = z = w = 0;
    }

    public Vector4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vector4f(Vector4f clone) {
        x = clone.x;
        y = clone.y;
        z = clone.z;
        w = clone.w;
    }

    public void multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        w *= scalar;
    }

    public void add(Vector4f add) {
        x += add.x;
        y += add.y;
        z += add.z;
        w += add.w;
    }

    public void subtract(Vector4f sub) {
        x -= sub.x;
        y -= sub.y;
        z -= sub.z;
        w -= sub.w;
    }

    public float dot(Vector4f dot) {
        return x * dot.x + y * dot.y + z * dot.z + w * dot.w;
    }

    public float length() {
        return (float) sqrt(dot(this));
    }

    public void normalize() {
        float scalar = 1f / length();
        multiply(scalar);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (obj instanceof Vector4f) {
            Vector4f vec = (Vector4f) obj;
            return x == vec.x && y == vec.y && z == vec.z && w == vec.w;
        } else return false;
    }

    @Override
    public String toString() {
        return "Vector3f[" + x + ", " + y + ", " + z + ", " + w + "]";
    }

    public void invert() {
        multiply(-1);
    }
}