/*
 * KeyInputHandler.java
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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The KeyInputHandler calls game actions which are triggered by key
 * events.
 * @see GameAction
 *
 * @author Hj. Malthaner
 */
public class KeyInputHandler implements KeyListener
{
    private Map <ActionKey, GameAction> actions;

    /**
     * Add a new game action for the given key binding.
     *
     * @param action The game action to add.
     * @param key The key to bind the action to.
     */
    public void addAction(ActionKey key, GameAction action)
    {
        actions.put(key, action);
    }

    /**
     * Get the action bound to the key event.
     * @param key The action key
     * @return The bound action or null if no actions was bound to this event.
     */
    public GameAction getAction(ActionKey key)
    {
        return actions.get(key);
    }

    /**
     * Get a set of all defined action keys.
     * @return a set of all defined action keys.
     */
    public Set <ActionKey> getKeys()
    {
        return actions.keySet();
    }

    /**
     * Craetes a new KeyInputHandler with an empty list of
     * actions.
     */
    public KeyInputHandler()
    {
        actions = new HashMap<ActionKey, GameAction>();
    }

    /** Implements KeyListener */
    public void keyTyped(KeyEvent e)
    {
    }

    /** Implements KeyListener, calls onKeyPressed() for matching actions */
    public void keyPressed(KeyEvent e)
    {
        GameAction action = getAction(e);

        if(action != null) {
            action.onKeyPressed();
        }
    }

    /** Implements KeyListener, calls onKeyRelease() for matching actions */
    public void keyReleased(KeyEvent e)
    {
        GameAction action = getAction(e);

        if(action != null) {
            action.onKeyReleased();
        }
    }

    /**
     * Get the action bound to the key event.
     * @param e The key event
     * @return The bound action or null if no actions was bound to this event.
     */
    private GameAction getAction(KeyEvent e)
    {
        final int code = e.getKeyCode();
        final boolean  shift = e.isShiftDown();
        final boolean  ctrl = e.isControlDown();

        // System.err.println("Key code: " + code);

        // Hajo: find an action for this event.
        ActionKey key = new ActionKey(code, shift, ctrl);

        GameAction action = actions.get(key);
        return action;
    }
}
