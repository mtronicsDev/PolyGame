package com.mtronicsdev.polygame.graphics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class ShaderProgram {

    private int id;
    private List<Shader> shaders;

    public ShaderProgram(Shader... shaders) {
        this.shaders = new ArrayList<>(Arrays.asList(shaders));

        id = glCreateProgram();
        this.shaders.forEach(s -> glAttachShader(id, s.getId()));

        glLinkProgram(id);
        glValidateProgram(id);
    }

    void use() {
        glUseProgram(id);
    }

    public int getId() {
        return id;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        shaders.forEach(shader -> glDetachShader(id, shader.getId()));
        glDeleteProgram(id);
    }
}
