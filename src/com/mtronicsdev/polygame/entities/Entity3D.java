package com.mtronicsdev.polygame.entities;

import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;
import com.mtronicsdev.polygame.util.VectorMath;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Entity3D extends Entity {

    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public Entity3D(Vector3f position, Vector3f rotation, Vector3f scale, Module... modules) {
        super(modules);

        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Entity3D(Vector3f position, Vector3f rotation, Module... modules) {
        this(position, rotation, new Vector3f(1, 1, 1), modules);
    }

    public Entity3D(Vector3f position, Module... modules) {
        this(position, new Vector3f(), modules);
    }

    public Entity3D(Module... modules) {
        this(new Vector3f(), modules);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f delta) {
        position.add(delta);
    }

    public void addPosition(float dX, float dY, float dZ) {
        position.x += dX;
        position.y += dY;
        position.z += dZ;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public void addRotation(Vector3f delta) {
        rotation.add(delta);
    }

    public void addRotation(float dX, float dY, float dZ) {
        rotation.x += dX;
        rotation.y += dY;
        rotation.z += dZ;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public Matrix4f getTransformationMatrix() {
        return VectorMath.createTransformationMatrix(position, rotation, scale);
    }
}
