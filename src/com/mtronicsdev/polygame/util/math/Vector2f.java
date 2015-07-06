package com.mtronicsdev.polygame.util.math;

import static java.lang.Math.sqrt;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Vector2f {

    public float x, y;

    public Vector2f() {
        x = y = 0;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f clone) {
        x = clone.x;
        y = clone.y;
    }

    public Vector2f add(Vector2f vector) {
        x += vector.x;
        y += vector.y;
        return this;
    }

    public Vector2f subtract(Vector2f vector) {
        x -= vector.x;
        y -= vector.y;
        return this;
    }

    public Vector2f multiply(float scalar) {
        x *= scalar;
        y *= scalar;
        return this;
    }

    public float dot(Vector2f vector) {
        return x * vector.x + y * vector.y;
    }

    public float length() {
        return (float) sqrt(dot(this));
    }

    public Vector2f normalize() {
        return multiply(1 / length());
    }

    public Vector2f invert() {
        return multiply(-1);
    }

    public boolean equals(Object object) {
        if (this == object) return true;
        else if (object instanceof Vector2f) {
            Vector2f vector = (Vector2f) object;
            return x == vector.x && y == vector.y;
        } else return false;
    }

    public String toString() {
        return "Vector2f[" + x + ", " + y + "]";
    }
}