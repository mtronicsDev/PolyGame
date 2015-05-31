package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.Camera;
import com.mtronicsdev.polygame.util.math.Matrix4f;
import com.mtronicsdev.polygame.util.math.Vector3f;
import com.mtronicsdev.polygame.util.math.VectorMath;

/**
 * Used for rendering with offset camera position without moving any entity.
 *
 * @author mtronicsDev
 * @version 1.0
 */
final class AbstractCamera extends Camera {

    private Vector3f position, rotation;

    public AbstractCamera(Camera camera) {
        Entity3D parent = (Entity3D) camera.getParent();

        position = parent.getPosition();
        rotation = parent.getRotation();
    }

    @Override
    public Matrix4f getViewMatrix() {
        return VectorMath.createViewMatrix(position, rotation);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}
