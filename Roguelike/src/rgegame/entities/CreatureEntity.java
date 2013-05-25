package rgegame.entities;

import java.awt.Point;
import java.util.ArrayList;

import rgegame.demo.walkaround.Item;
import rgegame.objects.Registry;

public class CreatureEntity extends Entity{

	/**
     * Current location of the creature
     */
    public final Point location;

    public CreatureEntity(int creatureX, int creatureY, int health, int mana)
    {
		super(health, mana);
        location = new Point(creatureX, creatureY);
    } 
}
