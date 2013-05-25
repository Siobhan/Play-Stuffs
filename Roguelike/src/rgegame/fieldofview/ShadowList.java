/*
 * ShadowList.java
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

import java.util.ArrayList;

/**
 * Keeps track of shadow intervals.
 * 
 * @author Hj. Malthaner
 */
public class ShadowList
{
    private ArrayList <Interval> list;

    public ShadowList()
    {
        list = new ArrayList<Interval>(128);
    }

    boolean isFullyShadowed(final double low, final double high)
    {
        for(Interval interval : list) {
            if(interval.low <= low && interval.high >= high) {
                return true;
            }
        }

        return false;
    }


    boolean isPartiallyShadowed(final double low, final double high)
    {
        for(Interval interval : list) {
            if((interval.low < low && interval.high > low) ||
               (interval.low < high && interval.high > high)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Merge a new shadow interval into the shadow list.
     *
     * @param low lower angle of new shadow
     * @param high upper angle of new shadow
     */
    void merge(final double low, final double high)
    {
        if(list.isEmpty()) {
            list.add(new Interval(low, high));
        } else {
            final double eps = 0.01;

            // try to merge first
            boolean newShadow = true;
            for(Interval interval : list) {
                if((low <= interval.low && high >= interval.low-eps) ||
                   (low <= interval.high+eps && high >= interval.high)) {

                    if(low < interval.low) {
                        interval.low = low;
                    }

                    if(high > interval.high) {
                        interval.high = high;
                    }

                    newShadow = false;
                }
            }

            if(newShadow) {
                list.add(new Interval(low, high));
            }
        }
    }


    void deepCopy(final ShadowList src)
    {
        list.clear();

        for(Interval interval : src.list) {
            list.add(new Interval(interval.low, interval.high));
        }
    }

    /**
     * Show debug data.
     */
    void dump()
    {
        for(Interval interval : list) {
            System.err.println("Interval: " +
                               interval.low + ".." +
                               interval.high);
        }
    }

    void clear()
    {
        list.clear();
    }
}
