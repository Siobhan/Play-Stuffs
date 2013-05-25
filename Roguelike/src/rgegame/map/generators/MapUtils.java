/*
 * MapUtils.java
 *
 * Created on 2010/07/25
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
import java.util.ArrayList;
import java.util.Random;

import rgegame.map.data.LayeredMap;

/**
 * Utility methods for map handling.
 * 
 * @author Hj. Malthaner
 */
public class MapUtils
{

    /**
     * Find all open areas of the given size.
     *
     * @param map The map to scan.
     * @param width Min. width to find
     * @param height Min. height to find
     * @param predicate Object to check which map cells count as open.
     * 
     * @return A list of all open areas bigger or equal the given size.
     */
    public ArrayList <Point> findRooms(LayeredMap map, int width, int height,
                                       OpenSquare predicate)
    {
        ArrayList <Point> list = new ArrayList <Point> (128);

        for(int y=0; y<map.getHeight(); y++) {
            for(int x=0; x<map.getWidth(); x++) {
                if(predicate.isOpen(map, x, y)) {
                    boolean old = false;
                    for(Point p : list) {
                        if(isInside(x, y, p.x, p.y, width, height)) {
                            old = true;
                            break;
                        }
                    }

                    // New area found?
                    if(old == false) {
                        boolean ok = true;

                        for(int j=0; j<height && ok; j++) {
                            for(int i=0; i<width && ok; i++) {
                                ok &= predicate.isOpen(map, x+i, y+j);
                            }
                        }

                        if(ok) {
                            list.add(new Point(x,y));
                        }
                    }
                }
            }
        }

        return list;
    }

    /**
     * Checks weather a position is inside a rectangle. Does not allocate
     * any objects.
     *
     * @param posX position x coordinate.
     * @param posY position y coordinate.
     * @param x rectangle top
     * @param y rectangle left
     * @param w rectangle width
     * @param h rectangle height
     * @return true if position is inside, false otherwise.
     */
    public boolean isInside(int posX, int posY, int x, int y, int w, int h)
    {
        return posX >= x && posY >= y && posX < x+w && posY < y+h;
    }

    /**
     * Place a cluster of tiles elements at location x,y.
     * The cluster will spread from x-1,y-1 to x+1,y+1 at most.
     *
     * @param tiles Deco tile array, must have at least 2 elements, better 3
     * @param layer The map layer to affect.
     */
    public void placeDecoCluster(LayeredMap map,
                                 int [] tiles, int x, int y, int layer,
                                 Random rand)
    {
        // System.err.println("Placing cluster at: " + x + ", " + y + " n=" + tiles.length);

        map.set(layer, x, y, tiles[0]);

        if(rand.nextInt(1000) < 700) {
            map.set(layer, x+1, y+0, tiles[1+rand.nextInt(tiles.length-1)]);
        }
        if(rand.nextInt(1000) < 700) {
            map.set(layer, x-1, y+0, tiles[1+rand.nextInt(tiles.length-1)]);
        }
        if(rand.nextInt(1000) < 700) {
            map.set(layer, x+0, y+1, tiles[1+rand.nextInt(tiles.length-1)]);
        }
        if(rand.nextInt(1000) < 700) {
            map.set(layer, x+0, y-1, tiles[1+rand.nextInt(tiles.length-1)]);
        }

        if(rand.nextInt(1000) < 300 && tiles.length > 2) {
            map.set(layer, x+1, y+1, tiles[2+rand.nextInt(tiles.length-2)]);
        }
        if(rand.nextInt(1000) < 300 && tiles.length > 2) {
            map.set(layer, x-1, y+1, tiles[2+rand.nextInt(tiles.length-2)]);
        }
        if(rand.nextInt(1000) < 300 && tiles.length > 2) {
            map.set(layer, x+1, y-1, tiles[2+rand.nextInt(tiles.length-2)]);
        }
        if(rand.nextInt(1000) < 300 && tiles.length > 2) {
            map.set(layer, x-1, y-1, tiles[2+rand.nextInt(tiles.length-2)]);
        }
    }


    /**
     * Must be subclassed in a game specific manner to tell which sqaures
     * are considered open.
     */
    public interface OpenSquare
    {
        /**
         * Should return true for all squares that are to be considered
         * open by the calling code.
         *
         * @param map The map to check.
         * @param posX Position x coordinate
         * @param posY Position y coordinate
         * 
         * @return True if the cell is considered open, false otherwise.
         */
        public boolean isOpen(LayeredMap map, int posX, int posY);
    }
}
