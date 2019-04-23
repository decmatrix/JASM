package com.sokolovskyi.jasm.compiler.parser;

import com.sokolovskyi.jasm.compiler.Lexis.LexemesTable;
import com.sokolovskyi.jasm.compiler.syntax.SentenceTable;

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
