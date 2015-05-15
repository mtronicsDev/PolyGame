package com.mtronicsdev.polygame.io;

import com.mtronicsdev.polygame.graphics.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Textures {

    static {
        Resources.registerResourceHandler(file -> {
            try {
                return TextureLoader.getTexture("PNG", new BufferedInputStream(new FileInputStream(file)));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, org.newdawn.slick.opengl.Texture.class);
    }

    public static Texture loadTexture(String filename) {
        org.newdawn.slick.opengl.Texture texture =
                Resources.getResource(filename, org.newdawn.slick.opengl.Texture.class);

        return new Texture(texture);
    }

}
