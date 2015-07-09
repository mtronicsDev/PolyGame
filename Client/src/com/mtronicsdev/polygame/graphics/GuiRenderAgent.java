package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.gui.GuiEngine;
import com.mtronicsdev.polygame.gui.GuiPanel;
import com.mtronicsdev.polygame.gui.GuiText;
import com.mtronicsdev.polygame.util.math.Vector2f;

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

    private TextShaderProgram textShaderProgram = new TextShaderProgram();

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
            if (panel instanceof GuiText) {
                GuiText text = (GuiText) panel;

                String chars = text.getText();
                GuiText.Font font = text.getFont();

                textShaderProgram.bind();
                font.getBitmap().bind();

                for (int i = 0; i < chars.length(); i++) {
                    TextQuadModel quad = text.getQuadAt(i);

                    if (quad == null) continue;

                    glEnableVertexAttribArray(0);
                    glEnableVertexAttribArray(1);

                    quad.bind();

                    Vector2f pos = text.getPosition();

                    textShaderProgram.loadOffsetVector(pos);
                    textShaderProgram.loadSize(new Vector2f(1, 1));

                    glDrawArrays(GL_TRIANGLE_STRIP, 0, quad.getSize());
                }

                glDisableVertexAttribArray(0);
                glDisableVertexAttribArray(1);
                font.getBitmap().unbind();
                shaderProgram.bind();
            } else {
                panel.getTexture().bind();

                QUAD_MODEL.bind();

                shaderProgram.loadOffsetVector(panel.getPosition());
                shaderProgram.loadSize(panel.getSize());

                glDrawArrays(GL_TRIANGLE_STRIP, 0, QUAD_MODEL.getSize());

                panel.getTexture().unbind();
            }
        }

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
        glDisableVertexAttribArray(0);
        QUAD_MODEL.unbind();
        shaderProgram.unbind();
    }
}
