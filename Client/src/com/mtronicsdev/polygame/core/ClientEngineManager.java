package com.mtronicsdev.polygame.core;

import com.mtronicsdev.polygame.display.Display;
import com.mtronicsdev.polygame.display.Window;

import static org.lwjgl.glfw.GLFW.glfwInit;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class ClientEngineManager {

    public static void run() {
        try {
            Class.forName("com.mtronicsdev.polygame.graphics.Shader");
            Class.forName("com.mtronicsdev.polygame.io.Colors");
            Class.forName("com.mtronicsdev.polygame.io.Models");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (glfwInit() != 1)
            throw new IllegalStateException("Unable to initialize GLFW");

        new Window("Polygame", 400, 300);
        //Monitors monitors = new Monitors();

        //GLFWWindowPosCallback callback = GLFW.GLFWWindowPosCallback((l, i, i1) -> monitors.contentPane.repaint());
        //GLFWWindowSizeCallback callback2 = GLFW.GLFWWindowSizeCallback((l, i, i1) -> monitors.contentPane.repaint());
        //GLFWWindowCloseCallback callback3 = GLFW.GLFWWindowCloseCallback((l) -> monitors.contentPane.repaint());

        /*Display.getWindows().forEach(w -> {
            glfwSetWindowPosCallback(w.getId(), callback);
            glfwSetWindowSizeCallback(w.getId(), callback2);
            glfwSetWindowCloseCallback(w.getId(), callback3);
        });*/

        while (Display.getWindows().size() > 0) {
            Display.refresh();
        }
    }

}
