package com.sokolovskyi.jasm.ide.components.ideErrors;

import javax.swing.*;
import java.awt.*;

public class NoteError extends JDialog {
    private JButton ok;
    private String reason;
    private JFrame mainWindow;

    public NoteError(JFrame mainWindow, String reason){
        super(mainWindow, "Running error!", true);

        this.reason = reason;
        this.mainWindow = mainWindow;

        initComponents();
        initPanel();
    }

    private void initPanel(){
        JPanel panel = new JPanel();
        panel.add(ok);
        add(panel, BorderLayout.SOUTH);
        setSize(250, 100);
        setLocation(mainWindow.getX() + 200, mainWindow.getY() + 100);
        setResizable(false);
    }

    private void initComponents(){
        add(new JLabel(reason), BorderLayout.CENTER
        );

        ok = new JButton("OK");
        ok.addActionListener(actionEvent -> setVisible(false));
    }
    //the the the
}
