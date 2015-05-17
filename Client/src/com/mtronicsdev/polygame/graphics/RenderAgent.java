package com.mtronicsdev.polygame.graphics;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public abstract class RenderAgent<S extends ShaderProgram> {

    protected S shaderProgram;

    protected RenderAgent(S shaderProgram) {
        this.shaderProgram = shaderProgram;
    }

    protected final void bindShaderProgram() {
        shaderProgram.bind();
    }

    protected final void unbindShaderProgram() {
        shaderProgram.unbind();
    }
}
