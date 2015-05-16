package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.RenderEngine;
import com.mtronicsdev.polygame.graphics.SharedModel;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Model extends Module {

    private SharedModel sharedModel;

    public Model(SharedModel sharedModel) {
        this.sharedModel = sharedModel;

        RenderEngine.registerModel(this);
    }

    public SharedModel getSharedModel() {
        return sharedModel;
    }

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
        RenderEngine.unRegisterModel(this);
    }
}
