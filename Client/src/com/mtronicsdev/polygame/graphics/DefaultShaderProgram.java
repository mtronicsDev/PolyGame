package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.math.Matrix4f;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class DefaultShaderProgram extends ShaderProgram {

    private int transformationMatrix;

    public DefaultShaderProgram() throws URISyntaxException {
        super(Resources.getResource(new File(
                                RenderEngine.class.getResource("/com/mtronicsdev/polygame/res/default_vert.glsl").toURI()),
                        Shader.class),
                Resources.getResource(
                        new File(RenderEngine.class.getResource("/com/mtronicsdev/polygame/res/default_frag.glsl").toURI()),
                        Shader.class));

        transformationMatrix = getUniformLocation("transformationMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix4f) {
        loadMatrix4f(transformationMatrix, matrix4f);
    }

}
