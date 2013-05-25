/*
 * InventoryAction.java
 *
 * Created on 2010/07/21
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */
package rgegame.demo.walkaround.actions;

import java.util.ArrayList;
import javax.swing.JFrame;

import rgegame.control.GameAction;
import rgegame.demo.walkaround.Item;
import rgegame.entities.PlayerEntity;
import rgegame.menu.MultipleChoiceBox;

/**
 * Quit the walkaround demo.
 * 
 * @author Hj. Malthaner
 */
public class InventoryAction implements GameAction
{
    /** Needed as callback */
    private final JFrame frame;
    private final PlayerEntity player;

    public InventoryAction(JFrame frame, PlayerEntity player)
    {
        this.frame = frame;
        this.player = player;
    }

    /**
     * Get a human-understandable name for this game action.
     */
    public String getName()
    {
        return "Show inventory";
    }

    /**
     * Move a mobile object on the map.
     * Key presses will be repeated if the player holds
     * down the key.
     */
    public void onKeyPressed()
    {
        ArrayList<Item> inventory = player.inventory;

        StringBuffer list = new StringBuffer();

        if(inventory.isEmpty()) {
            list.append("You have nothing.<br>");
        } else {
            for(int i=0; i<inventory.size(); i++) {
                list.append('(');
                list.append((char)('a' + i));
                list.append(") ");
                list.append(inventory.get(i).getName());
                list.append(".<br>");
            }
        }
        
        MultipleChoiceBox mcb =
                new MultipleChoiceBox(frame,
                                      "Your inventory:",
                                      list.toString(),
                                      "Select which?");

        mcb.setSize(300, 100+inventory.size()*19);
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
