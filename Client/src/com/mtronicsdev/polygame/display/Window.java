package com.mtronicsdev.polygame.display;

import com.mtronicsdev.polygame.entities.modules.gui.GuiPanel;
import com.mtronicsdev.polygame.graphics.RenderEngine;
import com.mtronicsdev.polygame.io.Preferences;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GLContext;

import java.awt.*;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

/**
 * Creates a window with an OpenGL context.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Window {

    private long id;
    private float[] backgroundColor;
    private GLFWScrollCallback scrollCallback;
    private GLFWWindowSizeCallback windowSizeCallback;

    public Window(String title, int width, int height) {
        this(title, width, height, 0);
    }

    public Window(String title, Monitor monitor) {
        this(title, monitor.getVideoMode().getWidth(), monitor.getVideoMode().getHeight(), monitor);
    }

    public Window(String title, int width, int height, Monitor monitor) {
        this(title, width, height, monitor.getId());
    }

    private Window(String title, int width, int height, long monitor) {
        glfwSetErrorCallback(errorCallbackPrint(System.err));

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);

        id = glfwCreateWindow(width, height, title, monitor, 0);

        if (id == 0)
            throw new RuntimeException("Failed to create the GLFW window");

        glfwMakeContextCurrent(id);
        glfwSwapInterval(1);
        GLContext.createFromCurrent();

        glfwSetInputMode(id, GLFW_STICKY_KEYS, 1);
        glfwSetInputMode(id, GLFW_STICKY_MOUSE_BUTTONS, 1);

        glfwSetScrollCallback(id, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long l, double v, double v1) {
                Input.registerScrollInput(v1);
            }
        });

        glfwSetWindowSizeCallback(id, windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long l, int i, int i1) {
                glViewport(0, 0, i, i1);
                RenderEngine.updateProjectionMatrix(i, i1);
                GuiPanel.updateLayout();
            }
        });

        setBackgroundColor(Preferences.getPreference("window.backgroundColor", Color.class));
        glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);

        Display.addWindow(this);

        glfwShowWindow(id);
    }

    public void refresh() {
        glfwMakeContextCurrent(id);
        GLContext.createFromCurrent();
        glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        RenderEngine.render();

        glfwSwapBuffers(id);
        glfwPollEvents();
        Input.update(this);
    }

    public void resetViewport() {
        Point size = getSize();
        glViewport(0, 0, size.x, size.y);
    }

    public Color getBackgroundColor() {
        return new Color(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
    }

    public void setBackgroundColor(Color color) {
        backgroundColor = new float[]{
                (float) color.getRed() / 255,
                (float) color.getGreen() / 255,
                (float) color.getBlue() / 255,
                (float) color.getAlpha() / 255};

        glClearColor(backgroundColor[0], backgroundColor[1], backgroundColor[2], backgroundColor[3]);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(id) == GL_TRUE;
    }

    public void close() {
        glfwDestroyWindow(id);
    }

    public long getId() {
        return id;
    }

    public Point getSize() {
        IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);

        glfwGetWindowSize(id, width, height);

        return new Point(width.get(0), height.get(0));
    }

    public void setSize(int width, int height) {
        glfwSetWindowSize(id, width, height);
    }
}
