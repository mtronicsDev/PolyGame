package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.net.URI;
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

    private static final ShaderProgram defaultProgram;

    private static VertexArrayObject vao;

    static {
        Shader vertex, fragment;

        //Shader.init();

        try {
            URI uri = RenderEngine.class.getResource("/com/mtronicsdev/polygame/res/default_vert.glsl").toURI();
            vertex = Resources.getResource(new File(uri), Shader.class);

            uri = RenderEngine.class.getResource("/com/mtronicsdev/polygame/res/default_frag.glsl").toURI();
            fragment = Resources.getResource(new File(uri), Shader.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            vertex = fragment = null;
        }

        System.out.println(vertex + " " + fragment);
        defaultProgram = new ShaderProgram(vertex, fragment);
        defaultProgram.use();

        vao = new VertexArrayObject(new int[]{0, 1, 3, 3, 1, 2},
                new float[]{-.5f, .5f, 0, -.5f, -.5f, 0, .5f, -.5f, 0, .5f, .5f, 0});
    }

    public static void render() {
        vao.bind();
        glEnableVertexAttribArray(0);
        glDrawElements(GL_TRIANGLES, 6, GL11.GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(0);
        vao.unbind();
    }
}
