/*
 * Area.java
 *
 * Created on 2010/07/21
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.pathfinding;

import java.awt.Point;
import java.util.ArrayList;

/**
 * A breadth-first area finder. It will find all locations which
 * are reachable from the given start point, using the given
 * path source.
 *
 * @author Hj. Malthaner
 */
public class Area
{

    /** Result path of area finding */
    private final ArrayList <Point> area;

    /** use diagonals in pathfinding? */
    private boolean useDiagonals = true;

    /**
     * Use diagonals in pathfinding?
     * Default is true.
     */
    public void setUseDiagonals(boolean yesno)
    {
        useDiagonals = yesno;
    }

    /**
     * Access the list of all points in the found area.
     * @return the list of all points in the found area.
     */
    public ArrayList <Point> getArea()
    {
        return area;
    }

    /** Reset area, remove all points.*/
    public void clear()
    {
        area.clear();
    }

    /**
     * Breadth first area finding.
     *
     * @param pathSource the pathSource to search
     *
     * @param sx Source x-coordinate
     * @param sy Source y-coordinate
     * 
     * @return true if path was found, false otherwise
     */
    public boolean findArea(final PathSource pathSource,
                            final int sx, final int sy)
    {
        clear();

        final ArrayList <Point> queue = new ArrayList <Point> ();

        queue.add(new Point(sx, sy));
        Point p;

        do {
            p = queue.remove(0);

            // Recursive pathfinding in the 4 cardinal directions.
            checkMove(pathSource, area, queue, p, 1, 0);
            checkMove(pathSource, area, queue, p, 0, 1);
            checkMove(pathSource, area, queue, p, -1, 0);
            checkMove(pathSource, area, queue, p, 0, -1);

            if(useDiagonals) {
                checkMove(pathSource, area, queue, p, 1, 1);
                checkMove(pathSource, area, queue, p, 1, -1);
                checkMove(pathSource, area, queue, p, -1, 1);
                checkMove(pathSource, area, queue, p, -1, -1);
            }

        } while(queue.isEmpty() == false);

        return false;
    }

    /**
     * Check next possible step on path.
     */
    private void checkMove(final PathSource pathSource,
                           final ArrayList <Point> area,
                           final ArrayList <Point> queue,
                           final Point p,
                           final int w, final int h)
    {
        final int dx = p.x+w;
        final int dy = p.y+h;

        final Point pos = new Point(dx, dy);

        if(pathSource.isMoveAllowed(p.x, p.y, dx, dy) &&
           area.contains(pos) == false)
        {
            queue.add(pos);
            area.add(pos);
        }
    }

    /** 
     * Creates a new instance of Path with no nodes.
     */
    public Area()
    {
        area = new ArrayList <Point> ();
        clear();
    }
}
