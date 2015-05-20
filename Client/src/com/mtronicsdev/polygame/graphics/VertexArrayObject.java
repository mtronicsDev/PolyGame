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
abstract class VertexArrayObject extends GLObject {
    private int id;

    protected VertexArrayObject() {
        id = glGenVertexArrays();
    }

    public final void bind() {
        glBindVertexArray(id);
    }

    public final int getId() {
        return id;
    }

    public final void unbind() {
        glBindVertexArray(0);
    }

    protected final int createAttributeList(int position, int elementSize, float[] data) {
        //Creating the vertex buffer
        int id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, storeArrayInFloatBuffer(data), GL_STATIC_DRAW);

        //Setting up the vertex buffer
        glVertexAttribPointer(position, elementSize, GL11.GL_FLOAT, false, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);

        return id;
    }

    protected final FloatBuffer storeArrayInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);

        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    protected final IntBuffer storeArrayInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);

        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    @Override
    public void cleanUp() {
        glDeleteVertexArrays(id);
    }
}

