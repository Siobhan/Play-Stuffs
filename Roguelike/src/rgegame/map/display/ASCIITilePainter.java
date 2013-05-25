/*
 * ASCIITilePainter.java
 *
 * Created on 06.07.2010
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
import java.awt.Font;
import java.awt.Graphics;

/**
 * Paint an ASCII character for a map cell at a specified screen location.
 *
 * @author Hj. Malthaner
 */
public class ASCIITilePainter implements TilePainter
{
    /** Paint a cell in "remembered" style */
    public static final int VIEW_REMEMBERED = 1;

    /** Paint a cell in full detail */
    public static final int VIEW_VISIBLE = 2;

    private final char [] myChar;
    private Font font;

    private int viewWidth;
    private int viewHeight;
    private int [] viewMap;

    /**
     * Set up field of view data
     *
     * @param viewMap Which map cells are visible?
     * @param viewWidth Width of view array.
     * @param viewHeight height of view array.
     */
    public void setViewMap(int [] viewMap,
                           int viewWidth,
                           int viewHeight)
    {
        this.viewMap = viewMap;
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    /**
     * Set the font to be used for drawing.
     * @param font The font to use.
     */
    public void setFont(Font font)
    {
        this.font = font;
    }

    /**
     * Check weather cell (x,y) is visible
     *
     * @return true if visible, false otherwise
     */
    private boolean isVisible(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < viewWidth && y < viewHeight) {
            return (viewMap[y*viewWidth + x] & VIEW_VISIBLE) != 0;
        }
        return false;
    }

    /**
     * Check weather cell (x,y) is remembered
     *
     * @return true if remembered, false otherwise
     */
    private boolean isRemembered(int x, int y)
    {
        if(x >= 0 && y >= 0 && x < viewWidth && y < viewHeight) {
            return (viewMap[y*viewWidth + x] & VIEW_REMEMBERED) != 0;
        }
        return false;
    }

    public ASCIITilePainter()
    {
        myChar = new char [1];
        font = new Font(Font.MONOSPACED, Font.BOLD, 18);
    }

    public void paint(final Graphics gr,
                      final int layer,
                      final int mapX,
                      final int mapY,
                      final int xpos,
                      final int ypos,
                      final int tileNo)
    {
        gr.setFont(font);

        if(isRemembered(mapX, mapY)) {
            if(isVisible(mapX, mapY)) {
                final Color color = ColorCodes.getTileColor(tileNo);
                gr.setColor(color);
            } else {
                gr.setColor(Color.DARK_GRAY);
            }

            myChar[0] = (char)tileNo;

            gr.drawChars(myChar, 0, 1, xpos, ypos);
        }
    }
}
