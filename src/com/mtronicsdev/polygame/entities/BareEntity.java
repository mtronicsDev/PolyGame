package com.mtronicsdev.polygame.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
class BareEntity {

    protected Set<Entity> children;

    BareEntity() {
        children = new HashSet<>();
    }

    void addChild(Entity child) {
        child.setParent(this);
    }

    void removeChild(Entity child) {
        children.remove(child);
    }
}
