package com.mtronicsdev.polygame.entities.modules;

import com.mtronicsdev.polygame.entities.Module;
import com.mtronicsdev.polygame.graphics.*;
import com.mtronicsdev.polygame.io.Preferences;
import com.mtronicsdev.polygame.util.math.Vector3f;

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

    protected Texture texture0, texture1, texture2, texture3;
    private SharedModel sharedModel;
    private Texture blendMap;

    private float resolution = RESOLUTION;
    private int width = WIDTH;
    private int height = HEIGHT;
    private int maxHeight = MAX_HEIGHT;
    private float[] heightmap;

    public Terrain(Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3) {
        this(WIDTH, HEIGHT, RESOLUTION, MAX_HEIGHT, blendMap, texture0, texture1, texture2, texture3);
    }

    public Terrain(int width, int height, float resolution, int maxHeight,
                   Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3) {
        this(width, height, resolution, maxHeight, blendMap, texture0, texture1, texture2, texture3,
                new float[(int) (width * height * resolution * resolution)]);
    }

    public Terrain(Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3,
                   float[] heightmap) {
        this(WIDTH, HEIGHT, RESOLUTION, MAX_HEIGHT, blendMap, texture0, texture1, texture2, texture3, heightmap);
    }

    public Terrain(int width, int height, float resolution, int maxHeight,
                   Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3,
                   float[] heightmap) {

        this.width = width;
        this.height = height;
        this.resolution = resolution;
        this.maxHeight = maxHeight;
        this.heightmap = heightmap;

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

        this.blendMap = blendMap;

        RenderEngine.registerTerrain(this);
    }

    public Terrain(Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3,
                   BufferedImage heightmap) {
        this(WIDTH, HEIGHT, RESOLUTION, MAX_HEIGHT, blendMap, texture0, texture1, texture2, texture3, heightmap);
    }

    public Terrain(int width, int height, float resolution, int maxHeight,
                   Texture blendMap, Texture texture0, Texture texture1, Texture texture2, Texture texture3,
                   BufferedImage heightmap) {
        this(width, height, resolution, maxHeight, blendMap, texture0, texture1, texture2, texture3,
                readHeightmap(heightmap));
    }

    private static float[] readHeightmap(BufferedImage heightmap) {
        float[] heights = new float[heightmap.getWidth() * heightmap.getHeight()];

        for (int x = 0; x < heightmap.getWidth(); x++) {
            for (int y = 0; y < heightmap.getHeight(); y++) {
                float value = heightmap.getRGB(x, heightmap.getHeight() - y - 1);
                value /= 256 * 256 * 256 / 2; //Normalize to [0; 2]
                value -= 1; //Shift to [-1; 1]
                heights[y * heightmap.getWidth() + x] = value;
            }
        }

        return heights;
    }

    private RawModel generateMesh(float[] heightmap) {
        int xVertexCount = (int) (width * resolution);
        int yVertexCount = (int) (height * resolution);

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
                vertices[vertexPointer * 3] = (float) x / ((float) xVertexCount - 1) * width;
                vertices[vertexPointer * 3 + 1] = heightmap == null ? 0 : heightmap[y * xVertexCount + x] * maxHeight;
                vertices[vertexPointer * 3 + 2] = -(float) y / ((float) yVertexCount - 1) * height;

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
                int topLeft = (y * width) + x;
                int topRight = topLeft + 1;
                int bottomLeft = ((y + 1) * width) + x;
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

    public void setHeight(int x, int y, float height) {
        setHeight(x, y, height, heightmap, (int) (width * resolution), (int) (this.height * resolution));

        try {
            //noinspection FinalizeCalledExplicitly
            RenderEngine.unRegisterSharedModel(sharedModel);
            sharedModel.getRawModel().cleanUp();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        sharedModel = new SharedModel(generateMesh(heightmap),
                new Material(blendMap,
                        new Vector3f(1, 1, 1),
                        new Vector3f(1, 1, 1),
                        new Vector3f(1, 1, 1),
                        new Vector3f(.05f, .05f, .05f),
                        new Vector3f(), 97));
    }

    public void setHeights(int x, int y, int width, int height, float[][] heights) {
        setHeights(x, y, width, height, heights, (int) (this.width * resolution), (int) (this.height * resolution));

        try {
            //noinspection FinalizeCalledExplicitly
            RenderEngine.unRegisterSharedModel(sharedModel);
            sharedModel.getRawModel().cleanUp();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        sharedModel = new SharedModel(generateMesh(heightmap),
                new Material(blendMap,
                        new Vector3f(1, 1, 1),
                        new Vector3f(1, 1, 1),
                        new Vector3f(1, 1, 1),
                        new Vector3f(.05f, .05f, .05f),
                        new Vector3f(), 97));
    }

    private void setHeights(int x, int y, int width, int height, float[][] heights, int xVertexCount, int yVertexCount) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x >= xVertexCount) x = xVertexCount - 1;
        if (y >= yVertexCount) y = yVertexCount - 1;
        if (x + width > xVertexCount) width = xVertexCount - x;
        if (y + height > yVertexCount) height = yVertexCount - y;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                heightmap[(j + y) * xVertexCount + (i + x)] = heights[i][j];
            }
        }
    }

    private void setHeight(int x, int y, float height, float[] heightmap, int xVertexCount, int yVertexCount) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x >= xVertexCount) x = xVertexCount - 1;
        if (y >= yVertexCount) y = yVertexCount - 1;

        heightmap[y * xVertexCount + x] = height;
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

    public float getResolution() {
        return resolution;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    private float getHeight(int x, int y, float[] heightmap, int xVertexCount, int yVertexCount) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x >= xVertexCount) x = xVertexCount - 1;
        if (y >= yVertexCount) y = yVertexCount - 1;

        return heightmap[y * xVertexCount + x];
    }

    public float getHeight(int x, int y) {
        return getHeight(x, y, heightmap, (int) (width * resolution), (int) (height * resolution));
    }

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
        RenderEngine.unRegisterTerrain(this);
    }

}
