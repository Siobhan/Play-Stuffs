/*
 * Randomlib.java
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

package rgegame.map.generators;

/**
 * Helper library for customized random calculations.
 *
 * @author Hj. Malthaner
 */
public class Randomlib {
    
    /**
     * Returns an integer from the range [low, high]
     */
    public static int linearRange(final int low, final int high) 
    {
        return low + (int)((high - low  + 1) * Math.random());
    }
    
    
    /**
     * Gets a random element out of a weighted list.
     * @return index of chosen interval.
     */
    public static int oneOfWeightedList(final int [] weights)
    {
        int sum = 0;
        
        for(int i=0; i<weights.length; i++) {
            sum += weights[i];
        }

        final int weight = (int)(sum*Math.random());
    
        sum = 0;
        
        for(int i=0; i<weights.length; i++) {
            final int high = sum + weights[i];
            if(sum <= weight && weight < high) {
                return i;
            }
            sum = high;
        }
        
        // Hajo: empty lists or crippled weights
        return -1;
    }   
}
