package com.sokolovskyi.jasm.compiler;

import com.sokolovskyi.jasm.compiler.lexemes.LexemesTable;
import com.sokolovskyi.jasm.compiler.parser.ParserLexemesTables;
import com.sokolovskyi.jasm.compiler.parser.TextParser;

import java.util.ArrayList;

public class Compile implements Runnable{
    private String text;

    public Compile(String text){
        this.text = text;
    }

    @Override
    public void run() {
        //get parsed text
        TextParser parser = new TextParser(text);
        ArrayList<String[]> parseList;
        parseList = parser.getParsedText();


        //create tables (Lexical analysis)
        ArrayList<LexemesTable[]> tablesList = new ArrayList<>();
        for (String[] strings : parseList) {
            tablesList.add(ParserLexemesTables.getTablesFromLine(strings));
        }


        //for debugging
        Debugger.outLexemesTables(tablesList);

    }
}
