/*
 * Interval.java
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
 * Intervals (ranges) in the mathematical sense.
 * No operations are defined yet.
 *
 * @author Hj. Malthaner
 */
public class Interval {

    public double low;
    public double high;

    public Interval()
    {
        high = low = 0.0;
    }

    public Interval(double l, double h)
    {
        low = l;
        high = h;
    }
}
