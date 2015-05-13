package com.mtronicsdev.polygame.graphics;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class VertexBufferObject {

    private int id;

    public VertexBufferObject(float... data) {
        id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, storeArrayInBuffer(data), GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    private FloatBuffer storeArrayInBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);

        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    public int getId() {
        return id;
    }
}
