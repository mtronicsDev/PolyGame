package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.math.Matrix4f;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class SkyboxShaderProgram extends ShaderProgram {

    private int locationOfProjectionMatrix;
    private int locationOfViewMatrix;

    public SkyboxShaderProgram() throws URISyntaxException {
        super(Resources.getResource(
                        new File(
                                RenderEngine.class
                                        .getResource("/com/mtronicsdev/polygame/res/skybox_vert.glsl").toURI()),
                        Shader.class),
                Resources.getResource(
                        new File(RenderEngine.class
                                .getResource("/com/mtronicsdev/polygame/res/skybox_frag.glsl").toURI()),
                        Shader.class));

        locationOfProjectionMatrix = getUniformLocation("projectionMatrix");
        locationOfViewMatrix = getUniformLocation("viewMatrix");

    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes();

        bindAttribute(0, "position");
    }

    public void loadProjectionMatrix(Matrix4f matrix4f) {
        loadMatrix4f(locationOfProjectionMatrix, matrix4f);
    }

    public void loadViewMatrix(Matrix4f matrix4f) {
        loadMatrix4f(locationOfViewMatrix, matrix4f);
    }

}

