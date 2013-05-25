/*
 * Registry.java
 *
 * Created on 19.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.objects;

import java.util.HashMap;
import java.util.Set;

/**
 * Since the LayeredMap class can only store integers,
 * but most games need some kind of objects to keep track
 * of item data, this class was introduced to create the
 * association from integers (map data) to some sort of
 * game-defined item data.
 * 
 * It can be used more generic, too, for any sort of
 * integer based object lookup.
 *
 * This class uses a Cardinal object internally to keep
 * object allocation for map lookup low.
 *
 * @see Cardinal
 * @see rgegame.map.data.LayeredMap
 *
 * @author Hj. Malthaner
 */
public class Registry <E>
{
    private final HashMap <Cardinal, E> map;
    private final Cardinal key;

    /**
     * Put a new object into the registry
     * @param key The objects key
     * @param item The object
     */
    public void put(int key, E item)
    {
        map.put(new Cardinal(key), item);
    }

    /**
     * Find the key for the item in the registry. Will do a full
     * table scan.
     * @param item The item to search.
     * @return 0 if not found, the item key otherwise
     */
    public int findKey(E item)
    {
        final Set <Cardinal> keys = map.keySet();

        for(final Cardinal k : keys) {
            E comp = map.get(k);
            if(comp.equals(item)) {
                return k.intValue();
            }
        }
        
        return 0;
    }

    /**
     * Find the next free key in the registry.
     * @return the next free key.
     */
    public int nextFreeKey()
    {
        final Set <Cardinal> keys = map.keySet();

        int index = 1;
        do {
            key.set(index);
            if(keys.contains(key) == false) {
                return index;
            }
            index ++;
        } while(true);
    }

    /**
     * Look up the object bound to keyValue.
     *
     * @param keyValue The key value
     * @return The bound object or null if there is no such object
     */
    synchronized public E get(int keyValue)
    {
        key.set(keyValue);
        return map.get(key);
    }

    /**
     * Clear the registry from all registered items.
     */
    public void clear()
    {
        map.clear();
    }
    
    /**
     * Create a new object registry for type E
     */
    public Registry()
    {
        map = new HashMap<Cardinal, E>();
        key = new Cardinal(0);
    }
}
