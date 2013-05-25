/*
 * FovDestination.java
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
 * Linking interface between the FOV calculation and the map display
 * code. Contains only one method to set weather a given location
 * can be seen or not.
 *
 * @author Hj. Malthaner
 */
public interface FovDestination
{
    /**
     * FOV code will call this for each location that can be seen.
     */
    public void setCanBeSeen(int posX, int posY);
}
