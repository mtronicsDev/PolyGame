package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.RawModel;
import com.mtronicsdev.polygame.graphics.Texture;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Model extends Module {

    private Texture texture;
    private RawModel rawModel;

    public Model(Texture texture, RawModel rawModel) {
        this.texture = texture;
        this.rawModel = rawModel;
    }

    public Texture getTexture() {
        return texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

}
