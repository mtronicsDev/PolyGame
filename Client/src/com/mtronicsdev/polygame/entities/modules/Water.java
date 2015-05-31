package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.RenderEngine;
import com.mtronicsdev.polygame.util.math.Vector3f;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public class Water extends Module {

    private static final Vector3f size = new Vector3f(40, 0, 40);

    public Water() {
        RenderEngine.registerWater(this);
    }

    public Vector3f getSize() {
        return size;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        RenderEngine.unRegisterWater(this);
    }
}
