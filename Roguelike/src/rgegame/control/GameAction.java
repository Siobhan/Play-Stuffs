/*
 * GameAction.java
 *
 * Created on 08.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.control;

/**
 * Interface of all game actions.
 * 
 * @author Hj. Malthaner
 */
public interface GameAction
{
    /**
     * Get a human-understandable name for this game action, e.g. to
     * show a comprehensive list of action (names).
     */
    public String getName();
    
    /**
     * Do whatever this action has to do on a key press.
     * Key presses will be repeated if the player holds
     * down the key.
     */
    public void onKeyPressed();

    /**
     * Do whatever this action has to do on a key release.
     * There is only one key release event generated after
     * a (series) of key press events.
     */
    public void onKeyReleased();
}
