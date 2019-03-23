package com.sokolovskyi.jasm.ide.components.helpMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NoteAbout extends JDialog{
    private JButton ok;

    public NoteAbout(JFrame mainWindow){
        super(mainWindow, "About JASM", true);

        initComponents();
        initPanel();
    }

    private void initPanel(){
        JPanel panel = new JPanel();
        panel.add(ok);
        add(panel, BorderLayout.SOUTH);
        setSize(260, 160);
        setLocation(200, 100);
    }

    private void initComponents(){
        add(new JLabel(
                "<html><h1><i> JASM v.0.1 beta</i></h1><hr>"
                        + "Created by Sokolovskyi Bohdan"), BorderLayout.CENTER
        );

        ok = new JButton("OK");
        ok.addActionListener(actionEvent -> setVisible(false));
    }
}

