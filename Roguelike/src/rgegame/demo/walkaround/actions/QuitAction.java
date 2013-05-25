/*
 * QuitAction.java
 *
 * Created on 12.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */
package rgegame.demo.walkaround.actions;

import javax.swing.JFrame;

import rgegame.control.GameAction;

/**
 * Quit the walkaround demo.
 * 
 * @author Hj. Malthaner
 */
public class QuitAction implements GameAction
{
    /** Needed as callback */
    private JFrame frame;

    public QuitAction(JFrame frame)
    {
        this.frame = frame;
    }

    /**
     * Get a human-understandable name for this game action.
     */
    public String getName()
    {
        return "Quit game";
    }

    /**
     * Move a mobile object on the map.
     * Key presses will be repeated if the player holds
     * down the key.
     */
    public void onKeyPressed()
    {
        // Quit the walkaround demo.
        frame.setVisible(false);
        frame.dispose();
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
