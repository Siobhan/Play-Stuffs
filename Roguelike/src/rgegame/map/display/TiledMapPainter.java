/*
 * TiledMapPainter.java
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

package rgegame.map.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import rgegame.map.data.LayeredMap;

/**
 * Paint a tiled (cell based) map.
 * 
 * @author Hj. Malthaner
 */
public class TiledMapPainter
{
    private LayeredMap map;
    private TileLocator tileLocator;
    private TilePainter tilePainter;

    private final Point mouseToTile;

    private int xoff, yoff;
    private int markX, markY;

    private Rectangle clip = new Rectangle();

    private int currentLayer;
    private Color gridColor = new Color(100, 0, 100);

    private boolean showScripts = false;
    private boolean hasTransparentTiles = false;

    public void setShowScripts(boolean yesno)
    {
        showScripts = yesno;
    }

    public void setHasTransparentTiles(boolean yesno)
    {
        hasTransparentTiles = yesno;
    }

    public void setMark(int x, int y)
    {
        markX = x;
        markY = y;
    }

    public void setMap(LayeredMap map)
    {
        this.map = map;
    }

    public void setLocator(TileLocator tileLocator)
    {
        this.tileLocator = tileLocator;
    }

    public void setTilePainter(TilePainter tilePainter)
    {
        this.tilePainter = tilePainter;
    }

    public int getCurrentLayer()
    {
        return currentLayer;
    }
    
    public void setCurrentLayer(int layer)
    {
        currentLayer = layer;
    }

    public Dimension calculateMapBounds()
    {
        int xmin = 9999999;
        int ymin = 9999999;
        int xmax = -9999999;
        int ymax = -9999999;

        int xpos, ypos;

        xpos = 0 + tileLocator.getTileScreenX(0, 0);
        ypos = 0 + tileLocator.getTileScreenY(0, 0);

        xmin = Math.min(xmin, xpos);
        ymin = Math.min(ymin, ypos);
        xmax = Math.max(xmax, xpos);
        ymax = Math.max(ymax, ypos);

        xpos = 0 + tileLocator.getTileScreenX(map.getWidth(), 0);
        ypos = 0 + tileLocator.getTileScreenY(map.getWidth(), 0);

        xmin = Math.min(xmin, xpos);
        ymin = Math.min(ymin, ypos);
        xmax = Math.max(xmax, xpos);
        ymax = Math.max(ymax, ypos);

        xpos = 0 + tileLocator.getTileScreenX(0, map.getHeight());
        ypos = 0 + tileLocator.getTileScreenY(0, map.getHeight());

        xmin = Math.min(xmin, xpos);
        ymin = Math.min(ymin, ypos);
        xmax = Math.max(xmax, xpos);
        ymax = Math.max(ymax, ypos);

        xpos = 0 + tileLocator.getTileScreenX(map.getWidth(), map.getHeight());
        ypos = 0 + tileLocator.getTileScreenY(map.getWidth(), map.getHeight());

        xmin = Math.min(xmin, xpos);
        ymin = Math.min(ymin, ypos);
        xmax = Math.max(xmax, xpos);
        ymax = Math.max(ymax, ypos);

        // Hajo: add some extra margins here
        xoff = -xmin;
        yoff = -ymin+40;

        return new Dimension(xmax-xmin+60, ymax-ymin+60);
    }

    public TiledMapPainter()
    {
        mouseToTile = new Point();
        markX = markY = -1;
    }

    private boolean paintTile(final Graphics gr, final int n, final int x, final int y)
    {
        final int ypos = yoff + tileLocator.getTileScreenY(x, y);

        if(ypos > clip.y - 64 &&
           ypos < clip.y + clip.height + 100)
        {
            final int xpos = xoff + tileLocator.getTileScreenX(x, y);

            if(xpos > clip.x - 100 &&
               xpos < clip.x + clip.width + 100)
            {
                final int tileNo = map.get(n, x, y);
                if(tileNo > 0) {
                    tilePainter.paint(gr, n, x, y,
                                      xpos + map.getXoff(n, x, y),
                                      ypos + map.getYoff(n, x, y),
                                      tileNo);

                    return true;
                }

                if(showScripts) {
                    String script = map.getScript(x, y);
                    if(script.length() > 0) {
                        gr.setColor(Color.PINK);
                        gr.drawString(script.substring(0, Math.min(script.length(), 5)),
                                      xpos+4, ypos+20);
                        // System.err.println(script);
                    }
                }
            }
        }

        return false;
    }

