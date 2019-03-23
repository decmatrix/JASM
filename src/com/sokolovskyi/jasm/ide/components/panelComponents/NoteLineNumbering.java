package com.sokolovskyi.jasm.ide.components.panelComponents;

import javax.swing.*;
import javax.swing.text.Element;
import java.awt.*;


public class NoteLineNumbering extends JTextArea {
    private JTextArea theText;

    public NoteLineNumbering(JTextArea theText){
        this.theText = theText;

        setBackground(new Color(0, 0, 0));
        setForeground(new Color(255, 255, 255));

        setEditable(false);
    }

    public void updateLineNumbers()
    {
        String lineNumbersText = getLineNumbersText();
        setText(lineNumbersText);
    }


    private String getLineNumbersText()
    {
        int caretPosition = theText.getDocument().getLength();
        Element root = theText.getDocument().getDefaultRootElement();
        StringBuilder lineNumbersTextBuilder = new StringBuilder();

        for (int elementIndex = 1; elementIndex < root.getElementIndex(caretPosition) + 2; elementIndex++)
        {
            lineNumbersTextBuilder.append(elementIndex).append(" ").append(System.lineSeparator());
        }

        return lineNumbersTextBuilder.toString();
    }


}
