package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;

import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class DefaultShaderProgram extends ShaderProgram {

    private int locationOfTransformationMatrix;
    private int locationOfProjectionMatrix;
    private int locationOfViewMatrix;

    private int locationOfLightPosition;
    private int locationOfLightColor;

    public DefaultShaderProgram() throws URISyntaxException {
        super(Resources.getResource(
                        new File(
                                RenderEngine.class
                                        .getResource("/com/mtronicsdev/polygame/res/default_vert.glsl").toURI()),
                        Shader.class),
                Resources.getResource(
                        new File(RenderEngine.class
                                .getResource("/com/mtronicsdev/polygame/res/default_frag.glsl").toURI()),
                        Shader.class));

        locationOfTransformationMatrix = getUniformLocation("transformationMatrix");
        locationOfProjectionMatrix = getUniformLocation("projectionMatrix");
        locationOfViewMatrix = getUniformLocation("viewMatrix");

        locationOfLightPosition = getUniformLocation("lightPosition");
        locationOfLightColor = getUniformLocation("lightColor");

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

    public void loadLight(Vector3f position, Color color) {
        loadVector3f(locationOfLightPosition, position);
        loadVector3f(locationOfLightColor, new Vector3f((float) color.getRed() / 255,
                (float) color.getGreen() / 255,
                (float) color.getBlue() / 255));
    }

}
