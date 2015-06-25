package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.gui.GuiEngine;
import com.mtronicsdev.polygame.gui.GuiPanel;

import java.net.URISyntaxException;

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

    void render() {
        shaderProgram.bind();
        QUAD_MODEL.bind();
        glEnableVertexAttribArray(0);
        glActiveTexture(GL_TEXTURE0);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        for (GuiPanel panel : GuiEngine.getRenderList()) {
            panel.getTexture().bind();

            shaderProgram.loadOffsetVector(panel.getPosition());
            shaderProgram.loadSize(panel.getCurrentSize());

            glDrawArrays(GL_TRIANGLE_STRIP, 0, QUAD_MODEL.getSize());

            panel.getTexture().unbind();
        }

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisableVertexAttribArray(0);
        QUAD_MODEL.unbind();
        shaderProgram.unbind();
    }
}
