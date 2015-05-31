package com.mtronicsdev.polygame.util.math;

import static java.lang.Math.sqrt;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Vector3f {

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

    public void multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        z *= scalar;
    }

    public void add(Vector3f add) {
        x += add.x;
        y += add.y;
        z += add.z;
    }

    public void subtract(Vector3f sub) {
        x -= sub.x;
        y -= sub.y;
        z -= sub.z;
    }

    public void cross(Vector3f vec1, Vector3f vec2) {
        x = vec2.z * vec1.y - vec2.y * vec1.z;
        y = vec2.x * vec1.z - vec2.z * vec1.x;
        z = vec2.y * vec1.x - vec2.x * vec1.y;
    }

    public float dot(Vector3f dot) {
        return x * dot.x + y * dot.y + z * dot.z;
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
        else if (obj instanceof Vector3f) {
            Vector3f vec = (Vector3f) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else return false;
    }

    @Override
    public String toString() {
        return "Vector3f[" + x + ", " + y + ", " + z + "]";
    }

    public void invert() {
        multiply(-1);
    }
}