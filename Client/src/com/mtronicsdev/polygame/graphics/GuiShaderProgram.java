package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.math.Matrix4f;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class GuiShaderProgram extends ShaderProgram {

    private int locationOfTransformationMatrix;

    public GuiShaderProgram() throws URISyntaxException {
        super(Resources.getResource(
                        new File(
                                RenderEngine.class
                                        .getResource("/com/mtronicsdev/polygame/res/gui_vert.glsl").toURI()),
                        Shader.class),
                Resources.getResource(
                        new File(RenderEngine.class
                                .getResource("/com/mtronicsdev/polygame/res/gui_frag.glsl").toURI()),
                        Shader.class));

        locationOfTransformationMatrix = getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes();

        bindAttribute(0, "position");
    }

    public void loadTransformationMatrix(Matrix4f matrix4f) {
        loadMatrix4f(locationOfTransformationMatrix, matrix4f);
    }
}