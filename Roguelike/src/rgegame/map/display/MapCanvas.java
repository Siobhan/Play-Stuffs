/*
 * MapCanvas.java
 *
 * Created on 25.06.2010
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
import java.awt.Rectangle;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import rgegame.map.data.LayeredMap;

/**
 * A map display canvas.
 *
 * @author Hj. Malthaner
 */
public class MapCanvas extends JPanel
{
    private TiledMapPainter tiledMapPainter;
    private Rectangle redrawRect;


    public void setTiledMapPainter(TiledMapPainter tiledMapPainter)
    {
        this.tiledMapPainter = tiledMapPainter;
    }

    public void setMap(LayeredMap map)
    {
        tiledMapPainter.setMap(map);
        Dimension d = tiledMapPainter.calculateMapBounds();
        setPreferredSize(d);
    }

    public MapCanvas()
    {
        // setDoubleBuffered(true);

        redrawRect = new Rectangle();
        setPreferredSize(new Dimension(640, 480));
        setSize(getPreferredSize());

        setBorder(new LineBorder(Color.BLACK, 20));
    }

    @Override
    public void paint(Graphics gr)
    {
        super.paint(gr);
        tiledMapPainter.paint(gr);
    }

    public void repaintCursor() {
        tiledMapPainter.getCursorRectangle(redrawRect);
        repaint(redrawRect);
    }
}
