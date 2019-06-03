package com.sokolovskyi.jasm.compiler.semantics;

import com.sokolovskyi.jasm.compiler.additionals.CalcMathExpr;
import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;
import com.sokolovskyi.jasm.compiler.syntax.SentenceTable;
import com.sokolovskyi.jasm.compiler.syntax.SyntaxErrors;
import com.sokolovskyi.jasm.compiler.syntax.SyntaxTable;

public final class SemanticExcHandler {
    private SemanticExcHandler(){}

    public static void catchException(String[] errors, SyntaxTable[] table){
        LexemesTable[] lexLine;
        SentenceTable sentenceLine;

        for(int i = 0; i < table.length; i++) {
            if (errors[i] != null) continue;

            lexLine = table[i].getLexemesTable();
            sentenceLine = table[i].getSentence();

            if(sentenceLine.getPosLexemeMnemonic() == -1 && sentenceLine.getPosLabelName() == -1) continue;

            if(lexLine[0].getLinkLexeme().equals(Lexemes.ASMCOMMAND)){
                String lex = lexLine[0].getLexeme().toUpperCase();

                switch (lex){
                    case "ADD":
                        errors[i] = checkADD(lexLine, sentenceLine, i);
                        break;
                    case "MOV":
                        errors[i] = checkMOV(lexLine, sentenceLine, i);
                        break;
                    case "CMP":
                        errors[i] = checkCMP(lexLine, sentenceLine, i);
                        break;
                }

            }else if(lexLine[0].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                if(lexLine[1].getLinkLexeme().equals(Lexemes.DATATYPE)){
                    errors[i] = checkVARS(lexLine, sentenceLine, i);
                }
            }
        }
    }

