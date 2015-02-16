package com.mtronicsdev.polygame.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Input {

    private static GLFWCursorPosCallback cursorPosCallback;
    private static GLFWScrollCallback scrollCallback;
    private static GLFWKeyCallback keyCallback;
    private static GLFWMouseButtonCallback mouseButtonCallback;

    private static double mouseX, mouseY;
    private static double scrollX, scrollY;

    static {
        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long l, double v, double v1) {
                mouseX = v;
                mouseY = v1;
            }
        };

        scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long l, double v, double v1) {
                scrollX = v;
                scrollY = v1;
            }
        };

        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long l, int i, int i1, int i2, int i3) {

            }
        };
    }

    private Input() {

    }
}
