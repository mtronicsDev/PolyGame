package com.mtronicsdev.polygame.graphics;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class SharedModel {

    private RawModel rawModel;
    private Material material;

    public SharedModel(RawModel rawModel, Material material) {
        this.rawModel = rawModel;
        this.material = material;

        RenderEngine.registerSharedModel(this);
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public final void finalize() throws Throwable {
        super.finalize();
        RenderEngine.unRegisterSharedModel(this);
    }
}
