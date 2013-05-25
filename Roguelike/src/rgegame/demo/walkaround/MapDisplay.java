/*
 * MapDisplay.java
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

package rgegame.demo.walkaround;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JPanel;

import rgegame.entities.PlayerEntity;
import rgegame.fieldofview.FieldOfView;
import rgegame.fieldofview.FovDestination;
import rgegame.fieldofview.FovSource;
import rgegame.map.data.LayeredMap;
import rgegame.map.display.ASCIITilePainter;
import rgegame.map.display.ColorCodes;
import rgegame.map.display.MapCanvas;
import rgegame.map.display.PlainTileLocator;
import rgegame.map.display.TileLocator;
import rgegame.map.display.TiledMapPainter;

/**
 * A wrapper for a MapCanvas, which will handle map movement for
 * display and other game related display modifications.
 * 
 * @author Hj. Malthaner
 */
public class MapDisplay extends JPanel
{
    private final MapCanvas mapCanvas;
    private final ASCIITilePainter asciiPainter;
    private final TileLocator tileLocator;
    
    private final FieldOfView fov;

    private int [] viewMap;
    private int viewWidth;
    private int viewHeight;

    private final PlayerEntity player;
    
    private boolean centerMapOnPlayer = true;



    /**
     * True will mean to have the player always centered on the map view
     * and the dungeon will scroll on player moves. Pass flase if you want
     * the dungeon to be static on screen and the player move around.
     *
     * @param yesno True to enable dungeon scrolling, false to have
     *        a non-scrolling display.
     */
    public void setCenterMapOnPlayer(boolean yesno)
    {
        centerMapOnPlayer = yesno;
    }

    /**
     * Prepare display for a new map.
     * 
     * @param map The new map to display
     */
    public void setMap(LayeredMap map)
    {
        viewWidth = map.getWidth();
        viewHeight = map.getHeight();

        viewMap = new int[viewWidth*viewHeight];

        final int playerX = map.getSpawnX();
        final int playerY = map.getSpawnY();

        player.location.x = playerX;
        player.location.y = playerY;
        player.gameMap = map;
        player.displayMap = new LayeredMap(map.getLayerCount(),
                                           map.getWidth(),
                                           map.getHeight());
        
        map.set(LayerCodes.MOBILES, playerX, playerY, '@' + ColorCodes.CYAN);
        
        asciiPainter.setViewMap(viewMap, viewWidth, viewHeight);

        mapCanvas.setMap(player.displayMap);
        mapCanvas.setSize(mapCanvas.getPreferredSize());

        recalcFov(playerX, playerY);
    }

    /**
     * Set up a new map display component.
     *
     * @param player The player to display the map for.
     */
    public MapDisplay(final PlayerEntity player)
    {
        this.player = player;
        
        setBackground(Color.BLACK);
        setLayout(null);

        asciiPainter = new ASCIITilePainter();

        TiledMapPainter tiledMapPainter = new TiledMapPainter();
        tiledMapPainter.setTilePainter(asciiPainter);

        tileLocator = new PlainTileLocator(12, 16);
        // tileLocator = new IsometricTileLocator(14, 8);
        tiledMapPainter.setLocator(tileLocator);

        mapCanvas = new MapCanvas();
        mapCanvas.setBackground(Color.BLACK);
        mapCanvas.setTiledMapPainter(tiledMapPainter);
        mapCanvas.setSize(mapCanvas.getPreferredSize());
        
        add(mapCanvas);

        fov = new FieldOfView(new FovSourceLink(), new FovDestLink());

        addComponentListener(new ComponentListener() {

            public void componentResized(ComponentEvent e) {
                recalcFov(player.location.x, player.location.y);
            }

            public void componentMoved(ComponentEvent e) {
            }

            public void componentShown(ComponentEvent e) {
                recalcFov(player.location.x, player.location.y);
            }

            public void componentHidden(ComponentEvent e) {
            }
        });
    }

    /**
     * Calculate new field of view, e.g. after a move or change to the
     * map.
     *
     * @param x x origin of field of view.
     * @param y y origin of field of view.
     */
    public void recalcFov(int x, int y)
    {
        // Hajo: clear out "visible" flag, keep other flags
        for(int i=0; i<viewMap.length; i++) {
            viewMap[i] = (viewMap[i] & ~ASCIITilePainter.VIEW_VISIBLE);
        }
        
        fov.calculate(x, y, 20);

        if(centerMapOnPlayer) {
            int xoff = tileLocator.getTileScreenX(player.location.x, player.location.y);
            int yoff = tileLocator.getTileScreenY(player.location.x, player.location.y);

            mapCanvas.setLocation((getWidth()/2)-xoff, (getHeight()/2)-yoff);
        }

        mapCanvas.repaint();
    }

    /**
     * Field of view source link to map data.
     */
    private class FovSourceLink implements FovSource
    {
        /**
         * Check weather a given location
         * blocks the line of sight or not.
         *
         * @param posX X coordinate
         * @param posY Y coordinate
         *
         * @return true if blocking, false otherwise
         */
        public boolean isBlockingLOS(int posX, int posY)
        {
            final int feature =
                    FeatureCodes.getFeature(player.gameMap.get(LayerCodes.FEATURE,
                                                               posX, posY));

            // Hajo: just example checks, a real game should do
            // something more elaborate.
            boolean blocking = 
                    feature == FeatureCodes.WALL_BRICKS ||
                    feature == FeatureCodes.WALL_ROCK ||
                    feature == FeatureCodes.TREE ||
                    feature == FeatureCodes.DOOR_SHUT;

            // System.err.println("Src: " + posX + ", " + posY +" is blocking " + blocking);

            return blocking;
        }
    }
    
    /**
     * Field of view destination link to map data.
     */
    private class FovDestLink implements FovDestination
    {
        /**
         * FOV code will call this for each location that can be seen.
         * Seen cells will also be remembered.
         */
        public void setCanBeSeen(int posX, int posY)
        {
            if(posX >= 0 && posY >= 0 && posX < viewWidth && posY < viewHeight) {

                final int layers = player.gameMap.getLayerCount();
                for(int i=0; i<layers; i++) {

                    final int code =
                            player.gameMap.get(i, posX, posY);

                    player.displayMap.set(i, posX, posY, code);

                    if(i == LayerCodes.ITEMS) {
                        if(code != 0) {
                            int itemCode = player.objectRegistry.get(code).getDisplayCode();
                            player.displayMap.set(i, posX, posY, itemCode);
                        }
                    }
                }

                viewMap[posY*viewWidth+posX] |=
                        (ASCIITilePainter.VIEW_VISIBLE | ASCIITilePainter.VIEW_REMEMBERED);
            }
        }        
    }

}
