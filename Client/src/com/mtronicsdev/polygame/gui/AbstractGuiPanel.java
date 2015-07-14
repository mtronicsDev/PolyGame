package com.mtronicsdev.polygame.gui;

import com.mtronicsdev.polygame.display.Display;
import com.mtronicsdev.polygame.util.math.Vector2f;
import com.mtronicsdev.polygame.util.math.Vector4f;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maxi on 21.06.2015.
 */
public class AbstractGuiPanel {

    protected List<GuiPanel> children;

    protected Dimension4f padding;

    protected AbstractGuiPanel(Dimension4f padding) {
        this.padding = padding;

        children = new ArrayList<>();
    }

    public List<GuiPanel> getRenderList(LinkedList<GuiPanel> objects) {
        children.forEach(c -> objects.addAll(c.getRenderList(objects)));
        return objects;
    }

    protected final void addChild(GuiPanel guiPanel) {
        children.add(guiPanel);
    }

    protected final void removeChild(GuiPanel guiPanel) {
        children.remove(guiPanel);
    }

    public Vector2f getPosition() {
        return new Vector2f(0, 0);
    }

    public Vector2f getSize() {
        return new Vector2f(1, 1);
    }

    public Vector4f getPadding() {
        Point size = Display.getCurrentWindow().getSize();
        return padding.toVector4f(size.x, size.y);
    }

    public Vector2f getPadding(boolean firstHalf) {
        Point size = Display.getCurrentWindow().getSize();
        return padding.toVector2f(size.x, size.y, firstHalf);
    }
}
