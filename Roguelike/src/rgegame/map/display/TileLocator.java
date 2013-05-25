/*
 * TileLocator.java
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
 * Calculate where to place a tile onscreen based on its
 * map coordinates.
 *
 * @author Hj. Malthaner
 */
public interface TileLocator
{
    public int getRasterX();
    public int getRasterY();

    public int getCursorX();
    public int getCursorY();

    public int getTileScreenX(int x, int y);
    public int getTileScreenY(int x, int y);

}
