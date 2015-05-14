package com.mtronicsdev.polygame.graphics;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.glGenBuffers;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public abstract class VertexBufferObject {

    protected int id;

    protected VertexBufferObject() {
        id = glGenBuffers();
    }

    protected FloatBuffer storeArrayInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);

        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    protected IntBuffer storeArrayInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);

        buffer.put(data);
        buffer.flip();

        return buffer;
    }

    public abstract int getType();

    public int getId() {
        return id;
    }
}
