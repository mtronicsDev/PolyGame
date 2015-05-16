package com.mtronicsdev.polygame.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Texture extends GLObject {

    private int id;

    public Texture(org.newdawn.slick.opengl.Texture ref) {
        id = ref.getTextureID();

        glGenerateMipmap(GL_TEXTURE_2D);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
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
