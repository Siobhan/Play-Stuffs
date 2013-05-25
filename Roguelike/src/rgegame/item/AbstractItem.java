/*
 * AbstractItem.java
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
 * <p>A read only data container for generic item data.</p>
 *
 * <p>Check http://www.funkelwerk.de/forum/index.php?topic=341.0
 * for more details on the design ideas behind this class</p>
 * 
 * @see IndividualItem
 * @author Hj. Malthaner
 */
public class AbstractItem
{
    /** 
     * Key to be used in the item catalog
     */
    private String key;

    /** 
     * The string values array
     * @see ItemConfiguration
     */
    private final String [] strings;

    /** 
     * The integer values array
     * @see ItemConfiguration
     */
    private final int [] ints;

    /** 
     * The Triplet values array
     * @see ItemConfiguration
     */
    private final Triplet [] triplets;

    /** 
     * Unique key (within an item catalog) for this item data
     */
    public String getKey()
    {
        return key;
    }

    /**
     * Access string data through index
     */
    public String getString(int index)
    {
        return index >= 0 && index < strings.length ? strings[index] : "";
    }

    /**
     * Access integer data through index
     */
    public int getInt(int index)
    {
        return index >= 0 && index < ints.length ? ints[index] : 0;
    }

    /**
     * Access triplet data through index
     */
    public Triplet getTriplet(int index)
    {
        return index >= 0 && index < triplets.length ? triplets[index] : new Triplet();
    }

    /**
     * Create a new abstratc item fromm the data accessed through the reader.
     *
     * @param config The item config to use to interpret the data
     * @param reader The reader to read the data
     * @throws IOException IOException will be thrown on read errors, including
     *                     data integrity check fails.
     */
    public AbstractItem(final ItemConfiguration config,
                        final BufferedReader reader) throws IOException
    {
        strings = new String[config.stringLabels.length];
        ints = new int[config.intLabels.length];
        triplets = new Triplet[config.tripletLabels.length];

        for(int i=0; i<triplets.length; i++) {
            triplets[i] = new Triplet();
        }

        read(reader);
    }

    /**
     * Create a new, empty abstract item. Since abstract items are read
     * only, such are only good for dummy purposes.
     *
     * @param config The item config to use to interpret the data
     * @param key Must be unique within an item catalog
     * @see ItemCatalog
     */
    public AbstractItem(ItemConfiguration config, String key)
    {
        this.key = key;

        strings = new String[config.stringLabels.length];
        ints = new int[config.intLabels.length];
        triplets = new Triplet[config.tripletLabels.length];

        for(int i=0; i<triplets.length; i++) {
            triplets[i] = new Triplet();
        }
    }

    /**
     * Create a new abstract item from the data sets.
     * 
     * @param config The item config to use to interpret the data
     * @param key Must be unique within an item catalog
     * @param sval String values
     * @param ival Integer values
     * @param tval triplet values
     * @see ItemCatalog
     */
    public AbstractItem(ItemConfiguration config,
                        String key,
                        String [] sval,
                        int [] ival,
                        Triplet [] tval)
    {
        this.key = key;

        strings = new String[config.stringLabels.length];
        ints = new int[config.intLabels.length];
        triplets = new Triplet[config.tripletLabels.length];

        for(int i=0; i<strings.length; i++) {
            strings[i] = sval[i];
        }
        for(int i=0; i<ints.length; i++) {
            ints[i] = ival[i];
        }
        for(int i=0; i<triplets.length; i++) {
            triplets[i] = tval[i];
        }
    }

    /**
     * Write the item data through the given writer. Format is a simple
     * plain text list of item data.
     *
     * @param writer The writer to write to.
     * @throws IOException IOException is thrown if write errors occur.
     */
    public void write(Writer writer) throws IOException
    {
        writer.write("Item\n");
        writer.write("v.1\n");
        writer.write(key + "\n");

        writer.write("" + strings.length + "\n");
        for(int i=0; i<strings.length; i++) {
            writer.write(strings[i] + "\n");
        }

        writer.write("" + ints.length + "\n");
        for(int i=0; i<ints.length; i++) {
            writer.write("" + ints[i] + "\n");
        }

        writer.write("" + triplets.length + "\n");
        for(int i=0; i<triplets.length; i++) {
            triplets[i].write(writer);
        }
    }


    /**
     * Read item data from reader.
     * 
     * @param reader The reader to read the data
     * @throws IOException IOException will be thrown on read errors, including
     *                     data integrity check fails.
     */
    private void read(BufferedReader reader) throws IOException
    {
        String line;

        // Header
        line = reader.readLine();

        if(!"Item".equals(line)) {
            throw new IOException("Wrong header: " + line);
        }

        // Version
        line = reader.readLine();

        if(!"v.1".equals(line)) {
            throw new IOException("Wrong version: " + line);
        }

        line = reader.readLine();
        key = line;

        int n;

        line = reader.readLine();
        n = Integer.parseInt(line);
        for(int i=0; i<n; i++) {
            line = reader.readLine();
            if(i < strings.length) {
                strings[i] = line;
            }
        }

        line = reader.readLine();
        n = Integer.parseInt(line);
        for(int i=0; i<n; i++) {
            line = reader.readLine();
            if(i < ints.length) {
                ints[i] = Integer.parseInt(line);
            }
        }

        line = reader.readLine();
        n = Integer.parseInt(line);
        for(int i=0; i<n; i++) {
            if(i < triplets.length) {
                triplets[i].read(reader);
            } else {
                reader.readLine();
            }
        }
    }

    @Override
    public String toString()
    {
        return key;
    }
}
