/*
 * MoveAction.java
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

package rgegame.demo.walkaround.actions;

import rgegame.control.GameAction;
import rgegame.demo.walkaround.FeatureCodes;
import rgegame.demo.walkaround.LayerCodes;
import rgegame.demo.walkaround.MapControl;
import rgegame.entities.PlayerEntity;


/**
 * React to a movement input for the given player.
 * 
 * @author Hj. Malthaner
 */
public class MoveAction implements GameAction
{
    private PlayerEntity player;
    private MapControl mapControl;
    
    /** The offset for a move */
    private int dx, dy;

    public MoveAction(PlayerEntity player,
                      int dx, int dy,
                      MapControl mapControl)
    {
        this.player = player;
        this.mapControl = mapControl;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Get a human-understandable name for this game action.
     */
    public String getName()
    {
        return "Move";
    }

    /**
     * Move a mobile object on the map.
     * Key presses will be repeated if the player holds
     * down the key.
     */
    public void onKeyPressed()
    {
        // Dispatch according to mode.
        switch(player.getMode()) {
            case PlayerEntity.MODE_CLOSE:
                doClose();
                break;
            default:
                doMove();
        }
    }

    /**
     * Do a move type actions
     */
    private void doMove()
    {
        final int mapX = player.location.x+dx;
        final int mapY = player.location.y+dy;

        final int code = player.gameMap.get(LayerCodes.FEATURE, mapX, mapY);

        final int feature = code & 0xFFFF;
        final int color = code & 0xFF0000;

        if(feature != FeatureCodes.OPEN && feature != FeatureCodes.DOOR_OPEN) {
            doBump(feature, color, mapX, mapY);
            return;
        }

        // Hajo: do the move now

        final int playerCode =
                player.gameMap.get(LayerCodes.MOBILES, player.location.x, player.location.y);
        
        player.gameMap.set(LayerCodes.MOBILES, player.location.x, player.location.y, 0);

        player.location.x += dx;
        player.location.y += dy;

        player.gameMap.set(LayerCodes.MOBILES, player.location.x, player.location.y, playerCode);

        mapControl.playerMoved(player);
    }

    /**
     * Handle event if player bumped into something.
     * @param feature What was there?
     * @param color The color of the feature.
     * @param mapX X coordinate of the feature.
     * @param mapY Y coordinate of the feature.
     */
    private void doBump(final int feature, final int color,
                        final int mapX, final int mapY)
    {
        if(feature == FeatureCodes.WALL_BRICKS ||
           feature == FeatureCodes.WALL_ROCK) {
            // Hajo: Wall bump ... do nothing.
        } else {
            mapControl.doBump(player, feature, color, mapX, mapY);
        }
    }

    /**
     * Do a close type action. Will reset mode to MODE_MOVE after being
     * triggered.
     */
    private void doClose()
    {
        final int mapX = player.location.x+dx;
        final int mapY = player.location.y+dy;

        mapControl.doClose(player, mapX, mapY);
        
        player.setMode(PlayerEntity.MODE_MOVE);
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
