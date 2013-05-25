/*
 * PlainTileLocator.java
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

/**
 * Calculates tile positions for rectangular maps.
 * 
 * @author Hj. Malthaner
 */
public class PlainTileLocator implements TileLocator
{
    final int dx;
    final int dy;

    public int getRasterX()
    {
        return dx;
    }

    public int getRasterY()
    {
        return dy;
    }

    public int getCursorX()
    {
        return dx;
    }

    public int getCursorY()
    {
        return dy;
    }

    public int getTileScreenX(int x, int y) {
        return x*dx;
    }

    public int getTileScreenY(int x, int y) {
        return y*dy;
    }

    public PlainTileLocator(int dx, int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }
}
