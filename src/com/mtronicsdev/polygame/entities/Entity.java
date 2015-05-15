package com.mtronicsdev.polygame.entities;

import java.util.*;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Entity extends BareEntity {

    private static final BareEntity root;

    static {
        root = new BareEntity();
    }

    private BareEntity parent;
    private Set<Module> modules;
    private int id = UUID.randomUUID().hashCode();

    public Entity(Module... modules) {
        super();
        this.modules = new HashSet<>(Arrays.asList(modules));
        parent = root;
    }

    public final void addModule(Module module) {
        module.setParent(this);
        modules.add(module);
    }

    public final <M extends Module> List<M> getModules(Class<M> type) {
        //noinspection unchecked
        return Arrays.asList((M[]) modules.stream().filter(type::isInstance).toArray(Module[]::new));
    }

    public final <M extends Module> M getModule(Class<M> type) {
        //noinspection unchecked
        return (M) modules.stream().filter(type::isInstance).findAny().orElse(null);
    }

    public final void removeModule(Module module) {
        modules.remove(module);
        module.setParent(null);
    }

    @Override
    public final void addChild(Entity child) {
        if (child.parent.equals(this)) children.add(child);
        else throw new IllegalArgumentException();
    }

    @Override
    public final void removeChild(Entity child) {
        child.parent = root;
        children.remove(child);
    }

    public final BareEntity getParent() {
        return parent;
    }

    public final void setParent(BareEntity parent) {
        if (this.parent.equals(parent)) return; //No need to change anything

        this.parent.removeChild(this); //Remove this child from the old parent's children

        if (parent == null) {
            this.parent = root;
            root.addChild(this);
        } else {
            this.parent = parent;
            parent.addChild(this);
        }
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof Entity && id == ((Entity) obj).id;
    }
}
