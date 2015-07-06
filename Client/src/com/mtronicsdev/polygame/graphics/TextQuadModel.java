package com.mtronicsdev.polygame.graphics;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class TextQuadModel extends VertexArrayObject {

    private final int size;

    private int vertexBufferId, uvBufferId;

    public TextQuadModel(float[] vertices, float[] uvs) {
        bind();
        vertexBufferId = createAttributeList(0, 2, vertices);
        uvBufferId = createAttributeList(1, 2, uvs);
        size = vertices.length;
        unbind();
    }

    @Override
    public void cleanUp() {
        super.cleanUp();

        glDeleteBuffers(vertexBufferId);
        glDeleteBuffers(uvBufferId);
    }

    public int getSize() {
        return size;
    }
}
