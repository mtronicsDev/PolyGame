package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.Material;
import com.mtronicsdev.polygame.graphics.RawModel;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Model extends Module {

    private com.mtronicsdev.polygame.graphics.Model model;

    public Model(com.mtronicsdev.polygame.graphics.Model model) {
        this.model = model;
    }

    public Material getMaterial() {
        return model.getMaterial();
    }

    public RawModel getRawModel() {
        return model.getRawModel();
    }

}
