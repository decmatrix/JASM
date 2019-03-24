package com.sokolovskyi.jasm.compiler.syntax;

import com.sokolovskyi.jasm.compiler.lexemes.Lexemes;
import com.sokolovskyi.jasm.compiler.lexemes.LexemesTable;

public class SentenceTable {
    private LexemesTable[] tables;

    /* fields of table */

    //field labels
    private int labelName = -1;

    //field mnemonic
    private int lexemeMnemonic = -1;
    private int countLexemeMnemonic = 0;

    //field #1 operand
    private int firstOperand = -1;
    private int countFirstOperand = 0;

    //field #2 operand
    private int secondOperand = -1;
    private int countSecondOperand = 0;

    SentenceTable(LexemesTable[] tables){
        this.tables = tables;
        parseLine();
    }

    private boolean checkSentence(){
        //TODO: release effective algorithm
        return true;
    }

    private void parseLine(){
        parseLabelName();
        parseLexemeMnemonic();
        parseFirstOperand();
        parseSecondOperand();
    }

    private void parseLabelName(){
        for(int i = 0; i < tables.length; i++){
            if(isLabel(tables[i])){
                labelName = i;
                break;
            }
        }
    }

    private void parseLexemeMnemonic(){
        int pos;

        if(labelName == -1){
            pos = 0;
        }else{
            pos = labelName;
        }

        for(int i = pos; i < tables.length; i++){
            if(isMnemonic(tables[i])){
                lexemeMnemonic = i;
                break;
            }
        }

        //TODO; while is count  = 1
        countLexemeMnemonic = 1;
    }

    private void parseFirstOperand(){
        if(lexemeMnemonic == -1) return;

        for(int i = lexemeMnemonic + countLexemeMnemonic - 1; i < tables.length; i++){
            if(isOperand(tables[i])){
                firstOperand = i;
                break;
            }
        }

        if(firstOperand == -1) return;

        for(int i = firstOperand; i < tables.length; i++){
            if(tables[i].getLexeme().equals(",")){
                countFirstOperand = i - firstOperand ;
            }
        }

        if(countFirstOperand == 0){
            countFirstOperand = tables.length - firstOperand;
        }
    }

    private void parseSecondOperand(){
        if(firstOperand == -1) return;

        for(int i = firstOperand + countFirstOperand + 1; i < tables.length; i++){
            if(isOperand(tables[i])){
                secondOperand = i;
                break;
            }
        }

        if(secondOperand == -1) return;

        countSecondOperand = tables.length - secondOperand;
    }

    private boolean isLabel(LexemesTable table){
        return table.getLinkLexeme().equals(Lexemes.IDENTIFIER);
    }

    private boolean isMnemonic(LexemesTable table){
        return table.getLinkLexeme().equals(Lexemes.ASMCOMMAND) ||
                table.getLinkLexeme().equals(Lexemes.DIRECTIVE) ||
                table.getLinkLexeme().equals(Lexemes.DATATYPE);
    }

    private boolean isOperand(LexemesTable table){
        String lexeme = table.getLinkLexeme();

        if(lexeme.equals(Lexemes.REGISTER_8) || lexeme.equals(Lexemes.REGISTER_32) || lexeme.equals(Lexemes.IDENTIFIER) ||
           lexeme.equals(Lexemes.REGISTER_S) || lexeme.equals(Lexemes.DEC_CONSTANT) || lexeme.equals(Lexemes.HEX_CONSTANT) ||
           lexeme.equals(Lexemes.BIN_CONSTANT)){

            return true;
        }

        lexeme = table.getLexeme();

        return lexeme.equals("(") || lexeme.equals("[") || lexeme.equals("-") || lexeme.equals("+") || lexeme.equals("word") ||
                lexeme.equals("dword") || lexeme.equals("byte");

    }

    @Override
    public String toString() {
        return "Sentence: " + labelName + " | " + lexemeMnemonic + " (" + countLexemeMnemonic + ") | " +  firstOperand +
                " (" + countFirstOperand + ") | " + secondOperand + " (" + countSecondOperand + ")" + " |";
    }
}
