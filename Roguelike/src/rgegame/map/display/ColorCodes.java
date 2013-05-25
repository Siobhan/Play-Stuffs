/*
 * ColorCodes.java
 *
 * Created on 2010/07/06
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.map.display;

import java.awt.Color;

/**
 * Color codes to be used with the rendering backends. At the moment
 * only the ASCIITilePainter makes use of these.
 *
 * @see ASCIITilePainter
 * 
 * @author Hj. Malthaner
 */
public class ColorCodes
{
    public final static int WHITE = 0;
    public final static int ORANGE = (1 << 16);
    public final static int YELLOW = (2 << 16);
    public final static int RED = (3 << 16);
    public final static int GREEN = (4 << 16);
    public final static int BLUE = (5 << 16);
    public final static int PINK = (6 << 16);
    public final static int CYAN = (7 << 16);
    public final static int MAGENTA = (8 << 16);
    public final static int GRAY = (9 << 16);
    public final static int LEMONGREEN = (10 << 16);
    public final static int PURPLE = (11 << 16);
    public final static int BROWN = (12 << 16);
    public final static int STEELBLUE = (13 << 16);
    public final static int LIGHTGRAY = (14 << 16);
    public final static int COPPER = (15 << 16);

    private final static Color colors [] = {
        Color.WHITE,
        new Color(245, 140, 10), // Color.ORANGE
        new Color(255, 240, 10), // Color.YELLOW,
        Color.RED,
        Color.GREEN,
        Color.BLUE,
        Color.PINK,
        Color.CYAN,
        Color.MAGENTA,
        Color.GRAY,
        new Color(160, 240, 10),
        new Color(140, 10, 255),
        new Color(180, 120, 60),
        new Color(60, 120, 180),
        Color.LIGHT_GRAY,
        new Color(160, 70, 10),
    };


    /**
     * Set one of the colors to a new color.
     * @param index The index of the color to replace.
     * @param color The new color.
     */
    public static void setColor(int index, Color color)
    {
        colors[index] = color;
    }

    /**
     * Color indices are encoded in the upper 16 bit of the tile
     * numbers.
     * 
     * @param tileNo Combined tile number and color code.
     * @return Color index.
     */
    public final static int getColorIndex(int tileNo)
    {
        return tileNo >>> 16;
    }

    /**
     * Get the Color for the color index encoded in the upper 16 bit
     * of the tile number.
     *
     * @param tileNo Combined tile number and color code.
     * @return Color object for the tile.
     */
    public final static Color getTileColor(int tileNo)
    {
        return colors[getColorIndex(tileNo)];
    }

    /**
     * Parse a configuration code into an color value.
     *
     * 'r' : ColorCodes.RED
     * 'g' : ColorCodes.GREEN
     * 'b' : ColorCodes.BLUE
     * 'o' : ColorCodes.ORANGE
     * 'y' : ColorCodes.YELLOW
     * 'p' : ColorCodes.PURPLE
     * 'i' : ColorCodes.PINK
     * 'l' : ColorCodes.LEMONGREEN
     * 'c' : ColorCodes.CYAN
     * 'm' : ColorCodes.MAGENTA
     * 'd' : ColorCodes.GRAY
     * 'w' : ColorCodes.WHITE
     * 'u' : ColorCodes.BROWN
     * 'e' : ColorCodes.STEELBLUE
     * 's' : ColorCodes.LIGHTGRAY
     * 'R' : ColorCodes.COPPER
     *
     * @param clr The color identifier
     * @return The color code parsed from the identifier, or
     *         white, if the code could not be parsed.
     */
    public static int getColorCode(final char clr)
    {
        int result = WHITE;

        switch(clr) {
            case 'r':
                result = RED;
                break;
            case 'g':
                result = GREEN;
                break;
            case 'b':
                result = BLUE;
                break;
            case 'o':
                result = ORANGE;
                break;
            case 'y':
                result = YELLOW;
                break;
            case 'p':
                result = PURPLE;
                break;
            case 'i':
                result = PINK;
                break;
            case 'l':
                result = LEMONGREEN;
                break;
            case 'c':
                result = CYAN;
                break;
            case 'm':
                result = MAGENTA;
                break;
            case 'd':
                result = GRAY;
                break;
            case 'u':
                result = BROWN;
                break;
            case 'e':
                result = STEELBLUE;
                break;
            case 's':
                result = LIGHTGRAY;
                break;
            case 'R':
                result = COPPER;
                break;
        }
        
        return result;
    }
}
