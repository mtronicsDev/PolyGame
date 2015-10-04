package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.util.math.Vector2f;
import com.mtronicsdev.polygame.util.math.Vector4f;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class TextShaderProgram extends ShaderProgram {

    private int locationOfOffsetVector;
    private int locationOfSize;
    private int locationOfColor;

    public TextShaderProgram() throws URISyntaxException {
        super(Resources.getResource(new File("res/shaders/text_vert.glsl"), Shader.class),
                Resources.getResource(new File("res/shaders/text_frag.glsl"), Shader.class));

        locationOfOffsetVector = getUniformLocation("offsetVector");
        locationOfSize = getUniformLocation("size");
        locationOfColor = getUniformLocation("color");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttributes();

        bindAttribute(0, "position");
        bindAttribute(1, "textureCoordinates");
    }

    public void loadOffsetVector(Vector2f offset) {
        loadVector2f(locationOfOffsetVector, offset);
    }

    public void loadSize(Vector2f size) {
        loadVector2f(locationOfSize, size);
    }

    public void loadColor(Vector4f color) {
        loadVector4f(locationOfColor, color);
    }
}