/*
 * CloseAction.java
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

package rgegame.demo.walkaround.actions;

import rgegame.control.GameAction;
import rgegame.entities.PlayerEntity;

/**
 * Trigger a "close" type action on the next move.
 * 
 * @author Hj. Malthaner
 */
public class CloseAction implements GameAction
{
    private final PlayerEntity player;

    public CloseAction(final PlayerEntity player)
    {
        this.player = player;
    }

    /**
     * Get a human-understandable name for this game action.
     */
    public String getName()
    {
        return "Close";
    }

    /**
     * Close something with the next player move command.
     */
    public void onKeyPressed()
    {
        player.setMode(PlayerEntity.MODE_CLOSE);
    }

    /**
     * Do whatever this action has to do on a key release.
     * There is only one key release event generated after
     * a (series) of key press events.
     */
    public void onKeyReleased()
    {
        // Nothing to do here.
    }
}
