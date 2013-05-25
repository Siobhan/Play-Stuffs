/*
 * PathfinderThread.java
 *
 * Created on 16.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.demo.pathfinding;

import rgegame.demo.walkaround.LayerCodes;
import rgegame.demo.walkaround.MapDisplay;
import rgegame.entities.PlayerEntity;
import rgegame.map.data.LayeredMap;
import rgegame.map.display.ColorCodes;
import rgegame.pathfinding.CoordinatePathDestination;
import rgegame.pathfinding.Path;
import rgegame.pathfinding.PathSource;

/**
 * Do the actual pathfinding and move player symbol along the path.
 * 
 * @author Hj. Malthaner
 */
public class PathfinderThread extends Thread
{
    private final MapDisplay mapDisplay;
    private final PlayerEntity player;
    private final LayeredMap map;
    private boolean go = true;

    /**
     * Search paths to random locations in a loop.
     */
    @Override
    public void run()
    {
        Path path = new Path();
        PathSource pathSource = new PathSourceLink(map);
        int sx = player.location.x;
        int sy = player.location.y;
        int dx, dy;

        while(go) {

            // Find a random empty spot on the map
            do {
                dx = (int) (Math.random()*map.getWidth());
                dy = (int) (Math.random()*map.getHeight());
            } while(pathSource.isMoveAllowed(sx, sy, dx, dy) == false);

            CoordinatePathDestination destination =
                    new CoordinatePathDestination(dx, dy);

            // Try to find a path there
            boolean ok = path.findPath(pathSource, destination, sx, sy);

            // See if we found a path
            if(ok) {
                // mark destination with X on screen
                if(player.location.x != dx || player.location.y != dy) {
                      map.set(LayerCodes.MOBILES, dx, dy, 'X' + ColorCodes.RED);
                }

                // now walk.
                walkPath(path);
            }

            // pathing done

            path.clear();

            // update start for next pathdinfind call.
            
            sx = player.location.x;
            sy = player.location.y;
        }
    }

    public PathfinderThread(MapDisplay mapDisplay,
                            PlayerEntity player)
    {
        this.mapDisplay = mapDisplay;
        this.player = player;
        this.map = player.gameMap;
        
        setPriority(MIN_PRIORITY);
        setDaemon(true);
    }

    /**
     * Follow the path. Update field of view after each step.
     *
     * @param path The path to follow
     */
    private void walkPath(final Path path)
    {
        Path.Node node = path.currentStep();

        // follow the path
        while(node != null) {

            int playerCode = map.get(LayerCodes.MOBILES, player.location.x, player.location.y);
            map.set(LayerCodes.MOBILES, player.location.x, player.location.y, 0);

            player.location.x = node.x;
            player.location.y = node.y;

            map.set(LayerCodes.MOBILES, player.location.x, player.location.y, playerCode);

            path.advance();
            node = path.currentStep();

            mapDisplay.recalcFov(player.location.x, player.location.y);
            try {sleep(80);} catch(InterruptedException ex) {/* */}
        }
    }
}
