/*
 * Player.java
 *
 * Created on 11.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */
package rgegame.entities;

import java.awt.Point;
import java.util.ArrayList;

import rgegame.map.data.LayeredMap;
import rgegame.objects.Registry;
import rgegame.demo.walkaround.Item;
import rgegame.entities.Entity;

/**
 * In a real game, this would keep real player data.
 * 
 * @author Hj. Malthaner
 */
public class PlayerEntity extends Entity
{
    public static final int MODE_MOVE = 0;
    public static final int MODE_CLOSE = 1;

    /**
     * Current location of the player
     */
    public final Point location;

    /**
     * The map data that is shown to the player (will not show
     * hidden features or changed to "remembered" areas).
     */
    public LayeredMap displayMap;

    /**
     * The actual game map. All game functions should use this.
     */
    public LayeredMap gameMap;

    public final Registry <Item> objectRegistry;

    /**
     * The players inventory
     */
    public final ArrayList <Item> inventory;


    /**
     * Operating mode for player moves.
     */
    private int mode;

    public int getMode()
    {
        return mode;
    }
    
    /**
     * The "MoveAction" can operate on different modes. The modes will
     * be set per player, since a player can have several move actions
     * which all should be influenced by the mode setting.
     *
     * @param mode The mode to set.
     */
    public void setMode(int mode)
    {
        this.mode = mode;
    }

    public PlayerEntity(int playerX, int playerY)
    {
		super();
        location = new Point(playerX, playerY);
        objectRegistry = new Registry<Item>();
        inventory = new ArrayList<Item>();
    }   

    public PlayerEntity(int playerX, int playerY, int health, int mana)
    {
		super(health, mana);
        location = new Point(playerX, playerY);
        objectRegistry = new Registry<Item>();
        inventory = new ArrayList<Item>();
    }    
}
