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
        Vector2f size = calculateSize();
        Vector2f offset = calculateOffset();

        currentPosition = alignment.getPosition(parentPadding, parentSize, margin, size);
        currentPosition.add(parent.getPosition());
        currentPosition.add(offset);

        currentSize = new Vector2f(size.x + margin.z + parentPadding.z > parentSize.x - offset.x ?
                parentSize.x - offset.x - parentPadding.z - margin.z : size.x,
                size.y + margin.w + parentPadding.w > parentSize.y - offset.y ?
                        parentSize.y - offset.y - parentPadding.w - margin.w : size.y);
    }

    @Override
    public Vector2f getPosition() {
        return currentPosition;
    }

    @Override
    public Vector2f getSize() {
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

    public void setMargin(Dimension4f margin) {
        this.margin = margin;
        updateLayout();
    }

    public Vector2f getMargin(boolean firstHalf) {
        Point size = Display.getCurrentWindow().getSize();
        return margin.toVector2f(size.x, size.y, firstHalf);
    }

    public Vector2f calculateSize() {
        Point size = Display.getCurrentWindow().getSize();
        return this.size.toVector2f(size.x, size.y);
    }

    public void setSize(Dimension2f size) {
        this.size = size;
        updateLayout();
    }

    public Vector2f calculateOffset() {
        Point size = Display.getCurrentWindow().getSize();
        return offset.toVector2f(size.x, size.y);
    }

    public void setOffset(Dimension2f offset) {
        this.offset = offset;
        updateLayout();
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }
}
