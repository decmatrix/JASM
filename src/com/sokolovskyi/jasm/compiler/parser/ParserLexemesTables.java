package com.sokolovskyi.jasm.compiler.parser;

import com.sokolovskyi.jasm.compiler.lexemes.LexemesTable;

public class ParserLexemesTables {

    private ParserLexemesTables(){}

    public static LexemesTable[] getTablesFromLine(String[] words){
        LexemesTable[] tables = new LexemesTable[words.length];

        for(int i = 0; i < words.length; i++){
            tables[i] = new LexemesTable(words[i]);
        }

        return tables;
    }
}
