/*
 * Triplet.java
 *
 * Created on 2010/07/22
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.item;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

/**
 * A group of three integer values. Triplets are immutable values.
 * 
 * @author Hj. Malthaner
 */
public class Triplet
{
    private final int v[];

    /**
     * Get value no. i. I must be in [0...2]
     * @param i Index of the value to get
     * @return The value at index i.
     */
    public int get(int i) {
        return v[i];
    }

    public Triplet()
    {
        v = new int[3];
    }

    public Triplet(BufferedReader reader) throws IOException
    {
        v = new int[3];
        read(reader);
    }

    public Triplet(int v1, int v2, int v3)
    {
        v = new int[3];
        v[0] = v1;
        v[1] = v2;
        v[2] = v3;
    }

    public void write(Writer writer) throws IOException
    {
        writer.write("" + v[0] + " " + v[1] + " " + v[2] + "\n");
    }


    public void read(BufferedReader reader) throws IOException
    {
        final String line = reader.readLine();
        final String [] parts = line.split(" ");

        v[0] = Integer.parseInt(parts[0]);
        v[1] = Integer.parseInt(parts[1]);
        v[2] = Integer.parseInt(parts[2]);
    }

    @Override
    public String toString()
    {
        return "" + v[0] + " " + v[1] + " " + v[2];
    }
}
