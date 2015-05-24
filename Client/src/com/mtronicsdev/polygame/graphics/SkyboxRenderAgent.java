package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.modules.Camera;
import com.mtronicsdev.polygame.entities.modules.Skybox;
import com.mtronicsdev.polygame.math.Matrix4f;

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
public class SkyboxRenderAgent extends RenderAgent<SkyboxShaderProgram> {

    private static final SkyboxModel SKYBOX_MODEL = new SkyboxModel();
    private CubeMapTexture cubeMap;

    protected SkyboxRenderAgent(Matrix4f projectionMatrix) throws URISyntaxException {
        super(new SkyboxShaderProgram());
        shaderProgram.bind();
        shaderProgram.loadProjectionMatrix(projectionMatrix);
        shaderProgram.unbind();
    }

    void render(List<Camera> cameras) {
        if (cubeMap != null) {
            shaderProgram.bind();

            Matrix4f view = cameras.get(0).getViewMatrix();
            view.m30 = view.m31 = view.m32 = 0;

            shaderProgram.loadViewMatrix(view);
            SKYBOX_MODEL.bind();
            glEnableVertexAttribArray(0);

            glActiveTexture(GL_TEXTURE0);
            cubeMap.bind();

            glDrawElements(GL_TRIANGLES, SKYBOX_MODEL.getSize(), GL_UNSIGNED_INT, 0);

            cubeMap.unbind();
            glDisableVertexAttribArray(0);
            SKYBOX_MODEL.unbind();
            shaderProgram.unbind();

            glClear(GL_DEPTH_BUFFER_BIT);
        }
    }

    public void setSkybox(Skybox skybox) {
        this.cubeMap = skybox == null ? null : skybox.getTexture();
    }

    void setProjectionMatrix(Matrix4f projectionMatrix) {
        shaderProgram.loadProjectionMatrix(projectionMatrix);
    }
}
