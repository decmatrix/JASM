package com.sokolovskyi.jasm.compiler.lexis;

import java.util.ArrayList;

public final class LexicalExcHandler {

    private LexicalExcHandler(){}

    public static void catchLexicalExceptions(String[] errors, ArrayList<LexemesTable[]> tablesOfLexemes){
        for(int i = 0; i < tablesOfLexemes.size() - 1; i++){
            for(int j = 0; j < tablesOfLexemes.get(i).length; j++){
                if(tablesOfLexemes.get(i)[j].getLinkLexeme().equals(Lexemes.UNKNOWN_LEXEME)){
                    errors[i] = "\n(" + (i + 1) + ") error: " + LexicalErrors.Ox0;
                }
            }
        }
    }
}