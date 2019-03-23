package com.sokolovskyi.jasm.compiler.syntax;

import com.sokolovskyi.jasm.compiler.lexemes.LexemesTable;

import java.util.ArrayList;

public class ParserSentencesTable {

    private ParserSentencesTable(){}

    public static SentenceTable[] getTablesFromLexemsTable(ArrayList<LexemesTable[]> tablesList){
        SentenceTable[] stables = new SentenceTable[tablesList.size()];

        for(int i = 0; i < tablesList.size(); i++){
            stables[i] = new SentenceTable(tablesList.get(i));
        }

        return stables;
    }
}
