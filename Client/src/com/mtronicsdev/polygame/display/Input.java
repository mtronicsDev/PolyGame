package com.mtronicsdev.polygame.display;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Input {

    private static GLFWKeyCallback keyCallback;
    private static GLFWMouseButtonCallback buttonCallback;
    private static GLFWCursorPosCallback cursorPosCallback;
    private static GLFWScrollCallback scrollCallback;
    private static Map<Integer, InputHandler> keyHandlers;
    private static Map<Integer, InputHandler> buttonHandlers;

    static {
        keyHandlers = new HashMap<>();
        buttonHandlers = new HashMap<>(2);
    }

    private Input() {

    }

    static void update(Window window) {
        for (int key : keyHandlers.keySet()) {
            switch (glfwGetKey(window.getId(), key)) {
                case GLFW_PRESS:
                    keyHandlers.get(key).down();
                    break;
                case GLFW_RELEASE:
                    keyHandlers.get(key).up();
                    break;
            }
        }

        for (int button : buttonHandlers.keySet()) {
            switch (glfwGetMouseButton(window.getId(), button)) {
                case GLFW_PRESS:
                    buttonHandlers.get(button).down();
                    break;
                case GLFW_RELEASE:
                    buttonHandlers.get(button).up();
                    break;
            }
        }
    }

    public static void registerKeyHandler(int keycode) {
        keyHandlers.put(keycode, new InputHandler());
    }

    public static void registerMouseButtonHandler(int button) {
        buttonHandlers.put(button, new InputHandler());
    }

    public static boolean keyDown(int keycode) {
        InputHandler handler = keyHandlers.get(keycode);

        try {
            return handler.keyDown();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Keys have to be assigned before being queried!");
        }
    }

    public static boolean keyUp(int keycode) {
        InputHandler handler = keyHandlers.get(keycode);

        try {
            return handler.keyUp();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Keys have to be assigned before being queried!");
        }
    }

    public static boolean keyPressed(int keycode) {
        InputHandler handler = keyHandlers.get(keycode);

        try {
            return handler.keyPressed();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Keys have to be assigned before being queried!");
        }
    }

    public static boolean buttonDown(int button) {
        InputHandler handler = buttonHandlers.get(button);

        try {
            return handler.keyDown();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Keys have to be assigned before being queried!");
        }
    }

    public static boolean buttonUp(int button) {
        InputHandler handler = buttonHandlers.get(button);

        try {
            return handler.keyUp();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Keys have to be assigned before being queried!");
        }
    }

    public static boolean buttonPressed(int button) {
        InputHandler handler = buttonHandlers.get(button);

        try {
            return handler.keyPressed();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Keys have to be assigned before being queried!");
        }
    }

    private static final class InputHandler {
        private boolean wasDown, isDown;

        private void down() {
            wasDown = isDown;
            isDown = true;
        }

        private void up() {
            wasDown = isDown;
            isDown = false;
        }

        private boolean keyDown() {
            return !wasDown && isDown;
        }

        private boolean keyPressed() {
            return wasDown && isDown;
        }

        private boolean keyUp() {
            return wasDown && !isDown;
        }
    }
}
