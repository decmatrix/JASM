package com.sokolovskyi.jasm.compiler.Lexis;

public class LexemesTable {
    private String word;

    private String lexeme;
    private int sizeLexeme;
    private String linkLexeme;


    public LexemesTable(String word) {
        this.word = word;
        createTable();
    }

    private void createTable() {
        lexeme = word;
        sizeLexeme = word.length();
        parseWord();
    }


    public String getLexeme() {
        return lexeme;
    }

    public int getSizeLexeme() {
        return sizeLexeme;
    }

    public String getLinkLexeme() {
        return linkLexeme;
    }

    private void parseWord() {
        String upWord = word.toUpperCase();


        if(Mnemonic.isMnemonic(upWord)){
            if(Mnemonic.isRegister8(upWord)){
                linkLexeme = Lexemes.REGISTER_8;
            }
            else if(Mnemonic.isRegister32(upWord)){
                linkLexeme = Lexemes.REGISTER_32;
            }else if(Mnemonic.isRegisterS(upWord)){
                linkLexeme = Lexemes.REGISTER_S;
            }else if(Mnemonic.isDirective(upWord)){
                linkLexeme = Lexemes.DIRECTIVE;
            }else if(Mnemonic.isDataType(upWord)){
                linkLexeme = Lexemes.DATATYPE;
            }else if(Mnemonic.isAsmCommand(upWord)){
                linkLexeme = Lexemes.ASMCOMMAND;
            }else if(Mnemonic.isLiteral(upWord)){
                linkLexeme = Lexemes.LITERAL;
            }
        }else if(Mnemonic.isDecNumber(upWord)){
            linkLexeme = Lexemes.DEC_CONSTANT;
        }else if(Mnemonic.isHexNumber(upWord)){
            linkLexeme = Lexemes.HEX_CONSTANT;
        }else if(Mnemonic.isBinNumber(upWord)){
            linkLexeme = Lexemes.BIN_CONSTANT;
        }else if(Mnemonic.isIdentifier(upWord)){
            linkLexeme = Lexemes.IDENTIFIER;
        }else{
            linkLexeme = Lexemes.UNKNOWN_LEXEME;
        }
    }

    @Override
    public String toString() {
        return word + "  ( " + sizeLexeme + " )  " + linkLexeme;
    }
}
