package com.sokolovskyi.jasm.compiler.syntax;

import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

public class SentenceTable {
    private LexemesTable[] tables;

    /* fields of table */

    //field labels
    private int posLabelName = -1;

    //field mnemonic
    private int posLexemeMnemonic = -1;
    private int countLexemeMnemonic = 0;

    //field #1 operand
    private int posFirstOperand = -1;
    private int countFirstOperand = 0;

    //field #2 operand
    private int posSecondOperand = -1;
    private int countSecondOperand = 0;

    public SentenceTable(LexemesTable[] tables){
        this.tables = tables;
        parseLine();
    }

    //getters
    public int getPosLabelName(){
        return posLabelName;
    }

    public int getPosLexemeMnemonic(){
        return posLexemeMnemonic;
    }

    public int getCountLexemeMnemonic(){
        return countLexemeMnemonic;
    }

    public int getPosFirstOperand(){
        return posFirstOperand;
    }

    public int getCountFirstOperand(){
        return countFirstOperand;
    }

    public int getPosSecondOperand(){
        return posSecondOperand;
    }

    public int getCountSecondOperand(){
        return countSecondOperand;
    }
    //end getters

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
            if(isMnemonic(tables[i])) break;

            if(isLabel(tables[i])){
                posLabelName = i;
                break;
            }
        }
    }

    private void parseLexemeMnemonic(){
        int pos;

        if(posLabelName == -1){
            pos = 0;
        }else{
            pos = posLabelName;
        }

        for(int i = pos; i < tables.length; i++){
            if(isMnemonic(tables[i])){
                posLexemeMnemonic = i;
                break;
            }
        }

        //TODO; while is count  = 1
        if(posLexemeMnemonic == -1){
            countLexemeMnemonic = 0;
        }else{
            countLexemeMnemonic = 1;
        }
    }

    private void parseFirstOperand(){
        if(posLexemeMnemonic == -1) return;

        for(int i = posLexemeMnemonic + countLexemeMnemonic - 1; i < tables.length; i++){
            if(isOperand(tables[i])){
                posFirstOperand = i;
                break;
            }
        }

        if(posFirstOperand == -1) return;

        for(int i = posFirstOperand; i < tables.length; i++){
            if(tables[i].getLexeme().equals(",")){
                countFirstOperand = i - posFirstOperand;
            }
        }

        if(countFirstOperand == 0){
            countFirstOperand = tables.length - posFirstOperand;
        }
    }

    private void parseSecondOperand(){
        if(posFirstOperand == -1) return;

        for(int i = posFirstOperand + countFirstOperand + 1; i < tables.length; i++){
            if(isOperand(tables[i])){
                posSecondOperand = i;
                break;
            }
        }

        if(posSecondOperand == -1) return;

        countSecondOperand = tables.length - posSecondOperand;
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
        String ln = posLabelName == -1 ? "NONE" : posLabelName +"";
        String lm = posLexemeMnemonic == -1 ? "NONE" : posLexemeMnemonic + " (" + countLexemeMnemonic + ")";
        String fo = posFirstOperand == -1 ? "NONE" : posFirstOperand + " (" + countFirstOperand + ")";
        String so = posSecondOperand == -1 ? "NONE" : posSecondOperand + " (" + countSecondOperand + ")";

        return "Sentence: " + ln + " | " + lm + " | " +  fo + " | " + so + " |";
    }
}
