package com.mtronicsdev.polygame.display;

import com.mtronicsdev.polygame.io.Colors;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWWindowCloseCallback;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Main {

    public static void main(String... args) {
        Colors.getColor("#1659de");
        try {
            Class.forName("com.mtronicsdev.polygame.graphics.Shader");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (glfwInit() != 1)
            throw new IllegalStateException("Unable to initialize GLFW");

        new Window("Polygame", 400, 300);
        Monitors monitors = new Monitors();

        GLFWWindowPosCallback callback = GLFW.GLFWWindowPosCallback((l, i, i1) -> monitors.contentPane.repaint());
        GLFWWindowSizeCallback callback2 = GLFW.GLFWWindowSizeCallback((l, i, i1) -> monitors.contentPane.repaint());
        GLFWWindowCloseCallback callback3 = GLFW.GLFWWindowCloseCallback((l) -> monitors.contentPane.repaint());

        Display.getWindows().forEach(w -> {
            glfwSetWindowPosCallback(w.getId(), callback);
            glfwSetWindowSizeCallback(w.getId(), callback2);
            glfwSetWindowCloseCallback(w.getId(), callback3);
        });

        while (true) {
            Display.refresh();
        }
    }

}
