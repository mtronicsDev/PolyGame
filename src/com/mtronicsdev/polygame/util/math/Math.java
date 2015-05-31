package com.mtronicsdev.polygame.util.math;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public class Math {

    public float clamp(float in, float min, float max) {
        if (in < min) return min;
        if (in > max) return max;
        else return in;
    }

}
