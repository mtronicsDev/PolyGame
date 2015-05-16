package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.Camera;
import com.mtronicsdev.polygame.entities.modules.LightSource;
import com.mtronicsdev.polygame.entities.modules.Model;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.io.Textures;
import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;
import com.mtronicsdev.polygame.util.VectorMath;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.net.URISyntaxException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

/**
 * Renders and communicates with the graphics card.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class RenderEngine {

    private static DefaultShaderProgram shaderProgram;

    private static Matrix4f projectionMatrix;

    private static float fov = 70;
    private static float zNear = .1f;
    private static float zFar = 1000;

    private static Entity3D m, c;

    static {
        try {
            shaderProgram = new DefaultShaderProgram();
            shaderProgram.use();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        RawModel vao = Resources.getResource("first.obj", RawModel.class);

        projectionMatrix = VectorMath.createProjectionMatrix(fov, zNear, zFar, 400, 300);
        shaderProgram.loadProjectionMatrix(projectionMatrix);

        Texture texture = Textures.loadTexture("layout.png");

        m = new Entity3D(new Vector3f(0, -1, -10), new Model(texture, vao));
        c = new Entity3D(new Camera(), new LightSource(Color.WHITE));

        glEnable(GL_DEPTH_TEST);

    }

    private RenderEngine() {
    }

    public static void render() {
        Vector3f cR = m.getRotation();
        cR.y += .5f;
        m.setRotation(cR);

        m.getModule(Model.class).getRawModel().bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);

        shaderProgram.loadTransformationMatrix(m.getTransformationMatrix());
        shaderProgram.loadViewMatrix(c.getModule(Camera.class).getViewMatrix());

        shaderProgram.loadLight(c.getPosition(), c.getModule(LightSource.class).getColor());

        glActiveTexture(GL_TEXTURE0);
        m.getModule(Model.class).getTexture().bind();

        glDrawElements(GL_TRIANGLES, m.getModule(Model.class).getRawModel().getSize(), GL11.GL_UNSIGNED_INT, 0);

        m.getModule(Model.class).getTexture().unbind();

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);

        m.getModule(Model.class).getRawModel().unbind();
    }
}
