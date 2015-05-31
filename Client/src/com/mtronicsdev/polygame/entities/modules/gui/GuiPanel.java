package com.mtronicsdev.polygame.entities.modules.gui;

import com.mtronicsdev.polygame.graphics.GuiObject;
import com.mtronicsdev.polygame.util.math.Vector2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class GuiPanel {

    private static final GuiPanel ROOT = new GuiPanel();
    protected boolean active;
    protected List<GuiPanel> children;
    protected Alignment alignment;
    protected float margin = 0.003f;

    public GuiPanel(boolean active, Alignment alignment, GuiPanel... children) {
        this.active = active;
        this.alignment = alignment;
        this.children = new ArrayList<>(Arrays.asList(children));
    }

    public GuiPanel(GuiPanel... children) {
        this(true, Alignment.TOP_LEFT, children);
    }

    public GuiPanel(boolean active, Alignment alignment) {
        this.active = active;
        this.alignment = alignment;
        children = new ArrayList<>();
    }

    public GuiPanel(boolean active) {
        this(active, Alignment.TOP_LEFT);
    }

    public GuiPanel() {
        this(true);
    }

    public static void updateLayout() {
        ROOT.children.forEach(c -> c.updateLayout(new Vector2f(-1, -1), new Vector2f(2, 2)));
    }

    public static GuiPanel getRoot() {
        return ROOT;
    }

    protected void updateLayout(Vector2f parentPosition, Vector2f parentSize) {
        children.forEach(c -> c.updateLayout(new Vector2f(parentPosition.x + margin, parentPosition.y + margin),
                new Vector2f(parentSize.x - margin, parentSize.y - margin)));
    }

    public final List<GuiObject> getRenderList() {
        List<GuiObject> renderList = new LinkedList<>();
        return getRenderList(renderList);
    }

    protected List<GuiObject> getRenderList(List<GuiObject> renderList) {
        if (!active) return renderList;
        else {
            for (GuiPanel child : children) {
                child.getRenderList(renderList);
            }

            return renderList;
        }
    }

    public final void addChild(GuiPanel panel) {
        children.add(panel);
    }

    public final void removeChild(GuiPanel panel) {
        children.remove(panel);
    }

    public enum Alignment {
        TOP_LEFT((p, ps, s, m) -> new Vector2f(p.x + s.x + m, p.y + ps.y - s.y - m)),
        TOP((p, ps, s, m) -> new Vector2f(p.x + ps.x / 2, p.y + ps.y - s.y - m)),
        TOP_RIGHT((p, ps, s, m) -> new Vector2f(p.x + ps.x - s.x - m, p.y + ps.y - s.y - m)),
        LEFT((p, ps, s, m) -> new Vector2f(p.x + s.x + m, p.y + ps.y / 2)),
        CENTER((p, ps, s, m) -> new Vector2f(p.x + ps.x / 2, p.y + ps.y / 2)),
        RIGHT((p, ps, s, m) -> new Vector2f(p.x + ps.x - s.x - m, p.y + ps.y / 2)),
        BOTTOM_LEFT((p, ps, s, m) -> new Vector2f(p.x + s.x + m, p.y + s.y + m)),
        BOTTOM((p, ps, s, m) -> new Vector2f(p.x + ps.x / 2, p.y + s.y + m)),
        BOTTOM_RIGHT((p, ps, s, m) -> new Vector2f(p.x + ps.x - s.x - m, p.y + s.y + m));

        private final AlignmentHandler handler;

        Alignment(AlignmentHandler handler) {
            this.handler = handler;
        }

        public Vector2f getPosition(Vector2f parentPosition, Vector2f parentSize, Vector2f size, float margin) {
            return handler.getPosition(parentPosition, parentSize, size, margin);
        }
    }

    @FunctionalInterface
    private interface AlignmentHandler {
        Vector2f getPosition(Vector2f parentPosition, Vector2f parentSize, Vector2f size, float margin);
    }
}
