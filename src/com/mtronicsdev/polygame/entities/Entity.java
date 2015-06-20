package com.mtronicsdev.polygame.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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

    private BareEntity parent = null;
    private List<Module> modules;
    private int id = UUID.randomUUID().hashCode();

    public Entity(Module... modules) {
        super();

        this.modules = new ArrayList<>(Arrays.asList(modules));
        for (Module module : modules) module.setParent(this);

        root.addChild(this);
    }

    public static BareEntity getRoot() {
        return root;
    }

    @Override
    public final void update() {
        modules.forEach(Module::update);
        children.forEach(Entity::update);
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

    public final BareEntity getParent() {
        return parent;
    }

    public final void setParent(BareEntity parent) {
        if (parent == null || parent.equals(this.parent)) return; //No need to change anything
        this.parent = parent;
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
