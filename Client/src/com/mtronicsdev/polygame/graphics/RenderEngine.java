package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.entities.Entity3D;
import com.mtronicsdev.polygame.entities.modules.Camera;
import com.mtronicsdev.polygame.entities.modules.Model;
import com.mtronicsdev.polygame.io.Resources;
import com.mtronicsdev.polygame.io.Textures;
import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;
import com.mtronicsdev.polygame.util.VectorMath;
import org.lwjgl.opengl.GL11;

import java.net.URISyntaxException;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawElements;
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

        m = new Entity3D(new Vector3f(0, 0, -5), new Model(texture, vao));
        c = new Entity3D(new Camera());

    }

    private RenderEngine() {
    }

    public static void render() {
        m.getModule(Model.class).getRawModel().bind();
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        shaderProgram.loadTransformationMatrix(m.getTransformationMatrix());
        shaderProgram.loadViewMatrix(c.getModule(Camera.class).getViewMatrix());

        glActiveTexture(GL_TEXTURE0);
        m.getModule(Model.class).getTexture().bind();

        glDrawElements(GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);

        m.getModule(Model.class).getTexture().unbind();

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        m.getModule(Model.class).getRawModel().unbind();
    }
}
