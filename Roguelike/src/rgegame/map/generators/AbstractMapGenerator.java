/*
 * AbstractMapGenerator.java
 *
 * Created on 2010/07/26
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.map.generators;

import java.awt.Point;
import java.util.Properties;
import java.util.Random;

import rgegame.map.data.LayeredMap;
import rgegame.map.display.ColorCodes;

/**
 * Base class for map generators, containing mostly configuration
 * and helper routines which many generators need.
 * 
 * @author Hj. Malthaner
 */
public abstract class AbstractMapGenerator implements MapGenerator
{
    /**
     * Get an array of int values.
     */
    protected int [] getIntegers(final Properties props, 
                                 final String key,
                                 final int def)
    {
        final String line = props.getProperty(key);

        if(line == null) {
            return new int [0];
        }

        final String [] elements = line.split(", ");
        final int [] result = new int[elements.length];

        for(int i=0; i<result.length; i++) {
            result[i] = getInt(elements[i].trim(), def);
        }

        return result;
    }

    /**
     * Parse a configuration code into an int value. The configuration code
     * can be number or a character + color pair, denoted like #r or +r with the
     * character code being the first  character, and the color code being the second.
     *
     * @param props The config file values.
     * @param key The key to look up
     * @param def The default avlue to return if the key was not found.
     * @return The integer value parsed from the config data
     *
     * @see ColorCodes
     */
    protected int getInt(final Properties props,
                       final String key,
                       final int def)
    {
        final String value = props.getProperty(key);
        return getInt(value, def);
    }

    /**
     * Parse a configuration code into an int value. The configuration code
     * can be number or a character + color pair, denoted like #r or +r with the
     * character code being the first  character, and the color code being the second.
     *
     * @param value The config file value.
     * @param def The default avlue to return if the key was not found.
     * @return The integer value parsed from the config data
     *
     * @see ColorCodes
     */
    protected int getInt(final String value, final int def)
    {
        int result = def;

        if(value != null) {
            // Hajo: check format
            if(Character.isDigit(value.charAt(0)) ||
               value.charAt(0) == '-') {
                // Hajo: should be a number
                result = Integer.parseInt(value);
            } else {
                // We try to parse a charcater + color pair
                char chr = value.charAt(0);
                char clr = value.charAt(1);

                // Hajo: parse Angband style a:r codes.
                if(clr == ':') {
                    clr = value.charAt(2);
                }

                result = chr;
                result += ColorCodes.getColorCode(clr);
            }
        }

        return result;
    }

    /**
     * Return a Random either seeded with seed value from properties,
     * or if no seed given in properties, seeded with the current time.
     *
     * @param props Properties to read from.
     * @return Seeded Random.
     */
    Random getSeededRandom(Properties props)
    {
        final long seed = Long.parseLong(props.getProperty("seed","" + System.currentTimeMillis()));
//        final long seed = Long.parseLong(props.getProperty("seed","2"));
        
        System.out.println(seed);

        return new Random(seed);
    }

    /**
     * Fill an area of a map with values.
     * 
     * @param map The map to work on.
     * @param x Left of the area.
     * @param y Top of the area.
     * @param w Width of the area.
     * @param h Height of the area.
     * @param values Array of tile numbers. Actual values will be chosen
     *               randomly from this.
     */
    void fillArea(final LayeredMap map,
                  int layer, int x, int y, int w, int h,
                  final int [] values)
    {
        for(int j=0; j<h; j++) {
            for(int i=0; i<w; i++) {
                map.set(layer, x+i, y+j,
                        values[Randomlib.linearRange(0, values.length-1)]);
            }
        }        
    }


    /**
     * Rotate the vector 90 degrees left
     */
    void rot90l(final Point p)
    {
        final int x = p.y;
        final int y = -p.x;
        p.x = x;
        p.y = y;
    }

    /**
     * Rotate the vector 90 degrees right
     */
    void rot90r(final Point p)
    {
        final int x = -p.y;
        final int y = p.x;
        p.x = x;
        p.y = y;
    }
}
