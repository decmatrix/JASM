package com.sokolovskyi.jasm.compiler.listing;

import com.sokolovskyi.jasm.compiler.Lexis.Lexemes;
import com.sokolovskyi.jasm.compiler.Lexis.LexemesTable;

final class Adress {

    private Adress(){}

    static String getStrAdress(int adress){
        String strAdress = "0000";

        switch (Integer.toHexString(adress).length()){
            case 1: strAdress = "000" + Integer.toHexString(adress).toUpperCase();
                break;

            case 2: strAdress = "00" + Integer.toHexString(adress).toUpperCase();
                break;

            case 3: strAdress = "0" +Integer.toHexString(adress).toUpperCase();
                break;

            case 4: strAdress = Integer.toHexString(adress).toUpperCase() + "";
        }

        return strAdress;
    }

    static int calcAdressDATATYPE(LexemesTable[] buffTable, int adress) {
        if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[0])) {
            adress += 0x1;
        } else if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[1])) {
            adress += 0x2;
        } else if (buffTable[1].getLexeme().toUpperCase().equals(Lexemes.DATA_TYPES[2])) {
            adress += 0x4;
        }

        return adress;
    }

    static int calcAdressINC(LexemesTable[] buffTable, int adress){
        if(buffTable.length > 0){
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[1])){
                if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_32)){
                    return adress + 0x1;
                }
                if(buffTable[1].getLinkLexeme().equals(Lexemes.REGISTER_8)){
                    return adress + 0x2;
                }
            }
        }

        return adress;
    }

    static int calcAdressADD(LexemesTable[] buffTable, int adress){
        if(buffTable.length > 0){
            if(buffTable[0].getLexeme().toUpperCase().equals(Lexemes.ASM_COMMANDS[3])){
                System.out.println(Integer.toHexString(adress + 0x2));
                return adress + 0x2;
            }
        }

        return adress;
    }
}
