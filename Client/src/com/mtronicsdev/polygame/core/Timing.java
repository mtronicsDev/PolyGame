package com.mtronicsdev.polygame.core;

import org.lwjgl.glfw.GLFW;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public final class Timing {

    private static double lastFrameTime;
    private static double delta;

    static {
        lastFrameTime = currentTime();
        delta = 0;
    }

    private Timing() {

    }

    public static void refresh() {
        double current = currentTime();
        delta = currentTime() - lastFrameTime;
        lastFrameTime = current;
    }

    private static double currentTime() {
        return GLFW.glfwGetTime();
    }

    public static double getDelta() {
        return delta;
    }

}
