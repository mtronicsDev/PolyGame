package com.mtronicsdev.polygame.gui;

import com.mtronicsdev.polygame.util.math.Vector2f;

/**
 * Created by Maxi on 21.06.2015.
 */
public class Dimension2f {

    /**
     * X and Y dimension values.
     */
    private float x, y;

    /**
     * Relative value if true, absolute if false.
     */
    private boolean rX, rY;

    public Dimension2f(float x, float y, boolean rX, boolean rY) {
        this.x = x;
        this.y = y;
        this.rX = rX;
        this.rY = rY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isXRelative() {
        return rX;
    }

    public boolean isYRelative() {
        return rY;
    }

    public Vector2f toVector2f(int pixelWidth, int pixelHeight) {
        float finalX = rX ? x : x / pixelWidth,
                finalY = rY ? y : y / pixelHeight;

        return new Vector2f(finalX, finalY);
    }
}
