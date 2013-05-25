/*
 * MinesGenerator.java
 *
 * Created on 2010/07/27
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

/**
 * This will create 'mines' type maps, that is, mazes of
 * straight corridors.
 * 
 * @author Hj. Malthaner
 */
public class MinesGenerator extends AbstractMapGenerator
{
    private Properties props;
    private int branchChance = 200;

    /**
     * Generate a map with the setting passed to the constructor
     * and the given height and width.
     *
     * @param width Width of map to generate.
     * @param height Height of map to generate.
     * 
     * @return The generated map.
     */
    public LayeredMap generate(int width, int height)
    {
        final Random rand = getSeededRandom(props);

        // Hajo: at least 2 layers are needed.
        final int mapLayers = Math.max(getInt(props, "map_layers", 2) , 2);
        LayeredMap map = new LayeredMap(mapLayers, width, height);

        int [] walls = new int [] {'#'};

        // Hajo: walls for all squares of this map
        if(props.get("walls") != null) {
            walls = getIntegers(props, "walls", '#');
        }

        int [] standardGrounds = new int [] {'.'};

        // Hajo: Floor for all squares of this map that have no other floor
        //       Floors are randomly picked from the interval, including both borders.
        if(props.get("floors") != null) {
            standardGrounds = getIntegers(props, "floors", '.');
        }

        // Hajo: Probability to create a branch off a corridor
        branchChance = getInt(props, "branch_chance", branchChance);

        final int outside = getInt(props, "outside_floor", 0);

        // Hajo: preset map default values.
        fillArea(map, 0, 0, 0, width, height, standardGrounds);
        fillArea(map, 1, 0, 0, width, height, walls);

        Point p = new Point(5, height/2);
        Point dir = new Point(1, 0);
        
        map.setSpawnX(p.x);
        map.setSpawnY(p.y);
        map.setOutside(0, outside);
        
        int range = (width*2/3) + rand.nextInt(width/3);
        range = Math.min(range, width-20);

        // Hajo: create tunnels recursively.
        digOut(map, p, dir, range, rand);

        return map;
    }

    public MinesGenerator(Properties props)
    {
        this.props = props;
    }


    private void digOut(final LayeredMap map,
                        Point p, Point dir, int range, Random rand)
    {
        if(range < 1) {
            return;
        }

        // System.err.println("Digging " + range + " squares.");

        final Point pos = new Point(p.x, p.y);

        for(int x=0; x<range; x++) {
            
            if(pos.x >= 1 && pos.y >= 1 && pos.x < map.getWidth() - 1 && pos.y < map.getHeight()-1) {
                map.set(1, pos.x, pos.y, 0);
            } else {
                // we would run off map ...
                return;
            }

            if(rand.nextInt(1000) < branchChance) {
                final Point side = new Point(dir.x, dir.y);

                if(rand.nextBoolean()) {
                    rot90l(side);
                } else {
                    rot90r(side);
                }

                digOut(map, pos, side, range/4 + rand.nextInt(range/5+1), rand);
            }

            pos.x += dir.x;
            pos.y += dir.y;
        }
    }
}
