package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.util.math.Matrix4f;
import com.mtronicsdev.polygame.util.math.Vector3f;
import com.mtronicsdev.polygame.util.math.Vector4f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL20.*;

/**
 * Represents a shader program and manages it.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class ShaderProgram extends GLObject {

    protected int id;
    protected List<Shader> shaders;

    //Increases performance as GC doesn't have to collect a new FloatBuffer for each frame
    protected FloatBuffer reusableMatrixCache;

    public ShaderProgram(Shader... shaders) {
        this.shaders = new ArrayList<>(Arrays.asList(shaders));
        reusableMatrixCache = BufferUtils.createFloatBuffer(16);

        id = glCreateProgram();
        this.shaders.forEach(s -> glAttachShader(id, s.getId()));

        bindAttributes();

        glLinkProgram(id);
        glValidateProgram(id);
    }

    protected void bindAttributes() {
    }

    protected void bindAttribute(int attribute, String name) {
        glBindAttribLocation(id, attribute, name);
    }

    final void bind() {
        glUseProgram(id);
    }

    final void unbind() {
        glUseProgram(0);
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

    protected final void loadVector4f(int location, Vector4f value) {
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    protected final void loadVector4f(int location, Vector3f value, float w) {
        glUniform4f(location, value.x, value.y, value.z, w);
    }

    protected final void loadMatrix4f(int location, Matrix4f value) {
        reusableMatrixCache = value.store(reusableMatrixCache);
        reusableMatrixCache.flip();

        glUniformMatrix4fv(location, false, reusableMatrixCache);
    }

    protected final int getUniformLocation(String name) {
        return glGetUniformLocation(id, name);
    }

    public final int getId() {
        return id;
    }

    @Override
    public void cleanUp() {
        shaders.forEach(shader -> glDetachShader(id, shader.getId()));
        glDeleteProgram(id);
    }
}
