package com.mtronicsdev.polygame.gui;

import com.mtronicsdev.polygame.util.math.Vector2f;
import com.mtronicsdev.polygame.util.math.Vector4f;

/**
 * Created by Maxi on 21.06.2015.
 */
public class Dimension4f {

    /**
     * X1, Y1, X2 and Y2 dimension values.
     */
    private float x1, y1, x2, y2;

    /**
     * Relative value if true, absolute if false.
     */
    private boolean rX1, rY1, rX2, rY2;

    public Dimension4f(float x1, float y1, float x2, float y2, boolean rX1, boolean rY1, boolean rX2, boolean rY2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.rX1 = rX1;
        this.rY1 = rY1;
        this.rX2 = rX2;
        this.rY2 = rY2;
    }

    public float getX1() {
        return x1;
    }

    public float getY1() {
        return y1;
    }

    public float getX2() {
        return x2;
    }

    public float getY2() {
        return y2;
    }

    public boolean isrX1() {
        return rX1;
    }

    public boolean isrY1() {
        return rY1;
    }

    public boolean isrX2() {
        return rX2;
    }

    public boolean isrY2() {
        return rY2;
    }

    public Vector4f toVector4f(int pixelWidth, int pixelHeight) {
        float finalX1 = rX1 ? x1 : x1 / pixelWidth,
                finalY1 = rY1 ? y1 : y1 / pixelHeight,
                finalX2 = rX2 ? x2 : x2 / pixelWidth,
                finalY2 = rY2 ? y2 : y2 / pixelHeight;

        return new Vector4f(finalX1, finalY1, finalX2, finalY2);
    }

    public Vector2f toVector2f(int pixelWidth, int pixelHeight, boolean firstHalf) {
        float x = firstHalf ? x1 : x2;
        float y = firstHalf ? y1 : y2;

        boolean rX = firstHalf ? rX1 : rX2,
                rY = firstHalf ? rY1 : rY2;

        float finalX = rX ? x : x / pixelWidth,
                finalY = rY ? y : y / pixelHeight;

        return new Vector2f(finalX, finalY);
    }
}
