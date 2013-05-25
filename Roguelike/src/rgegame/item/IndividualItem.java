/*
 * IndividualItem.java
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
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * <p>A read-write data container for generic item data.</p>
 *
 * <p>Check http://www.funkelwerk.de/forum/index.php?topic=341.0
 * for more details on the design ideas behind this class</p>
 *
 * @see AbstractItem
 * @author Hj. Malthaner
 */
public class IndividualItem
{
    /** Items only make sense in the context of a catalog */
    private final ItemCatalog itemCatalog;

    /** Values which deviate from the base item are kept in here */
    private final Hashtable <String, Object> modContainer;
    
    /** Key to the base item */
    private String key;

    /**
     * Unique key (within the item catalog) for this item data
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
        final String tkey = "S" + index;
        String result = (String)modContainer.get(tkey);

        if(result == null) {
            AbstractItem baseItem = itemCatalog.getAbstractItem(key);
            result = baseItem.getString(index);
        }

        return result;
    }

    public void setString(int index, String value)
    {
        final String tkey = "S" + index;
        modContainer.put(tkey, value);
    }

    /**
     * Access integer data through index
     */
    public int getInt(int index)
    {
        final String tkey = "I" + index;
        Integer result = (Integer)modContainer.get(tkey);

        if(result == null) {
            AbstractItem baseItem = itemCatalog.getAbstractItem(key);
            return baseItem.getInt(index);
        }

        return result;
    }

    public void setInt(int index, int value)
    {
        final String tkey = "I" + index;
        modContainer.put(tkey, new Integer(value));
    }

    /**
     * Access triplet data through index
     */
    public Triplet getTriplet(int index)
    {
        final String tkey = "T" + index;
        Triplet result = (Triplet)modContainer.get(tkey);

        if(result == null) {
            AbstractItem baseItem = itemCatalog.getAbstractItem(key);
            result = baseItem.getTriplet(index);
        }

        return result;
    }
    
    public void setTriplet(int index, Triplet value)
    {
        final String tkey = "T" + index;
        modContainer.put(tkey, value);
    }

    public IndividualItem(String key, ItemCatalog itemCatalog)
    {
        this.key = key;
        this.itemCatalog = itemCatalog;

        modContainer = new Hashtable <String, Object> ();

        if(itemCatalog.getAbstractItem(key) == null) {
            throw new IllegalArgumentException("Item " + key + " not in catalog.");
        }
    }

    public IndividualItem(BufferedReader reader, ItemCatalog itemCatalog)
            throws IOException
    {
        modContainer = new Hashtable <String, Object> ();
        this.itemCatalog = itemCatalog;
        
        read(reader);
    }

    public void write(Writer writer) throws IOException
    {
        writer.write("Individual Item\n");
        writer.write("v.1\n");
        writer.write(key + "\n");

        Enumeration keys = modContainer.keys();
        while(keys.hasMoreElements()) {
            String tkey = (String)keys.nextElement();
            Object value = (Object)modContainer.get(tkey);

            writer.write(tkey + "\n");
            writer.write(value + "\n");
        }

        writer.write("End Of Item\n");
    }


    public void read(BufferedReader reader) throws IOException
    {
        modContainer.clear();

        String line;

        // Header
        line = reader.readLine();

        if(!"Individual Item".equals(line)) {
            throw new IOException("Wrong header: " + line);
        }

        // Version
        line = reader.readLine();

        if(!"v.1".equals(line)) {
            throw new IOException("Wrong version: " + line);
        }

        // Key
        key = reader.readLine();
        
        do {
            line = reader.readLine();
            if("End Of Item".equals(line) == false) {
                String tkey = line;

                if(tkey.startsWith("S")) {
                    line = reader.readLine();
                    modContainer.put(tkey, line);
                }

                if(tkey.startsWith("I")) {
                    line = reader.readLine();
                    modContainer.put(tkey, new Integer(Integer.parseInt(line)));
                }

                if(tkey.startsWith("T")) {
                    Triplet tri = new Triplet(reader);
                    modContainer.put(tkey, tri);
                }

            } else {
                break;
            }
        } while(true);
    }
}
