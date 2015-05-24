package com.mtronicsdev.polygame.display;

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

    private static long lastFrameTime;
    private static float delta;
    private static Window currentWindow;

    static {
        windows = new ArrayList<>(1);
        lastFrameTime = getCurrentTime();
    }

    private Display() {

    }

    static void addWindow(Window window) {
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

        long currentFrameTime = getCurrentTime();
        delta = (float) (currentFrameTime - lastFrameTime) / 1000;
        lastFrameTime = currentFrameTime;
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static Window getCurrentWindow() {
        return currentWindow;
    }

    public static float deltaTime() {
        return delta;
    }

}
