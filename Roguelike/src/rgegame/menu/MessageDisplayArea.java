/*
 * MessageDisplayArea.java
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

package rgegame.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;

/**
 * A display area for game messages. Allows HTML formatted messages.
 * Maybe not 100% stable yet - used by walkaround demo as an example case.
 * 
 * @author Hj. Malthaner
 */
public class MessageDisplayArea extends JPanel
{
    // private final ArrayList <J> messages;
    private DefaultListModel defaultListModel;
    private final JList textArea;

    private int messageLimit = 10;

    /**
     * Set the font to be used for drawing.
     * @param font The font to use.
     */
    @Override
    public void setFont(Font font)
    {
        if(textArea != null) {
            textArea.setFont(font);
        }
    }

    /**
     * Set the default text color
     * @param color The color to use for default text.
     */
    @Override
    public void setForeground(Color color)
    {
        if(textArea != null) {
            textArea.setForeground(color);
        }
    }

    public MessageDisplayArea()
    {
        textArea = new JList();
        defaultListModel = new DefaultListModel();
        textArea.setModel(defaultListModel);
        textArea.setForeground(Color.WHITE);
        textArea.setBackground(Color.BLACK);
        textArea.setFocusable(false);
        setLayout(new BorderLayout());

        JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(4, 1));
        spacer.setBackground(Color.BLACK);

        add(spacer, BorderLayout.WEST);
        add(textArea, BorderLayout.CENTER);
    }


    /**
     * Add a message at the bottom of the message list.
     * 
     * @param message the message to add.
     */
    public void addMessageBottom(String message)
    {
        defaultListModel.addElement(message);
        textArea.ensureIndexIsVisible(defaultListModel.getSize()-1);

        // prune excess messages
        if(defaultListModel.size() > messageLimit) {
            defaultListModel.removeElementAt(0);
        }
    }


    /**
     * Add a message at the top of the message list.
     *
     * @param message the message to add.
     */
    public void addMessageTop(String message)
    {
        defaultListModel.add(0, message);

        // prune excess messages
        if(defaultListModel.size() > messageLimit) {
            defaultListModel.removeElementAt(messageLimit);
        }
    }
}
