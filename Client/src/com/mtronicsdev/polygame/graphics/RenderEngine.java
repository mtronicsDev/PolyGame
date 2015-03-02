package com.mtronicsdev.polygame.graphics;

import com.mtronicsdev.polygame.io.Resources;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class RenderEngine {

    private static final ShaderProgram defaultProgram;

    static {
        Shader vertex, fragment;

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
    }
}
