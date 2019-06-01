package com.sokolovskyi.jasm.compiler.parser;

import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;
import com.sokolovskyi.jasm.compiler.syntax.SentenceTable;
import com.sokolovskyi.jasm.compiler.syntax.SyntaxTable;

import java.util.ArrayList;

public class TableParser {
    private TableParser(){}

    public static LexemesTable[] getLexemeTables(String[] words){
        LexemesTable[] tables = new LexemesTable[words.length];

        for(int i = 0; i < words.length; i++){
            tables[i] = new LexemesTable(words[i]);
        }

        return tables;
    }

    public static SentenceTable[] getSentenceTables(ArrayList<LexemesTable[]> tablesList){
        SentenceTable[] stables = new SentenceTable[tablesList.size()];

        for(int i = 0; i < tablesList.size(); i++){
            stables[i] = new SentenceTable(tablesList.get(i));
        }

        return stables;
    }

    public static SyntaxTable[] getSyntaxTables(ArrayList<LexemesTable[]> tableList, SentenceTable[] sentenceTables, String[] sourceCode){
        SyntaxTable[] table = new SyntaxTable[tableList.size()];

        for(int i = 0; i < tableList.size(); i++){
            table[i] = new SyntaxTable(tableList.get(i), sentenceTables[i], sourceCode[i]);
        }

        return table;
    }
}
