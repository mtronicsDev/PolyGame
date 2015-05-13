package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

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

        VertexBufferObject vbo = new VertexBufferObject(
                -.5f, .5f, 0,
                -.5f, -.5f, 0,
                .5f, -.5f, 0,

                .5f, -.5f, 0,
                .5f, .5f, 0,
                -.5f, .5f, 0);
        vao = new VertexArrayObject(vbo);
    }

    public static void render() {
        glBindVertexArray(vao.getId());
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }
}
