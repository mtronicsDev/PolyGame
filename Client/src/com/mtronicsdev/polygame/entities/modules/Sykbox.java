package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.CubeMapTexture;
import com.mtronicsdev.polygame.graphics.RenderEngine;

import java.awt.image.BufferedImage;

import static com.mtronicsdev.polygame.io.Textures.readData;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Sykbox extends Module {

    private CubeMapTexture texture;

    public Sykbox(BufferedImage front, BufferedImage back, BufferedImage left, BufferedImage right,
                  BufferedImage bottom, BufferedImage top) {
        texture = new CubeMapTexture(front.getWidth(), front.getHeight(), readData(front), readData(back),
                readData(left), readData(right), readData(bottom), readData(top));

        RenderEngine.setSkybox(this);
    }

    public CubeMapTexture getTexture() {
        return texture;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        RenderEngine.setSkybox(null);
    }
}
