package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.modules.*;
import com.mtronicsdev.polygame.io.Preferences;
import com.mtronicsdev.polygame.util.math.Matrix4f;
import com.mtronicsdev.polygame.util.math.VectorMath;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;

/**
 * Renders and communicates with the graphics card.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class RenderEngine {

    private static DefaultRenderAgent defaultRenderAgent;
    private static TerrainRenderAgent terrainRenderAgent;
    private static SkyboxRenderAgent skyboxRenderAgent;
    private static GuiRenderAgent guiRenderAgent;

    private static Map<SharedModel, List<Model>> modelPool;

    private static java.util.List<LightSource> lightSources;
    private static java.util.List<Camera> cameras;
    private static List<Terrain> terrains;

    private static Matrix4f projectionMatrix;

    private static float fov;
    private static float zNear = .1f;
    private static float zFar;

    private static float ambientLightStrength;

    static {
        fov = Preferences.getPreference("renderEngine.fieldOfView", float.class);
        zFar = Preferences.getPreference("renderEngine.viewDistance", float.class);
        ambientLightStrength = Preferences.getPreference("renderEngine.ambientLightStrength", float.class);

        projectionMatrix = VectorMath.createProjectionMatrix(fov, zNear, zFar, 1280, 720);

        try {
            defaultRenderAgent = new DefaultRenderAgent(projectionMatrix, ambientLightStrength);
            terrainRenderAgent = new TerrainRenderAgent(projectionMatrix, ambientLightStrength);
            skyboxRenderAgent = new SkyboxRenderAgent(projectionMatrix);
            guiRenderAgent = new GuiRenderAgent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        modelPool = new HashMap<>();

        lightSources = new ArrayList<>();
        cameras = new ArrayList<>();
        terrains = new ArrayList<>();

        glEnable(GL_DEPTH_TEST);
        if (Preferences.getPreference("renderEngine.faceCulling", boolean.class)) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
    }

    private RenderEngine() {
    }

    public static void render() {
        if (!cameras.isEmpty()) {
            skyboxRenderAgent.render(cameras);
            defaultRenderAgent.render(modelPool, cameras, lightSources);
            terrainRenderAgent.render(terrains, cameras, lightSources);
        }

        guiRenderAgent.render();
    }

    public static void registerSharedModel(SharedModel sharedModel) {
        modelPool.put(sharedModel, new ArrayList<>());
    }

    public static void registerModel(Model model) {
        modelPool.get(model.getSharedModel()).add(model);
    }

    public static void registerLightSource(LightSource lightSource) {
        lightSources.add(lightSource);
    }

    public static void registerCamera(Camera camera) {
        cameras.add(camera);
    }

    public static void registerTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public static void setSkybox(Skybox skybox) {
        skyboxRenderAgent.setSkybox(skybox);
    }

    public static void unRegisterSharedModel(SharedModel sharedModel) {
        modelPool.remove(sharedModel);
    }

    public static void unRegisterModel(Model model) {
        modelPool.get(model.getSharedModel()).remove(model);
    }

    public static void unRegisterLightSource(LightSource lightSource) {
        lightSources.remove(lightSource);
    }

    public static void unRegisterCamera(Camera camera) {
        cameras.remove(camera);
    }

    public static void unRegisterTerrain(Terrain terrain) {
        terrains.remove(terrain);
    }

    public static Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public static float getFov() {
        return fov;
    }

    public static float getzNear() {
        return zNear;
    }

    public static float getzFar() {
        return zFar;
    }

    public static float getAmbientLightStrength() {
        return ambientLightStrength;
    }

    public static void updateProjectionMatrix(int width, int height) {
        projectionMatrix = VectorMath.createProjectionMatrix(fov, zNear, zFar, width, height);
        defaultRenderAgent.setProjectionMatrix(projectionMatrix);
        terrainRenderAgent.setProjectionMatrix(projectionMatrix);
        skyboxRenderAgent.setProjectionMatrix(projectionMatrix);
    }
}
