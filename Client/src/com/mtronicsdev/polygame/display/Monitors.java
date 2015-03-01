package com.mtronicsdev.polygame.display;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import java.awt.*;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public class Monitors extends JFrame {
    public JPanel contentPane;

    public Monitors() {
        super("Monitors - Polygame");
        setSize(1280, 720);
        setContentPane(contentPane);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void createUIComponents() {
        contentPane = new JPanel(getLayout(), isDoubleBuffered()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                java.util.List<Point> sizes = new ArrayList<>();
                List<Point> positions = new ArrayList<>();

                for (Monitor monitor : Monitor.getMonitors()) {
                    sizes.add(new Point(monitor.getVideoMode().getWidth(), monitor.getVideoMode().getHeight()));
                    IntBuffer x = BufferUtils.createIntBuffer(1);
                    IntBuffer y = BufferUtils.createIntBuffer(1);
                    GLFW.glfwGetMonitorPos(monitor.getId(), x, y);
                    positions.add(new Point(x.get(), y.get()));
                }

                float scale;
                float totalWidth = 0, totalHeight = 0;

                float scaleFactorX;
                float scaleFactorY;

                for (Point size : sizes) {
                    totalWidth += size.x;
                    totalHeight += size.y;
                }

                scaleFactorX = getWidth() / totalWidth;
                scaleFactorY = getHeight() / totalHeight;

                if (scaleFactorX < scaleFactorY) scale = scaleFactorX;
                else scale = scaleFactorY;

                int smallestX = positions.stream().min((p1, p2) -> p1.x - p2.x).get().x;
                int smallestY = positions.stream().min((p1, p2) -> p1.y - p2.y).get().y;

                for (int c = 0; c < sizes.size(); c++) {
                    Color color = Color.DARK_GRAY;

                    for (int i = 0; i < c; i++) {
                        color = color.brighter();
                    }

                    g.setColor(color);

                    int x = (int) ((positions.get(c).x - smallestX) * scale),
                            y = (int) ((positions.get(c).y - smallestY) * scale);

                    g.fillRect(x, y,
                            (int) (sizes.get(c).x * scale),
                            (int) (sizes.get(c).y * scale));

                    char[] name = GLFW.glfwGetMonitorName(Monitor.getMonitors().get(c).getId()).toCharArray();
                    g.setColor(Color.RED);
                    g.drawChars(name, 0, name.length,
                            x - g.getFontMetrics().charsWidth(name, 0, name.length) / 2 + (int) (sizes.get(c).x * scale / 2),
                            y - g.getFontMetrics().getHeight() / 2 + (int) (sizes.get(c).y * scale));
                }

                for (Window window : Display.getWindows()) {
                    int x, y, sizeX, sizeY;
                    Point size = window.getSize();
                    sizeX = size.x;
                    sizeY = size.y;

                    IntBuffer posX = BufferUtils.createIntBuffer(1), posY = BufferUtils.createIntBuffer(1);

                    GLFW.glfwGetWindowPos(window.getId(), posX, posY);

                    x = posX.get();
                    y = posY.get();

                    g.setColor(window.getBackgroundColor());
                    g.fillRect((int) ((x - smallestX) * scale),
                            (int) ((y - smallestY) * scale),
                            (int) (sizeX * scale),
                            (int) (sizeY * scale));
                }
            }
        };
    }
}

