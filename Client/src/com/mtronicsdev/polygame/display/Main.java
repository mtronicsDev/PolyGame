package com.mtronicsdev.polygame.display;

import com.mtronicsdev.polygame.io.Colors;

import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.opengl.GL11.GL_TRUE;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Main {

    public static void main(String... args) {
        Colors.getColor("#1659de");

        if (glfwInit() != GL_TRUE)
            throw new IllegalStateException("Unable to initialize GLFW");

        Window window = new Window("Polygame", Monitor.getPrimary());

        while (!window.shouldClose()) {
            window.refresh();
        }

        window.close();
    }

}
