package com.mtronicsdev.polygame.gui;

import com.mtronicsdev.polygame.graphics.Texture;
import com.mtronicsdev.polygame.util.math.Vector4f;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public class GuiButton extends GuiPanel {
    public GuiButton(Dimension4f padding, Dimension4f margin, Dimension2f size, Dimension2f offset, Vector4f color, GuiEngine.Alignment alignment, Texture texture, GuiText text) {
        super(padding, margin, size, offset, color, alignment, texture);

        text.setParent(this);
    }
}
