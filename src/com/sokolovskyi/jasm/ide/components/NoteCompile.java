package com.sokolovskyi.jasm.ide.components;

import com.sokolovskyi.jasm.compiler.Compile;
import com.sokolovskyi.jasm.ide.IDEParams;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NoteCompile extends JFrame{
    private Font font;
    private JTextArea textArea;
    private String programText;
    private JScrollPane spane;

    private static String TMP_COMPILE_FILE = "./src/tmp/compile.tmp";

    public NoteCompile(Font font, String programText){
        super("Result"); //FIXME choose name of window
        this.font = font;
        this.programText = programText;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //init
        initWindow();
        initTextArea();
        initPanel();

        //compile
        compile();

        setVisible(true);
    }

    private void compile(){
        new Thread(new Compile(programText, TMP_COMPILE_FILE)).start();

        try(FileReader reader = new FileReader(TMP_COMPILE_FILE)){
            BufferedReader br = new BufferedReader(reader);

            String line = br.readLine();

            while(line != null){
                textArea.append(line + '\n');
                line = br.readLine();
            }

            //TODO delete tmp file

        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initTextArea(){
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(font);

        setBackground(new Color(0, 0, 0));
        setForeground(new Color(255, 255, 255));
    }

    private void initWindow(){
        setFont(font);

        setPreferredSize(new Dimension(270, 225));
        pack();
        setLocationRelativeTo(null);

        setBounds( IDEParams.CORD_X_MAIN_WIN, IDEParams.CORD_Y_MAIN_WIN, 700, 700);
    }

    private void initPanel(){
        spane = new JScrollPane(textArea);
        //FIXME don't work
        //spane.setRowHeaderView(new NoteLineNumbering(textArea));

        JPanel panel = new JPanel(new BorderLayout());

        panel.add(spane, BorderLayout.CENTER);
        getContentPane().add(panel);
    }

}
