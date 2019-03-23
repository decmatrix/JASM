package com.sokolovskyi.jasm.ide.components.settingsMenu;

import com.sokolovskyi.jasm.ide.components.panelComponents.NoteLineNumbering;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.*;

public final class NoteColors {
    private JTextArea text;
    private NoteLineNumbering textNumber;
    private Font font;
    private JMenu menu;

    private JMenuItem colorBlack;
    private JMenuItem colorWhite;
    private JMenuItem colorLightOceanic;
    private JMenuItem colorPink;
    private JMenuItem colorNubes;
    private JMenuItem colorSailorMoon;

    //path of file of last colors
    private final static String PATH_LAST_FILE_COLORS = "./src/configs/lastColors.jasm";

    public NoteColors(JTextArea text, NoteLineNumbering textNumber, Font font, JMenu menu){
        this.text = text;
        this.textNumber = textNumber;
        this.font = font;
        this.menu = menu;

        initColors();
        initListeners();
    }

    @SuppressWarnings("Duplicates")
    private void initColors(){
        colorBlack = new JMenuItem("Black & White");
        colorWhite = new JMenuItem("White & Black");
        colorLightOceanic = new JMenuItem("Light Oceanic");
        colorPink = new JMenuItem("Pink");
        colorNubes = new JMenuItem("Nubes");
        colorSailorMoon = new JMenuItem("Sailor Moon");

        colorBlack.setFont(font);
        colorWhite.setFont(font);
        colorLightOceanic.setFont(font);
        colorPink.setFont(font);
        colorNubes.setFont(font);
        colorSailorMoon.setFont(font);

        menu.add(colorWhite);
        menu.add(colorBlack);
        menu.add(colorLightOceanic);
        menu.add(colorPink);
        menu.add(colorNubes);
        menu.add(colorSailorMoon);
    }

    public void initLastColors(){
        if(isEmptyLastColorFile()) return;

        int[] colors = getLastColors();

        text.setBackground(new Color(colors[0], true));
        text.setForeground(new Color(colors[1], true));
        text.setCaretColor(new Color(colors[2], true));

        textNumber.setBackground(new Color(colors[3], true));
        textNumber.setForeground(new Color(colors[4], true));
    }

    private boolean isEmptyLastColorFile(){
        return new File(PATH_LAST_FILE_COLORS).length() == 0;
    }

    private int[] getLastColors(){

        String[] colors = null;

        try(FileReader reader = new FileReader(PATH_LAST_FILE_COLORS)){
            BufferedReader br = new BufferedReader(reader);
            colors = br.readLine().split(";");
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        int[] colorsInt  = new int[colors.length];
        for(int i = 0; i < colors.length; i++) {
            colorsInt[i] = Integer.parseInt(colors[i]);
        }

        return colorsInt;
    }

    public void saveLastColors(){
        try(FileWriter writer = new FileWriter(PATH_LAST_FILE_COLORS, false)){
            writer.write(text.getBackground().getRGB() + ";");
            writer.write(text.getForeground().getRGB() + ";");
            writer.write(text.getCaretColor().getRGB() + ";");
            writer.write(textNumber.getBackground().getRGB() + ";");
            writer.write(textNumber.getForeground().getRGB() + ";");
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void initListeners(){
        colorBlack.addActionListener(actionEvent -> {
            text.setBackground(new Color(0, 0, 0, 255));
            text.setForeground(new Color(255, 255, 255, 255));
            text.setCaretColor(new Color(255, 255, 255, 255));

            textNumber.setBackground(new Color(255, 255, 255, 255));
            textNumber.setForeground(new Color(0, 0, 0, 255));
        });

        colorWhite.addActionListener(actionEvent -> {
            text.setBackground(new Color(255, 255, 255, 255));
            text.setForeground(new Color(0, 0, 0, 255));
            text.setCaretColor(new Color(0, 0, 0, 255));

            textNumber.setBackground(new Color(0, 0, 0, 255));
            textNumber.setForeground(new Color(255, 255, 255, 255));
        });

        colorLightOceanic.addActionListener(actionEvent -> {
            text.setBackground(new Color(31, 27, 77, 255));
            text.setForeground(new Color(120, 120, 120, 255));
            text.setCaretColor(new Color(255, 255, 255, 255));

            textNumber.setBackground(new Color(0, 0, 0, 255));
            textNumber.setForeground(new Color(51, 255, 0, 255));
        });

        colorPink.addActionListener(actionEvent -> {
            text.setBackground(new Color(17, 18, 28, 255));
            text.setForeground(new Color(179, 179, 198, 255));
            text.setCaretColor(new Color(255, 255, 255, 255));

            textNumber.setBackground(new Color(29, 32, 61, 255));
            textNumber.setForeground(new Color(90, 94, 132, 255));
        });

        colorNubes.addActionListener(actionEvent -> {
            text.setBackground(new Color(0, 11, 48, 255));
            text.setForeground(new Color(52, 181, 191, 255));
            text.setCaretColor(new Color(255, 255, 255, 255));

            textNumber.setBackground(new Color(15, 29, 80, 255));
            textNumber.setForeground(new Color(150, 255, 172, 255));
        });

        colorSailorMoon.addActionListener(actionEvent -> {
            text.setBackground(new Color(46, 12, 43, 255));
            text.setForeground(new Color(176, 180, 191, 255));
            text.setCaretColor(new Color(255, 255, 255, 255));

            textNumber.setBackground(new Color(74, 46, 75, 255));
            textNumber.setForeground(new Color(123, 123, 123, 255));
        });
    }

}
