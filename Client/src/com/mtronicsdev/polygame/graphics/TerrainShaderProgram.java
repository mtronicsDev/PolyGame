package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.LightSource;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Matrix4f;
import com.mtronicsdev.polygame.util.math.Vector3f;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class TerrainShaderProgram extends ShaderProgram {

    private int locationOfTransformationMatrix;
    private int locationOfProjectionMatrix;
    private int locationOfViewMatrix;

    private int[] locationOfLightPosition;
    private int[] locationOfLightColor;
    private int locationOfAmbientLightStrength;

    private int locationOfAmbientReflectivity;
    private int locationOfDiffuseReflectivity;
    private int locationOfSpecularReflectivity;

    private int locationOfSpecularExponent;

    private int locationOfClipPlane;

    private int locationOfBlendMap;
    private int locationOfTexture0;
    private int locationOfTexture1;
    private int locationOfTexture2;
    private int locationOfTexture3;

    public TerrainShaderProgram() throws URISyntaxException {
        super(Resources.getResource(new File("res/shaders/terrain_vert.glsl"), Shader.class),
                Resources.getResource(new File("res/shaders/terrain_frag.glsl"), Shader.class));

        locationOfTransformationMatrix = getUniformLocation("transformationMatrix");
        locationOfProjectionMatrix = getUniformLocation("projectionMatrix");
        locationOfViewMatrix = getUniformLocation("viewMatrix");

        locationOfLightPosition = new int[4];
        locationOfLightColor = new int[4];

        for (int i = 0; i < 4; i++) {
            locationOfLightPosition[i] = getUniformLocation("lightPosition[" + i + "]");
            locationOfLightColor[i] = getUniformLocation("lightColor[" + i + "]");
        }

        locationOfAmbientLightStrength = getUniformLocation("ambientLightStrength");

        locationOfAmbientReflectivity = getUniformLocation("ambientReflectivity");
        locationOfDiffuseReflectivity = getUniformLocation("diffuseReflectivity");
        locationOfSpecularReflectivity = getUniformLocation("specularReflectivity");

        locationOfSpecularExponent = getUniformLocation("specularExponent");

        locationOfClipPlane = getUniformLocation("clipPlane");

        locationOfBlendMap = getUniformLocation("blendMap");
        locationOfTexture0 = getUniformLocation("texture0");
        locationOfTexture1 = getUniformLocation("texture1");
        locationOfTexture2 = getUniformLocation("texture2");
        locationOfTexture3 = getUniformLocation("texture3");

    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes();

        bindAttribute(0, "position");
        bindAttribute(1, "textureCoordinates");
        bindAttribute(2, "normal");
    }

    public void loadTransformationMatrix(Matrix4f matrix4f) {
        loadMatrix4f(locationOfTransformationMatrix, matrix4f);
    }

    public void loadProjectionMatrix(Matrix4f matrix4f) {
        loadMatrix4f(locationOfProjectionMatrix, matrix4f);
    }

    public void loadViewMatrix(Matrix4f matrix4f) {
        loadMatrix4f(locationOfViewMatrix, matrix4f);
    }

    public void loadAmbientLight(float strength) {
        loadFloat(locationOfAmbientLightStrength, strength);
    }

    public void loadLights(List<LightSource> lightSources) {
        for (int i = 0; i < Math.min(4, lightSources.size()); i++) {
            loadVector3f(locationOfLightPosition[i], ((Entity3D) lightSources.get(i).getParent()).getPosition());
            loadVector3f(locationOfLightColor[i], lightSources.get(i).getColor());
        }
    }

    public void loadMaterial(Material material) {
        loadVector3f(locationOfAmbientReflectivity, material.getAmbientReflectivity());
        loadVector3f(locationOfDiffuseReflectivity, material.getDiffuseReflectivity());
        loadVector3f(locationOfSpecularReflectivity, material.getSpecularReflectivity());

        loadFloat(locationOfSpecularExponent, material.getSpecularExponent());
    }

    public void loadTextureUnits() {
        loadInteger(locationOfBlendMap, 0);
        loadInteger(locationOfTexture0, 1);
        loadInteger(locationOfTexture1, 2);
        loadInteger(locationOfTexture2, 3);
        loadInteger(locationOfTexture3, 4);
    }

    public void loadClipPlane(Vector3f clipPlane, float height) {
        loadVector4f(locationOfClipPlane, clipPlane, height);
    }

}
