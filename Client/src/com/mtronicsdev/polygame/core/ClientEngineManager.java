package com.mtronicsdev.polygame.core;

import com.mtronicsdev.polygame.display.Display;
import com.mtronicsdev.polygame.entities.Entity;
import com.mtronicsdev.polygame.entities.modules.gui.GuiPanel;

import static org.lwjgl.glfw.GLFW.glfwInit;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class ClientEngineManager {

    public static void init() {
        try {
            Class.forName("com.mtronicsdev.polygame.graphics.Shader");
            Class.forName("com.mtronicsdev.polygame.io.Colors");
            Class.forName("com.mtronicsdev.polygame.io.Models");
            Class.forName("com.mtronicsdev.polygame.io.Textures");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (glfwInit() != 1)
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    public static void run() {
        GuiPanel.updateLayout();

        while (Display.getWindows().size() > 0) {
            Entity.getRoot().update();
            Display.refresh();
        }
    }

}
