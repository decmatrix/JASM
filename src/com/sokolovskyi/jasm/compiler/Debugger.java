package com.sokolovskyi.jasm.compiler;

import com.sokolovskyi.jasm.compiler.lexemes.LexemesTable;

import java.util.ArrayList;

final class Debugger {
    private Debugger(){}


    static void outLexemesTables(ArrayList<LexemesTable[]> tables){
        System.out.println("\n***************DEBUG TABLE OF LEXEMES***************\n");
        for (LexemesTable[] table : tables) {
            System.out.println("-----------------------------------");

            for (LexemesTable lexemesTable : table) {
                System.out.println(lexemesTable);
            }
        }
    }
}
