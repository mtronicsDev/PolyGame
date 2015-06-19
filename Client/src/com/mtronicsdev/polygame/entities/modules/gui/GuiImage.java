package com.mtronicsdev.polygame.entities.modules.gui;

import com.mtronicsdev.polygame.graphics.GuiObject;
import com.mtronicsdev.polygame.util.math.Vector2f;

import java.util.List;

/**
 * @author mtronics_dev
 * @version 1.0
 */
public class GuiImage extends GuiPanel {

    private GuiObject image;

    public GuiImage(GuiObject image, boolean active, Alignment alignment) {
        super(active, alignment);
        this.image = image;
    }

    public GuiImage(GuiObject image, boolean active) {
        this(image, active, Alignment.TOP_LEFT);
    }

    public GuiImage(GuiObject image) {
        this(image, true);
    }

    @Override
    protected void updateLayout(Vector2f parentPosition, Vector2f parentSize) {
        Vector2f imageSize = image.getSize();

        Vector2f position = alignment.getPosition(parentPosition, parentSize, imageSize, margin);
        position.add(offset);

        image.setPosition(position);
        children.forEach(c -> c.updateLayout(image.getPosition(), imageSize));
    }

    @Override
    protected List<GuiObject> getRenderList(List<GuiObject> renderList) {
        if (!active) return renderList;
        else {
            renderList.add(image);
            for (GuiPanel child : children) {
                child.getRenderList(renderList);
            }

            return renderList;
        }
    }
}
