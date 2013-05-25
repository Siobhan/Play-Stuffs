/*
 * Path.java
 *
 * Created on 9. December 2008
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
import java.util.HashSet;
import java.util.Stack;

/**
 * A breadth-first pathfinder, searching the shortest path from
 * a start coordinate to a destination condistion.
 *
 * A path consists of discrete steps from one location to another.
 * 
 * This pathfinding implementation is not reentrant, so each thread
 * needs it's own instance of a path for pathfinding.
 *
 * @author Hj. Malthaner
 */
public class Path
{
    /**
     * One step of the path. The path as a whole is a linked list
     * of these nodes.
     * 
     * @author Hj. Malthaner
     */
    public class Node
    {
        public final int x, y;
        private final Node link;

        Node(int x, int y) {
            this.x = x;
            this.y = y;
            this.link = null;
        }

        Node(int x, int y, Node link) {
            this.x = x;
            this.y = y;
            this.link = link;
        }
    }

    /** Result path of pathfinding */
    private final ArrayList <Node> path;

    /** Current step in stepping through the path */
    private int step;

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
     * Look ahead on next step
     * @return node at next position,
     *         or null if there is no such node (end of path reached)
     */
    public Node nextStep()
    {
        return getStep(step+1);
    }

    /** 
     * Retrieve current step
     * @return node at current position,
     *         or null if there is no such node (end of path reached)
     */
    public Node currentStep()
    {
        return getStep(step);
    }

    /** 
     * Random access for a step
     * @return node at position n, or null if there is no such node
     */
    public Node getStep(int n)
    {
        if(n >= 0 && n < path.size()) {
            return path.get(n);
        }
        return null;
    }

    /** Advance current step by one. */
    public void advance()
    {
        step ++;
    }

    /** Add a step to this path */
    public void addStep(int x, int y)
    {
        path.add(new Node(x, y));
    }

    /** Reset path, remove all nodes.*/
    public void clear()
    {
        path.clear();
        step = 0;
    }

    /**
     * Breadth first pathfinding.
     *
     * @param pathSource the pathSource to search
     * @param pathDestination to check weather a pathfinding step
     *                        reachded the destination.
     *
     * @param sx Source x-coordinate
     * @param sy Source y-coordinate
     * 
     * @return true if path was found, false otherwise
     */
    public boolean findPath(final PathSource pathSource,
                            final PathDestination pathDestination,
                            int sx, int sy)
    {
        clear();

        HashSet <Point> marks = new HashSet <Point> ();
        ArrayList <Node> queue = new ArrayList <Node> ();

        queue.add(new Node(sx, sy));
        marks.add(new Point(sx, sy));

        Node p;

        do {
            p = (Node)queue.remove(0);

            if(pathDestination.isDestinationReached(p.x, p.y)) {
                // Hajo: destination found
                // unwind path

                Stack <Node> stack = new Stack <Node> ();

                while(p.link != null) {
                    stack.push(p);
                    p = p.link;
                }

                // Hajo: Push starting node
                stack.push(p);

                // Hajo: Setup path
                while(stack.isEmpty() == false) {
                    p = stack.pop();
                    addStep(p.x, p.y);
                }

                return true;
            } else {
                // Recursive pathfinding in the 4 cardinal directions.
                checkMove(pathSource, marks, queue, p, 1, 0);
                checkMove(pathSource, marks, queue, p, 0, 1);
                checkMove(pathSource, marks, queue, p, -1, 0);
                checkMove(pathSource, marks, queue, p, 0, -1);

                if(useDiagonals) {
                    checkMove(pathSource, marks, queue, p, 1, 1);
                    checkMove(pathSource, marks, queue, p, 1, -1);
                    checkMove(pathSource, marks, queue, p, -1, 1);
                    checkMove(pathSource, marks, queue, p, -1, -1);
                }
            }
         } while(queue.isEmpty() == false);

        return false;
    }

    /**
     * Check next possible step on path.
     */
    private void checkMove(final PathSource pathSource,
                           final HashSet <Point> marks,
                           final ArrayList <Node> queue,
                           final Node p,
                           final int w, final int h)
    {
        final int dx = p.x+w;
        final int dy = p.y+h;

        final Point pos = new Point(dx, dy);

        if(pathSource.isMoveAllowed(p.x, p.y, dx, dy) &&
           marks.contains(pos) == false)
        {
            queue.add(new Node(dx, dy, p));
            marks.add(pos);
        }
    }

    /** 
     * Creates a new instance of Path with no nodes.
     */
    public Path()
    {
        path = new ArrayList <Node> ();
        clear();
    }
}
