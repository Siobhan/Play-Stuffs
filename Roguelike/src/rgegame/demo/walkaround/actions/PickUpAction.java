/*
 * PickUpAction.java
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

package rgegame.demo.walkaround.actions;

import rgegame.control.GameAction;
import rgegame.demo.walkaround.Item;
import rgegame.demo.walkaround.LayerCodes;
import rgegame.entities.PlayerEntity;
import rgegame.menu.MessageDisplayArea;

/**
 * Game action to pick up an item from the ground.
 * 
 * @author Hj. Malthaner
 */
public class PickUpAction implements GameAction
{
    private final PlayerEntity player;
    private final MessageDisplayArea messageDisplayArea;

    public PickUpAction(PlayerEntity player,
                        MessageDisplayArea messageDisplayArea)
    {
        this.player = player;
        this.messageDisplayArea = messageDisplayArea;
    }

    /**
     * Get a human-understandable name for this game action.
     */
    public String getName()
    {
        return "Pick something up";
    }

    public void onKeyPressed()
    {
        final int itemCode = player.gameMap.get(LayerCodes.ITEMS,
                                                player.location.x,
                                                player.location.y);

        if(itemCode != 0) {

            if(player.inventory.size() > 22) {
                messageDisplayArea.addMessageTop("You cannot carry any more items.");
            } else {
                final Item item = player.objectRegistry.get(itemCode);

                messageDisplayArea.addMessageTop("You pick up a " +
                                                 item.getName().toLowerCase() +
                                                 ".");

                player.gameMap.set(LayerCodes.ITEMS,
                                   player.location.x,
                                   player.location.y,
                                   0);

                player.inventory.add(item);
            }
        } else {
            messageDisplayArea.addMessageTop("There is nothing to pick up here.");
        }
    }

    public void onKeyReleased() {
    }
}
