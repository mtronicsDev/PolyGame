package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.RenderEngine;
import com.mtronicsdev.polygame.util.math.Matrix4f;
import com.mtronicsdev.polygame.util.math.Vector3f;
import com.mtronicsdev.polygame.util.math.VectorMath;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Camera extends Module {

    public Camera() {
        RenderEngine.setCamera(this);
    }

    public Matrix4f getViewMatrix() {
        if (getParent() instanceof Entity3D) {
            Entity3D p = (Entity3D) getParent();

            return VectorMath.createViewMatrix(p.getPosition(), p.getRotation());
        } else return VectorMath.createViewMatrix(new Vector3f(), new Vector3f());
    }

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
        //RenderEngine.setCamera(null);
    }
}
