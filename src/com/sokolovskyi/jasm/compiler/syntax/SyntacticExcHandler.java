package com.sokolovskyi.jasm.compiler.syntax;

import com.sokolovskyi.jasm.compiler.additionals.CalcMathExpr;
import com.sokolovskyi.jasm.compiler.lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.lexis.LexemesTable;

public class SyntacticExcHandler {
    private static String[] ids;

    private SyntacticExcHandler() {}

    private static void getAllIds(SyntaxTable[] table){
        ids = new String[table.length];

        for(int i = 0; i < ids.length; i++){
            LexemesTable[] tab = table[i].getLexemesTable();

            if(tab.length >= 2) {
                if (tab[0].getLinkLexeme().equals(Lexemes.IDENTIFIER) && tab[1].getLexeme().equals(Lexemes.LITERALS[8])) {
                    ids[i] = tab[0].getLexeme();
                } else if (tab[0].getLinkLexeme().equals(Lexemes.IDENTIFIER) && tab[1].getLinkLexeme().equals(Lexemes.DATATYPE)) {
                    ids[i] = tab[0].getLexeme();
                }
            }
        }
    }

    public static void catchException(String[] errors, SyntaxTable[] table){
        LexemesTable[] lexLine;
        SentenceTable sentenceLine;

        getAllIds(table);

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
                String lex = lexLine[0].getLexeme().toUpperCase();

                if(lexLine[0].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[2])) {
                    errors[i] = checkEND(lexLine, sentenceLine, i);
                }

            }else if(lexLine[0].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                String lex = lexLine[0].getLexeme().toUpperCase();

                if(lexLine[0].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                        errors[i] = checkID(lexLine, sentenceLine, i);
                }

            }else{



                errors[i] = "\n(" + (i + 1) + ") error: " + SyntaxErrors.Ox1;
            }
        }
    }

    /* methods for check dirs */
    private static String checkID(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        int p = 0;

        if(p == lexLine.length - 1){
            if(!isExistId(lexLine[0].getLexeme())){
                return pattern + SyntaxErrors.Ox2 + lexLine[0].getLexeme();
            }else{
                return pattern + SyntaxErrors.Ox1;
            }
        }

        if(lexLine[p].getLexeme().toUpperCase().equals("CODE") || lexLine[p].getLexeme().toUpperCase().equals("DATA")){

            p++;

            if(lexLine[p].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[0]) || lexLine[p].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[1])){

                if(p != lexLine.length - 1){
                    p++;

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                        if(!isExistId(lexLine[p].getLexeme())){
                            return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                        }
                    }

                    return  pattern + SyntaxErrors.OxF;
                }

                if(isMultiDefId(lexLine[0].getLexeme())){
                    return pattern + SyntaxErrors.Ox10 + lexLine[0].getLexeme();
                }

                return null;
            }

            return  pattern + SyntaxErrors.OxF;
        }else{
            p++;

            if(lexLine[p].getLexeme().equals(Lexemes.LITERALS[8])){

                if(p != lexLine.length - 1){
                    p++;

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                        if(!isExistId(lexLine[p].getLexeme())){
                            return  pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                        }
                    }

                    return pattern + SyntaxErrors.Ox1;
                }

                if(isMultiDefId(lexLine[0].getLexeme())){
                    return pattern + SyntaxErrors.Ox10 + lexLine[p - 1].getLexeme();
                }

                return null;
            }

            if(lexLine[p].getLinkLexeme().equals(Lexemes.DATATYPE)){

                if(p != lexLine.length - 1){
                    p++;

                   if(lexLine.length - 1 - p > 2){
                        int pos_e = lexLine.length - 1;

                        String res = checkMathExp(lexLine, p, pos_e);

                        if(res == null){
                            if(isMultiDefId(lexLine[0].getLexeme())){
                                return pattern + SyntaxErrors.Ox10 + lexLine[0].getLexeme();
                            }

                            return null;
                        }

                        return pattern + res;
                    }

                    if(lexLine[p].getLexeme().equals(Lexemes.LITERALS[1]) || lexLine[p].getLexeme().equals(Lexemes.LITERALS[2])) {

                        if (p != lexLine.length - 1) {
                            p++;

                            if (lexLine[p].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)) {

                                if (p != lexLine.length - 1) {
                                    p++;

                                    if (lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)) {
                                        if (!isExistId(lexLine[p].getLexeme())) {
                                            return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                                        }
                                    }

                                    if (lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)) {
                                        return pattern + SyntaxErrors.Ox4;
                                    }

                                    return pattern + SyntaxErrors.Ox3;
                                }

                                if (isMultiDefId(lexLine[0].getLexeme())) {
                                    return pattern + SyntaxErrors.Ox10 + lexLine[0].getLexeme();
                                }

                                return null;
                            }


                        }

                        return pattern + SyntaxErrors.Ox5;
                    }

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[p].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                            lexLine[p].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)) {

                        if (p != lexLine.length - 1) {
                            p++;

                            if (lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)) {
                                if (!isExistId(lexLine[p].getLexeme())) {
                                    return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                                }
                            }

                            if (lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)) {
                                return pattern + SyntaxErrors.Ox4;
                            }

                            return pattern + SyntaxErrors.Ox3;
                        }

                        if (isMultiDefId(lexLine[0].getLexeme())) {
                            return pattern + SyntaxErrors.Ox10 + lexLine[0].getLexeme();
                        }

                        return null;
                    }

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                        return pattern + SyntaxErrors.Ox5;
                    }

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_S) ||
                            lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                        return pattern + SyntaxErrors.Ox11;
                    }

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                        if(!isExistId(lexLine[p].getLexeme())){
                            return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                        }
                    }

                    return pattern + SyntaxErrors.Ox1;
                }

                return pattern + SyntaxErrors.Ox12;
            }



            if(!isExistId(lexLine[0].getLexeme())){
                return  pattern + SyntaxErrors.Ox2 + lexLine[0].getLexeme();
            }

        }

        return  pattern + SyntaxErrors.Ox1;
    }

    private static String checkMathExp(LexemesTable[] lexLine, int pos_s, int pos_e){

        //Integer res = CalcMathExpr.calcLinearExp(lexLine, pos_s);

        StringBuilder exp = new StringBuilder();

        for(int i = pos_s; i <= pos_e; i++){
            exp.append(lexLine[i].getLexeme());
        }

        try {
            double res = eval(exp.toString());
        }catch (Exception e){
            return SyntaxErrors.Ox13;
        }

        return null;
    }

    private static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }

                return x;
            }
        }.parse();
    }


    /* methods for check directives*/
    private static String checkEND(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(0 == lexLine.length - 1){
            return null;
        }

        if(lexLine[1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
            if(!isExistId(lexLine[1].getLexeme())){
                return pattern + SyntaxErrors.Ox2 + lexLine[1].getLexeme();
            }
        }

        return pattern + SyntaxErrors.OxE;
    }

    /* methods for check asm machine commands */
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

    private static boolean isExistId(String id){
        for(String i : ids){
            if(i == null) continue;
            if(i.equals(id)) return true;
        }

        return false;
    }

    private static boolean isMultiDefId(String id){
        int counter = 0;

        for(String i : ids){
            if(i == null) continue;

            if(i.equals(id)){
                counter++;
            }
        }

        return counter >= 2;
    }

    private static String checkINC(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() == -1){
            return pattern + SyntaxErrors.Ox5;
        }else{
            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.ASMCOMMAND)){
                if(isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())){
                    return pattern + SyntaxErrors.Ox8;
                }else{
                    return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
                }
            }

            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) ||
                    lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
                return pattern + SyntaxErrors.Ox6;
            }

            if((lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) ||
                    lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)) && lexLine.length > 2){
                if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER) || lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.ASMCOMMAND)){
                    if(isExistId(lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme())){
                        return pattern + SyntaxErrors.Ox8;
                    }else{
                        return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme();
                    }
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
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() == -1) {
            return pattern + SyntaxErrors.Ox5;
        }

        if(lexLine[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[5])){
            return pattern + SyntaxErrors.Ox3;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)){
            return pattern + SyntaxErrors.OxB;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.LITERAL) && !lexLine[sentenceLine.getPosFirstOperand()].getLexeme().equals(Lexemes.LITERALS[4])){
            return pattern + SyntaxErrors.Ox4;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
            return pattern + SyntaxErrors.Ox6;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
            if(!isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())){
                return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
            }
        }

        if(!lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DIRECTIVE) && !lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.DIRECTIVE)){
            return pattern + SyntaxErrors.OxD;
        }

        //it true
        if(lexLine[sentenceLine.getPosFirstOperand()].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[3]) || lexLine[sentenceLine.getPosFirstOperand()].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[4]) ||
                lexLine[sentenceLine.getPosFirstOperand()].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[6])){
            int p = sentenceLine.getPosFirstOperand();

            if(lexLine[p + 1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[5])) {
                p += 2;

                System.out.println(lexLine[p]);

                //it true
                if (lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_S) || lexLine[p].getLexeme().equals(Lexemes.LITERALS[4])) {

                    int pos_e = -1;
                    for (int i = 0; i < lexLine.length; i++) {
                        if (lexLine[i].getLexeme().equals(Lexemes.LITERALS[5])) {
                            pos_e = i;
                            break;
                        }
                    }

                    if (pos_e == -1) return pattern + SyntaxErrors.OxA;

                    String res = checkEffAddres(lexLine, p, pos_e);

                    if (res == null) {
                        p = pos_e;

                        if(p != lexLine.length - 1){
                            p++;

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                                return pattern + SyntaxErrors.Ox4;
                            }

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                                if(!isExistId(lexLine[p].getLexeme())){
                                    return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                                }
                            }

                            return pattern + SyntaxErrors.Ox3;
                        }

                        return null;
                    }

                    return pattern + res;
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                    return pattern + SyntaxErrors.Ox4;
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                    if(!isExistId(lexLine[p].getLexeme())){
                        return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                    }
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                    return pattern + SyntaxErrors.OxB;
                }

                return pattern + SyntaxErrors.Ox3;
            }
        }

        return pattern + SyntaxErrors.Ox3;
    }

    private static String checkADD(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(lexLine.length > 1 && lexLine[1].getLinkLexeme().equals(Lexemes.LITERAL)){
            return pattern + SyntaxErrors.Ox5;
        }

        if(sentenceLine.getPosFirstOperand() == -1 && sentenceLine.getPosSecondOperand() == -1) {
            return pattern + SyntaxErrors.Ox5;
        }

        if(lexLine.length > 4 && sentenceLine.getPosFirstOperand() != -1 && sentenceLine.getPosSecondOperand() != -1){
            System.out.println("tut");

            if(lexLine[sentenceLine.getPosSecondOperand() + 1].getLinkLexeme().equals(Lexemes.LITERAL)){
                return pattern + SyntaxErrors.Ox4;
            } else if(lexLine[sentenceLine.getPosSecondOperand() + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                if(isExistId(lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme())){
                    return pattern + SyntaxErrors.Ox8;
                }else{
                    return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosSecondOperand() + 1].getLexeme();
                }
            }else{
                return pattern + SyntaxErrors.Ox3;
            }
        }

        if(lexLine.length == 2 && sentenceLine.getPosFirstOperand() != -1 && (lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) ||
                lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32))){
            return pattern + SyntaxErrors.Ox7;
        }

        if(sentenceLine.getPosFirstOperand() != -1){
            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.ASMCOMMAND)){
                if(isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())){
                    return pattern + SyntaxErrors.Ox8;
                }else{
                    return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
                }
            }

            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                    lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
                return pattern + SyntaxErrors.Ox6;
            }
        }

        if(lexLine.length > 2){
            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER) || lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.ASMCOMMAND)){
                    if(isExistId(lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme())){
                        return pattern + SyntaxErrors.Ox8;
                    }else{
                        return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme();
                    }
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
                if(lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER) || lexLine[sentenceLine.getPosSecondOperand()].getLinkLexeme().equals(Lexemes.ASMCOMMAND)){
                    if(isExistId(lexLine[sentenceLine.getPosSecondOperand()].getLexeme())){
                        return pattern + SyntaxErrors.Ox8;
                    }else{
                        return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosSecondOperand()].getLexeme();
                    }
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
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() == -1 || sentenceLine.getPosSecondOperand() == -1){
            return pattern + SyntaxErrors.Ox5;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
            return pattern + SyntaxErrors.Ox6;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
            if(!isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())){
                return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
            }
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.LITERAL)){
            return pattern + SyntaxErrors.Ox4;
        }

        int p = sentenceLine.getPosFirstOperand();

        if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_32)){
            p++;

            if(lexLine[p].getLexeme().equals(Lexemes.LITERALS[0])){
                p++;

                //it true
                if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_S) || lexLine[p].getLexeme().equals(Lexemes.LITERALS[4])){
                    int pos_e = -1;
                    for (int i = 0; i < lexLine.length; i++) {
                        if (lexLine[i].getLexeme().equals(Lexemes.LITERALS[5])) {
                            pos_e = i;
                            break;
                        }
                    }

                    if (pos_e == -1) return pattern + SyntaxErrors.OxA;

                    String res = checkEffAddres(lexLine, p, pos_e);

                    if (res == null) {

                        p = pos_e;

                        if(pos_e != lexLine.length - 1){
                            p++;

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                                return pattern + SyntaxErrors.Ox4;
                            }

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                                if(!isExistId(lexLine[p].getLexeme())){
                                    return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                                }
                            }

                            return pattern + SyntaxErrors.Ox3;
                        }

                        return null;
                    }

                    return pattern + res;
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                    return pattern + SyntaxErrors.Ox4;
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                    if(!isExistId(lexLine[p].getLexeme())){
                        return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                    }
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                    return pattern + SyntaxErrors.OxB;
                }

                return pattern + SyntaxErrors.Ox3;
            }

            return pattern + SyntaxErrors.Ox7;
        }

        return pattern + SyntaxErrors.Ox3;
    }

    private static String checkAND(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() == -1 || sentenceLine.getPosSecondOperand() == -1){
            return pattern + SyntaxErrors.Ox5;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)){
            return pattern + SyntaxErrors.OxB;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.LITERAL) && !lexLine[sentenceLine.getPosFirstOperand()].getLexeme().equals(Lexemes.LITERALS[4])){
            return pattern + SyntaxErrors.Ox4;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
            return pattern + SyntaxErrors.Ox6;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
            if(!isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())){
                return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
            }
        }

        int p = sentenceLine.getPosFirstOperand();

        //it true
        if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_S) || lexLine[p].getLexeme().equals(Lexemes.LITERALS[4])){
            int pos_e = -1;
            for (int i = 0; i < lexLine.length; i++) {
                if (lexLine[i].getLexeme().equals(Lexemes.LITERALS[5])) {
                    pos_e = i;
                    break;
                }
            }

            if (pos_e == -1) return pattern + SyntaxErrors.OxA;

            String res = checkEffAddres(lexLine, p, pos_e);

            if (res == null){
                p = pos_e + 1;

                if(lexLine[p].getLexeme().equals(Lexemes.LITERALS[0])){
                    p++;

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_32)){

                        if(p != lexLine.length - 1){
                            p++;

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                                return pattern + SyntaxErrors.Ox4;
                            }

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                                if(!isExistId(lexLine[p].getLexeme())){
                                    return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                                }
                            }

                            return pattern + SyntaxErrors.Ox3;
                        }

                        return null;
                    }

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                        if(!isExistId(lexLine[p].getLexeme())){
                            return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                        }
                    }

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                        return pattern + SyntaxErrors.Ox4;
                    }

                    return pattern + SyntaxErrors.Ox3;
                }

                return pattern + SyntaxErrors.Ox7;
            }

            return pattern + res;
        }

        return pattern + SyntaxErrors.Ox3;
    }


    //TODO build
    private static String checkMOV(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() == -1 || sentenceLine.getPosSecondOperand() == -1) {
            return pattern + SyntaxErrors.Ox5;
        }

        if(lexLine[1].getLinkLexeme().equals(Lexemes.LITERAL)){
            return pattern + SyntaxErrors.Ox5;
        }

        int p = sentenceLine.getPosFirstOperand();

        if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_32)){
            p++;

            if(lexLine[p].getLexeme().equals(Lexemes.LITERALS[0])){
                p++;

                if(lexLine[p].getLexeme().equals(Lexemes.LITERALS[1]) || lexLine[p].getLexeme().equals(Lexemes.LITERALS[2])){
                    p++;

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)){

                        if(p != lexLine.length - 1){
                            p++;

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                                if(!isExistId(lexLine[p].getLexeme())){
                                    return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                                }
                            }

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                                return pattern + SyntaxErrors.Ox4;
                            }

                            return pattern + SyntaxErrors.Ox3;
                        }

                        return null;
                    }

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                        return pattern + SyntaxErrors.Ox4;
                    }

                    if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                        if(!isExistId(lexLine[p].getLexeme())){
                            return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                        }
                    }

                    return pattern + SyntaxErrors.Ox3;
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[p].getLinkLexeme().equals(Lexemes.BIN_CONSTANT) ||
                        lexLine[p].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){

                    if(p != lexLine.length - 1){
                        p++;

                        if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                            if(!isExistId(lexLine[p].getLexeme())){
                                return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                            }
                        }

                        if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                            return pattern + SyntaxErrors.Ox4;
                        }

                        return pattern + SyntaxErrors.Ox3;
                    }

                    return null;
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                    if(!isExistId(lexLine[p].getLexeme())){
                        return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                    }
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                    return pattern + SyntaxErrors.Ox4;
                }

                return pattern + SyntaxErrors.Ox3;
            }

            return pattern + SyntaxErrors.Ox7;
        }

        if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
            if(!isExistId(lexLine[p].getLexeme())){
                return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
            }
        }

        if(lexLine[p].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[p].getLinkLexeme().equals(Lexemes.BIN_CONSTANT) ||
                lexLine[p].getLinkLexeme().equals(Lexemes.HEX_CONSTANT)){
            return pattern + SyntaxErrors.Ox6;
        }


        return pattern + SyntaxErrors.Ox3;
    }

    private static String checkCMP(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() == -1 || sentenceLine.getPosSecondOperand() == -1){
            return pattern + SyntaxErrors.Ox5;
        }

        if(lexLine[1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[5])){
            return pattern + SyntaxErrors.Ox3;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32)){
            return pattern + SyntaxErrors.OxB;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.LITERAL) && !lexLine[sentenceLine.getPosFirstOperand()].getLexeme().equals(Lexemes.LITERALS[4])){
            return pattern + SyntaxErrors.Ox4;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
            return pattern + SyntaxErrors.Ox6;
        }

        if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
            if(!isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())){
                return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
            }
        }

        if(!lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DIRECTIVE) && !lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.DIRECTIVE)){
            return pattern + SyntaxErrors.OxD;
        }

        //it true
        if(lexLine[sentenceLine.getPosFirstOperand()].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[3]) || lexLine[sentenceLine.getPosFirstOperand()].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[4]) ||
                lexLine[sentenceLine.getPosFirstOperand()].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[6])){

            int p = sentenceLine.getPosFirstOperand();

            if(lexLine[p + 1].getLexeme().toUpperCase().equals(Lexemes.DIRECTIVES[5])){
                p += 2;

                System.out.println(lexLine[p]);

                //it true
                if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_S) || lexLine[p].getLexeme().equals(Lexemes.LITERALS[4])){

                    int pos_e = -1;
                    for (int i = 0; i < lexLine.length; i++) {
                        if (lexLine[i].getLexeme().equals(Lexemes.LITERALS[5])) {
                            pos_e = i;
                            break;
                        }
                    }

                    if (pos_e == -1) return pattern + SyntaxErrors.OxA;

                    String res = checkEffAddres(lexLine, p, pos_e);

                    if (res == null){
                        p = pos_e + 1;

                        if(lexLine[p].getLexeme().equals(Lexemes.LITERALS[0])){
                            p++;

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[p].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) || lexLine[p].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){

                                if(p != lexLine.length - 1){
                                    p++;

                                    if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                                        return pattern + SyntaxErrors.Ox4;
                                    }

                                    if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                                        if(!isExistId(lexLine[p].getLexeme())){
                                            return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                                        }
                                    }

                                    return pattern + SyntaxErrors.Ox3;
                                }

                                return null;
                            }

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                                if(!isExistId(lexLine[p].getLexeme())){
                                    return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                                }
                            }

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                                return pattern + SyntaxErrors.OxB;
                            }

                            if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                                return pattern + SyntaxErrors.Ox4;
                            }

                            return pattern + SyntaxErrors.Ox3;
                        }

                        return pattern + SyntaxErrors.Ox7;
                    }

                    return pattern + res;
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_8) || lexLine[p].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                    return pattern + SyntaxErrors.OxB;
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.LITERAL)){
                    return pattern + SyntaxErrors.Ox4;
                }

                if(lexLine[p].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                    if(!isExistId(lexLine[p].getLexeme())){
                        return pattern + SyntaxErrors.Ox2 + lexLine[p].getLexeme();
                    }
                }

                return pattern + SyntaxErrors.Ox3;

            }

            return pattern + SyntaxErrors.Ox3;

        }

        return pattern + SyntaxErrors.Ox3;
    }

    private static String checkJB(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() == -1){
            return pattern + SyntaxErrors.Ox5;
        }else{
            if(lexLine.length > 2){
                if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.LITERAL)){
                    return pattern + SyntaxErrors.Ox4;
                }

                if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                    if(!isExistId(lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme())){
                        return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme();
                    }
                }

                return pattern + SyntaxErrors.Ox3;
            }

            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                    lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)){
                return pattern + SyntaxErrors.Ox6;
            }

            if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.ASMCOMMAND)){
                if(!isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())){
                    return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
                }else{
                     return null;
                }
            }
        }

        return pattern + SyntaxErrors.Ox9;
    }

    private static String checkJMP(LexemesTable[] lexLine, SentenceTable sentenceLine, int pos){
        String pattern = "\n(" + (pos + 1) + ") error: ";

        if(sentenceLine.getPosFirstOperand() == -1){
            return pattern + SyntaxErrors.Ox5;
        }else {

            if(sentenceLine.getCountFirstOperand() > 2){

                if(lexLine[1].getLexeme().equals(Lexemes.LITERALS[8])){
                    return pattern + SyntaxErrors.Ox5;
                }

                if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_S) || lexLine[sentenceLine.getPosFirstOperand()].getLexeme().equals(Lexemes.LITERALS[4])) {
                    int pos_e = -1;
                    for (int i = 0; i < lexLine.length; i++) {
                        if (lexLine[i].getLexeme().equals(Lexemes.LITERALS[5])) {
                            pos_e = i;
                            break;
                        }
                    }

                    if (pos_e == -1) return pattern + SyntaxErrors.OxA;

                    String res = checkEffAddres(lexLine, sentenceLine.getPosFirstOperand(), pos_e);

                    if (res == null){
                        if(pos_e != lexLine.length - 1){
                            if(lexLine[pos_e + 1].getLinkLexeme().equals(Lexemes.LITERAL)){
                                return pattern + SyntaxErrors.Ox4;
                            }

                            if(lexLine[pos_e + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                                if(isExistId(lexLine[pos_e + 1].getLexeme())){
                                    return pattern + SyntaxErrors.Ox3;
                                }else{
                                    return pattern + SyntaxErrors.Ox2 + lexLine[pos_e + 1].getLexeme();
                                }
                            }

                            return pattern + SyntaxErrors.Ox3;

                        }else{
                            return null;
                        }
                    }

                    return pattern + res;
                }else{

                    if (lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.ASMCOMMAND)) {
                        if (!isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())) {
                            return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
                        }
                    }

                    if((lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8)) &&
                        lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme().equals(Lexemes.LITERALS[8])){
                        return pattern + SyntaxErrors.OxC;
                    }

                    if((lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_32) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.REGISTER_8)) &&
                            lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme().equals(Lexemes.LITERALS[4])){
                        return pattern + SyntaxErrors.OxB;
                    }

                    if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.LITERAL)){
                        return pattern + SyntaxErrors.Ox5;
                    }

                    if(lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                        if(isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())){
                            return pattern + SyntaxErrors.Ox9;
                        }else{
                            return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
                        }
                    }

                    return pattern + SyntaxErrors.Ox3;
                }

            }else {
                if(sentenceLine.getPosFirstOperand() != lexLine.length - 1){
                    if (lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.ASMCOMMAND)) {
                        if (!isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())) {
                            return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
                        }
                    }

                    if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)){
                        if(isExistId(lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme())){
                            return pattern + SyntaxErrors.Ox9;
                        }else{
                            return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand() + 1].getLexeme();
                        }
                    }

                    if(lexLine[sentenceLine.getPosFirstOperand() + 1].getLinkLexeme().equals(Lexemes.LITERAL)){
                        return pattern + SyntaxErrors.Ox4;
                    }

                    return pattern + SyntaxErrors.Ox3;
                }

                if (lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.IDENTIFIER) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.ASMCOMMAND)) {
                    if (!isExistId(lexLine[sentenceLine.getPosFirstOperand()].getLexeme())) {
                        return pattern + SyntaxErrors.Ox2 + lexLine[sentenceLine.getPosFirstOperand()].getLexeme();
                    } else {
                        return null;
                    }
                }

                if (lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.DEC_CONSTANT) || lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.HEX_CONSTANT) ||
                        lexLine[sentenceLine.getPosFirstOperand()].getLinkLexeme().equals(Lexemes.BIN_CONSTANT)) {
                    return pattern + SyntaxErrors.Ox6;
                }
            }
        }


        return pattern + SyntaxErrors.Ox9;
    }

    //TODO optim.
    private static String checkEffAddres(LexemesTable[] tables, int pos_s, int pos_f) {
        boolean f = false;

        if (tables[pos_s].getLinkLexeme().equals(Lexemes.REGISTER_S)) {
            if (tables[pos_s + 1].getLexeme().equals(Lexemes.LITERALS[8])) {
                pos_s += 2;
                f = true;
            } else {
                return SyntaxErrors.OxB;
            }
        }

        if (f || tables[pos_s].getLexeme().equals(Lexemes.LITERALS[4])) {
            if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.REGISTER_32)) {
                pos_s++;

                if (tables[pos_s + 1].getLexeme().equals(Lexemes.LITERALS[1])) {
                    pos_s++;

                    if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.REGISTER_32)) {
                        pos_s++;

                        if (tables[pos_s + 1].getLexeme().equals(Lexemes.LITERALS[1])) {
                            pos_s++;

                            if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.DEC_CONSTANT)) {
                                pos_s++;

                                if (tables[pos_s + 1].getLexeme().equals(Lexemes.LITERALS[5])) {
                                    return null;
                                }

                                return SyntaxErrors.OxA;
                            }

                            if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)) {
                                if (isExistId(tables[pos_s + 1].getLexeme())) {
                                    return SyntaxErrors.Ox9;
                                } else {
                                    return SyntaxErrors.Ox2 + tables[pos_s + 1].getLexeme();
                                }
                            }

                            return SyntaxErrors.Ox9;
                        }

                        if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.LITERAL)) {
                            return SyntaxErrors.OxA;
                        }

                        if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)) {
                            if (isExistId(tables[pos_s + 1].getLexeme())) {
                                return SyntaxErrors.Ox9;
                            } else {
                                return SyntaxErrors.Ox2 + tables[pos_s + 1].getLexeme();
                            }
                        }

                        return SyntaxErrors.Ox3;
                    }

                    if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)) {
                        if (isExistId(tables[pos_s + 1].getLexeme())) {
                            return SyntaxErrors.Ox9;
                        } else {
                            return SyntaxErrors.Ox2 + tables[pos_s + 1].getLexeme();
                        }
                    }

                    return SyntaxErrors.Ox9;
                }

                if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.LITERAL)) {
                    return SyntaxErrors.OxA;
                }

                if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)) {
                    if (isExistId(tables[pos_s + 1].getLexeme())) {
                        return SyntaxErrors.Ox9;
                    } else {
                        return SyntaxErrors.Ox2 + tables[pos_s + 1].getLexeme();
                    }
                }

                return SyntaxErrors.Ox3;
            }

            if (tables[pos_s + 1].getLinkLexeme().equals(Lexemes.IDENTIFIER)) {
                if (isExistId(tables[pos_s + 1].getLexeme())) {
                    return SyntaxErrors.Ox9;
                } else {
                    return SyntaxErrors.Ox2 + tables[pos_s + 1].getLexeme();
                }
            }

            return SyntaxErrors.Ox9;
        }

        return null;
    }

}
