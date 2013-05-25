/*
 * ItemCatalog.java
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
import java.util.Hashtable;
import java.util.Vector;

/**
 * A catalog structure for items.
 * 
 * @author Hj. Malthaner
 */
public class ItemCatalog
{
    /**
     * All items in this catalog share the configuration.
     * Configuration cannot be changed during the lifetime of a catalog.
     */
    private final ItemConfiguration itemConfiguration;

    private final Vector <AbstractItem> itemsVec;
    private final Hashtable <String, AbstractItem> itemsHash;

    /**
     * @return The item configuration of the items in this catalog.
     */
    public ItemConfiguration getItemConfiguration()
    {
        return itemConfiguration;
    }

    /**
     * Items are addressed by their keys.
     *
     * @param key The item key.
     * @return The item for the key or null if not found.
     */
    public AbstractItem getAbstractItem(final String key)
    {
        return itemsHash.get(key);
    }

    /**
     * @return The items of this catalog in the catalogs natural order.
     */
    public AbstractItem [] getAllItemsSorted()
    {
        return itemsVec.toArray(new AbstractItem[itemsVec.size()]);
    }

    /**
     * Add an item to the item list.
     * @param ait The item to add.
     */
    public void addItem(AbstractItem ait)
    {
        for(int i=0; i<itemsVec.size(); i++) {
            if(ait.getKey().equals(itemsVec.get(i).getKey())) {
                itemsVec.set(i, ait);
                itemsHash.put(ait.getKey(), ait);
                return;
            }
        }

        itemsVec.add(ait);
        itemsHash.put(ait.getKey(), ait);
    }

    /**
     * Remove an item from the item list.
     * @param key The item key.
     */
    public void removeItem(final String key)
    {
        final AbstractItem item = itemsHash.get(key);
        itemsVec.remove(item);
        itemsHash.remove(item.getKey());
    }

    /**
     * Move an item in the item list. This method swaps
     * the items from position index and index+offset
     * @param index Base item index.
     * @param offset Second item offset from base index.
     */
    public void moveItem(int index, int offset)
    {
        if(index + offset >= 0 && index + offset < itemsVec.size()) {
            AbstractItem one = itemsVec.get(index);
            AbstractItem two = itemsVec.get(index+offset);

            itemsVec.set(index, two);
            itemsVec.set(index+offset, one);
        }
    }


    /**
     * Build a new item catalog with the given item configuartion
     * @param config The item configuration to use for all itemsVec in this catalog.
     */
    public ItemCatalog(ItemConfiguration config)
    {
        this.itemConfiguration = config;
        
        itemsVec = new Vector<AbstractItem>(128);
        itemsHash = new Hashtable<String, AbstractItem>();
    }

    public void read(BufferedReader reader) throws IOException
    {
        // Header
        String line = reader.readLine();
        if(!"Item Catalog".equals(line)) {
            throw new IOException("Wrong header: " + line);
        }

        // Version
        line = reader.readLine();
        if(!"v.1".equals(line)) {
            throw new IOException("Wrong version: " + line);
        }

        line = reader.readLine();
        final int n = Integer.parseInt(line);

        itemsVec.clear();
        
        for(int i=0; i<n; i++) {
            AbstractItem ait = new AbstractItem(itemConfiguration, reader);
            itemsVec.add(ait);
            itemsHash.put(ait.getKey(), ait);
        }        
    }


    public void write(Writer writer) throws IOException
    {
        writer.write("Item Catalog\n");
        writer.write("v.1\n");

        writer.write("" + itemsVec.size() + "\n");

        for(int i=0; i<itemsVec.size(); i++) {
            itemsVec.get(i).write(writer);
        }
    }
}
