/*
 * FeatureCodes.java
 *
 * Created on 10.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.demo.walkaround;

/**
 * Constants for dungeon features.
 *
 * @author Hj. Malthaner
 */
public class FeatureCodes
{
    /** Strictly seen, this is not a feature ... */
    public static final int GROUND = '.';

    public static final int OPEN = 0;

    public static final int WALL_BRICKS = '#';
    public static final int WALL_ROCK = '%';

    public static final int STAIRS_UP = '<';
    public static final int STAIRS_DOWN = '>';
    public static final int DOOR_SHUT = '+';
    public static final int DOOR_OPEN = '`';
    
    public static final int FOUNTAIN = '~';
    public static final int TREE = '&';
    public static final int ENEMY = 'e';

    /**
     * Extract feature number for a color+feature code
     * @param tileNo the code containing color + feature;
     * @return The feature number.
     */
    public final static int getFeature(final int tileNo)
    {
        return tileNo & 0xFFFF;
    }

}
