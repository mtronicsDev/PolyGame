package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;

import java.awt.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class LightSource extends Module {

    private Color color;

    public LightSource(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
