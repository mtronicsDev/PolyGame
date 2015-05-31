package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.Camera;
import com.mtronicsdev.polygame.entities.modules.LightSource;
import com.mtronicsdev.polygame.entities.modules.Model;
import com.mtronicsdev.polygame.util.math.Matrix4f;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class DefaultRenderAgent extends RenderAgent<DefaultShaderProgram> {

    public DefaultRenderAgent(Matrix4f projectionMatrix, float ambientLightStrength) throws URISyntaxException {
        super(new DefaultShaderProgram());

        bindShaderProgram();
        shaderProgram.loadProjectionMatrix(projectionMatrix);
        shaderProgram.loadAmbientLight(ambientLightStrength);
        unbindShaderProgram();
    }

    void render(Map<SharedModel, List<Model>> modelPool, List<Camera> cameras, List<LightSource> lightSources) {
        bindShaderProgram();
        glActiveTexture(GL_TEXTURE0);

        shaderProgram.loadViewMatrix(cameras.get(0).getViewMatrix());

        shaderProgram.loadLights(lightSources);

        modelPool.keySet().forEach(sharedModel -> {
            sharedModel.getRawModel().bind();
            shaderProgram.loadMaterial(sharedModel.getMaterial());
            sharedModel.getMaterial().getTexture().bind();

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            modelPool.get(sharedModel).forEach(model -> {
                shaderProgram.loadTransformationMatrix(((Entity3D) model.getParent()).getTransformationMatrix());
                glDrawElements(GL_TRIANGLES, sharedModel.getRawModel().getSize(), GL_UNSIGNED_INT, 0);
            });

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);

            sharedModel.getRawModel().unbind();
            sharedModel.getMaterial().getTexture().unbind();
        });

        unbindShaderProgram();
    }

    void setProjectionMatrix(Matrix4f projectionMatrix) {
        shaderProgram.loadProjectionMatrix(projectionMatrix);
    }
}
