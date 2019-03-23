package com.sokolovskyi.jasm.compiler;

import com.sokolovskyi.jasm.compiler.lexemes.LexemesTable;
import com.sokolovskyi.jasm.compiler.syntax.SentenceTable;

import java.util.ArrayList;

final class Debugger {
    private Debugger(){}


    private static void outLexemeTable(LexemesTable[] tables){
        int i = 0;
        for (LexemesTable lexemesTable : tables) {
            System.out.println(i + ") " + lexemesTable);
            i++;
        }
    }

    static void outLexemesTables(ArrayList<LexemesTable[]> tables){
        System.out.println("\n***************DEBUG TABLE OF LEXEMES***************\n");
        for (LexemesTable[] table : tables) {
            System.out.println("-----------------------------------");

            outLexemeTable(table);
        }
    }

    static void outSentenceTables(SentenceTable[] tables){
        System.out.println("\n***************DEBUG TABLE OF SENTENCES***************\n");
        for(SentenceTable table : tables){
            System.out.println("************************************");
            System.out.println(table);
        }
    }

    static void outLSTables(ArrayList<LexemesTable[]> tables, SentenceTable[] stables){
        System.out.println("\n***************DEBUG TABLE OF LEXEMES & SENTENCES***************\n");
        for (LexemesTable[] table : tables) {
            System.out.println("\n-----------------------------------");

           outLexemeTable(table);

            for(SentenceTable stable : stables){
                System.out.println("************************************");
                System.out.println(stable);
            }
        }
    }

}
