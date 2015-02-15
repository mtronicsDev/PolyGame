package com.mtronicsdev.polygame.entities;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public abstract class Module {

    private Entity parent;

    public final Entity getParent() {
        return parent;
    }

    final void setParent(Entity parent) {
        if (parent != null) parent.removeModule(this);
        this.parent = parent;
    }
}
