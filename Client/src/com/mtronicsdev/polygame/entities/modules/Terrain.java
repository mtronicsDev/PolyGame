package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.*;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Terrain extends Module {

    private static final int WIDTH = 128, HEIGHT = 128;
    private static final float RESOLUTION = 1;
    protected Texture texture0, texture1, texture2, texture3;
    private SharedModel sharedModel;

    public Terrain(Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3) {
        sharedModel = new SharedModel(generateMesh(), new Material(blendMap));

        this.texture0 = texture0;
        this.texture1 = texture1;
        this.texture2 = texture2;
        this.texture3 = texture3;

        RenderEngine.registerTerrain(this);
    }

    private RawModel generateMesh() {
        int xVertexCount = (int) (WIDTH * RESOLUTION);
        int yVertexCount = (int) (HEIGHT * RESOLUTION);

        int vertexCount = xVertexCount * yVertexCount;

        float[] vertices = new float[vertexCount * 3];
        float[] uvs = new float[vertexCount * 2];
        float[] normals = new float[vertexCount * 3];

        // (xVertexCount - 1) because the rightmost row doesn't have faces to it's right
        int[] indices = new int[6 * (xVertexCount - 1) * yVertexCount];

        int vertexPointer = 0;

        //Generating the vertices / normals / uvs
        for (int x = 0; x < xVertexCount; x++) {
            for (int y = 0; y < yVertexCount; y++) {
                vertices[vertexPointer * 3] = (float) x / ((float) xVertexCount - 1) * WIDTH;
                vertices[vertexPointer * 3 + 1] = 0;
                vertices[vertexPointer * 3 + 2] = -(float) y / ((float) yVertexCount - 1) * HEIGHT;

                //TODO: Generate normals based on height
                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;

                uvs[vertexPointer * 2] = (float) x / ((float) xVertexCount - 1);
                uvs[vertexPointer * 2 + 1] = (float) y / ((float) yVertexCount - 1);

                vertexPointer++;
            }
        }

        int pointer = 0;

        //Generating the faces (indices)
        for (int x = 0; x < xVertexCount - 1; x++) {
            for (int y = 0; y < yVertexCount - 1; y++) {
                int topLeft = (y * WIDTH) + x;
                int topRight = topLeft + 1;
                int bottomLeft = ((y + 1) * WIDTH) + x;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return new RawModel(indices, vertices, uvs, normals);
    }

    public SharedModel getSharedModel() {
        return sharedModel;
    }

    public Texture getTexture0() {
        return texture0;
    }

    public Texture getTexture1() {
        return texture1;
    }

    public Texture getTexture2() {
        return texture2;
    }

    public Texture getTexture3() {
        return texture3;
    }

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
        RenderEngine.unRegisterTerrain(this);
    }

}
