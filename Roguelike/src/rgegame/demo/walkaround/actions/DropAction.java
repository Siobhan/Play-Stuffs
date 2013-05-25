/*
 * DropAction.java
 *
 * Created on 2010/07/29
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.demo.walkaround.actions;

import rgegame.control.GameAction;
import rgegame.demo.walkaround.MapControl;
import rgegame.entities.PlayerEntity;

/**
 * Game action to drop an item from the players inventory to the ground.
 * 
 * @author Hj. Malthaner
 */
public class DropAction implements GameAction
{
    private final PlayerEntity player;
    private final MapControl mapControl;

    public DropAction(PlayerEntity player,
                      MapControl mapControl)
    {
        this.player = player;
        this.mapControl = mapControl;
    }

    /**
     * Get a human-understandable name for this game action.
     */
    public String getName()
    {
        return "Drop item from inventory";
    }

    public void onKeyPressed()
    {
        mapControl.doDropItem(player);
    }

    public void onKeyReleased() {
    }
}
