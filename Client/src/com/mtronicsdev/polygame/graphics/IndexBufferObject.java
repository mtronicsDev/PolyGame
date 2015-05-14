package com.mtronicsdev.polygame.graphics;

import static org.lwjgl.opengl.GL15.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class IndexBufferObject extends VertexBufferObject {

    public IndexBufferObject(int... indices) {
        super();

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, id);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, storeArrayInIntBuffer(indices), GL_STATIC_DRAW);
    }

    @Override
    public int getType() {
        return GL_ELEMENT_ARRAY_BUFFER;
    }
}
