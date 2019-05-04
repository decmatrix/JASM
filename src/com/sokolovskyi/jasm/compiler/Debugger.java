package com.sokolovskyi.jasm.compiler;

import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;
import com.sokolovskyi.jasm.compiler.syntax.SentenceTable;

import java.io.FileWriter;
import java.io.IOException;
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


        try(FileWriter writer = new FileWriter("./src/tmp/out.salt")){
            for (int  i = 0; i < tables.size(); i++) {
                int j = 0;


                writer.write("-----------------------------------\n");
                for (LexemesTable lexemesTable : tables.get(i)) {
                    writer.write(j + ") " + lexemesTable + "\n");
                    j++;
                }
                writer.write("************************************\n");
                writer.write("label | mnemocode | 1 operand | 2 operand\n");
                writer.write(stables[i]+"\n");
            }
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        //System.out.println("\n***************DEBUG TABLE OF LEXEMES & SENTENCES***************\n");
        //for (int  i = 0; i < tables.size(); i++) {
            //System.out.println("\n-----------------------------------");

           //outLexemeTable(tables.get(i));

            //System.out.println("************************************");
            //System.out.println(stables[i]);
        //}
    }

}
