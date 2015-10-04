package com.mtronicsdev.polygame.entities;

import java.io.Serializable;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public abstract class Module implements Serializable {

    private transient Entity parent;

    public final Entity getParent() {
        return parent;
    }

    final void setParent(Entity parent) {
        this.parent = parent;
    }

    public void update() {

    }
}
