package com.mtronicsdev.polygame.graphics;

import java.net.URISyntaxException;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class GuiRenderAgent extends RenderAgent<GuiShaderProgram> {

    private static final QuadModel QUAD_MODEL = new QuadModel();

    protected GuiRenderAgent() throws URISyntaxException {
        super(new GuiShaderProgram());

    }

    void render(List<GuiObject> guiObjects) {
        shaderProgram.bind();
        QUAD_MODEL.bind();
        glEnableVertexAttribArray(0);
        glActiveTexture(GL_TEXTURE0);
        glDisable(GL_DEPTH_TEST);

        for (GuiObject guiObject : guiObjects) {
            guiObject.getTexture().bind();

            shaderProgram.loadTransformationMatrix(guiObject.getTransformationMatrix());
            glDrawArrays(GL_TRIANGLE_STRIP, 0, QUAD_MODEL.getSize());

            guiObject.getTexture().unbind();
        }

        glEnable(GL_DEPTH_TEST);
        glDisableVertexAttribArray(0);
        QUAD_MODEL.unbind();
        shaderProgram.unbind();
    }
}
