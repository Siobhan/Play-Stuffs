/*
 * MultipleChoiceBox.java
 *
 * Created on 11.07.2010
 *
 * Copyright (c) Hansjoerg Malthaner
 * <h_malthaner@users.sourceforge.net>
 *
 * This file is part of the Roguelike Game Kit project.
 *
 * For details, please read the license.txt file.
 */

package rgegame.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * A box which lets the user select one of multiple choices.
 *
 * This box can also be used to display rather generic messages
 * to the user which are confirmed by a key press. It supports
 * HTML formatting through Swing capabilities for all three text
 * blocks.
 * 
 * @author Hj. Malthaner
 */
public class MultipleChoiceBox extends JDialog
{

    private boolean  ok = true;
    private int responseKey;


    /**
     * Center the box on the parent frame.
     */
    public void center(JFrame frame)
    {
        Point p = frame.getLocation();
        setLocation(p.x + (frame.getWidth() - getWidth())/2,
                    p.y + (frame.getHeight() - getHeight())/2);
    }

    /**
     * When the box was closed check this if the user canceled the box.
     * 
     * @return False if canceled, true otherwise.
     */
    public boolean isOk()
    {
        return ok;
    }


    /**
     * Call this to find out which key the user pressed to respond to the
     * box content.
     * 
     * @return The key character.
     */
    public int getResponseKey()
    {
        return responseKey;
    }

    /**
     * Display a box with a list of multiple choices.
     *
     * @param parent Parent frame or null if there is no parent for this dialog.
     * @param upperMessage Display in upper area (limited HTML abilities)
     * @param choiceList Display in middle area (limited HTML abilities)
     * @param lowerMessage Display in lower area (limited HTML abilities)
     */
    public MultipleChoiceBox(JFrame parent,
                             String upperMessage,
                             String choiceList,
                             String lowerMessage)
    {
        super(parent, true);

        setUndecorated(true);

        setLayout(new BorderLayout());

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BorderLayout());
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new BorderLayout());

        JLabel upperLabel = makeLabel(upperMessage);
        JLabel choiceLabel = makeLabel(choiceList);
        JLabel lowerLabel = makeLabel(lowerMessage);

        innerPanel.add(upperLabel, BorderLayout.NORTH);
        innerPanel.add(choiceLabel, BorderLayout.CENTER);
        innerPanel.add(lowerLabel, BorderLayout.SOUTH);

        addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e)
            {
            }

            public void keyPressed(KeyEvent e)
            {
                int code = e.getKeyCode();
                int chr = e.getKeyChar();

                responseKey = chr;

                // cancel box.
                ok = (code != KeyEvent.VK_ESCAPE);

                setVisible(false);
                dispose();
            }

            public void keyReleased(KeyEvent e)
            {
            }
        });

        innerPanel.setBackground(Color.DARK_GRAY);
        outerPanel.setBackground(Color.DARK_GRAY);

        outerPanel.setBorder(new LineBorder(Color.WHITE, 1));
        innerPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
        
        outerPanel.add(innerPanel);
        add(outerPanel);
    }

    /**
     * Factor method to create labels with proepr UI styles.
     * 
     * @param text The text to set for the label.
     * @return A JLabel with text and formatting presets.
     */
    private JLabel makeLabel(String text)
    {
        JLabel label = new JLabel("<html>" + text + "</html>");
        label.setForeground(Color.WHITE);
        label.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        return label;
    }


    /**
     * Shows an example dialog.
     * 
     * @param args Ignored.
     */
    public static void main(String args [])
    {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MultipleChoiceBox mcb =
                        new MultipleChoiceBox(null,
                                              "Equip which item?",
                                              "(a) Leather cap<br>" +
                                              "(b) Leather armor<br>" +
                                              "(c) Leather boots<br><br>" +
                                              "(esc) Cancel",
                                              "Press key to select.");

                mcb.setSize(200, 200);
                mcb.setLocation(100, 100);
                mcb.setVisible(true);

                System.err.println("Ok = " + mcb.isOk());
                System.err.println("Key = " + mcb.getResponseKey());
            }
        });
    }
}
