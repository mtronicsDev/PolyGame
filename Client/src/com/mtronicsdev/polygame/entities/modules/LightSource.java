package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.RenderEngine;
import com.mtronicsdev.polygame.util.math.Vector3f;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class LightSource extends Module {

    private Vector3f color;

    public LightSource(Vector3f color) {
        this.color = color;

        RenderEngine.registerLightSource(this);
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
        RenderEngine.unRegisterLightSource(this);
    }
}
