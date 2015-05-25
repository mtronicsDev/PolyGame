package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.*;
import com.mtronicsdev.polygame.io.Preferences;
import com.mtronicsdev.polygame.math.Vector3f;

import java.awt.image.BufferedImage;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class Terrain extends Module {

    private static final int WIDTH = Preferences.getPreference("terrain.width", int.class),
            HEIGHT = Preferences.getPreference("terrain.height", int.class);
    private static final float RESOLUTION = Preferences.getPreference("terrain.resolution", float.class);
    private static final int MAX_HEIGHT = Preferences.getPreference("terrain.maxHeight", int.class);
    private static final int MAX_VALUE = Preferences.getPreference("terrain.maxValue", int.class);

    protected Texture texture0, texture1, texture2, texture3;
    private SharedModel sharedModel;

    public Terrain(Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3) {
        this(blendMap, texture0, texture1, texture2, texture3, (float[]) null);
    }

    public Terrain(Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3,
                   float[] heightmap) {
        sharedModel = new SharedModel(generateMesh(heightmap),
                new Material(blendMap,
                        new Vector3f(1, 1, 1),
                        new Vector3f(1, 1, 1),
                        new Vector3f(1, 1, 1),
                        new Vector3f(.05f, .05f, .05f),
                        new Vector3f(), 97));

        this.texture0 = texture0;
        this.texture1 = texture1;
        this.texture2 = texture2;
        this.texture3 = texture3;

        RenderEngine.registerTerrain(this);
    }

    public Terrain(Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3,
                   BufferedImage heightmap) {
        this(blendMap, texture0, texture1, texture2, texture3, readHeightmap(heightmap));
    }

    private static float[] readHeightmap(BufferedImage heightmap) {
        float[] heights = new float[heightmap.getWidth() * heightmap.getHeight()];

        for (int x = 0; x < heightmap.getWidth(); x++) {
            for (int y = 0; y < heightmap.getHeight(); y++) {
                float value = heightmap.getRGB(x, heightmap.getHeight() - y - 1);
                value += MAX_VALUE / 2;
                value /= MAX_VALUE / 2;
                value *= MAX_HEIGHT;
                heights[y * heightmap.getWidth() + x] = value;
            }
        }

        return heights;
    }

    private RawModel generateMesh(float[] heightmap) {
        int xVertexCount = (int) (WIDTH * RESOLUTION);
        int yVertexCount = (int) (HEIGHT * RESOLUTION);

        int vertexCount = xVertexCount * yVertexCount;

        if (heightmap != null)
            if (heightmap.length != vertexCount)
                throw new IllegalArgumentException("The heightmap does not have the right number of values " +
                        "(Is: " + heightmap.length + "; Should: " + vertexCount + ").");

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
                vertices[vertexPointer * 3 + 1] = heightmap == null ? 0 : heightmap[y * xVertexCount + x];
                vertices[vertexPointer * 3 + 2] = -(float) y / ((float) yVertexCount - 1) * HEIGHT;

                //Calculate normal
                float heightLeft = getHeight(x - 1, y, heightmap, xVertexCount, yVertexCount);
                float heightRight = getHeight(x + 1, y, heightmap, xVertexCount, yVertexCount);
                float heightTop = getHeight(x, y - 1, heightmap, xVertexCount, yVertexCount);
                float heightBottom = getHeight(x, y + 1, heightmap, xVertexCount, yVertexCount);

                Vector3f normal = new Vector3f(heightLeft - heightRight, 2, heightTop - heightBottom);
                normal.normalize();

                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

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

    private float getHeight(int x, int y, float[] heightmap, int xVertexCount, int yVertexCount) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x >= xVertexCount) x = xVertexCount - 1;
        if (y >= yVertexCount) y = yVertexCount - 1;

        return heightmap[y * xVertexCount + x];
    }

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
        RenderEngine.unRegisterTerrain(this);
    }

}
