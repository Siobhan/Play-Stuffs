/*
 * WildernessGenerator.java
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
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import rgegame.map.data.LayeredMap;


/**
 * Generator for wilderness type maps.
 * 
 * @author Hj. Malthaner
 */
public class WildernessGenerator extends AbstractMapGenerator
{
    private Properties props;

    /**
     * Generate a new map of width x height squares. Some generators may
     * not be able to create exact sizes.
     *
     * @param width desired map width
     * @param height desired map height
     * @return the generated map model or null if generation failed.
     */
    public LayeredMap generate(int width, int height)
    {
        final Random rand = getSeededRandom(props);

        // Hajo: at least 2 layers are needed.
        final int mapLayers = Math.max(getInt(props, "map_layers", 2) , 2);
        LayeredMap map = new LayeredMap(mapLayers, width, height);
        MapUtils utils = new MapUtils();
        
        // Hajo: we can now decorate the map.
                   
        int [] grounds = new int [] {0};

        // Hajo: Ground for all squares of this map that have no other ground
        //       Grounds are randomly picked from the interval, including both borders.
        if(props.get("grounds") != null) {
            grounds = getIntegers(props, "grounds", '.');
        }

        // Random decorations

        int [] deco1 = getIntegers(props, "decorations.tiles", 0);
        int [] chances1 = getIntegers(props, "decorations.chances", 0);

        if(deco1 == null) {
            chances1 = deco1 = new int [0];
        }

        // Cluster data
        final ArrayList <int []> clusters = new ArrayList <int []> ();
        int n = 0;
        while(props.get("clusters["+n+"].tiles") != null) {
            clusters.add(getIntegers(props, "clusters["+n+"].tiles", 0));
            n++;
        }

        final int [] clusterChances = new int [clusters.size()];
        for(n=0; n<clusters.size(); n++) {
            clusterChances[n] = getInt(props, "clusters["+n+"].chance", 100);
        }

        // System.err.println("Found " + n + " clusters: " + clusters.size());

        for(int j=0; j<height; j++) {
            for(int i=0; i<width; i++) {

                map.set(0, i, j,
                              grounds[rand.nextInt(grounds.length)]);

                for(n=0; n<deco1.length; n++) {
                    if(chances1[n] > rand.nextInt(10000)) {
                        map.set(1, i, j, deco1[n]);
                    }
                }

                if(clusterChances.length > 0) {
                    for(n=0; n<clusterChances.length; n++) {
                        if(clusterChances[n] > rand.nextInt(10000)) {
                            utils.placeDecoCluster(map,
                                                   (int [])clusters.get(rand.nextInt(clusters.size())),
                                                   i, j, 1, rand);
                        }
                    }
                }
            }
        }

        // Wall/border

        int [] margin = getIntegers(props, "margins", '%');
        int mw = getInt(props, "margin.width", 1);

        int [] border = getIntegers(props, "borders", '^');
        int bw = getInt(props, "border.width", 1);

        if(margin != null) {
            for(int j=0; j<height; j++) {
                for(int i=0; i<width; i++) {
                    if(i<mw || j<mw || i>=width-mw || j>=height-mw) {
                        int index = rand.nextInt(margin.length);
                        map.set(1, i, j, margin[index]);
                    }
                }
            }
        }

        if(border != null) {
            for(int j=0; j<height; j++) {
                for(int i=0; i<width; i++) {
                    if(i<bw || j<bw || i>=width-bw || j>=height-bw) {
                        int index = rand.nextInt(border.length);
                        map.set(1, i, j, border[index]);
                    }
                }
            }
        }

        // Hajo: look for a suitable spawning location.

        Point spawn = findSpawnLocation(map);

        final int outside = getInt(props, "outside", 0);

        map.setOutside(0, outside);
        map.setSpawnX(spawn.x);
        map.setSpawnY(spawn.y);
        
        return map;
    }
    
    /** 
     * Creates a new instance of WildernessGenerator 
     */
    public WildernessGenerator(Properties props)
    {
        this.props = props;
    }


    private Point findSpawnLocation(LayeredMap map)
    {
        for(int y=3; y<map.getHeight(); y++) {
            for(int x=3; x<map.getWidth(); x++) {
                boolean ok = true;
                for(int j=0; j<4; j++) {
                    for(int i=0; i<4; i++) {
                        int ground = map.get(0, x+i, y+j) & 0xFFFF;
                        int feat = map.get(1, x+i, y+j) & 0xFFFF;
                        if(ground != '.' || feat != 0) {
                            ok = false;
                        }
                    }
                }

                if(ok) {
                    return new Point(x, y);
                }
            }
        }

        // Hajo: Player most likely stuck ...
        return new Point(10, 10);
    }
}
