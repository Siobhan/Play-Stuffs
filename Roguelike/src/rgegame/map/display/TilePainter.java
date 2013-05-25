/*
 * TilePainter.java
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

import java.awt.Graphics;

/**
 * Paint a map element at a specified screen location.
 *
 * @author Hj. Malthaner
 */
public interface TilePainter
{

    /**
     * Paint a map element.
     *
     * @param gr The graphics context to paint onto.
     * @param layer The map layer
     * @param mapX The map x coordinate
     * @param mapY The map y coordinate
     * @param xpos The screen x position to draw to
     * @param ypos The screen y position to draw to
     * @param tileNo The tile number to draw
     */
    public void paint(final Graphics gr,
                      final int layer,
                      final int mapX,
                      final int mapY,
                      final int xpos,
                      final int ypos,
                      final int tileNo);
}
