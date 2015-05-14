package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindFragDataLocation;

/**
 * Represents a shader program and manages it.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class ShaderProgram implements GLObject {

    protected int id;
    protected List<Shader> shaders;

    //Increases performance as GC doesn't have to collect a new FloatBuffer for each frame
    protected FloatBuffer reusableMatrixCache;

    public ShaderProgram(Shader... shaders) {
        this.shaders = new ArrayList<>(Arrays.asList(shaders));
        reusableMatrixCache = BufferUtils.createFloatBuffer(16);

        id = glCreateProgram();
        this.shaders.forEach(s -> glAttachShader(id, s.getId()));

        glBindFragDataLocation(id, 0, "fragColor");
        glLinkProgram(id);
        glValidateProgram(id);
    }

    final void use() {
        glUseProgram(id);
    }

    protected final void loadBoolean(int location, boolean value) {
        glUniform1i(location, value ? 1 : 0);
    }

    protected final void loadFloat(int location, float value) {
        glUniform1f(location, value);
    }

    protected final void loadInteger(int location, int value) {
        glUniform1i(location, value);
    }

    protected final void loadVector3f(int location, Vector3f value) {
        glUniform3f(location, value.x, value.y, value.z);
    }

    protected final void loadMatrix4f(int location, Matrix4f value) {
        reusableMatrixCache = value.store(reusableMatrixCache);
        reusableMatrixCache.flip();

        glUniformMatrix4(location, false, reusableMatrixCache);
    }

    protected final int getUniformLocation(String name) {
        return glGetUniformLocation(id, name);
    }

    public final int getId() {
        return id;
    }

    @Override
    protected final void finalize() throws Throwable {
        super.finalize();
        cleanUp();
    }

    @Override
    public void cleanUp() {
        shaders.forEach(shader -> glDetachShader(id, shader.getId()));
        glDeleteProgram(id);
    }
}
