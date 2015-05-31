package com.mtronicsdev.polygame.graphics;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public class PlaneModel extends VertexArrayObject {

    private static final float[] VERTICES = {
            -1, -1,
            -1, 1,
            1, -1,
            1, 1
    };

    private static final int[] INDICES = {
            0, 1, 2, 2, 1, 3
    };

    private static final int SIZE = INDICES.length;

    private int indexBufferId, vertexBufferId;

    public PlaneModel() {
        bind();

        indexBufferId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, storeArrayInIntBuffer(INDICES), GL_STATIC_DRAW);

        vertexBufferId = createAttributeList(0, 2, VERTICES);

        unbind();
    }

    public static int getSize() {
        return SIZE;
    }

    @Override
    public void cleanUp() {
        super.cleanUp();

        glDeleteBuffers(indexBufferId);
        glDeleteBuffers(vertexBufferId);
    }
}
