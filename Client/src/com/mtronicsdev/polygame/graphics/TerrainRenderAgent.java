package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.Camera;
import com.mtronicsdev.polygame.entities.modules.LightSource;
import com.mtronicsdev.polygame.entities.modules.Terrain;
import com.mtronicsdev.polygame.math.Matrix4f;

import java.net.URISyntaxException;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class TerrainRenderAgent extends RenderAgent<TerrainShaderProgram> {

    protected TerrainRenderAgent(Matrix4f projectionMatrix, float ambientLightStrength) throws URISyntaxException {
        super(new TerrainShaderProgram());

        bindShaderProgram();
        shaderProgram.loadProjectionMatrix(projectionMatrix);
        shaderProgram.loadAmbientLight(ambientLightStrength);
        unbindShaderProgram();
    }

    void render(List<Terrain> terrains, List<Camera> cameras, List<LightSource> lightSources) {
        bindShaderProgram();

        shaderProgram.loadViewMatrix(cameras.get(0).getViewMatrix());

        shaderProgram.loadLights(lightSources);

        for (Terrain terrain : terrains) {
            SharedModel sharedModel = terrain.getSharedModel();

            sharedModel.getRawModel().bind();
            shaderProgram.loadMaterial(sharedModel.getMaterial());
            sharedModel.getMaterial().getTexture().bind();

            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);

            shaderProgram.loadTransformationMatrix(((Entity3D) terrain.getParent()).getTransformationMatrix());
            glDrawElements(GL_TRIANGLES, sharedModel.getRawModel().getSize(), GL_UNSIGNED_INT, 0);

            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);

            sharedModel.getRawModel().unbind();
            sharedModel.getMaterial().getTexture().unbind();
        }

        unbindShaderProgram();
    }
}
