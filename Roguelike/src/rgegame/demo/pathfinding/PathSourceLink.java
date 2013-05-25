/*
 * PathSourceLink.java
 *
 * Created on 2010/07/21
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.demo.pathfinding;

import rgegame.demo.walkaround.FeatureCodes;
import rgegame.map.data.LayeredMap;
import rgegame.pathfinding.PathSource;

/**
 * Pathfinding will access map data through this class.
 *
 * @author Hj. Malthaner
 */
public class PathSourceLink implements PathSource
{
    private final LayeredMap map;


    /**
     * Checks if player is allowed to move into map cell (toX, toY)
     */
    public boolean isMoveAllowed(int fromX, int fromY, int toX, int toY) {
        if(toX >= 0 && toY >= 0 &&
           toX < map.getWidth() && toY < map.getHeight()) {

            final int code = map.get(1, toX, toY);
            final int feature = FeatureCodes.getFeature(code);
            return 
                    feature == FeatureCodes.OPEN ||
                    feature == FeatureCodes.DOOR_OPEN ||
                    feature == FeatureCodes.DOOR_SHUT;
        }

        return false;
    }

    public PathSourceLink(LayeredMap map)
    {
        this.map = map;
    }
}
