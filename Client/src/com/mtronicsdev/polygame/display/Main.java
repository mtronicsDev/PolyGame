package com.mtronicsdev.polygame.display;

import com.mtronicsdev.polygame.io.Colors;

import static org.lwjgl.glfw.GLFW.glfwInit;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Main {

    public static void main(String... args) {
        Colors.getColor("#1659de");

        if (glfwInit() != 1)
            throw new IllegalStateException("Unable to initialize GLFW");

        Window window = new Window("Polygame", 400, 300);

        while (!window.shouldClose()) {
            window.refresh();
            System.out.println(Input.getMouseDelta());
        }

        window.close();
    }

}
