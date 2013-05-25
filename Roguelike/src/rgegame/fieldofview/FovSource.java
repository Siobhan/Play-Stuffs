/*
 * FovSource.java
 *
 * Created on 07.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.fieldofview;

/**
 * Linking interface between the FOV calculation and the data (map)
 * source. Contains only one method to check weather a given location
 * blocks the line of sight or not.
 * 
 * @author Hj. Malthaner
 */
public interface FovSource
{

    /**
     * Check weather a given location
     * blocks the line of sight or not.
     * 
     * @param posX X coordinate
     * @param posY Y coordinate
     * 
     * @return true if blocking, false otherwise
     */
    public boolean isBlockingLOS(int posX, int posY);

}
