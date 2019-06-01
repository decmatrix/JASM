package com.sokolovskyi.jasm.compiler.syntax;

import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

public class SyntacticExcHandler {
    private SyntacticExcHandler() {}

    public static void catchException(String[] errors, SyntaxTable[] table){
        LexemesTable[] lexLine;
        SentenceTable sentenceLine;

        int countDataSeg = 0;
        int countCodeSeg = 0;

        for(int i = 0; i < table.length; i++){
            if(errors[i] != null) continue;

            lexLine = table[i].getLexemesTable();
            sentenceLine = table[i].getSentence();


            if(sentenceLine.getPosLexemeMnemonic() == -1 && sentenceLine.getPosLabelName() == -1) continue;

            if(lexLine[0].getLinkLexeme().equals(Lexemes.ASMCOMMAND)){
                String lex = lexLine[0].getLexeme().toUpperCase();

                switch (lex){
                    case "CLI":
                        errors[i] = checkCLI(lexLine, sentenceLine, i);
                        break;
                    case "INC":
                        errors[i] = checkINC(lexLine, sentenceLine, i);
                        break;
                    case "DEC":
                        errors[i] = checkDEC(lexLine, sentenceLine, i);
                        break;
                    case "ADD":
                        errors[i] = checkADD(lexLine, sentenceLine, i);
                        break;
                    case "OR":
                        errors[i] = checkOR(lexLine, sentenceLine, i);
                        break;
                    case "AND":
                        errors[i] = checkAND(lexLine, sentenceLine, i);
                        break;
                    case "MOV":
                        errors[i] = checkMOV(lexLine, sentenceLine, i);
                        break;
                    case "CMP":
                        errors[i] = checkCMP(lexLine, sentenceLine, i);
                        break;
                    case "JB":
                        errors[i] = checkJB(lexLine, sentenceLine, i);
                        break;
                    case "JMP":
                        errors[i] = checkJMP(lexLine, sentenceLine, i);
                        break;
                }
            }else if(lexLine[0].getLinkLexeme().equals(Lexemes.DIRECTIVE)){
                //TODO dirs
            }else if(lexLine[0].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                //TODO ids
            }else{
                errors[i] = "\n(" + (i + 1) + ") error: " + SyntaxErrors.Ox1;
            }
        }
    }

    private static String checkCLI(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() != -1){
            if(lexLine[1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                return  pattern + SyntaxErrors.Ox2 + lexLine[1].getLexeme();
            }

            return pattern + SyntaxErrors.Ox3;
        }

        if(lexLine.length > 1){
            if(lexLine[1].getLinkLexeme().equals(Lexemes.LITERAL)){
                return pattern + SyntaxErrors.Ox4;
            }

            return pattern + SyntaxErrors.err;
        }

        return null;
    }

    private static String checkINC(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() == -1){
            return pattern + SyntaxErrors.Ox5;
        }else{
            /*if(sentenceLine.getCountFirstOperand() > 1){
                return pattern + SyntaxErrors.err;
            }*/

            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
            }

            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) ||
                    lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
                return pattern + SyntaxErrors.Ox6;
            }

            if((lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) ||
                    lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)) && lexLine.length > 2){
                if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                    return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme();
                }

                if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.LITERAL)){
                    return pattern + SyntaxErrors.Ox4;
                }

                return pattern + SyntaxErrors.Ox3;
            }
        }

        return null;
    }

    private static String checkDEC(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){


        return null;
    }

    private static String checkADD(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(lexLine.length > 1 && lexLine[1].getLinkLexeme().equals(Lexemes.LITERAL)){
            return pattern + SyntaxErrors.Ox5;
        }

        if(sentenceLine.getPosFirstOperand() == -1 && sentenceLine.getPosSecondOperand() == -1) {
            return pattern + SyntaxErrors.Ox5;
        }

        //TODO bug
        if(lexLine.length == 2 && sentenceLine.getPosFirstOperand() != -1 && (lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) ||
                lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32))){
            return pattern + SyntaxErrors.Ox7;
        }

        if(sentenceLine.getPosFirstOperand() != -1){
            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
            }

            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                    lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
                return pattern + SyntaxErrors.Ox6;
            }
        }

        if(lexLine.length > 2){
            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                    return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme();
                }
            }

            if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                    lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
                return pattern + SyntaxErrors.Ox3;
            }
        }

        if(sentenceLine.getPosFirstOperand() != -1 && sentenceLine.getPosSecondOperand() == -1){
            return pattern + SyntaxErrors.Ox5;
        }

        if(sentenceLine.getPosFirstOperand() != -1 && sentenceLine.getPosSecondOperand() != -1){
            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                    return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosSecondOperand()].getLexeme();
                }
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                    lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
                return pattern + SyntaxErrors.Ox6;
            }
        }

        return null;
    }

    private static String checkOR(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        //TODO

        return null;
    }

    private static String checkAND(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        //TODO

        return null;
    }

    private static String checkMOV(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";



        return null;
    }

    private static String checkCMP(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){


        return null;
    }

    private static String checkJB(LexemesTable[] lexLine, SentenceTable sentenceTable, int pos){


        return null;
    }

    private static String checkJMP(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){

        return null;
    }


}
