/*
 * Matrix.java
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

import java.awt.Point;

/**
 * 2D integer matrix class.
 *
 * @author Hj. Malthaner
 */
public class Matrix
{

    public int a;
    public int b;
    public int c;
    public int d;

    public Matrix()
    {
        a = b = c = d = 0;
    }

    public Matrix(int aa, int bb, int cc, int dd)
    {
        a = aa;
        b = bb;
        c = cc;
        d = dd;
    }

    /**
     * Matrix multiplication with 2D vector (aka Point)
     * @param k The 2D vector to multiply with
     * @return the new vector (a Point object).
     */
    public Point mult(final Point k)
    {
        return new Point(a * k.x + b * k.y, c * k.x + d * k.y);
    }

    /**
     * X component of multiplication (no object allocation)
     */
    public int multX(final int kX, final int kY)
    {
        return a * kX + b * kY;
    }

    /**
     * Y component of multiplication (no object allocation)
     */
    public int multY(final int kX, final int kY)
    {
        return c * kX + d * kY;
    }
}
