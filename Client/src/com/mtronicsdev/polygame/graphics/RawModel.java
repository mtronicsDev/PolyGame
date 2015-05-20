package com.mtronicsdev.polygame.graphics;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class RawModel extends VertexArrayObject {

    private int indexBufferId, vertexBufferId, uvBufferId, normalBufferId;
    private int size;

    public RawModel(int[] indices, float[] vertices, float[] uvs, float[] normals) {
        bind();

        //Creating the index buffer
        indexBufferId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, storeArrayInIntBuffer(indices), GL_STATIC_DRAW);

        vertexBufferId = createAttributeList(0, 3, vertices);
        uvBufferId = createAttributeList(1, 2, uvs);
        normalBufferId = createAttributeList(2, 3, normals);

        unbind();

        size = indices.length;
    }

    @Override
    public void cleanUp() {
        super.cleanUp();

        glDeleteBuffers(indexBufferId);
        glDeleteBuffers(vertexBufferId);
        glDeleteBuffers(uvBufferId);
        glDeleteBuffers(normalBufferId);
    }

    public int getSize() {
        return size;
    }
}
