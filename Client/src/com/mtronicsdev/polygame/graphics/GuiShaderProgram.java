package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Vector2f;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class GuiShaderProgram extends ShaderProgram {

    private int locationOfOffsetVector;
    private int locationOfSize;

    public GuiShaderProgram() throws URISyntaxException {
        super(Resources.getResource(new File("res/shaders/gui_vert.glsl"), Shader.class),
                Resources.getResource(new File("res/shaders/gui_frag.glsl"), Shader.class));

        locationOfOffsetVector = getUniformLocation("offsetVector");
        locationOfSize = getUniformLocation("size");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes();

        bindAttribute(0, "position");
    }

    public void loadOffsetVector(Vector2f offset) {
        loadVector2f(locationOfOffsetVector, offset);
    }

    public void loadSize(Vector2f size) {
        loadVector2f(locationOfSize, size);
    }
}
