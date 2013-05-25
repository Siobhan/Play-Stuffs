/*
 * PathDestination.java
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
 * The pathfinding code will check "destination reached"
 * conditions through this interface.
 * 
 * @author Hj. Malthaner
 */
public interface PathDestination
{
    /**
     * Checks if the destination has been reached.
     *
     * @param posX The x coordinate to check
     * @param posY The y coordinate to check
     *
     * @return true if move is allowed, false otherwise
     */
    public boolean isDestinationReached(int posX, int posY);
}
