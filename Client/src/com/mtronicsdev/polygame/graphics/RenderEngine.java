package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.display.Input;
import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.Camera;
import com.mtronicsdev.polygame.entities.modules.LightSource;
import com.mtronicsdev.polygame.entities.modules.Model;
import com.mtronicsdev.polygame.entities.modules.Terrain;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.io.Textures;
import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;
import com.mtronicsdev.polygame.util.VectorMath;
import org.lwjgl.glfw.GLFW;

import java.net.URISyntaxException;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Renders and communicates with the graphics card.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class RenderEngine {

    private static DefaultRenderAgent defaultRenderAgent;
    private static TerrainRenderAgent terrainRenderAgent;

    private static Map<SharedModel, List<Model>> modelPool;

    private static java.util.List<LightSource> lightSources;
    private static java.util.List<Camera> cameras;
    private static List<Terrain> terrains;

    private static Matrix4f projectionMatrix;

    private static float fov = 70;
    private static float zNear = .1f;
    private static float zFar = 1000;

    private static Entity3D c;

    static {
        projectionMatrix = VectorMath.createProjectionMatrix(fov, zNear, zFar, 1280, 720);

        try {
            defaultRenderAgent = new DefaultRenderAgent(projectionMatrix, .2f);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        try {
            terrainRenderAgent = new TerrainRenderAgent(projectionMatrix, .2f);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        modelPool = new HashMap<>();

        lightSources = new ArrayList<>();
        cameras = new ArrayList<>();
        terrains = new ArrayList<>();

        SharedModel sharedModel =
                Resources.getResource("res/first.obj", SharedModel.class);

        Random random = new Random();

        for (int i = 0; i < 51; i++) {
            Entity3D e = new Entity3D(new Vector3f(random.nextInt(20) - 10, random.nextInt(10) + 50, random.nextInt(50) - 50),
                    new Vector3f(random.nextInt(360), random.nextInt(360), random.nextInt(360)),
                    new Model(sharedModel));

            if (i % 50 == 0) e.addModule(new LightSource(new Vector3f(Math.abs(random.nextFloat()),
                    Math.abs(random.nextFloat()), Math.abs(random.nextFloat()))));
        }

        Entity3D t = new Entity3D(new Terrain(Textures.loadTexture("res/rgba.png")));

        c = new Entity3D(new Camera());

        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);

        Input.registerKeyHandler(GLFW.GLFW_KEY_Q);
        Input.registerKeyHandler(GLFW.GLFW_KEY_W);
        Input.registerKeyHandler(GLFW.GLFW_KEY_E);
        Input.registerKeyHandler(GLFW.GLFW_KEY_A);
        Input.registerKeyHandler(GLFW.GLFW_KEY_S);
        Input.registerKeyHandler(GLFW.GLFW_KEY_D);
    }

    private RenderEngine() {
    }

    public static void render() {

        if (Input.keyPressed(GLFW.GLFW_KEY_W)) {
            Vector3f pos = c.getPosition();
            pos.z -= .1f;
            c.setPosition(pos);
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_S)) {
            Vector3f pos = c.getPosition();
            pos.z += .1f;
            c.setPosition(pos);
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_Q)) {
            Vector3f pos = c.getPosition();
            pos.y -= .1f;
            c.setPosition(pos);
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_E)) {
            Vector3f pos = c.getPosition();
            pos.y += .1f;
            c.setPosition(pos);
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_A)) {
            Vector3f pos = c.getPosition();
            pos.x -= .1f;
            c.setPosition(pos);
        }

        if (Input.keyPressed(GLFW.GLFW_KEY_D)) {
            Vector3f pos = c.getPosition();
            pos.x += .1f;
            c.setPosition(pos);
        } //Camera steering

        /*Model model = m.getModule(Model.class);

        model.getRawModel().bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        shaderProgram.loadTransformationMatrix(m.getTransformationMatrix());
        shaderProgram.loadViewMatrix(c.getModule(Camera.class).getViewMatrix());

        shaderProgram.loadLight(c.getPosition(), c.getModule(LightSource.class).getColor());

        shaderProgram.loadMaterial(model.getMaterial());

        glActiveTexture(GL_TEXTURE0);
        model.getMaterial().getTexture().bind();

        glDrawElements(GL_TRIANGLES, m.getModule(Model.class).getRawModel().getSize(), GL11.GL_UNSIGNED_INT, 0);

        model.getMaterial().getTexture().unbind();

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        model.getRawModel().unbind();*/

        ////////////////////////////////////////////////////////////////////////////////////////////////////////

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
}
