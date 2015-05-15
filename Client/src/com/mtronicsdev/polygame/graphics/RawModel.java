package com.mtronicsdev.polygame.graphics;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class RawModel extends GLObject {

    private int id;
    private int indexBufferId, vertexBufferId, uvBufferId;

    public RawModel(int[] indices, float[] vertices, float[] uvs) {
        id = glGenVertexArrays();

        bind();

        //Creating the index buffer
        indexBufferId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, storeArrayInIntBuffer(indices), GL_STATIC_DRAW);

        vertexBufferId = createAttributeList(0, 3, vertices);
        uvBufferId = createAttributeList(1, 2, uvs);

        unbind();
    }

    public void bind() {
        glBindVertexArray(id);
    }

    public int getId() {
        return id;
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    private int createAttributeList(int position, int elementSize, float[] data) {
        //Creating the vertex buffer
        int id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, storeArrayInFloatBuffer(data), GL_STATIC_DRAW);

        //Setting up the vertex buffer
        glVertexAttribPointer(position, elementSize, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return id;
    }

    private FloatBuffer storeArrayInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);

        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    private IntBuffer storeArrayInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);

        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    @Override
    public void cleanUp() {
        glDeleteBuffers(indexBufferId);
        glDeleteBuffers(vertexBufferId);
        glDeleteBuffers(uvBufferId);

        glDeleteVertexArrays(id);
    }
}
