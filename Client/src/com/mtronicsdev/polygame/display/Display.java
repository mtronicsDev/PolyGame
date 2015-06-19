package com.mtronicsdev.polygame.display;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages {@link Window windows}.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Display {

    private static final List<Window> windows;

    private static double lastFrameTime;
    private static double delta;
    private static Window currentWindow;

    static {
        windows = new ArrayList<>(1);
        lastFrameTime = getCurrentTime();
    }

    private Display() {

    }

    static void addWindow(Window window) {
        if (windows.size() == 0) currentWindow = window;
        windows.add(window);
    }

    public static List<Window> getWindows() {
        return windows;
    }

    public static void refresh() {
        for (Iterator<Window> iterator = windows.iterator(); iterator.hasNext(); ) {
            currentWindow = iterator.next();
            if (currentWindow.shouldClose()) {
                iterator.remove();
                currentWindow.close();
            } else currentWindow.refresh();
        }

        double currentFrameTime = getCurrentTime();
        delta = (float) (currentFrameTime - lastFrameTime);
        lastFrameTime = currentFrameTime;
    }

    private static double getCurrentTime() {
        return GLFW.glfwGetTime();
    }

    public static Window getCurrentWindow() {
        return currentWindow;
    }

    public static double deltaTime() {
        return delta;
    }

}
