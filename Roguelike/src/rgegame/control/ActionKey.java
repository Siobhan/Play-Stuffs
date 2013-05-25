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
 * A key binding for a game action.
 * 
 * @author Hj. Malthaner
 */
public class ActionKey
{
    /** Key code to bind to */
    private int keyCode;
    private boolean needsShift;
    private boolean needsCtrl;

    /**
     * @return The key keyCode which this action is bound to.
     */
    public int getKeyCode()
    {
        return keyCode;
    }

    /**
     * @return the needsShift
     */
    public boolean getNeedsShift() {
        return needsShift;
    }

    /**
     * @return the needsCtrl
     */
    public boolean getNeedsCtrl() {
        return needsCtrl;
    }


    /**
     * Bind this action to the given keyCode
     * @param keyCode The code to trigger this action.
     */
    public ActionKey(int keyCode,
                         boolean needsShift,
                         boolean needsCtrl)
    {
        this.keyCode = keyCode;
        this.needsCtrl = needsCtrl;
        this.needsShift = needsShift;
    }

    @Override
    public boolean equals(Object other)
    {
        if(other instanceof ActionKey) {
            ActionKey key = (ActionKey)other;
            return
                    this.keyCode == key.keyCode &&
                    this.needsCtrl == key.needsCtrl &&
                    this.needsShift == key.needsShift;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.keyCode;
        hash = 29 * hash + (this.needsShift ? 1 : 0);
        hash = 29 * hash + (this.needsCtrl ? 1 : 0);
        return hash;
    }
}
