package com.mtronicsdev.polygame.graphics;

import static org.lwjgl.opengl.GL15.glDeleteBuffers;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class QuadModel extends VertexArrayObject {

    private static final float[] VERTICES = {
            -1, 1,
            -1, -1,
            1, 1,
            1, -1
    };

    private static final int size = VERTICES.length;

    private int vertexBufferId;

    public QuadModel() {
        bind();
        vertexBufferId = createAttributeList(0, 2, VERTICES);
        unbind();
    }

    @Override
    public void cleanUp() {
        super.cleanUp();

        glDeleteBuffers(vertexBufferId);
    }

    public int getSize() {
        return size;
    }
}
