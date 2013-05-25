/*
 * ItemConfiguration.java
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
 * Used by the item catalog and the astract item classes to
 * determine the data configuration for items.
 *
 * @see ItemCatalog
 * @see AbstractItem
 * 
 * @author Hj. Malthaner
 */
public class ItemConfiguration
{
    /** Example String presets */
    public String [] stringLabels = {
        "Name",
    };

    /** Example int presets */
    public String [] intLabels = {
        "Price",
        "Weight",
        "Size",
    };

    /** Example triplet presets */
    public String [] tripletLabels = {
        "Durability",
        "Damage",
        "Defense",
    };


    public void addNewStringAttribute(final String name)
    {
        String [] old = stringLabels;

        String [] tmp = new String[old.length+1];
        System.arraycopy(old, 0, tmp, 0, old.length);

        tmp[tmp.length-1] = name;

        stringLabels = tmp;
    }

    public void addNewIntAttribute(final String name)
    {
        String [] old = intLabels;

        String [] tmp = new String[old.length+1];
        System.arraycopy(old, 0, tmp, 0, old.length);

        tmp[tmp.length-1] = name;

        intLabels = tmp;
    }

    public void addNewTripletAttribute(final String name)
    {
        String [] old = tripletLabels;

        String [] tmp = new String[old.length+1];
        System.arraycopy(old, 0, tmp, 0, old.length);

        tmp[tmp.length-1] = name;

        tripletLabels = tmp;
    }

    private String [] removeStringFromArray(String [] in, int skip)
    {
        String [] tmp = new String[in.length+-1];

        int j=0;
        for(int i=0; i<in.length; i++) {
            if(i != skip) {
                tmp[j++] = in[i];
            }
        }

        return tmp;
    }

    public void removeStringAttribute(int n)
    {
        String [] tmp = removeStringFromArray(stringLabels, n);
        stringLabels = tmp;
    }

    public void removeIntAttribute(int n)
    {
        String [] tmp = removeStringFromArray(intLabels, n);
        intLabels = tmp;
    }

    public void removeTripletAttribute(int n)
    {
        String [] tmp = removeStringFromArray(tripletLabels, n);
        tripletLabels = tmp;
    }

    public void write(Writer writer) throws IOException
    {
        writer.write("Item Configuration\n");
        writer.write("v.1\n");

        writer.write("" + stringLabels.length + "\n");
        for(int i=0; i<stringLabels.length; i++) {
            writer.write(stringLabels[i] + "\n");
        }

        writer.write("" + intLabels.length + "\n");
        for(int i=0; i<intLabels.length; i++) {
            writer.write(intLabels[i] + "\n");
        }

        writer.write("" + tripletLabels.length + "\n");
        for(int i=0; i<tripletLabels.length; i++) {
            writer.write(tripletLabels[i] + "\n");
        }
    }


    public void read(BufferedReader reader) throws IOException
    {
        String line;

        // Header
        line = reader.readLine();
        
        // Version
        line = reader.readLine();

        if(!"v.1".equals(line)) {
            throw new IOException("Wrong version: " + line);
        }

        int n;

        line = reader.readLine();
        n = Integer.parseInt(line);
        stringLabels = new String [n];
        for(int i=0; i<n; i++) {
            line = reader.readLine();
            stringLabels[i] = line;
        }

        line = reader.readLine();
        n = Integer.parseInt(line);
        intLabels = new String [n];
        for(int i=0; i<n; i++) {
            line = reader.readLine();
            intLabels[i] = line;
        }

        line = reader.readLine();
        n = Integer.parseInt(line);
        tripletLabels = new String [n];
        for(int i=0; i<n; i++) {
            line = reader.readLine();
            tripletLabels[i] = line;
        }
    }
}
