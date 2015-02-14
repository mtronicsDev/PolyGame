package com.mtronicsdev.polygame.entities;

import com.mtronicsdev.polygame.math.Vector2f;

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
        super(modules);
        this.position = position;
    }

    public Entity2D(Module... modules) {
        super(modules);
        position = new Vector2f();
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
