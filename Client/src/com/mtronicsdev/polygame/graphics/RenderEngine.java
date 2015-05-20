package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.*;
import com.mtronicsdev.polygame.io.Preferences;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;
import com.mtronicsdev.polygame.util.VectorMath;

import java.awt.image.BufferedImage;
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

    private static Map<SharedModel, List<Model>> modelPool;

    private static java.util.List<LightSource> lightSources;
    private static java.util.List<Camera> cameras;
    private static List<Terrain> terrains;

    private static Matrix4f projectionMatrix;

    private static float fov;
    private static float zNear = .1f;
    private static float zFar;

    private static float ambientLightStrength;

    private static Entity3D c;

    static {
        fov = Preferences.getPreference("renderEngine.fieldOfView", float.class);
        zFar = Preferences.getPreference("renderEngine.viewDistance", float.class);
        ambientLightStrength = Preferences.getPreference("renderEngine.ambientLightStrength", float.class);

        projectionMatrix = VectorMath.createProjectionMatrix(fov, zNear, zFar, 1280, 720);

        try {
            defaultRenderAgent = new DefaultRenderAgent(projectionMatrix, ambientLightStrength);
            terrainRenderAgent = new TerrainRenderAgent(projectionMatrix, ambientLightStrength);
            skyboxRenderAgent = new SkyboxRenderAgent(projectionMatrix);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        modelPool = new HashMap<>();

        lightSources = new ArrayList<>();
        cameras = new ArrayList<>();
        terrains = new ArrayList<>();

        SharedModel sharedModel =
                Resources.getResource("res/stall.obj", SharedModel.class);

        new Entity3D(new Vector3f(20, 0, -20), new Vector3f(0, 180, 0), new Model(sharedModel));
        new Entity3D(new Vector3f(64, 100, 64), new LightSource(new Vector3f(1, 1, 1)));

        Entity3D t = new Entity3D(new Terrain(Resources.getResource("res/blendMap.png", Texture.class),
                Resources.getResource("res/Grass 00 seamless.png", Texture.class),
                Resources.getResource("res/Dirt 00 seamless.png", Texture.class),
                Resources.getResource("res/Dirt cracked 00 seamless.png", Texture.class),
                Resources.getResource("res/Seamless cobblestones at sunset texture.png", Texture.class),
                Resources.getResource("res/heightmap.png", BufferedImage.class)));

        c = new Entity3D(new ThirdPersonController(), new Model(sharedModel),
                new Sykbox(Resources.getResource("res/lostvalley_front.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_back.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_left.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_right.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_bottom.jpg", BufferedImage.class),
                        Resources.getResource("res/lostvalley_top.jpg", BufferedImage.class)));

        if (Preferences.getPreference("renderEngine.depthTesting", boolean.class)) glEnable(GL_DEPTH_TEST);
        if (Preferences.getPreference("renderEngine.faceCulling", boolean.class)) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
    }

    private RenderEngine() {
    }

    public static void render() {
        skyboxRenderAgent.render(cameras);
        defaultRenderAgent.render(modelPool, cameras, lightSources);
        terrainRenderAgent.render(terrains, cameras, lightSources);
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

    public static void setSkybox(Sykbox skybox) {
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
}
