package com.mtronicsdev.polygame.io;

import com.mtronicsdev.polygame.graphics.Material;
import com.mtronicsdev.polygame.graphics.RawModel;
import com.mtronicsdev.polygame.graphics.SharedModel;
import com.mtronicsdev.polygame.graphics.Texture;
import com.mtronicsdev.polygame.math.Vector2f;
import com.mtronicsdev.polygame.math.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public final class Models {

    private static Map<String, RawModel> loadedModels;
    private static Map<String, Material> loadedMaterials;

    static {
        loadedModels = new HashMap<>();
        loadedMaterials = new HashMap<>();

        Resources.registerResourceHandler(file -> {
            String filePath = file.getPath();
            filePath = filePath.substring(0, filePath.length() - file.getName().length());

            List<Vector3f> vertices = new LinkedList<>();
            List<Vector2f> uvs = new LinkedList<>();
            List<Vector3f> normals = new LinkedList<>();
            List<Integer> indices = new LinkedList<>();

            float[] vertexArray = null;
            float[] normalArray = null;
            float[] uvArray = null;
            int[] indexArray = null;

            Material material = null;

            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("mtllib")) loadMaterialLibrary(filePath, line.split(" ")[1]);
                    else if (line.startsWith("usemtl")) material = loadedMaterials.get(line.split(" ")[1]);
                    else if (line.startsWith("v ")) vertices.add(new Vector3f(
                            Float.parseFloat(line.split(" ")[1]),
                            Float.parseFloat(line.split(" ")[2]),
                            Float.parseFloat(line.split(" ")[3])));
                    else if (line.startsWith("vn ")) normals.add(new Vector3f(
                            Float.parseFloat(line.split(" ")[1]),
                            Float.parseFloat(line.split(" ")[2]),
                            Float.parseFloat(line.split(" ")[3])));
                    else if (line.startsWith("vt ")) uvs.add(new Vector2f(
                            Float.parseFloat(line.split(" ")[1]),
                            Float.parseFloat(line.split(" ")[2])));
                    else if (line.startsWith("f ")) break;
                }

                int size = vertices.size();

                normalArray = new float[size * 3];
                uvArray = new float[size * 2];

                while (line != null) {
                    if (line.startsWith("f")) {
                        String[] vertexIndices = line.split(" ");

                        String[] vertex1 = vertexIndices[1].split("/");
                        String[] vertex2 = vertexIndices[2].split("/");
                        String[] vertex3 = vertexIndices[3].split("/");

                        arrangeIndices(vertex1, indices, uvs, normals, uvArray, normalArray);
                        arrangeIndices(vertex2, indices, uvs, normals, uvArray, normalArray);
                        arrangeIndices(vertex3, indices, uvs, normals, uvArray, normalArray);

                    }

                    line = reader.readLine();
                }

                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            vertexArray = new float[vertices.size() * 3];
            indexArray = new int[indices.size()];

            int pointer = 0;
            for (Vector3f vertex : vertices) {
                vertexArray[pointer++] = vertex.x;
                vertexArray[pointer++] = vertex.y;
                vertexArray[pointer++] = vertex.z;
            }

            for (int i = 0; i < indices.size(); i++) {
                indexArray[i] = indices.get(i);
            }

            RawModel rawModel = new RawModel(indexArray, vertexArray, uvArray, normalArray);
            return new SharedModel(rawModel, material);

        }, SharedModel.class);
    }

    private Models() {
    }

    private static void arrangeIndices(String[] vertexData, List<Integer> indices, List<Vector2f> uvs,
                                       List<Vector3f> normals, float[] uvArray, float[] normalArray) {
        int vertexPointer = Integer.parseInt(vertexData[0]) - 1;

        indices.add(vertexPointer);

        Vector2f uv = uvs.get(Integer.parseInt(vertexData[1]) - 1);
        uvArray[vertexPointer * 2] = uv.x;
        uvArray[vertexPointer * 2 + 1] = uv.y;

        Vector3f normal = normals.get(Integer.parseInt(vertexData[2]) - 1);
        normalArray[vertexPointer * 3] = normal.x;
        normalArray[vertexPointer * 3 + 1] = normal.y;
        normalArray[vertexPointer * 3 + 2] = normal.z;
    }

    private static void loadMaterialLibrary(String filePath, String filename) {

        String currentMaterialName = null;

        Texture currentTexture = null;
        Vector3f currentColor = new Vector3f(1, 1, 1);

        float currentSpecularExponent = 1;

        Vector3f currentAmbientReflectivity = new Vector3f(1, 1, 1);
        Vector3f currentDiffuseReflectivity = new Vector3f(1, 1, 1);
        Vector3f currentSpecularReflectivity = new Vector3f(1, 1, 1);
        Vector3f currentEmit = new Vector3f();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath + filename));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("newmtl")) {
                    if (currentMaterialName != null) loadedMaterials.put(currentMaterialName,
                            new Material(currentTexture,
                                    currentColor,
                                    currentAmbientReflectivity,
                                    currentDiffuseReflectivity,
                                    currentSpecularReflectivity,
                                    currentEmit,
                                    currentSpecularExponent)); //If new material is started, previous one gets saved

                    currentMaterialName = line.split(" ")[1];

                    currentTexture = null;
                    currentColor = new Vector3f(1, 1, 1);

                    currentSpecularExponent = 1;

                    currentAmbientReflectivity = new Vector3f(1, 1, 1);
                    currentDiffuseReflectivity = new Vector3f(1, 1, 1);
                    currentSpecularReflectivity = new Vector3f(1, 1, 1);
                    currentEmit = new Vector3f();
                } else if (currentMaterialName != null) {
                    if (line.startsWith("Ns")) currentSpecularExponent = Float.parseFloat(line.split(" ")[1]);
                    else if (line.startsWith("Ka")) currentAmbientReflectivity = new Vector3f(
                            Float.parseFloat(line.split(" ")[1]),
                            Float.parseFloat(line.split(" ")[2]),
                            Float.parseFloat(line.split(" ")[3]));
                    else if (line.startsWith("Kd")) currentDiffuseReflectivity = new Vector3f(
                            Float.parseFloat(line.split(" ")[1]),
                            Float.parseFloat(line.split(" ")[2]),
                            Float.parseFloat(line.split(" ")[3]));
                    else if (line.startsWith("Ks")) currentSpecularReflectivity = new Vector3f(
                            Float.parseFloat(line.split(" ")[1]),
                            Float.parseFloat(line.split(" ")[2]),
                            Float.parseFloat(line.split(" ")[3]));
                        //else if (line.startsWith("d")); //Transparency [0.0; 1.0]
                    else if (line.startsWith("map_Kd"))
                        currentTexture = Resources.getResource(filePath + line.split(" ")[1], Texture.class);
                }
            }

            if (currentMaterialName != null) loadedMaterials.put(currentMaterialName,
                    new Material(currentTexture,
                            currentColor,
                            currentAmbientReflectivity,
                            currentDiffuseReflectivity,
                            currentSpecularReflectivity,
                            currentEmit,
                            currentSpecularExponent)); //If new material is started, previous one gets saved

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
