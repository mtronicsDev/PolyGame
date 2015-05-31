package com.mtronicsdev.polygame.util.math;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public final class Math {

    private Math() {

    }

    public static float clamp(float in, float min, float max) {
        if (in < min) return min;
        if (in > max) return max;
        else return in;
    }

}
