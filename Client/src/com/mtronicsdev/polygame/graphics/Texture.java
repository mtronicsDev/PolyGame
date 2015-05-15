package com.mtronicsdev.polygame.graphics;

import static org.lwjgl.opengl.GL11.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Texture extends GLObject {

    private int id;

    public Texture(org.newdawn.slick.opengl.Texture ref) {
        id = ref.getTextureID();
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return id;
    }

    @Override
    public void cleanUp() {
        glDeleteTextures(id);
    }
}
