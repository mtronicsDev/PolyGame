package com.mtronicsdev.polygame.util.math;

import java.io.Serializable;

import static java.lang.Math.sqrt;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Vector3f implements Serializable {

    public static final Vector3f ZERO = new Vector3f();
    public static final Vector3f UP = new Vector3f(0, 1, 0);

    public float x, y, z;

    public Vector3f() {
        x = y = z = 0;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f clone) {
        x = clone.x;
        y = clone.y;
        z = clone.z;
    }

    public Vector3f multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
        return this;
    }

    public Vector3f add(Vector3f add) {
        x += add.x;
        y += add.y;
        z += add.z;
        return this;
    }

    public Vector3f subtract(Vector3f sub) {
        x -= sub.x;
        y -= sub.y;
        z -= sub.z;
        return this;
    }

    public Vector3f cross(Vector3f vec1, Vector3f vec2) {
        x = vec2.z * vec1.y - vec2.y * vec1.z;
        y = vec2.x * vec1.z - vec2.z * vec1.x;
        z = vec2.y * vec1.x - vec2.x * vec1.y;
        return this;
    }

    public float dot(Vector3f dot) {
        return x * dot.x + y * dot.y + z * dot.z;
    }

    public float length() {
        return (float) sqrt(dot(this));
    }

    public Vector3f normalize() {
        return multiply(1f / length());
    }

    public Vector3f invert() {
        return multiply(-1);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        else if (obj instanceof Vector3f) {
            Vector3f vec = (Vector3f) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else return false;
    }

    @Override
    public String toString() {
        return "Vector3f[" + x + ", " + y + ", " + z + "]";
    }
}