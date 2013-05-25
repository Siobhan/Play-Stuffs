/*
 * Item.java
 *
 * Created on 20.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.demo.walkaround;

//import rgegame.map.display.ColorCodes;

/**
 * Placeholder/dummy only at the moment.
 * 
 * @author Hj. Malthaner
 */
public class Item
{
    private final String name;
    private final int displayCode;

    public String getName()
    {
        return name;
    }

    public int getDisplayCode()
    {
        return displayCode;
    }

    public Item(String name, int displayCode)
    {
        this.name = name;
        this.displayCode = displayCode;
    }
}
