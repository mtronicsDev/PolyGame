package com.mtronicsdev.polygame.io;

import java.awt.*;

/**
 * Class description.
 *
 * @author Maxi Schmeller (mtronics_dev)
 */
public final class Colors {

    static {
        Preferences.registerPreferenceHandler(Colors::getColor, Color.class);
    }

    private Colors() {

    }

    /**
     * Converts a string representing a RGB color in HEX format to a {@link java.awt.Color Color}.
     *
     * @param hex The color representation in HEX format, e.g. "#1659DE", "1659de", "ffa", "#0AB"
     * @return The color represented by {@code hex} or, if {@code hex} is invalid,
     * {@link java.awt.Color#BLACK Color.BLACK}
     */
    public static Color getHexColor(String hex) {
        hex = hex.replaceFirst("#", "");
        if (hex.length() == 3) hex = hex.replaceAll("([a-fA-F0-9])", "$1$1");
        if (hex.length() != 6) return Color.BLACK;

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);

        return new Color(r, g, b);
    }

    /**
     * This method converts a string containing a representation of a color to a {@link java.awt.Color color}.
     * <p>
     * <p>It can convert:
     * <ul>
     * <li>RGB (Hex representation), e.g. "#1659de", "FfF", "#dE7", "1760EF"</li>
     * <li>RGB (Comma separated values), e.g. "20, 255, 9", "255,255,255", "19  ,  45, 23"</li>
     * <li>RGBA (Comma separated values), e.g. "20, 255, 9, 19", "34  , 34  ,  43,  127"</li>
     * <li>name (Has to be recognized by {@link java.awt.Color Color}), e.g. "black", "red"</li>
     * </ul>
     * </p>
     *
     * @param anyFormat The representation string of the color in any of the formats given above
     * @return The color that is represented by {@code anyFormat} or,
     * if {@code anyFormat} is invalid or represents the color in a way that is not supported by this method,
     * {@link java.awt.Color#BLACK Color.BLACK}
     */
    public static Color getColor(String anyFormat) {
        if (anyFormat.startsWith("#")) return getHexColor(anyFormat); //RGB (HEX)
        else if (anyFormat.matches("[0-9]{1,3}[ ]*,[ ]*[0-9]{1,3}[ ]*,[ ]*[0-9]{1,3}")) { //RGB
            String[] values = anyFormat.split("[ ]*,[ ]*]");
            return new Color(Integer.parseInt(values[0]),
                    Integer.parseInt(values[1]),
                    Integer.parseInt(values[2]));
        } else if (anyFormat.matches("[0-9]{1,3}[ ]*,[ ]*[0-9]{1,3}[ ]*,[ ]*[0-9]{1,3}[ ]*,[ ]*[0-9]{1,3}")) { //RGBA
            String[] values = anyFormat.split("[ ]*,[ ]*]");
            return new Color(Integer.parseInt(values[0]),
                    Integer.parseInt(values[1]),
                    Integer.parseInt(values[2]),
                    Integer.parseInt(values[3]));
        } else if (anyFormat.length() == 3 || anyFormat.length() == 6 && anyFormat.matches("[a-fA-F0-9]+")) //RGB (HEX)
            return getHexColor(anyFormat);
        else { //Color by name or black
            Color byName = Color.getColor(anyFormat);
            return byName == null ? Color.BLACK : byName;
        }
    }

}
