package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.math.Matrix4f;
import com.mtronicsdev.polygame.math.Vector3f;
import com.mtronicsdev.polygame.util.VectorMath;
import org.lwjgl.opengl.GL11;

import java.net.URISyntaxException;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;

/**
 * Renders and communicates with the graphics card.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class RenderEngine {

    private static RawModel vao;
    private static DefaultShaderProgram shaderProgram;

    private static Matrix4f projectionMatrix;
    private static Vector3f position = new Vector3f(0, 0, -5);
    private static Vector3f cam = new Vector3f(0, 0, 0);

    private static float fov = 70;
    private static float zNear = .1f;
    private static float zFar = 1000;

    static {
        try {
            shaderProgram = new DefaultShaderProgram();
            shaderProgram.use();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        vao = new RawModel(new int[]{0, 1, 3, 3, 1, 2},
                new float[]{-.5f, .5f, 0, -.5f, -.5f, 0, .5f, -.5f, 0, .5f, .5f, 0});

        projectionMatrix = VectorMath.createProjectionMatrix(fov, zNear, zFar, 400, 300);
        shaderProgram.loadProjectionMatrix(projectionMatrix);
    }

    public static void render() {
        vao.bind();
        glEnableVertexAttribArray(0);

        //position.z -= 0.02f;
        cam.x -= 0.005f;

        Matrix4f transform = VectorMath.createTransformationMatrix(position,
                new Vector3f(0, 0, 45),
                new Vector3f(1, 1, 1));

        Matrix4f view = VectorMath.createViewMatrix(cam, new Vector3f());

        shaderProgram.loadTransformationMatrix(transform);
        shaderProgram.loadViewMatrix(view);

        glDrawElements(GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        vao.unbind();
    }
}
