package com.sokolovskyi.jasm.compiler.Lexis.exceptions;

import com.sokolovskyi.jasm.compiler.Lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.Lexis.LexemesTable;

import java.util.ArrayList;

public class LexicalExcHandler {
    private LexicalExcHandler(){}

    public static void catchException(ArrayList<LexemesTable[]> lexemesTables) throws LexicalException{
        boolean err = false;

        for(int i = 0; i < lexemesTables.size(); i++){
            for(LexemesTable table : lexemesTables.get(i)){
                if(table.getLinkLexeme().equals(Lexemes.UNKNOWN_LEXEME)){
                    System.out.println("(" + (i + 1) + ") " + "LexicalException: \"" + table.getLexeme() + "\" - > UNKNOWN LEXEME");
                    if(!err) err = true;
                }
            }
        }

        if(err) throw new LexicalException();
    }
}
