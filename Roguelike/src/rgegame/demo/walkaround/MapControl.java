/*
 * MapControl.java
 *
 * Created on 18.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.demo.walkaround;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.JFrame;

import rgegame.demo.pathfinding.PathSourceLink;
import rgegame.entities.PlayerEntity;
import rgegame.map.data.LayeredMap;
import rgegame.map.display.ColorCodes;
import rgegame.map.generators.Dungeon;
import rgegame.map.generators.MinesGenerator;
import rgegame.menu.MessageDisplayArea;
import rgegame.menu.MultipleChoiceBox;
import rgegame.pathfinding.Area;
import rgegame.pathfinding.PathSource;

/**
 * Controller class for map altering actions.
 * 
 * @author Hj. Malthaner
 */
public class MapControl
{
    private final JFrame frame;
    private final MapDisplay mapDisplay;
    private MessageDisplayArea messageDisplayArea;
    private MessageDisplayArea statDisplayArea;

    /**
     * Handle event if player bumped into something.
     * @param feature What was there?
     * @param color The color of the feature.
     * @param mapX X coordinate of the feature.
     * @param mapY Y coordinate of the feature.
     */
    public void doBump(final PlayerEntity player,
                       final int feature, final int color,
                       final int mapX, final int mapY)
    {
        if(feature == FeatureCodes.FOUNTAIN) {
            MultipleChoiceBox mcb =
                    new MultipleChoiceBox(frame,
                                          "You see a fountain.",
                                          "(a) Drink from fountain.<br>" +
                                          "(b) Wash hands.<br>" +
                                          "(esc) Leave fountain.<br>",
                                          "What to do?");

            mcb.setSize(300, 180);
            mcb.center(frame);

            mcb.setVisible(true);
        } else if(feature == FeatureCodes.DOOR_SHUT) {
            // Door bump, rather.
            player.gameMap.set(LayerCodes.FEATURE, mapX, mapY, FeatureCodes.DOOR_OPEN + color);
            mapDisplay.recalcFov(player.location.x, player.location.y);
            showMessage("You successfully opened the door.");
        } else if(feature == FeatureCodes.STAIRS_DOWN) {
            showMessage("You enter a maze of staircases.");
            makeNewMap(player);
        } else if(feature == FeatureCodes.ENEMY) {
        	player.setMana(player.getMana() + 1);
        	showStats(
            "<html>Health: " + player.getHealth() +
            " Mana: " + player.getMana() +
            "</html>");
        }
    }

    /**
     * Do a close type action. Will reset mode to MODE_MOVE after being
     * triggered.
     */
    public void doClose(final PlayerEntity player,
                        final int mapX, final int mapY)
    {
        final int code = player.gameMap.get(LayerCodes.FEATURE, mapX, mapY);

        final int feature = code & 0xFFFF;
        final int color = code & 0xFF0000;

        if(feature == FeatureCodes.DOOR_OPEN) {
            player.gameMap.set(LayerCodes.FEATURE, mapX, mapY, FeatureCodes.DOOR_SHUT + color);
            mapDisplay.recalcFov(player.location.x, player.location.y);

            showMessage("You close the door.");
        }

        player.setMode(PlayerEntity.MODE_MOVE);
    }

    /**
     * Drop an item from the players inventory.
     * 
     * @param player The player.
     */
    public void doDropItem(final PlayerEntity player)
    {
        showMessage("Select item to drop:");
        Item item = selectItemFromInventory(player);

        if(item != null) {
            int key = player.objectRegistry.findKey(item);

            if(key == 0) {
                key = player.objectRegistry.nextFreeKey();
                player.objectRegistry.put(key, item);
            }

            player.gameMap.set(LayerCodes.ITEMS,
                               player.location.x, player.location.y,
                               key);

            player.inventory.remove(item);
        }
    }

    /**
     * Select an item from the players inventory.
     *
     * @param player The player.
     * @return The selected item or null if no item was selected.
     */
    public Item selectItemFromInventory(PlayerEntity player)
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

        mcb.setSize(300, 100+inventory.size()*15);
        mcb.center(frame);

        mcb.setVisible(true);

        final int selection = mcb.getResponseKey() - 'a';
        
