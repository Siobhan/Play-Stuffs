/*
 * HelpAction.java
 *
 * Created on 2010/07/25
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */
package rgegame.demo.walkaround.actions;

import java.util.Set;
import javax.swing.JFrame;
import javax.swing.KeyStroke;

import rgegame.control.ActionKey;
import rgegame.control.GameAction;
import rgegame.control.KeyInputHandler;
import rgegame.menu.MultipleChoiceBox;

/**
 * Show a help message with all key bindings.
 * 
 * @author Hj. Malthaner
 */
public class HelpAction implements GameAction
{
    private JFrame frame;
    private KeyInputHandler keyInputHandler;

    public HelpAction(JFrame frame, KeyInputHandler keyInputHandler)
    {
        this.frame = frame;
        this.keyInputHandler = keyInputHandler;
    }

    /**
     * Get a human-understandable name for this game action.
     */
    public String getName()
    {
        return "Help";
    }

    /**
     * Show a help message with all key bindings.
     */
    public void onKeyPressed()
    {
        StringBuffer list = new StringBuffer();
        Set <ActionKey> keys = keyInputHandler.getKeys();

        for(ActionKey key : keys) {
            list.append('(');

            if(key.getNeedsShift()) {
                list.append("Shift-");
            }
            if(key.getNeedsCtrl()) {
                list.append("Ctrl-");
            }

            // Is there a better way to get the text representation for a
            // key code? This depends on the format of toString() response
            // and might break with every format change (depends on Java version)
            GameAction action = keyInputHandler.getAction(key);
            KeyStroke stroke = KeyStroke.getKeyStroke(key.getKeyCode(), 0);
            list.append(stroke.toString().substring(8));

            list.append(") ");
            list.append(action.getName());
            list.append(".<br>");
        }

        MultipleChoiceBox mcb =
                new MultipleChoiceBox(frame,
                                      "The key bindings:",
                                      list.toString(),
                                      "Press escape to continue.");

        mcb.setSize(300, 100+keys.size()*19);
        mcb.center(frame);

        mcb.setVisible(true);
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
