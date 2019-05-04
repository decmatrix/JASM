package com.sokolovskyi.jasm.compiler.lexis;

@SuppressWarnings("Duplicates")
public class Mnemonic {
    public static boolean correctSize(String word){
        return word.length() <= 6;
    }

    static boolean isMnemonic(String word){
        for(String str : Lexemes.ALL_MNEMONICS){
            if(word.equals(str)) return true;
        }

        return false;
    }

    static boolean isRegister8(String word){
        for(String str : Lexemes.REGISTERS_8){
            if(word.equals(str)) return true;
        }

        return false;
    }

    static boolean isRegister32(String word){
        for(String str : Lexemes.REGISTERS_32){
            if(word.equals(str)) return true;
        }

        return false;
    }

    static boolean isRegisterS(String word){
        for(String str : Lexemes.REGISTERS_S){
            if(word.equals(str)) return true;
        }

        return false;
    }

    static boolean isDirective(String word){
        for(String str : Lexemes.DIRECTIVES){
            if(word.equals(str)) return true;
        }

        return false;
    }

    static boolean isDataType(String word){
        for(String str : Lexemes.DATA_TYPES){
            if(word.equals(str)) return true;
        }

        return false;
    }

    static boolean isAsmCommand(String word){
        for(String str : Lexemes.ASM_COMMANDS){
            if(word.equals(str)) return true;
        }

        return false;
    }

    static boolean isLiteral(String word){
        for(String str : Lexemes.LITERALS){
            if(word.equals(str)) return true;
        }

        return false;
    }

    static boolean isDecNumber(String word){
        for(int i = 0; i < word.length(); i++){
            if(!Character.isDigit(word.charAt(i))){
                return false;
            }
        }

        return true;
    }

    static boolean isHexNumber(String word){
        if(word.charAt(word.length() - 1) != 'H') return false;

        boolean flag = true;

        String checkLine = "0123456789ABCDEF";


        for(int i = 0; i < word.length() - 1; i++){
            for(int j = 0; j < checkLine.length(); j++){
                if(word.charAt(i) != checkLine.charAt(j)){
                    flag = false;
                }else{
                    flag = true;
                    break;
                }
            }

            if(!flag) return false;
        }

        return true;
    }

    static boolean isBinNumber(String word){
        if(word.charAt(word.length() - 1) != 'B') return false;

        boolean flag = true;

        String checkLine = "01";


        for(int i = 0; i < word.length() - 1; i++){
            for(int j = 0; j < checkLine.length(); j++){
                if(word.charAt(i) != checkLine.charAt(j)){
                    flag = false;
                }else{
                    flag = true;
                    break;
                }
            }

            if(!flag) return false;
        }

        return true;
    }

    static boolean isIdentifier(String word){
        if(!Character.isLetter(word.charAt(0))) return false;
        if(word.length() > 6) return false;


        for(int i = 1; i < word.length(); i++){
            if(!Character.isLetter(word.charAt(i)) && !Character.isDigit(word.charAt(i))){
                return false;
            }
        }

        return true;
    }


}
