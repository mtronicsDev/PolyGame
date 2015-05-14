package com.mtronicsdev.polygame.graphics;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class DefaultVertexBufferObject extends VertexBufferObject {

    public DefaultVertexBufferObject(float... data) {
        super();

        glBindBuffer(GL_ARRAY_BUFFER, id);
        glBufferData(GL_ARRAY_BUFFER, storeArrayInFloatBuffer(data), GL_STATIC_DRAW);
    }

    @Override
    public int getType() {
        return GL_ARRAY_BUFFER;
    }
}
