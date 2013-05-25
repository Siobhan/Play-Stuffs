/*
 * CoordinatePathDestination.java
 *
 * Created on 18. Juli 2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.pathfinding;

/**
 * Tell pathfinder if the destination coordinate has been reached.
 * 
 * @author Hj. Malthaner
 */
public class CoordinatePathDestination implements PathDestination
{
    private final int x, y;

    /**
     * Set the destination coordinate to be checked.
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public CoordinatePathDestination(int x, int y)
    {
        this.x = x;
        this.y = y;
    }


    /**
     * Tell pathfinder if the destination coordinate has been reached.
     *
     * @param posX The x coordinate to check
     * @param posY The y coordinate to check
     *
     * @return true if destination has been reached, false otherwise.
     */
    public boolean isDestinationReached(final int posX, final int posY)
    {
        return x == posX && y == posY;
    }
}