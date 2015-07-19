package com.mtronicsdev.polygame.core;

import com.mtronicsdev.polygame.display.Display;
import com.mtronicsdev.polygame.entities.Entity;
import com.mtronicsdev.polygame.gui.GuiEngine;

import static org.lwjgl.glfw.GLFW.glfwInit;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public final class ClientEngineManager {

    private ClientEngineManager() {

    }

    public static void init() {
        //Initialize Classes for resource loader
        try {
            Class.forName("com.mtronicsdev.polygame.graphics.Shader");
            Class.forName("com.mtronicsdev.polygame.io.Colors");
            Class.forName("com.mtronicsdev.polygame.io.Models");
            Class.forName("com.mtronicsdev.polygame.io.Textures");
            Class.forName("com.mtronicsdev.polygame.gui.GuiEngine");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (glfwInit() != 1)
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    public static void run() {
        GuiEngine.updateLayout();

        while (Display.getWindows().size() > 0) {
            Entity.getRoot().update();
            Display.refresh();
        }
    }

}
