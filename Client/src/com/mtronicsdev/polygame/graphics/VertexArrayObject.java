package com.mtronicsdev.polygame.graphics;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class VertexArrayObject implements GLObject {

    private int id;
    private IndexBufferObject indices;
    private VertexBufferObject vertices;

    public VertexArrayObject(int[] indices, float[] vertices) {
        id = glGenVertexArrays();
        bind();

        this.indices = new IndexBufferObject(indices);
        this.vertices = new DefaultVertexBufferObject(vertices);

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

    @Override
    public void unbind() {
        glBindVertexArray(0);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        unbind();
    }
}
