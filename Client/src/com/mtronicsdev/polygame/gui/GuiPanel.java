package com.mtronicsdev.polygame.gui;

import com.mtronicsdev.polygame.display.Display;
import com.mtronicsdev.polygame.graphics.Texture;
import com.mtronicsdev.polygame.util.math.Vector2f;
import com.mtronicsdev.polygame.util.math.Vector4f;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public class GuiPanel extends AbstractGuiPanel {

    private AbstractGuiPanel parent;

    private Dimension4f margin;
    private Dimension2f size;
    private Dimension2f offset;
    private Vector4f color;

    private GuiEngine.Alignment alignment;

    private Vector2f currentPosition, currentSize;
    private Texture texture;

    public GuiPanel(Dimension4f padding, Dimension4f margin, Dimension2f size, Dimension2f offset,
                    Vector4f color, GuiEngine.Alignment alignment, Texture texture) {
        super(padding);

        this.margin = margin;
        this.size = size;
        this.offset = offset;
        this.color = color;
        this.alignment = alignment;
        this.texture = texture;

        setParent(GuiEngine.getRoot());
    }

    @Override
    public final List<GuiPanel> getRenderList(LinkedList<GuiPanel> objects) {
        objects.add(this);
        children.forEach(c -> objects.addAll(c.getRenderList(objects)));
        return objects;
    }

    public final AbstractGuiPanel getParent() {
        return parent;
    }

    public final void setParent(AbstractGuiPanel parent) {
        if (parent == null || parent.equals(this.parent)) return;
        this.parent = parent;
        this.parent.removeChild(this);
        parent.addChild(this);
    }

    public void updateLayout() {
        Vector4f parentPadding = parent.getPadding();
        Vector2f parentSize = parent.getSize();
        Vector4f margin = getMargin();
        Vector2f size = getSize();
        Vector2f offset = getOffset();

        currentPosition = alignment.getPosition(parentPadding, parentSize, margin, size);
        currentPosition.add(parent.getPosition());

        currentSize = new Vector2f(size.x + margin.z + parentPadding.z > parentSize.x - offset.x ?
                parentSize.x - offset.x - parentPadding.z - margin.z : size.x,
                size.y + margin.w + parentPadding.w > parentSize.y - offset.y ?
                        parentSize.y - offset.y - parentPadding.w - margin.w : size.y);
    }

    @Override
    public Vector2f getPosition() {
        return currentPosition;
    }

    public Vector2f getCurrentSize() {
        return currentSize;
    }

    public Vector4f getMargin() {
        Point size = Display.getCurrentWindow().getSize();
        return margin.toVector4f(size.x, size.y);
    }

    public Vector2f getMargin(boolean firstHalf) {
        Point size = Display.getCurrentWindow().getSize();
        return margin.toVector2f(size.x, size.y, firstHalf);
    }

    @Override
    public Vector2f getSize() {
        Point size = Display.getCurrentWindow().getSize();
        return this.size.toVector2f(size.x, size.y);
    }

    public Vector2f getOffset() {
        Point size = Display.getCurrentWindow().getSize();
        return offset.toVector2f(size.x, size.y);
    }

    public Texture getTexture() {
        return texture;
    }
}
