package com.mtronicsdev.polygame.graphics;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Texture extends GLObject {

    private int id;

    public Texture(int width, int height, ByteBuffer textureData) {
        id = glGenTextures();
        bind();

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, textureData);

        glGenerateMipmap(GL_TEXTURE_2D);
        unbind();
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
