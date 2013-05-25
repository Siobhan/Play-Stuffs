/*
 * Cardinal.java
 *
 * Created on 19.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.objects;

/**
 * A class made as a key class for map lookup operations.
 * It allows to set the key value, so one Cardinal object
 * can be used for many lookups, without the need to instantiate
 * new key objects all over (as would be needed with Integer keys).
 *
 * Be careful though, not to change the key value of Cardinals
 * that are kept in maps already as keys.
 *
 * It can be used in mixed comparisons with Integer objects.
 *
 * @see Registry
 * @author Hj. Malthaner
 */
public class Cardinal
{
    private int value;

    /**
     * @return the int value of this Cardinal.
     */
    public int intValue()
    {
        return value;
    }

    /**
     * Set a new int value for this Cardinal.
     * @param value The new value.
     */
    public void set(int value)
    {
        this.value = value;
    }

    /**
     * Create a new Cardinal with the given value.
     * @param value The initial value.
     */
    public Cardinal(int value) {
        this.value = value;
    }

    @Override
    public int hashCode()
    {
        return value;
    }

    /**
     * Cardinals can equal other Cardinals as well as Integers.
     * 
     * @param other the object to compare to.
     * @return true if and only if equal.
     * @see Integer
     */
    @Override
    public boolean equals(Object other)
    {
        if(other instanceof Cardinal) {
            Cardinal comp = (Cardinal)other;
            return comp.value == value;
        }

        if(other instanceof Integer) {
            Integer comp = (Integer)other;
            return comp.intValue() == value;
        }

        return false;
    }
}
