/*
 * PathSource.java
 *
 * Created on 14. Juli 2010
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
 * The pathfinding code will access maps through this interface.
 * 
 * @author Hj. Malthaner
 */
public interface PathSource
{
    /**
     * Checks if a move is allowed.
     * 
     * @return true if move is allowed, false otherwise
     */
    public boolean isMoveAllowed(int fromX, int fromY,
            int toX, int toY);
}
