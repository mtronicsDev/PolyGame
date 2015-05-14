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
public class RawModel implements GLObject {

    private int id;
    private int indexBufferId, vertexBufferId;

    public RawModel(int[] indices, float[] vertices) {
        id = glGenVertexArrays();

        bind();

        //Creating the index buffer
        indexBufferId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, storeArrayInIntBuffer(indices), GL_STATIC_DRAW);

        //Creating the vertex buffer
        vertexBufferId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vertexBufferId);
        glBufferData(GL_ARRAY_BUFFER, storeArrayInFloatBuffer(vertices), GL_STATIC_DRAW);

        //Setting up the vertex buffer
        glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

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
    protected void finalize() throws Throwable {
        super.finalize();
        cleanUp();
    }

    @Override
    public void cleanUp() {
        glDeleteBuffers(indexBufferId);
        glDeleteBuffers(vertexBufferId);

        glDeleteVertexArrays(id);
    }
}
