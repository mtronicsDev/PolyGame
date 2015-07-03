package com.mtronicsdev.polygame.gui;

import com.mtronicsdev.polygame.io.Preferences;
import com.mtronicsdev.polygame.util.math.Vector2f;
import com.mtronicsdev.polygame.util.math.Vector4f;

import java.util.LinkedList;
import java.util.List;

/**
 * @author mtronicsDev
 * @version 1.0
 */
public final class GuiEngine {

    private static final AbstractGuiPanel ROOT;

    static {
        Preferences.registerPreferenceHandler(value -> {
            String[] dimensions = value.split("[ ]*,[ ]*");

            if (dimensions.length != 2) throw new IllegalArgumentException("Wrong number of dimensions. Is: " +
                    dimensions.length + ", should be: 2");

            float x = 0, y = 0;
            boolean rX = false, rY = false;

            for (int i = 0; i < 2; i++) {
                boolean relative = dimensions[i].endsWith("%");

                float v = Float.parseFloat(dimensions[i].substring(0, dimensions.length - (relative ? 2 : 3)));

                if (i == 0) {
                    x = relative ? v / 100 : v;
                    rX = relative;
                } else {
                    y = relative ? v / 100 : v;
                    rY = relative;
                }
            }

            return new Dimension2f(x, y, rX, rY);
        }, Dimension2f.class);

        Preferences.registerPreferenceHandler(value -> {
            String[] dimensions = value.split("[ ]*,[ ]*");

            if (dimensions.length != 4) throw new IllegalArgumentException("Wrong number of dimensions. Is: " +
                    dimensions.length + ", should be: 4");

            float x = 0, y = 0, z = 0, w = 0;
            boolean rX = false, rY = false, rZ = false, rW = false;

            for (int i = 0; i < 4; i++) {
                boolean relative = dimensions[i].endsWith("%");

                float v = Float.parseFloat(dimensions[i].substring(0, dimensions.length - (relative ? 2 : 3)));

                if (i == 0) {
                    x = relative ? v / 100 : v;
                    rX = relative;
                } else if (i == 1) {
                    y = relative ? v / 100 : v;
                    rY = relative;
                } else if (i == 2) {
                    z = relative ? v / 100 : v;
                    rZ = relative;
                } else {
                    w = relative ? v / 100 : v;
                    rW = relative;
                }
            }

            return new Dimension4f(x, y, z, w, rX, rY, rZ, rW);
        }, Dimension4f.class);

        ROOT = new AbstractGuiPanel(Preferences.getPreference("guiEngine.defaultPadding", Dimension4f.class));
    }

    private GuiEngine() {
    }

    public static void updateLayout() {
        ROOT.children.forEach(GuiPanel::updateLayout);
    }

    public static List<GuiPanel> getRenderList() {
        return ROOT.getRenderList(new LinkedList<>());
    }

    public static AbstractGuiPanel getRoot() {
        return ROOT;
    }

    public enum Alignment {
        TOP_LEFT((pP, pS, m, s) -> new Vector2f(pP.x + m.x + s.x,
                pS.y - m.w - s.y - pP.w)),
        TOP((pP, pS, m, s) -> new Vector2f((pS.x - pP.x - pP.z) / 2 + m.x / 2 - m.z / 2 + pP.x,
                pS.y - m.w - s.y - pP.w)),
        TOP_RIGHT((pP, pS, m, s) -> new Vector2f(pS.x - m.z - s.x - pP.z,
                pS.y - m.w - s.y - pP.w)),
        LEFT((pP, pS, m, s) -> new Vector2f(pP.x + m.x + s.x,
                (pS.y - pP.y - pP.w) / 2 + m.y / 2 - m.w / 2 + pP.y)),
        CENTER((pP, pS, m, s) -> new Vector2f((pS.x - pP.x - pP.z) / 2 + m.x / 2 - m.z / 2 + pP.x,
                (pS.y - pP.y - pP.w) / 2 + m.y / 2 - m.w / 2 + pP.y)),
        RIGHT((pP, pS, m, s) -> new Vector2f(pS.x - m.z - s.x - pP.z,
                (pS.y - pP.y - pP.w) / 2 + m.y / 2 - m.w / 2 + pP.y)),
        BOTTOM_LEFT((pP, pS, m, s) -> new Vector2f(pP.x + m.x + s.x,
                pP.y + m.y + s.y)),
        BOTTOM((pP, pS, m, s) -> new Vector2f((pS.x - pP.x - pP.z) / 2 + m.x / 2 - m.z / 2 + pP.x,
                pP.y + m.y + s.y)),
        BOTTOM_RIGHT((pP, pS, m, s) -> new Vector2f(pS.x - m.z - s.x - pP.z,
                pP.y + m.y + s.y)),
        FREE((pP, pS, m, s) -> new Vector2f());

        private final AlignmentHandler handler;

        Alignment(AlignmentHandler handler) {
            this.handler = handler;
        }

        public Vector2f getPosition(Vector4f parentPadding, Vector2f parentSize, Vector4f margin, Vector2f size) {
            return handler.getPosition(parentPadding, parentSize, margin, size);
        }
    }

    @FunctionalInterface
    private interface AlignmentHandler {
        Vector2f getPosition(Vector4f parentPadding, Vector2f parentSize, Vector4f margin, Vector2f size);
    }
}