        Item item = null;
        if(selection >= 0 && selection<player.inventory.size()) {
            item = player.inventory.get(selection);
        }
        return item;
    }

    /**
     * Call this after player move to update the field of view.
     * 
     * @param player The player to update.
     */
    public void playerMoved(final PlayerEntity player)
    {
        mapDisplay.recalcFov(player.location.x, player.location.y);

        int code = player.gameMap.get(LayerCodes.ITEMS, player.location.x, player.location.y);
        if(code != 0) {
            Item item = player.objectRegistry.get(code);
            showMessage("You see a " + item.getName().toLowerCase() + ".");
        } else {
 //       	player.setHealth(player.getHealth() - 1);
 //       	showStats(
 //           "<html>Health: " + player.getHealth() +
 //           " Mana: " + player.getMana() +
 //           "</html>");
        }
    }

    /**
     * Create a new map and set the display up with the new map data.
     */
    public void makeNewMap(PlayerEntity player)
    {
      LayeredMap map;
      map = makeDungeonMap();

//        if(Math.random() < 0.5) {
//            map = makeDungeonMap();
//        } else {
//            map = makeMinesMap();
//        }

        Area area = new Area();
        PathSource pathSource = new PathSourceLink(map);
        area.findArea(pathSource, map.getSpawnX(), map.getSpawnY());

        ArrayList<Point> list = area.getArea();

        for(int i=0; i<5; i++) {
            int index = (int)(Math.random() * list.size());
            final Point p = list.get(index);
            // Place testing item - gold key
            if(map.get(LayerCodes.FEATURE, p.x, p.y) == FeatureCodes.OPEN) {
                map.set(LayerCodes.ITEMS, p.x, p.y, ((int)(Math.random()*4)) + 1);
            }
            list.remove(index);
        }

        // register object code.
        player.objectRegistry.put(1, new Item("Gold key", '"' + ColorCodes.YELLOW));
        player.objectRegistry.put(2, new Item("Rusty iron key", '"' + ColorCodes.COPPER));
        player.objectRegistry.put(3, new Item("Rock", ',' + ColorCodes.WHITE));
        player.objectRegistry.put(4, new Item("Mushroom", ';' + ColorCodes.PINK));
//        player.objectRegistry.put(5, new Item("Enemy", 'e' + ColorCodes.RED));

        mapDisplay.setMap(map);
    }

    /**
     * Make a dungeon type map.
     * @return A dungeon type map, with one stairs up feature.
     */
    private LayeredMap makeDungeonMap()
    {
        // Usually the properties would be loaded from a file ...
        Properties props = new Properties();
        props.setProperty("map_layers", "4");
        props.setProperty("roomGapHoriz", "7");
        props.setProperty("roomGapVert", "3");
        props.setProperty("floors", ".:s");
        props.setProperty("room_floors", ".:s");
        props.setProperty("corridor_floors", ".:s");
        // props.setProperty("door", "+:u");

        Dungeon dungeon = new Dungeon("", props);
        LayeredMap map = dungeon.generate(65, 40);
        return map;
    }

    /**
     * Make a mines type map.
     * @return A dungeon type map, with one stairs up feature.
     */
    private LayeredMap makeMinesMap()
    {
        Properties props = new Properties();
        props.setProperty("map_layers", "4");
        props.setProperty("walls", "#:s, #:s, %:u");
        props.setProperty("floors", ".:s");
        props.setProperty("branch_chance", "250");

        MinesGenerator mine = new MinesGenerator(props);
        LayeredMap map = mine.generate(65, 40);

        Area area = new Area();
        PathSource pathSource = new PathSourceLink(map);
        area.findArea(pathSource, map.getSpawnX(), map.getSpawnY());

        final ArrayList <Point> squares = area.getArea();
        final Point p = squares.get((int)(Math.random() * squares.size()));

        map.set(LayerCodes.FEATURE,
                p.x, p.y-1, FeatureCodes.STAIRS_UP + ColorCodes.RED);

        return map;
    }


    /**
     * Set message area to display game messages.
     * @param messageDisplayArea Message are for gam,e messages. Pass null
     *                           to trun message display off.
     */
    public void setMessageDisplayArea(MessageDisplayArea messageDisplayArea)
    {
        this.messageDisplayArea = messageDisplayArea;
    }

    /**
     * Set message area to display game messages.
     * @param statDisplayArea Message health mana.
     */
    public void setStatDisplayArea(MessageDisplayArea statDisplayArea)
    {
        this.statDisplayArea = statDisplayArea;
    }
    
    public MapControl(JFrame parent, MapDisplay mapDisplay)
    {
        this.frame = parent;
        this.mapDisplay = mapDisplay;
    }

    
    /**
     * Display a message in the display area (if set).
     * @param message The message to display. Can contain html code.
     */
    private void showMessage(String message)
    {
        if(messageDisplayArea != null) {
            	messageDisplayArea.addMessageTop(message);
        }
    }
    
    /**
     * Display stats.
     */
    private void showStats(String message)
    {
        if(statDisplayArea != null) {
        	statDisplayArea.addMessageTop(message);
        }
    }
}
