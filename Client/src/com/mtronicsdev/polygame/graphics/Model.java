package com.mtronicsdev.polygame.graphics;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Model {

    private RawModel rawModel;
    private Material material;

    public Model(RawModel rawModel, Material material) {
        this.rawModel = rawModel;
        this.material = material;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public Material getMaterial() {
        return material;
    }
}
