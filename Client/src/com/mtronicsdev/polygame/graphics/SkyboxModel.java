package com.mtronicsdev.polygame.graphics;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class SkyboxModel extends VertexArrayObject {

    private static final float[] VERTICES = {
            1, 1, 1,
            1, 1, -1,
            1, -1, 1,
            1, -1, -1,
            -1, 1, 1,
            -1, 1, -1,
            -1, -1, 1,
            -1, -1, -1
    };

    private static final int[] INDICES = {
            5, 7, 3, 3, 1, 5,
            6, 7, 5, 5, 4, 6,
            3, 2, 0, 0, 1, 3,
            6, 4, 0, 0, 2, 6,
            5, 1, 0, 0, 4, 5,
            7, 6, 3, 3, 6, 2
    };

    private static final int size = INDICES.length;

    private int indexBufferId, vertexBufferId;

    public SkyboxModel() {
        bind();

        //Creating the index buffer
        indexBufferId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, storeArrayInIntBuffer(INDICES), GL_STATIC_DRAW);

        vertexBufferId = createAttributeList(0, 3, VERTICES);

        unbind();
    }

    @Override
    public void cleanUp() {
        super.cleanUp();

        glDeleteBuffers(indexBufferId);
        glDeleteBuffers(vertexBufferId);
    }

    public int getSize() {
        return size;
    }
}
