package com.mtronicsdev.polygame.entities;

import com.mtronicsdev.polygame.util.math.Vector2f;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Entity2D extends Entity {

    private Vector2f position;
    private float rotation;

    public Entity2D(Vector2f position, float rotation, Module... modules) {
        super(modules);
        this.position = position;
        this.rotation = rotation;
    }

    public Entity2D(Vector2f position, Module... modules) {
        this(position, 0, modules);
    }

    public Entity2D(Module... modules) {
        this(new Vector2f(), modules);
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
