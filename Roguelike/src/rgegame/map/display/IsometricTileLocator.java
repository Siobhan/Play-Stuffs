/*
 * IsometricTileLocator.java
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
 * Calculates tile positions for isometric map layout.
 * 
 * @author Hj. Malthaner
 */
public class IsometricTileLocator implements TileLocator
{
    private final int dx;
    private final int dy;

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
        return dx*2;
    }

    public int getCursorY()
    {
        return dy*2;
    }

    public int getTileScreenX(final int x, final int y) {
        return x*dx - y*dx;
    }

    public int getTileScreenY(final int x, final int y) {
        return x*dy + y*dy;
    }

    public IsometricTileLocator(final int dx, final int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }
}