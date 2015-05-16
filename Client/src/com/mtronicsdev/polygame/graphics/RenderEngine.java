package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.display.Input;
import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.Camera;
import com.mtronicsdev.polygame.entities.modules.LightSource;
import com.mtronicsdev.polygame.entities.modules.Model;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;
import com.mtronicsdev.polygame.util.VectorMath;
import org.lwjgl.glfw.GLFW;

import java.net.URISyntaxException;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

/**
 * Renders and communicates with the graphics card.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class RenderEngine {

    private static DefaultShaderProgram shaderProgram;

    private static Map<SharedModel, List<Model>> modelPool;

    private static java.util.List<LightSource> lightSources;
    private static java.util.List<Camera> cameras;

    private static Matrix4f projectionMatrix;

    private static float fov = 70;
    private static float zNear = .1f;
    private static float zFar = 1000;

    private static Entity3D m, c;

    static {
        modelPool = new HashMap<>();

        lightSources = new ArrayList<>();
        cameras = new ArrayList<>();

        try {
            shaderProgram = new DefaultShaderProgram();
            shaderProgram.use();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        SharedModel sharedModel =
                Resources.getResource("first.obj", SharedModel.class);

        projectionMatrix = VectorMath.createProjectionMatrix(fov, zNear, zFar, 400, 300);
        shaderProgram.loadProjectionMatrix(projectionMatrix);
        shaderProgram.loadAmbientLight(.2f);

        Random random = new Random();

        for (int i = 0; i < 200; i++) {
            new Entity3D(new Vector3f(random.nextInt(20) - 10, random.nextInt(10) - 5, random.nextInt(50) - 50),
                    new Model(sharedModel));
        }

        m = new Entity3D(new Vector3f(0, -1, -10), new Model(sharedModel));
        c = new Entity3D(new Camera(), new LightSource(new Vector3f(1, 1, 1)));

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
        Vector3f cR = m.getRotation();
        cR.y += .5f;
        m.setRotation(cR);

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
        }

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

        shaderProgram.loadViewMatrix(cameras.get(0).getViewMatrix());

        lightSources.forEach(l ->
                shaderProgram.loadLight(((Entity3D) l.getParent()).getPosition(), l.getColor()));


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
}
