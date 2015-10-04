package com.mtronicsdev.polygame.entities;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class BareEntity implements Serializable {

    protected transient Set<Entity> children;

    BareEntity() {
        children = new HashSet<>();
    }

    protected final void addChild(Entity child) {
        children.add(child);
    }

    protected final void removeChild(Entity child) {
        children.remove(child);
    }

    public void update() {
        children.forEach(Entity::update);
    }

    public Set<Entity> getChildren() {
        return Collections.unmodifiableSet(children);
    }
}
