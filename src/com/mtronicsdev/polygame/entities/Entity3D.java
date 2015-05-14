package com.mtronicsdev.polygame.entities;

import com.mtronicsdev.polygame.math.Vector3f;

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

    public  void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }
}
