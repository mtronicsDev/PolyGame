package com.mtronicsdev.polygame.display;

import com.mtronicsdev.polygame.io.Colors;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
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

        Input.registerKeyHandler(GLFW_KEY_W);

        Window window = new Window("Polygame", 100, 100);

        while (!window.shouldClose()) {
            window.refresh();
            System.out.println(Input.keyDown(GLFW_KEY_W) + "|"
                    + Input.keyPressed(GLFW_KEY_W) + "|"
                    + Input.keyUp(GLFW_KEY_W));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        window.close();
    }

}
