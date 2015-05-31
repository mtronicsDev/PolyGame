package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Matrix4f;

import java.io.File;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public class WaterShaderProgram extends ShaderProgram {

    private int locationOfTransformationMatrix;
    private int locationOfProjectionMatrix;
    private int locationOfViewMatrix;

    private int locationOfReflectionTexture;
    private int locationOfRefractionTexture;

    public WaterShaderProgram() {
        super(Resources.getResource(new File("res/shaders/water_vert.glsl"), Shader.class),
                Resources.getResource(new File("res/shaders/water_frag.glsl"), Shader.class));

        locationOfTransformationMatrix = getUniformLocation("transformationMatrix");
        locationOfProjectionMatrix = getUniformLocation("projectionMatrix");
        locationOfViewMatrix = getUniformLocation("viewMatrix");

        locationOfReflectionTexture = getUniformLocation("reflectionTexture");
        locationOfRefractionTexture = getUniformLocation("refractionTexture");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes();

        bindAttribute(0, "position");
    }

    public void loadTextureUnits() {
        loadInteger(locationOfReflectionTexture, 0);
        loadInteger(locationOfRefractionTexture, 1);
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
}
