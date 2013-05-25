/*
 * MapGenerator.java
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

import rgegame.map.data.LayeredMap;


/**
 * All map generators must implement at least these methods.
 * 
 * @author Hj. Malthaner
 */
public interface MapGenerator
{   
    /**
     * Generate a new map of width x height squares. Some generators may
     * not be able to create exact sizes.
     *
     * @param width desired map width
     * @param height desired map height
     * @return the generated map model or null if generation failed.
     */
    public LayeredMap generate(int width, int height);
    
}