    public void getCursorRectangle(final Rectangle redrawRect)
    {
        redrawRect.x = xoff + tileLocator.getTileScreenX(markX, markY);
        redrawRect.y = yoff + tileLocator.getTileScreenY(markX, markY);

        redrawRect.width = tileLocator.getCursorX() + 1;
        redrawRect.height = tileLocator.getCursorY() + 1;
    }

    /**
     * Paint the map onto a graphics context.
     * 
     * @param gr Context to paint upon.
     */
    public void paint(Graphics gr)
    {
        final int w = map.getWidth();
        final int h = map.getHeight();

        gr.getClipBounds(clip);

        final int floor = Math.max(currentLayer, 0);

        if(hasTransparentTiles) {
            // Hajo: draw flooring always.
            for(int n=0; n<floor; n++) {
                for(int y=0; y<h; y++) {
                    for(int x=0; x<w; x++) {
                        paintTile(gr, n, x, y);
                    }
                }
            }
        }
        
        if(currentLayer > 0) {
            paintGrid(gr, clip);
        }

        final int layers = map.getLayerCount();

        if(hasTransparentTiles) {
            // Paint bottom up
            for(int y=floor; y<h; y++) {
                for(int x=0; x<w; x++) {
                    for(int n=0; n < layers; n--) {
                        paintTile(gr, n, x, y);
                    }
                }
            }
        } else {
            // Paint top down
            for(int y=0; y<h; y++) {
                for(int x=0; x<w; x++) {
                    boolean go = true;
                    for(int n=layers-1; go && n>=floor; n--) {
                        go = !paintTile(gr, n, x, y);
                    }
                }
            }
        }


        if(markX != -1 && markY != -1) {
            final int xpos = xoff + tileLocator.getTileScreenX(markX, markY);
            final int ypos = yoff + tileLocator.getTileScreenY(markX, markY);

            gr.setColor(Color.WHITE);
            gr.drawRect(xpos, ypos,
                        tileLocator.getCursorX(),
                        tileLocator.getCursorY());
        }
    }

    private void paintGrid(Graphics gr, Rectangle clip)
    {
        gr.setColor(gridColor);

        for(int y=clip.y; y<clip.y + clip.height; y++) {
            if((y & 3) == 0) {
                gr.fillRect(clip.x, y, clip.width, 1);
            }
        }
    }

    /**
     * Reverse mapping of mouse (screen) coordinate to tile coordinate.
     * Can be used with all sorts of map layouts (rectangular, isometric).
     *
     * This method is not thread safe! Subsequent calls will overwrite
     * the result object data of former calls.
     * 
     * @param mousex mouse x position
     * @param mousey mouse y position
     *
     * @return The tile coordinate where the mouse points to.
     */
    public Point mousePosToTile(int mousex, int mousey)
    {
        final int w = map.getWidth();
        final int h = map.getHeight();

        int best = 99999999;
        int bestRow = 0;

        mousex -= tileLocator.getCursorX() / 2;
        mousey -= tileLocator.getRasterY() / 2;
        
        // Find best row by quick scan
        for(int y=0; y<h; y+=2) {
            for(int x=0; x<w; x+=5) {
                final int xpos = xoff + tileLocator.getTileScreenX(x,y);
                final int ypos = yoff + tileLocator.getTileScreenY(x,y);

                final int dist = (xpos-mousex)*(xpos-mousex) + (ypos-mousey) * (ypos-mousey);
                if(dist < best) {
                    best = dist;
                    bestRow = y;
                }
            }
        }
        
        best = 999999;

        // System.err.println("Best row: " + bestRow);

        for(int y=bestRow-5; y<bestRow+5; y++) {
            for(int x=0; x<w; x++) {
                final int xpos = xoff + tileLocator.getTileScreenX(x,y);
                final int ypos = yoff + tileLocator.getTileScreenY(x,y);

                final int dist = (xpos-mousex)*(xpos-mousex) + (ypos-mousey) * (ypos-mousey);
                if(dist < best) {
                    mouseToTile.x = x;
                    mouseToTile.y = y;
                    best = dist;
                    
                    if(best < 16) {
                        return mouseToTile;
                    }
                }
            }
        }

        return mouseToTile;
    }
}
