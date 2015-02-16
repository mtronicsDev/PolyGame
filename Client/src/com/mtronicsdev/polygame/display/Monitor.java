package com.mtronicsdev.polygame.display;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFWvidmode;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Monitor {

    private long id;

    private Monitor(long id) {
        this.id = id;
    }

    public static Monitor getPrimary() {
        return new Monitor(glfwGetPrimaryMonitor());
    }

    public static List<Monitor> getMonitors() {
        PointerBuffer buffer = glfwGetMonitors();
        List<Monitor> monitors = new ArrayList<>();

        while (buffer.hasRemaining()) monitors.add(new Monitor(buffer.get()));

        return monitors;
    }

    public VideoMode getVideoMode() {
        ByteBuffer buffer = glfwGetVideoMode(id);
        return new VideoMode(
                GLFWvidmode.width(buffer),
                GLFWvidmode.height(buffer),
                GLFWvidmode.redBits(buffer),
                GLFWvidmode.greenBits(buffer),
                GLFWvidmode.blueBits(buffer),
                GLFWvidmode.refreshRate(buffer));
    }

    public List<VideoMode> getVideoModes() {
        ByteBuffer count = ByteBuffer.allocate(1);
        ByteBuffer modes = glfwGetVideoModes(id, count);

        List<VideoMode> videoModes = new ArrayList<>(count.get(0));

        for (int i = 0; i < count.get(0); i++) {
            modes.position(i * GLFWvidmode.SIZEOF);

            videoModes.add(new VideoMode(
                    GLFWvidmode.width(modes),
                    GLFWvidmode.height(modes),
                    GLFWvidmode.redBits(modes),
                    GLFWvidmode.greenBits(modes),
                    GLFWvidmode.blueBits(modes),
                    GLFWvidmode.refreshRate(modes)));
        }

        return videoModes;
    }

    public long getId() {
        return id;
    }

    class VideoMode {

        private final int width, height;
        private final int redBits, greenBits, blueBits;
        private final int refreshRate;

        private VideoMode(int width, int height, int redBits, int greenBits, int blueBits, int refreshRate) {
            this.width = width;
            this.height = height;
            this.redBits = redBits;
            this.greenBits = greenBits;
            this.blueBits = blueBits;
            this.refreshRate = refreshRate;
        }

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public int getRedBits() {
            return redBits;
        }

        public int getGreenBits() {
            return greenBits;
        }

        public int getBlueBits() {
            return blueBits;
        }

        public int getRefreshRate() {
            return refreshRate;
        }
    }
}