    /* methods for check machine commands */
    private static String checkADD(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) && lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8)){
            return null;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32) && lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)){
            return null;
        }

        return pattern + SemanticErrors.Ox1;
    }

    private static String checkMOV(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8)){
            int num  = 0;

            if(lexLine[sentenceLine.getPosSecondOperand()].getLexeme().equals(Lexemes.LITERALS[1])){
                num = Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme());

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLexeme().equals(Lexemes.LITERALS[2])){
                num = -1 * Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme());

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                num = Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand()].getLexeme());

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){

                String str = lexLine[sentenceLine.getPosSecondOperand()].getLexeme().toUpperCase().replaceAll("H", "");

                num = Integer.parseInt(str, 16);

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){

                String str = lexLine[sentenceLine.getPosSecondOperand()].getLexeme().toUpperCase().replaceAll("B", "");

                num = Integer.parseInt(str, 2);

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }


        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)){
            int num  = 0;

            if(lexLine[sentenceLine.getPosSecondOperand()].getLexeme().equals(Lexemes.LITERALS[1])){
                num = Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme());

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLexeme().equals(Lexemes.LITERALS[2])){
                num = -1 * Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme());

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                num = Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand()].getLexeme());

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){

                String str = lexLine[sentenceLine.getPosSecondOperand()].getLexeme().toUpperCase().replaceAll("H", "");

                num = Integer.parseInt(str, 16);

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){

                String str = lexLine[sentenceLine.getPosSecondOperand()].getLexeme().toUpperCase().replaceAll("B", "");

                num = Integer.parseInt(str, 2);

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }
        }

        return pattern + SemanticErrors.Ox1;
    }

    private static String checkCMP(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(lexLine[sentenceLine.getPosFirstOperand()].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[6])){
            int num  = 0;

            if(lexLine[sentenceLine.getPosSecondOperand()].getLexeme().equals(Lexemes.LITERALS[1])){
                num = Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme());

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLexeme().equals(Lexemes.LITERALS[2])){
                num = -1 * Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme());

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                num = Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand()].getLexeme());

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){

                String str = lexLine[sentenceLine.getPosSecondOperand()].getLexeme().toUpperCase().replaceAll("H", "");

                num = Integer.parseInt(str, 16);

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){

                String str = lexLine[sentenceLine.getPosSecondOperand()].getLexeme().toUpperCase().replaceAll("B", "");

                num = Integer.parseInt(str, 2);

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[3])){
            int num  = 0;

            if(lexLine[sentenceLine.getPosSecondOperand()].getLexeme().equals(Lexemes.LITERALS[1])){
                num = Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme());

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLexeme().equals(Lexemes.LITERALS[2])){
                num = -1 * Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme());

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                num = Integer.parseInt(lexLine[sentenceLine.getPosSecondOperand()].getLexeme());

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){

                String str = lexLine[sentenceLine.getPosSecondOperand()].getLexeme().toUpperCase().replaceAll("H", "");

                num = Integer.parseInt(str, 16);

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){

                String str = lexLine[sentenceLine.getPosSecondOperand()].getLexeme().toUpperCase().replaceAll("B", "");

                num = Integer.parseInt(str, 2);

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }
        }

        return pattern + SemanticErrors.Ox1;
    }

    /* methods for check variables */
    private static String checkVARS(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";


        if(lexLine[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[0])){

            if (lexLine.length - 3 > 2) {

                Integer res = CalcMathExpr.calcLinearExp(lexLine, 2);

                System.out.println(lexLine[2] + "   " + res);


                if(is8BitsNumber(res)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLexeme().equals(Lexemes.LITERALS[1])){
                int num = Integer.parseInt(lexLine[3].getLexeme());

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLexeme().equals(Lexemes.LITERALS[2])){
                int num = -1 * Integer.parseInt(lexLine[3].getLexeme());

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                int num = Integer.parseInt(lexLine[2].getLexeme());

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){

                String str = lexLine[2].getLexeme().toUpperCase().replaceAll("H", "");

                int num = Integer.parseInt(str, 16);

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){

                String str = lexLine[2].getLexeme().toUpperCase().replaceAll("B", "");

                int num = Integer.parseInt(str, 2);

                if(is8BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }
        }

        if(lexLine[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[1])){

            if (lexLine.length - 3 > 0) {

                Integer res = CalcMathExpr.calcLinearExp(lexLine, 2);

                System.out.println(lexLine[2] + "   " + res);

                if(is16BitsNumber(res)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLexeme().equals(Lexemes.LITERALS[1])){
                int num = Integer.parseInt(lexLine[3].getLexeme());

                if(is16BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLexeme().equals(Lexemes.LITERALS[2])){
                int num = -1 * Integer.parseInt(lexLine[3].getLexeme());

                if(is16BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                int num = Integer.parseInt(lexLine[2].getLexeme());

                if(is16BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){

                String str = lexLine[2].getLexeme().toUpperCase().replaceAll("H", "");

                int num = Integer.parseInt(str, 16);

                if(is16BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){

                String str = lexLine[2].getLexeme().toUpperCase().replaceAll("B", "");

                int num = Integer.parseInt(str, 2);

                if(is16BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }
        }

        if(lexLine[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[2])){

            if (lexLine.length - 3 > 2) {

                Integer res = CalcMathExpr.calcLinearExp(lexLine, 2);

                System.out.println(lexLine[2] + "   " + res);

                if(is32BitsNumber(res)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLexeme().equals(Lexemes.LITERALS[1])){
                long num = Integer.parseInt(lexLine[3].getLexeme());

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLexeme().equals(Lexemes.LITERALS[2])){
                long num = -1 * Integer.parseInt(lexLine[3].getLexeme());

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){
                long num = Integer.parseInt(lexLine[2].getLexeme());

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){

                String str = lexLine[2].getLexeme().toUpperCase().replaceAll("H", "");

                long num = Integer.parseInt(str, 16);

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }

            if(lexLine[2].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){

                String str = lexLine[2].getLexeme().toUpperCase().replaceAll("B", "");

                long num = Integer.parseInt(str, 2);

                if(is32BitsNumber(num)){
                    return null;
                }

                return pattern + SemanticErrors.Ox2;
            }


        }

        return pattern + SemanticErrors.Ox1;
    }

    /* additional methods */
    private static boolean is8BitsNumber(int num){
        return num >= -128 && num <= 127;
    }

    private static boolean is32BitsNumber(long num){
        return num >= -140737488355328L && num <= 140737488355327L;
    }

    private static boolean is16BitsNumber(int num){
        return num >= -32768 && num <= 32767;
    }
}
