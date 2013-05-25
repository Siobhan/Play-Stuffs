/*
 * WalkaroundMain.java
 *
 * Created on 06.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.demo.walkaround;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyEvent;

import rgegame.control.ActionKey;
import rgegame.control.KeyInputHandler;
import rgegame.demo.pathfinding.PathfinderThread;
import rgegame.demo.walkaround.actions.CloseAction;
import rgegame.demo.walkaround.actions.DropAction;
import rgegame.demo.walkaround.actions.HelpAction;
import rgegame.demo.walkaround.actions.InventoryAction;
import rgegame.demo.walkaround.actions.MoveAction;
import rgegame.demo.walkaround.actions.PickUpAction;
import rgegame.demo.walkaround.actions.QuitAction;
import rgegame.menu.MessageDisplayArea;
import rgegame.entities.PlayerEntity;
import rgegame.entities.CreatureEntity;

/**
 * Walkaround demo to showcase use and interaction of the
 * Roguelike Game Kit map, field of view and related modules.
 *
 * @author Hj. Malthaner
 */
public class WalkaroundMain extends javax.swing.JFrame
{
    private final PlayerEntity player;
    private final CreatureEntity enemy;

    /** Creates new form WalkaroundMain */
    public WalkaroundMain()
    {
        initComponents();

        enemy = new CreatureEntity(2, 2, 5, 0);
        player = new PlayerEntity(0, 0, 100, 0);

        MapDisplay mapDisplay = new MapDisplay(player);

        MessageDisplayArea messageDisplayArea = new MessageDisplayArea();
        messageDisplayArea.addMessageBottom(
                "<html>Welcome to your death!" +
                " Use the cursor keys, WASD or HJKL to move." +
                " Esc to exit, G to interact." +
                "</html>");
        messageDisplayArea.setForeground(Color.ORANGE);
        messageDisplayArea.setPreferredSize(new Dimension(800, 36));
        

        MessageDisplayArea statsDisplayArea = new MessageDisplayArea();
        statsDisplayArea.addMessageBottom(
                "<html>Health: " + player.getHealth() +
                " Mana: " + player.getMana() +
                "</html>");
        statsDisplayArea.setForeground(Color.GREEN);
        statsDisplayArea.setPreferredSize(new Dimension(800, 16));

        add(messageDisplayArea, BorderLayout.SOUTH);
        add(statsDisplayArea, BorderLayout.NORTH);
        add(mapDisplay, BorderLayout.CENTER);

        MapControl mapControl = new MapControl(this, mapDisplay);
        mapControl.setMessageDisplayArea(messageDisplayArea);
        mapControl.setStatDisplayArea(statsDisplayArea);
        mapControl.makeNewMap(player);

        installKeyHandler(mapDisplay, mapControl, messageDisplayArea);

        setTitle("Simple Roguelike");
        setSize(800, 780);
//@@@@@ Messsing with pathfinding        
//        PathfinderThread worker = new PathfinderThread(mapDisplay, player);
//        worker.start();
    }

    /**
     * Install the key handlers that we need for this demo.
     *
     * @param mapDisplay The map display
     * @param mapControl The map control
     */
    private void installKeyHandler(MapDisplay mapDisplay, 
                                   MapControl mapControl,
                                   MessageDisplayArea messageDisplayArea)
    {
        mapDisplay.setFocusable(true);
        mapDisplay.requestFocusInWindow();

        KeyInputHandler keyInputHandler = new KeyInputHandler();
        mapDisplay.addKeyListener(keyInputHandler);

        MoveAction up = new MoveAction(player, 0, -1, mapControl);
        MoveAction down = new MoveAction(player, 0, 1, mapControl);
        MoveAction left = new MoveAction(player, -1, 0, mapControl);
        MoveAction right = new MoveAction(player, 1, 0, mapControl);

        // Hajo: Cursor key bindings
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_UP, false, false), up);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_DOWN, false, false), down);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_LEFT, false, false), left);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_RIGHT, false, false), right);

        // Hajo: alternate set WASD
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_W, false, false), up);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_S, false, false), down);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_A, false, false), left);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_D, false, false), right);

        // Hajo: alternate set HJKL
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_J, false, false), up);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_K, false, false), down);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_H, false, false), left);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_L, false, false), right);

        CloseAction closeAction = new CloseAction(player);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_C, false, false), closeAction);

        QuitAction quitAction = new QuitAction(this);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_Q, true, false), quitAction);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_X, true, false), quitAction);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_ESCAPE, false, false), quitAction);

        PickUpAction pickUpAction = new PickUpAction(player, messageDisplayArea);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_G, false, false), pickUpAction);

        InventoryAction inventoryAction = new InventoryAction(this, player);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_I, false, false), inventoryAction);

        DropAction dropAction = new DropAction(player, mapControl);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_D, false, true), dropAction);

        HelpAction helpAction = new HelpAction(this, keyInputHandler);
        keyInputHandler.addAction(new ActionKey(KeyEvent.VK_F1, false, false), helpAction);
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
//    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WalkaroundMain().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
