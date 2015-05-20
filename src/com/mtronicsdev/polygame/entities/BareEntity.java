package com.mtronicsdev.polygame.entities;

import java.util.HashSet;
import java.util.Set;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class BareEntity {

    protected Set<Entity> children;

    BareEntity() {
        children = new HashSet<>();
    }

    final void addChild(Entity child) {
        children.add(child);
        child.setParent(this);
    }

    final void removeChild(Entity child) {
        children.remove(child);
        child.setParent(null);
    }

    public void update() {
        children.forEach(Entity::update);
    }
}
