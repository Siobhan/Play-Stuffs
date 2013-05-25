/*
 * FieldOfView.java
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

package rgegame.fieldofview;

/**
 * Generic field of view calculator. Uses fov_source_t and fov_destination_t
 * objects to communicate with the application.
 *
 * @author Hj. Malthaner
 */
public class FieldOfView
{
    /**
     * Matrix transforms for all 8 octants.
     */
    private final Matrix trans [] =
    {
        new Matrix(1,0,0,1),
        new Matrix(0,-1,-1,0),
        new Matrix(0,1,-1,0),
        new Matrix(-1,0,0,1),
        new Matrix(-1,0,0,-1),
        new Matrix(0,1,1,0),
        new Matrix(0,-1,1,0),
        new Matrix(1,0,0,-1),

    };

    private final FovSource source;
    private final FovDestination destination;


    /**
     * Create a new field of view calculator.
     *
     * @param source Used to check if a map cell blocks the line of sight
     * @param destination Used to write the results of the visibility calculation
     */
    public FieldOfView(FovSource source, FovDestination destination)
    {
        this.source = source;
        this.destination = destination;
    }

    private static double angle(final int x1, final int y1,
                         final int x2, final int y2)
    {
        final double xd = x2-x1;
        final double yd = y2-y1;

        return yd/xd;
    }


    private boolean fovPos(final Matrix transform,
                           final ShadowList formerShadows,
                           final ShadowList shadows,
                           final int centerX, final int centerY,
                           final int posX, final int posY)
    {
        final int kX = centerX + posX;
        final int kY = centerY + posY;

        final double min_w = angle(centerX*2+1, centerY*2+1, kX*2, kY*2);
        final double max_w = angle(centerX*2+1, centerY*2+1, kX*2+2, kY*2+2);

        boolean visible;

        // if shadowed by colums shadow list we can't see this square

        if(formerShadows.isFullyShadowed(min_w, max_w)) {

            // we can't see this grid, because other grids already
            // blocked the line of sight

            visible = false;

            // merge new shadowed area into shadows list
            // only needed of check was for partial shadow!
            // shadows.merge(min_w, max_w);

        } else {
            // this is a viewable grid
            visible = true;

            // is this grid a line of sight blocker ?
            final int x = transform.multX(posX, posY);
            final int y = transform.multY(posX, posY);
            if(source.isBlockingLOS(x + centerX, y + centerY)) {
                // merge new shadowed area into shadows list
                shadows.merge(min_w, max_w);
            }
        } // if(shaded ...

        return visible;
    }


    /**
     * Calculate field of view.
     *
     * @param centerX X origin (center) of field of view
     * @param centerY Y origin (center) of field of view
     * @param distance the max viewing distance
     */
    public void calculate(final int centerX, int centerY,
                          final int distance)
    {
        int x_off, y_off;

        final ShadowList shadows = new ShadowList();
        final ShadowList formerShadows = new ShadowList();

        for(int octant=0; octant < 8; octant ++) {
            Matrix transform = trans[octant];

            formerShadows.clear();
            shadows.clear();

            x_off = 1;

            while(x_off < distance) {
                int posX = x_off-1;
                int posY = 0;

                // save old shadows, we need last columns shadows while building
                // new shadows
                // we modify angles so we need a real copy

                formerShadows.deepCopy(shadows);

                boolean visible_row = false;
                y_off = 0;
                while(y_off < x_off) {
                    boolean visible_pos = fovPos(transform,
                                                 formerShadows,
                                                 shadows,
				                                 centerX, centerY,
                                                 posX, posY);

                    if(visible_pos) {
                        visible_row |= visible_pos;
                        int x = transform.multX(posX, posY);
                        int y = transform.multY(posX, posY);
                        destination.setCanBeSeen(x+centerX, y+centerY);
                    }

                    posY --;
                    y_off ++;
                }

                if(visible_row == false) {
                    break;
                }

                x_off ++;
            }
        } // for
    }
}
